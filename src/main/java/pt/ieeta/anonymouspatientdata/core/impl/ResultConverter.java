/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  ResultConverter is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ResultConverter is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ieeta.anonymouspatientdata.core.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import org.slf4j.LoggerFactory;

import pt.ua.dicoogle.sdk.datastructs.SearchResult;

/**
 * @author Jorge Miguel Ferreira da Silva
 * 
 */
public class ResultConverter {

	AnonDatabase lucy;
	public ResultConverter(AnonDatabase lucy) {
		this.lucy=lucy;
	}


	public SearchResult transform(SearchResult searchReasult) throws IOException{

		HashMap<String, Object> hM = new HashMap<>(searchReasult.getExtraData());
		if (hM.isEmpty())return searchReasult;

		if (hM.get("PatientID")==null) return searchReasult;
		String MapId =hM.get("PatientID").toString().trim();
		LoggerFactory.getLogger(ResultConverter.class).info("Attempting to convert result: Map ID = {}", MapId);

		Optional<PatientData> patientData = lucy.getPatientDataBypatientMapId(MapId);

		if (patientData.isPresent()){
			LoggerFactory.getLogger(ResultConverter.class).info("> Found patient data");
			if (patientData.get().getPatientName() != null) {
				hM.put("PatientName", patientData.get().getPatientName());
			}
			if (patientData.get().getPatientId() != null) {
				hM.put("PatientID", patientData.get().getPatientId());
			}
		}
		if(hM.get("AccessionNumber") != null){
			String acessNumb=hM.get("AccessionNumber").toString().trim();
			Optional<String> accessionNumber=lucy.getAccessionNumberByAccessionMapNumber(acessNumb);

			if (accessionNumber.isPresent()){
				hM.put("AccessionNumber", accessionNumber.get());
			}
		}
		SearchResult sR= new SearchResult(searchReasult.getURI(),searchReasult.getScore(), hM);
		return sR;
	}

}
