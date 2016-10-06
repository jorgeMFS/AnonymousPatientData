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


import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.apache.lucene.document.Field.Store;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
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



	private static final Logger log = LoggerFactory.getLogger(PersistantDataLucene.class);
	static final String DEFAULT_ANON_PATH = "./Anon_index/";
	static final String DEFAULT_ANON="Indexed";
	/**
	 * where the index files will be located
	 */
	private String indexFilePath;
	/**
	 * lucene variables which we need to track
	 */
	private Directory index;
	private Analyzer analyzer;
	/**
	 * constructs an indexer instance
	 */
	PersistantDataLucene(){

		this.indexFilePath = DEFAULT_ANON_PATH;
		log.info("Created Lucene Indexer default Constructor");
	}

	PersistantDataLucene(String path){
		this.setIndexPath(path);
		log.info("Created Lucene Indexer Plugin");
	}



	public final void setIndexPath(String indexPath) {
		this.indexFilePath = indexPath;
		log.debug("LUCENE: indexing at {}", indexFilePath);

		try {
			index = FSDirectory.open(new File(indexFilePath).toPath());
			File f = new File(indexFilePath);
			f.mkdirs();
			analyzer = new StandardAnalyzer();

			IndexWriterConfig indexConfig = new IndexWriterConfig(analyzer)
					.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			// this will create the index if it does not exist yet
			new IndexWriter(index, indexConfig).close();

		} catch (IOException ex) {
			log.error("Failed to open index", ex);
		}
	}


	@Override
	public void insertStudyData(StudyData studyData) throws IOException{

		if (index==null) throw new IllegalStateException();
		Document studyDataDoc= new Document();
		TextField AccessionNumber = new TextField("AccessionNumber",studyData.getAccessionNumber(),Store.YES);
		TextField Accession_Map_Number = new TextField("Accession_Map_Number",studyData.getMapAccessionNumber(),Store.YES);
		TextField other =new TextField("other",studyData.getAccessionNumber(),Store.NO);
		studyDataDoc.add(AccessionNumber);
		studyDataDoc.add(Accession_Map_Number);
		studyDataDoc.add(other);
		try(IndexWriter iw= new IndexWriter(this.index,new IndexWriterConfig().setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND))) {
			iw.addDocument(studyDataDoc);
			iw.flush();
		}
	}

	@Override
	public void insertPatientData(PatientData patientData) throws IOException{
		if (index==null) throw new IllegalStateException();
		Document patientDataDoc= new Document();
		TextField patientName = new TextField("PatientName",patientData.getPatientName(),Store.YES);
		TextField patientId = new TextField("PatientID",patientData.getPatientId(),Store.YES);
		TextField patient_Map_Id = new TextField("Patient_Map_Id",patientData.getMapId(),Store.YES);
		TextField other =new TextField("other",patientData.getPatientName()+" "+patientData.getPatientId(),Store.NO);
		patientDataDoc.add(patientName);
		patientDataDoc.add(patientId);
		patientDataDoc.add(patient_Map_Id);
		patientDataDoc.add(other);
		try(IndexWriter iw= new IndexWriter(this.index,new IndexWriterConfig().setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND))){
			iw.addDocument(patientDataDoc);
			iw.flush();
		}
	}


	@Override
	public Optional<StudyData> getStudyDataByAccessionNumber(String accessionNumber) throws IOException{
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("AccessionNumber",accessionNumber));
		int NElem=1;

		try(DirectoryReader dr =DirectoryReader.open(index)){
			IndexSearcher is = new IndexSearcher(dr);
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0)
				return Optional.empty();
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			StudyData sd=new StudyData(d.get("AccessionNumber"), d.get("Accession_Map_Number"));
			return Optional.of(sd);
		}
	}


	@Override
	public Optional<PatientData> getPatientDataById(String id) throws IOException{
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("PatientID",id));
		int NElem=1;
		try(DirectoryReader dr =DirectoryReader.open(index)){
			IndexSearcher is = new IndexSearcher(dr);
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0)
				return Optional.empty();
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			PatientData pd=new PatientData(d.get("PatientName"), d.get("PatientID"),d.get("Patient_Map_Id"));
			return Optional.of(pd);
		}
	}



	@Override
	public PatientData getPatientDataBypatientMapId(String patientMapId) throws IOException {
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("Patient_Map_Id",patientMapId));
		int NElem=1;		
		try(DirectoryReader dr =DirectoryReader.open(index)){
			IndexSearcher is = new IndexSearcher(dr);
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0)
				return null;
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			PatientData pd=new PatientData(d.get("PatientName"), d.get("PatientID"),d.get("Patient_Map_Id"));
			return pd;
		}
	}



	@Override
	public String getmapAccessionNumber(String accessionNumber) throws IOException{
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("AccessionNumber",accessionNumber));
		int NElem=1;
		try(DirectoryReader dr =DirectoryReader.open(index)){
			IndexSearcher is = new IndexSearcher(dr);
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0)
				return null;
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			StudyData sd=new StudyData(d.get("AccessionNumber"), d.get("Accession_Map_Number"));
			String mapAccessionNumber= sd.getMapAccessionNumber();
			return mapAccessionNumber;
		}
	}


	@Override
	public String getmapIdbyPatientId(String patientId) throws IOException{
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("PatientID",patientId));
		int NElem=1;
		try(DirectoryReader dr =DirectoryReader.open(index)){
			IndexSearcher is = new IndexSearcher(dr);
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0)
				return null;
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			PatientData pd=new PatientData(d.get("PatientName"), d.get("PatientID"),d.get("Patient_Map_Id"));
			String patientMapId= pd.getMapId();
			return patientMapId;
		}
	}


	@Override
	public String getmapIdbyPatientName(String patientName) throws IOException{
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("PatientName",patientName));
		int NElem=1;
		try(DirectoryReader dr =DirectoryReader.open(index)){
			IndexSearcher is = new IndexSearcher(dr);
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0)
				return null;
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			PatientData pd=new PatientData(d.get("PatientName"), d.get("PatientID"),d.get("Patient_Map_Id"));
			String patientMapId= pd.getMapId();
			return patientMapId;
		}
	}


	@Override
	public void close() throws IOException{
		index.close();
		index = null;

	}


	@Override
	public String getPatientNameByPatientMapId(String patientMapId) throws IOException {
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("Patient_Map_Id",patientMapId));
		int NElem=1;
		try(DirectoryReader dr =DirectoryReader.open(index)){
			IndexSearcher is = new IndexSearcher(dr);
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0)
				return null;
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			PatientData pd=new PatientData(d.get("PatientName"), d.get("PatientID"),d.get("Patient_Map_Id"));
			String patientName = pd.getPatientName();
			return patientName;}
	}


	@Override
	public String getPatientIdByPatientMapId(String patientMapId) throws IOException {
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("Patient_Map_Id",patientMapId));
		int NElem=1;
		try(DirectoryReader dr =DirectoryReader.open(index)){
			IndexSearcher is = new IndexSearcher(dr);
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0)
				return null;
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			PatientData pd=new PatientData(d.get("PatientName"), d.get("PatientID"),d.get("Patient_Map_Id"));
			String patientId = pd.getPatientId();
			return patientId;
		}
	}

	@Override
	public String getAccessionNumberByAccessionMapNumber(String mapAccessionNumber) throws IOException {
		if (index==null) throw new IllegalStateException();
		TermQuery termQuery = new TermQuery(new Term("Accession_Map_Number",mapAccessionNumber));
		int NElem=1;
		try(DirectoryReader dr =DirectoryReader.open(index)){
			IndexSearcher is = new IndexSearcher(dr);
			TopDocs tD= is.search(termQuery,NElem);
			if (tD.totalHits== 0)
				return null;
			int doc=tD.scoreDocs[0].doc;
			Document d = is.doc(doc);
			StudyData sd=new StudyData(d.get("AccessionNumber"), d.get("Accession_Map_Number"));
			String AccessionNumber = sd.getAccessionNumber();
			return AccessionNumber;
		}
	}

}
