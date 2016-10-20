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

	/**
	 * @param accessionNumber
	 * @param mapAccessionNumber
	 * @param patientId
	 */
	


	public StudyData(String accessionNumber, String mapAccessionNumber) {
		super();
		this.setAccessionNumber(accessionNumber);
		this.setMapAccessionNumber(mapAccessionNumber);
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
	
	public static String createmapAccessionNumber(){
		return UUID.randomUUID().toString();
	}
	public static StudyData createWithMapping(String accessionNumber){
		StudyData studyData = new StudyData(accessionNumber,createmapAccessionNumber());
		return studyData;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this==obj)
			return true;
		if (obj==null)
			return false;
		if (obj.getClass()!=this.getClass())
			return false;
		StudyData sd = (StudyData) obj;
		if (sd.accessionNumber!=accessionNumber)
			return false;
		if (sd.mapAccessionNumber!= mapAccessionNumber)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "accessionNumber : " + accessionNumber.toString() + "\n" + "mapAccessionNumber :" + mapAccessionNumber.toString();
	}
	
}


