/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  PersistantDataLucene is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PersistantDataLucene is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */

package pt.ieeta.anonymouspatientdata.core.impl;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */
public class PersistantDataLucene implements AnonDatabase {



	private static final Logger logger = LoggerFactory.getLogger(PersistantDataLucene.class);
	static final String DEFAULT_ANON_PATH = "./Anon_index/";
	static final String DEFAULT_ANON="Indexed";
	/**
	 * where the index files will be located
	 */
	private String indexFilePath;
	/** 
	 * Lucene variables which we need to track
	 */
	private Directory index;
	private Analyzer analyzer;
	private final IndexWriter writer;
	private SearcherManager manager;
	/**
	 * constructs an indexer instance
	 * @throws IOException 
	 */

	PersistantDataLucene(String path) throws IOException{
		this.setIndexPath(path);
		Path p = Paths.get(path);
		index = FSDirectory.open(p);
		Files.createDirectories(p);
		analyzer = new StandardAnalyzer();

		IndexWriterConfig indexConfig = new IndexWriterConfig(analyzer)
				.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		// this will create the index if it does not exist yet
		this.writer=new IndexWriter(this.index,indexConfig);
		this.manager=new SearcherManager(this.writer,new SearcherFactory());
		logger.info("Opened Lucene database");
	}

	private final void setIndexPath(String indexPath) {
		this.indexFilePath = indexPath;
		logger.debug("LUCENE: indexing at {}", indexFilePath);
	}


	@Override
	public void insertStudyData(StudyData studyData) throws IOException{
		if (index==null) {
			logger.debug("failed to insert study data");
			throw new IllegalStateException();
		}
		Document studyDataDoc= new Document();
		TextField AccessionNumber = new TextField("AccessionNumber",studyData.getAccessionNumber(),Store.YES);
		TextField Accession_Map_Number = new TextField("Accession_Map_Number",studyData.getMapAccessionNumber(),Store.YES);
		TextField other =new TextField("other",studyData.getAccessionNumber(),Store.NO);
		studyDataDoc.add(AccessionNumber);
		studyDataDoc.add(Accession_Map_Number);
		studyDataDoc.add(other);
		this.writer.addDocument(studyDataDoc);
		logger.debug("inserted study Data{}",studyDataDoc);
		this.writer.commit();
		this.writer.flush();
		boolean boolval=this.manager.maybeRefresh();
		if (boolval!=true){
			logger.warn("maybe.Refresh another thread is currently refreshing");
		}
	}

	@Override
	public void insertPatientData(PatientData patientData) throws IOException{
		if (index==null) {
			logger.debug("failed to insert patient data");
			throw new IllegalStateException();
		}
		Document patientDataDoc= new Document();
		TextField patientName = new TextField("PatientName",patientData.getPatientName(),Store.YES);
		TextField patientId = new TextField("PatientID",patientData.getPatientId(),Store.YES);
		TextField patient_Map_Id = new TextField("Patient_Map_Id",patientData.getMapId(),Store.YES);
		TextField other =new TextField("other",patientData.getPatientName()+" "+patientData.getPatientId(),Store.NO);
		patientDataDoc.add(patientName);
		patientDataDoc.add(patientId);
		patientDataDoc.add(patient_Map_Id);
		patientDataDoc.add(other);

		this.writer.addDocument(patientDataDoc);
		logger.debug("inserted patient data",patientDataDoc);
		this.writer.commit();
		this.writer.flush();
		boolean boolval=this.manager.maybeRefresh();
		if (boolval!=true){
			logger.warn("maybe.Refresh another thread is currently refreshing");
		}


	}


	@Override
	public Optional<StudyData> getStudyDataByAccessionNumber(String accessionNumber) throws IOException{
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("AccessionNumber",accessionNumber));
		int NElem=1;
		IndexSearcher is = manager.acquire();
		try{
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0){
				logger.debug("got empty Patient Data By Accession Number");
				return Optional.empty();
			}
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			StudyData sd=new StudyData(d.get("AccessionNumber"), d.get("Accession_Map_Number"));
			logger.debug("Could get Accession Number{}", sd.toString());
			return Optional.of(sd);} 

		finally {
			manager.release(is);
		}

	}


	@Override
	public Optional<PatientData> getPatientDataById(String id) throws IOException{
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("PatientID",id));
		int NElem=1;
		IndexSearcher is = manager.acquire();
		try{
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0){
				logger.debug("got empty Patient Data By Id");
				return Optional.empty();
			}
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			PatientData pd=new PatientData(d.get("PatientName"), d.get("PatientID"),d.get("Patient_Map_Id"));
			logger.debug("Could get Patient Data By Id{}", pd.toString());
			return Optional.of(pd);
		} 
		finally {
			manager.release(is);
		}

	}

	@Override
	public Optional<PatientData> getPatientDataBypatientMapId(String patientMapId) throws IOException {
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("Patient_Map_Id",patientMapId));
		int NElem=1;		
		IndexSearcher is = manager.acquire();
		try{
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0){
				logger.debug("got empty Patient Data By patient Map Id");
				return Optional.empty();
			}
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			PatientData pd=new PatientData(d.get("PatientName"), d.get("PatientID"),d.get("Patient_Map_Id"));
			logger.debug("Could get Data By patient Map Id{}", pd.toString());
			return Optional.of(pd);} 
		finally {
			manager.release(is);
		}

	}



	@Override
	public Optional<String> getmapAccessionNumber(String accessionNumber) throws IOException{
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("AccessionNumber",accessionNumber));
		int NElem=1;
		IndexSearcher is = manager.acquire();
		try{
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0){
				logger.debug("got empty map Accession Number");
				return Optional.empty();
			}
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			StudyData sd=new StudyData(d.get("AccessionNumber"), d.get("Accession_Map_Number"));
			String mapAccessionNumber= sd.getMapAccessionNumber();
			logger.debug("Could get map Accession Number{}", mapAccessionNumber);
			return Optional.of(mapAccessionNumber);} 
		finally {
			manager.release(is);
		}

	}


	@Override
	public Optional<String> getmapIdbyPatientId(String patientId) throws IOException{
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("PatientID",patientId));
		int NElem=1;
		IndexSearcher is = manager.acquire();
		try{
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0){
				logger.debug("got empty map Id by Patient Id");
				return Optional.empty();
			}
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			PatientData pd=new PatientData(d.get("PatientName"), d.get("PatientID"),d.get("Patient_Map_Id"));
			String patientMapId= pd.getMapId();
			logger.debug("Could get map Id by Patient Id{}", patientMapId);
			return Optional.of(patientMapId);} 
		finally {
			manager.release(is);
		}
	}


	@Override
	public Optional<String> getmapIdbyPatientName(String patientName) throws IOException{
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("PatientName",patientName));
		int NElem=1;
		IndexSearcher is = manager.acquire();
		try{
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0){
				logger.debug("got empty map Id by Patient Name");
				return Optional.empty();
			}
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			PatientData pd=new PatientData(d.get("PatientName"), d.get("PatientID"),d.get("Patient_Map_Id"));
			String patientMapId= pd.getMapId();
			logger.debug("Could get map Id by Patient Name{}", patientMapId);
			return Optional.of(patientMapId);} 
		finally {
			manager.release(is);
		}

	}


	@Override
	public void close() throws IOException{

		if (manager!=null)
			manager.close();
		if (writer!=null)
			this.writer.close();
		if (index!=null)
			index.close();

		logger.info("Closed Lucene database");
	}


	@Override
	public Optional<String> getPatientNameByPatientMapId(String patientMapId) throws IOException {
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("Patient_Map_Id",patientMapId));
		int NElem=1;
		IndexSearcher is = manager.acquire();
		try{
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0){
				logger.debug("got empty Patient Name By Patient Map Id");
				return Optional.empty();
			}
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			PatientData pd=new PatientData(d.get("PatientName"), d.get("PatientID"),d.get("Patient_Map_Id"));
			String patientName = pd.getPatientName();
			logger.debug("Could get Patient Name By Patient Map Id{}", patientName);
			return Optional.of(patientName);} 
		finally {
			manager.release(is);
		}
	}



	@Override
	public Optional<String> getPatientIdByPatientMapId(String patientMapId) throws IOException {
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("Patient_Map_Id",patientMapId));
		int NElem=1;
		IndexSearcher is = manager.acquire();
		try{
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0){
				logger.debug("got empty Patient Id By Patient Map Id");
				return Optional.empty();
			}
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			PatientData pd=new PatientData(d.get("PatientName"), d.get("PatientID"),d.get("Patient_Map_Id"));
			String patientId = pd.getPatientId();
			logger.debug("Could get Patient Id By Patient Map Id{}", patientId);

			return Optional.of(patientId);} 
		finally {
			manager.release(is);
		}

	}

	@Override
	public Optional<String> getAccessionNumberByAccessionMapNumber(String mapAccessionNumber) throws IOException {
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("Accession_Map_Number",mapAccessionNumber));
		int NElem=1;
		IndexSearcher is = manager.acquire();
		try{
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0){
				logger.debug("got empty Accession Number By Accession Map Number");
				return Optional.empty();
			}
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			StudyData sd=new StudyData(d.get("AccessionNumber"), d.get("Accession_Map_Number"));
			String AccessionNumber = sd.getAccessionNumber();
			logger.debug("Could get Accession Number By Accession Map Number{}", AccessionNumber);
			return Optional.of(AccessionNumber);} 
		finally {
			manager.release(is);
		}

	}

}
