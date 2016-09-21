/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  StudyData is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  StudyData is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ieeta.anonymouspatientdata.core.impl;

import java.util.UUID;

/**
 * @author Jorge Miguel Ferreira da Silva
 */

public class StudyData {

	private String accessionNumber;
	private String mapAccessionNumber;
	private String patientId;

	/**
	 * @param accessionNumber
	 * @param mapAccessionNumber
	 * @param patientId
	 */
	


	public StudyData(String accessionNumber, String mapAccessionNumber, String patientId) {
		super();
		this.setAccessionNumber(accessionNumber);
		this.setMapAccessionNumber(mapAccessionNumber);
		this.setPatientId(patientId);
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}
	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public String getMapAccessionNumber() {
		return mapAccessionNumber;
	}

	public void setMapAccessionNumber(String mapAccessionNumber) {
		this.mapAccessionNumber = mapAccessionNumber;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public static String createmapAccessionNumber(){
		return UUID.randomUUID().toString();
	}
	public static StudyData createWithMapping(String accessionNumber,String patientId){
		StudyData studyData = new StudyData(accessionNumber,createmapAccessionNumber(),patientId);
		return studyData;
	}
}


