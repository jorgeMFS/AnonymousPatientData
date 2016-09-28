/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  PatientStudy is free software: you can redistribute it and/or modify
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

public class PatientStudy {

	private PatientData patientData;
	private StudyData studyData;

	/**
	 * @param patientData
	 * @param studyData
	 */

	public PatientStudy(PatientData patientData, StudyData studyData) {
		this.patientData = patientData;
		this.studyData = studyData;
	}

	public PatientData getPatientData() {
		return patientData;
	}
	public void setPatientData(PatientData patientData) {
		this.patientData = patientData;
	}
	public StudyData getStudyData() {
		return studyData;
	}
	public void setStudyData(StudyData studyData) {
		this.studyData = studyData;
	}

}
