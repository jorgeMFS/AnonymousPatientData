package metal.utils.dicoogle.indextools;

import hu.ssh.progressbar.ProgressBar;
import hu.ssh.progressbar.console.ConsoleProgressBar;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;

import pt.ieeta.anonymouspatientdata.core.impl.ForeignNames;
import pt.ieeta.anonymouspatientdata.core.impl.MatchTable;

public class IndexAnonymizer {

	private static CommandLine cli(String[] args) {
		// create Options object
		Options options = new Options();

		// add t option
		Option i = new Option("i", true, "repository path");
		i.setRequired(true);
		options.addOption(i);

		Option o = new Option("o", true, "annonimized output repository path");
		o.setRequired(true);
		options.addOption(o);
		
		Option b = new Option("b", true, "bulk size");
		options.addOption(b);

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ant", options);
			System.exit(-1);
		}
		return cmd;
	}

	public static void main(String args[]) {

		// String name = MatchTable.getInstance().getName("Tiago");
		// System.out.println(name);

		CommandLine cmd = cli(args);

		String repository = cmd.getOptionValue("i");
		String targetRep = cmd.getOptionValue("o");
		int nProgess = Integer.parseInt(cmd.getOptionValue("b","100"));

		ForeignNames.load();
		MatchTable.load();

		MatchTable table = MatchTable.getInstance();

		IndexReader reader = null;
		IndexWriter wr = null;
		try {
			reader = Utils.openIndexReader(repository);
			wr = Utils.openIndexWriter(targetRep);
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			System.exit(-1);
		}

		System.out.println("Number of Documents: " + reader.maxDoc());

		@SuppressWarnings("rawtypes")
		ProgressBar progressBar = ConsoleProgressBar.on(System.out).withTotalSteps(reader.maxDoc());

		int i = 0;
		while (i < reader.maxDoc()) {
			int a = 0;
			for (; a < nProgess && i < reader.maxDoc(); a++,i++) {
				
				if (!reader.isDeleted(i)) {
					Document doc = null;
					try {
						doc = reader.document(i);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (doc != null) {
						Field patNameField = doc.getField("PatientName");
						Field patientIDField = doc.getField("PatientID");
						Field AccessionNumberField = doc
								.getField("AccessionNumber");
						
						Field instName = doc.getField("InstitutionName");
						Field physicianName = doc.getField("ReferringPhysicianName");
						Field instAddr = doc.getField("InstitutionAddress");
						
						patNameField.setValue(table.getName(patNameField
								.stringValue()));
						patientIDField.setValue(table
								.getPatientID(patientIDField.stringValue()));
						AccessionNumberField.setValue(table
								.getAccessionNumber(AccessionNumberField
										.stringValue()));
						instName.setValue("None");
						
						physicianName.setValue("Unkown");
						instAddr.setValue("None");
						

						try {
							wr.addDocument(doc);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

			try {
				wr.commit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			progressBar.tick(a);
		}

		try {
			System.out.printf("Annonimized %d/%d Documents", wr.numDocs(),
					reader.numDocs());
			
			wr.optimize();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
