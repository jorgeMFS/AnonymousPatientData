/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of MatchTables.
 *
 *  MatchTables is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PatientData is distributed in the hope that it will be useful,
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

import org.sql2o.Sql2oException;

public class MatchTables {
	
	private PersistantDataLiteSQL sql=null;
	private MatchTables() {}
	private static MatchTables instance = null;
	public static MatchTables getInstance()
	{

		if (instance==null)
			instance = new MatchTables();
		return instance;

	}


	public PatientStudy createMatch( String patientId, String patientName, String accessionNumber ) throws Sql2oException{

		PatientData patientData=sql.getPatientDataById(patientId).orElseGet(() -> {
			final PatientData pData2= PatientData.createWithMapping(patientName,patientId);
			sql.insertPatientData(pData2);
			return pData2;
		});

		StudyData studyData =sql.getStudyDataByAccessionNumber(accessionNumber).orElseGet(() ->{
			final StudyData sData = StudyData.createWithMapping(accessionNumber,patientId);
			return sData;
		});

		return new PatientStudy(patientData, studyData);
	}

	public Optional<PatientStudy> getMatch(String patientId, String accessionNumber)throws Sql2oException {
		return sql.getPatientDataById(patientId)
				.map(p -> {
					final Optional<StudyData> study = sql.getStudyDataByAccessionNumber(accessionNumber);
					return new PatientStudy(p, study.orElse(null));
				});
	}

	public void loadDataBase(String dbLocation){
	sql = new PersistantDataLiteSQL(dbLocation);
	}
}
