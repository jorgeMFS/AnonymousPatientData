package pt.ieeta.anonymouspatientdata.pluginset.jetty;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.dcm4che2.io.DicomInputStream;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ieeta.anonymouspatientdata.core.util.RuntimeIOException;
import pt.ua.dicoogle.sdk.StorageInterface;
import pt.ua.dicoogle.sdk.core.DicooglePlatformInterface;
import pt.ua.dicoogle.sdk.core.PlatformCommunicatorInterface;

/** Main web service.
 *
 * @author Jorge Miguel Ferreira da Silva 
 */
public class AnonymousWebServlet  extends HttpServlet implements PlatformCommunicatorInterface {


	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(AnonymousWebServlet.class);
	private final ForkJoinPool pool = new ForkJoinPool(1);
	private DicooglePlatformInterface platform;

	@SuppressWarnings("deprecation")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		response.setStatus(418, "I'm a teapot");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, RuntimeIOException {
	
		Stream<? extends InputStream> dicomObjects;
		resp.setContentType("application/json");
		if (req.getContentType() == null) {
			JSONObject reply = new JSONObject();
			try {
				reply.put("error", "no content");
			} catch (JSONException e) {
				logger.warn("Interrupted", e);
			}
			resp.getWriter().print(reply.toString());
			resp.setStatus(400);
			return;
		}

		else if (req.getContentType().startsWith("multipart/form-data")) {
			// for now we just retrieve the first valid part
			Collection<Part> parts = req.getParts();
			if (parts == null || parts.isEmpty()) {
				JSONObject reply = new JSONObject();
				try {
					reply.put("error", "no valid content in multipart entity");
				} catch (JSONException e) {
					logger.warn("Interrupted", e);
				}
				resp.getWriter().print(reply.toString());
				resp.setStatus(400);
				return;
			}
			
			dicomObjects = parts.stream().sequential()
					.filter(part -> part.getContentType() != null)
					.map( part -> {
						try {
							return new BufferedInputStream(part.getInputStream());
						} catch (IOException ex) {
							logger.warn("Failed to fetch file", ex);
							return null;
						}
					})
					.filter(Objects::nonNull);
			
			
		}
		else {
			dicomObjects = Stream.of(new BufferedInputStream(req.getInputStream()));
		}



		// fetch storage interface
		StorageInterface storage = platform.getStorageForSchema("anon");
		if (storage==null) throw new IllegalStateException();
		
		List<String> uris;
		try {
			uris = pool.submit(() -> dicomObjects
					.filter(Objects::nonNull)
					.map(dcm ->{

						try{ return storage.store(new DicomInputStream(dcm));
						} catch (IOException e) {
							throw new RuntimeIOException(e); }
					})

					.filter(Objects::nonNull)
					.map(URI::toString)
					.collect(Collectors.toList())).get();
			JSONObject reply = new JSONObject();
			reply.put("status", "COMPLETED");
			reply.put("dcmFiles", uris);
			resp.getWriter().print(reply.toString());
			resp.setStatus(200);

		} catch (RuntimeIOException | InterruptedException | ExecutionException | JSONException e) {
			logger.warn("Interrupted", e);
			JSONObject reply = new JSONObject();
			try {
				reply.put("message", e.getMessage());
				reply.put("status", "interrupted");
			} catch (JSONException e1) {
				logger.warn("Interrupted", e);
			}
			resp.getWriter().print(reply.toString());
			resp.setStatus(500);
		}

	}

	@Override
	public void setPlatformProxy(DicooglePlatformInterface core) {
		this.platform = core;
	}

}
