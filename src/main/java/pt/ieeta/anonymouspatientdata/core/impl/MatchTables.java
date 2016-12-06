/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  MatchTables is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  MatchTables is distributed in the hope that it will be useful,
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

import java.util.Optional;
import java.util.UUID;
import java.io.IOException;
import org.slf4j.LoggerFactory;

public class MatchTables {

	private AnonDatabase lucene=null;
	private MatchTables() {}
	private static MatchTables instance = null;
	public static synchronized MatchTables getInstance()
	{

		if (instance==null)
			instance = new MatchTables();
		return instance;

	}


	public PatientStudy createMatch( String patientId, String patientName, String accessionNumber ) throws IOException{
		if (patientId==null) {patientId= UUID.randomUUID().toString();}

		String effPatientName = (patientName != null) ? patientName : UUID.randomUUID().toString();
		String effPatientId = (patientId != null) ? patientId : UUID.randomUUID().toString();
		PatientData patientData=lucene.getPatientDataById(effPatientId).orElseGet(() -> {

			final PatientData pData2= PatientData.createWithMapping(effPatientName,effPatientId);
			try {
				lucene.insertPatientData(pData2);
			} catch (Exception e) {
				LoggerFactory.getLogger(MatchTables.class).warn("Issue while inserting PatientData",e);
			}
			return pData2;
		});
		String effAccessionNumber = (accessionNumber != null) ? accessionNumber : UUID.randomUUID().toString();
		StudyData studyData =lucene.getStudyDataByAccessionNumber(effAccessionNumber).orElseGet(() ->{
			final StudyData sData = StudyData.createWithMapping(effAccessionNumber);
			try {
				lucene.insertStudyData(sData);
			} catch (Exception e) {
				LoggerFactory.getLogger(MatchTables.class).warn("Issue while inserting StudyData",e);
			}
			return sData;
		});

		return new PatientStudy(patientData, studyData);
	}

	public Optional<PatientStudy> getMatch(String patientId, String accessionNumber)throws IOException {
		return lucene.getPatientDataById(patientId)
				.map(p -> {
					Optional<StudyData> study = Optional.empty();
					try {
						study = lucene.getStudyDataByAccessionNumber(accessionNumber);
					} catch (Exception e) {
						LoggerFactory.getLogger(MatchTables.class).warn("Issue while getStudyData By AccessionNumber",e);
					}
					return new PatientStudy(p, study.orElse(null));
				});
	}

	public void bootstrapDataBase(String path) throws IOException{

			lucene = new PersistantDataLucene(path); //new AnonCache(new PersistantDataLucene(path));
	}


	public AnonDatabase getDB(){
		return lucene;
	}

	public void close(){
		try {
			lucene.close();
		} catch (IOException e) {
			LoggerFactory.getLogger(MatchTables.class).warn("Issue while closing index",e);	
		}

	}

}
