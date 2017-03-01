/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  AnonymousQuery is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AnonymousQuery is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ieeta.anonymouspatientdata.pluginset.query;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ieeta.anonymouspatientdata.core.impl.AnonDatabase;
import pt.ieeta.anonymouspatientdata.core.impl.MatchTables;
import pt.ieeta.anonymouspatientdata.core.impl.QueryConverter;
import pt.ieeta.anonymouspatientdata.core.impl.ResultConverter;
import pt.ieeta.anonymouspatientdata.core.util.RuntimeIOException;
import pt.ieeta.anonymouspatientdata.pluginset.storage.AnonymousStorage;
import pt.ua.dicoogle.sdk.QueryInterface;
import pt.ua.dicoogle.sdk.core.DicooglePlatformInterface;
import pt.ua.dicoogle.sdk.core.PlatformCommunicatorInterface;
import pt.ua.dicoogle.sdk.datastructs.SearchResult;
import pt.ua.dicoogle.sdk.settings.ConfigurationHolder;

public class AnonymousQuery implements QueryInterface, PlatformCommunicatorInterface {

	private static final Logger logger = LoggerFactory.getLogger(AnonymousStorage.class);
	private ConfigurationHolder settings;
	private boolean enabled = true;
	protected DicooglePlatformInterface platform;
	private String plugin;

	public AnonymousQuery() {
		super();
		logger.info("Initializing -> Anonymous Query");
	}

	@Override
	public String getName() {
		return "Anonymous-Wrapper-Plugin";
	}

	@Override
	public boolean enable() {
		return this.enabled = true;
	}

	@Override
	public boolean disable() {
		return this.enabled = false;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void setSettings(ConfigurationHolder settings) {
		this.plugin = settings.getConfiguration().getString("Plugin", "lucene");
		this.settings = settings;

	}

	@Override
	public ConfigurationHolder getSettings() {
		return this.settings;
	}

	@Override
	public Iterable<SearchResult> query(String query, Object... parameters) {
		Objects.requireNonNull(query);
		logger.info("Requested query: {}", query);
		QueryParser qP = new QueryParser("other", new StandardAnalyzer());
		QueryInterface provider = this.platform.requestQueryPlugin(this.plugin);
		try {
			Query q = qP.parse(query);
			AnonDatabase anondB = MatchTables.getInstance().getDB();
			q = new QueryConverter(anondB).transformQuery(q);
			logger.info("Transformed query: {}", q);
			Iterable<SearchResult> it = provider.query(q.toString(), parameters);
			ResultConverter rC = new ResultConverter(anondB);
			Spliterator<SearchResult> splt= StreamSupport.stream(it.spliterator(), false)
					.map(rs -> {
						try {
							return rC.transform(rs);

						} catch (IOException e) {
							logger.warn("Problem with Search Result Transformation",e);
							return null;
						}
					})
					.filter(Objects::nonNull)
					.spliterator();

			return new Iterable<SearchResult>() {

				@Override
				public Spliterator<SearchResult> spliterator() {
					return splt;
				}

				@Override
				public Iterator<SearchResult> iterator() {
					return Spliterators.iterator(splt);
				}
			};

		} catch (ParseException | IOException | RuntimeIOException e1) {
			logger.warn("failed to query", e1);
			return Collections.emptyList();
		}
	}

	@Override
	public void setPlatformProxy(DicooglePlatformInterface core) {
		this.platform = core;
	}

}
