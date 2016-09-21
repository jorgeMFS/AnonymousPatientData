/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  PatientData is free software: you can redistribute it and/or modify
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

import java.util.UUID;

/**
 * @author Jorge Miguel Ferreira da Silva
 */

public class PatientData {


	private String patientName;
	private String patientId;
	private String mapId; 


	/**
	 * @param patientName
	 * @param mapName
	 * @param patientId
	 * @param mapId
	 */

	public PatientData(String patientName, String patientId, String mapId) {
		super();
		this.setPatientName(patientName);
		this.patientId = patientId;
		this.setMapId(mapId);
	}
	
	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	
	public static String createmapId(){
		return UUID.randomUUID().toString();
	}
	
	public static PatientData createWithMapping(String patientName,String patientId){
		PatientData patientData = new PatientData(patientName,patientId,createmapId());
		return patientData;
	}
}

