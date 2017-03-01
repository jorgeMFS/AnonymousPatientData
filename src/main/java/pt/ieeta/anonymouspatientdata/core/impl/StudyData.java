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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessionNumber == null) ? 0 : accessionNumber.hashCode());
		result = prime * result + ((mapAccessionNumber == null) ? 0 : mapAccessionNumber.hashCode());
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
		StudyData other = (StudyData) obj;
		if (accessionNumber == null) {
			if (other.accessionNumber != null)
				return false;
		} else if (!accessionNumber.equals(other.accessionNumber))
			return false;
		if (mapAccessionNumber == null) {
			if (other.mapAccessionNumber != null)
				return false;
		} else if (!mapAccessionNumber.equals(other.mapAccessionNumber))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "StudyData [accessionNumber=" + accessionNumber + ", mapAccessionNumber=" + mapAccessionNumber + "]";
	}
	
}


