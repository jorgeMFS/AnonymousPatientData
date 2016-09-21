package pt.ieeta.anonymouspatientdata.core.impl;

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
