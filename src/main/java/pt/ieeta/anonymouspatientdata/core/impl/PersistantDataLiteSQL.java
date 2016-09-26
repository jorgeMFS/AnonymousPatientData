/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of PersistantDataLiteSQL.
 *
 *  PersistantDataLiteSQL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PersistantDataLiteSQL is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */

package pt.ieeta.anonymouspatientdata.core.impl;
/**
 * @author Jorge Miguel Ferreira da Silva
 */

import java.util.Iterator;

import java.util.Optional;

import org.sql2o.Connection;
import org.sql2o.Sql2o;


public class PersistantDataLiteSQL {
	private final Sql2o sql2o;


	public PersistantDataLiteSQL(String local){
		sql2o= new Sql2o(local, "", "");
	}

	public void CreateTable(){

		String sqlTable1 ="CREATE TABLE IF NOT EXISTS MatchTablePatient" +
				"(patientId varchar(255) not NULL," +
				"mapId varchar(255) not NULL," +
				"patientName varchar(255) not NULL,"+
				"PRIMARY KEY (patientId))";

		String sqlTable2="CREATE TABLE IF NOT EXISTS MatchTableStudy" +
				"(accessionNumber varchar(255) UNIQUE not NULL," + 
				"patientId varchar(255) not NULL,"+
				"mapAccessionNumber varchar(255) not NULL," +
				"PRIMARY KEY (mapAccessionNumber),"+
				"FOREIGN KEY (patientId) REFERENCES MatchTablePatient(patientId))";


		try( Connection con = sql2o.open()){

			con.createQuery(sqlTable1).executeUpdate();
			con.createQuery(sqlTable2).executeUpdate();

		}
	}

	public Optional<PatientData> getPatientDataById(String id){

		String sql =
				"SELECT patientId, mapId, patientName " +
						"FROM MatchTablePatient " +
						"WHERE patientId == :id";

		try(Connection con = sql2o.open()) {
			Iterator<PatientData> it= con.createQuery(sql).addParameter("id", id)
					.executeAndFetch(PatientData.class).iterator();
			if (it.hasNext()) return Optional.of(it.next());
			return Optional.empty();
		}
	}

	public Optional<StudyData> getStudyDataByAccessionNumber(String accessionNumber){

		String sql =
				"SELECT accessionNumber, mapAccessionNumber, patientId " +
						"FROM MatchTableStudy " +
						"WHERE accessionNumber == :accessionNumber";

		try(Connection con = sql2o.open()) {
			Iterator<StudyData> it= con.createQuery(sql).addParameter("accessionNumber", accessionNumber)
					.executeAndFetch(StudyData.class).iterator();
			if (it.hasNext()) return Optional.of(it.next());
			return Optional.empty();
		}
	}

	public void insertPatientData(PatientData patientData){
		String sql= "INSERT INTO MatchTablePatient(patientId, mapId, patientName)" +
				"VALUES (:patientId, :mapId,:patientName)";
		try(Connection con = sql2o.open()) {
			con.createQuery(sql).bind(patientData).executeUpdate();
		}
	}

	public void insertStudyData(StudyData studyData){
		String sql= "INSERT INTO MatchTableStudy(accessionNumber, mapAccessionNumber, patientId)" +
				"VALUES (:accessionNumber, :mapAccessionNumber,:patientId)";
		try(Connection con = sql2o.open()) {
			con.createQuery(sql).bind(studyData).executeUpdate();
		}
	}



//	public Optional<PatientData> AccessionNumberMapAndPatientIdbyPatientName(String patientName){
//		String sql ="SELECT MatchTableStudy.mapAccessionNumber, MatchTableStudy.patientId" +
//				"FROM MatchTableStudy,MatchTablePatient " +
//				"WHERE MatchTableStudy.patientId == :MatchTablePatient.patientId"+
//				"MatchTablePatient.patientName=:patientName";
//
//		try(Connection con = sql2o.open()) {
//			Iterator<PatientData> it= con.createQuery(sql).addParameter("patientName", patientName)
//					.executeAndFetch(PatientData.class).iterator();
//			if (it.hasNext()) return Optional.of(it.next());
//			return Optional.empty();
//		}
//	}

//	public Optional<StudyData> AccessionNumberMapAndPatientIdbyPatientId(String patientId){
//		String sql ="SELECT mapAccessionNumber, patientId" +
//				"FROM MatchTableStudy " +
//				"WHERE patientId == :patientId";
//
//		try(Connection con = sql2o.open()) {
//			Iterator<StudyData> it= con.createQuery(sql).addParameter("patientId", patientId)
//					.executeAndFetch(StudyData.class).iterator();
//			if (it.hasNext()) return Optional.of(it.next());
//			return Optional.empty();
//		}
//	}
//
//	public Optional<StudyData> AccessionNumberMapAndPatientIdbyAccessionNumber(String accessionNumber){
//		String sql ="SELECT mapAccessionNumber, patientId" +
//				"FROM MatchTableStudy " +
//				"WHERE accessionNumber == :accessionNumber";
//
//		try(Connection con = sql2o.open()) {
//			Iterator<StudyData> it= con.createQuery(sql).addParameter("accessionNumber", accessionNumber)
//					.executeAndFetch(StudyData.class).iterator();
//			if (it.hasNext()) return Optional.of(it.next());
//			return Optional.empty();
//		}
//	}



}
