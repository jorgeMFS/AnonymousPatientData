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

	@Override
	public String toString() {
		return "PatientStudy [patientData=" + patientData + ", studyData=" + studyData + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((patientData == null) ? 0 : patientData.hashCode());
		result = prime * result + ((studyData == null) ? 0 : studyData.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PatientStudy other = (PatientStudy) obj;
		if (patientData == null) {
			if (other.patientData != null)
				return false;
		} else if (!patientData.equals(other.patientData))
			return false;
		if (studyData == null) {
			if (other.studyData != null)
				return false;
		} else if (!studyData.equals(other.studyData))
			return false;
		return true;
	}

}
