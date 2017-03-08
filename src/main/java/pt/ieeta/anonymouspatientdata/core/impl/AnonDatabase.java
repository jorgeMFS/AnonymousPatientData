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

/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AnonDatabase {

	void insertStudyData(StudyData studyData) throws IOException;

	void insertPatientData(PatientData patientData) throws IOException;

	Optional<StudyData> getStudyDataByAccessionNumber(String accessionNumber) throws IOException;

	Optional<PatientData> getPatientDataById(String id) throws IOException;

	Optional<PatientData> getPatientDataBypatientMapId(String patientMapId) throws IOException;
	
	Optional<String> getmapAccessionNumber(String accessionNumber) throws IOException;

	Optional<String> getmapIdbyPatientId(String patientId) throws IOException;

	List<String> getvariousmapIdbyPatientName (String patientName) throws IOException;

	void close() throws IOException;

	Optional<String> getPatientNameByPatientMapId(String patientMapId) throws IOException;

	Optional<String> getPatientIdByPatientMapId(String patientMapId) throws IOException;

	Optional<String> getAccessionNumberByAccessionMapNumber(String mapAccessionNumber) throws IOException;

}