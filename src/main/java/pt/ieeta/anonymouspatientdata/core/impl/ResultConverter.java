/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  PersistantDataLiteSQL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PersistantDataLiteSQL is distributed in the hope that it will be useful,
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

import pt.ua.dicoogle.sdk.datastructs.SearchResult;

/**
 * @author Jorge Miguel Ferreira da Silva
 * 
 */
public class ResultConverter {

	PersistantDataLucene Lucy= new PersistantDataLucene();
	public ResultConverter() {

	}


	public SearchResult transform(SearchResult searchReasult) throws IOException{

		HashMap<String, Object> hM = searchReasult.getExtraData();
		hM.replace("PatientName", hM.get("PatientName"), Lucy.getPatientNameByPatientMapId(hM.get("PatientName").toString()));
		hM.replace("PatientId", hM.get("PatientId"), Lucy.getPatientIdByPatientMapId(hM.get("PatientId").toString()));
		hM.replace("AccessionNumber", hM.get("AccessionNumber"), Lucy.getAccessionNumberByAccessionMapNumber(hM.get("AccessionNumber").toString()));
		SearchResult sR= new SearchResult(searchReasult.getURI(),searchReasult.getScore(), hM);
		return sR;
	}







}
