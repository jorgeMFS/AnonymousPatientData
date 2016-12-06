/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  ResultConverterTest is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ResultConverterTest is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ieeta.anonymouspatientdata.core.impl;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import pt.ua.dicoogle.sdk.datastructs.SearchResult;

/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */
public class ResultConverterTest {
	Map<String,PatientData> patientDataMap;
	Map<String, String> studyDataMap;
	HashMap<String,Object> data;
	PatientData Pd;
	URI location;
	@Mock
	AnonDatabase Anon;

	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	static final String ID="1";
	static final String NAME="Jorge Miguel";
	static final String MAPID="123";
	static final String ACCESSNUMB="19";
	static final String ACCESSMAPID="321";
	
	@Before 
	public void create()throws IOException{
		Anon = Mockito.mock(AnonDatabase.class);
		//Patient1

		//Mock Patient1
		when(Anon.getPatientDataBypatientMapId(MAPID))
		.thenReturn(Optional.of(new PatientData(NAME, ID, MAPID)));
		when(Anon.getStudyDataByAccessionNumber(ACCESSMAPID))
		.thenReturn(Optional.of(new StudyData(ACCESSNUMB, ACCESSMAPID)));
		when(Anon.getmapAccessionNumber(ACCESSNUMB))
		.thenReturn(Optional.of(ACCESSMAPID));
		when(Anon.getAccessionNumberByAccessionMapNumber(ACCESSMAPID))
		.thenReturn(Optional.of(ACCESSNUMB));		
		when(Anon.getPatientIdByPatientMapId(MAPID))
		.thenReturn(Optional.of(ID));
		when(Anon.getPatientNameByPatientMapId(MAPID))
		.thenReturn(Optional.of(NAME));		
		when(Anon.getmapIdbyPatientId(ID))
		.thenReturn(Optional.of(MAPID));
		when(Anon.getmapIdbyPatientName(NAME))
		.thenReturn(Optional.of(MAPID));
	}

	@Before
	public void setUp() throws Exception {
		patientDataMap= new HashMap<String, PatientData>();
		studyDataMap=new HashMap<String, String>() ;

		location= new URI("test");
		String patientId = ID;
		String accessionNumber = ACCESSNUMB;
		String patientName = NAME;
		String patientMapId = MAPID;
		String accessionMapNumber=ACCESSMAPID;
		
		PatientData Pd=new PatientData(patientName, patientId,patientMapId);
		patientDataMap.put(Pd.getMapId(), Pd);
		studyDataMap.put(accessionMapNumber,accessionNumber);
	}


	@Test
	public void test() throws IOException {

		ResultConverter rC =new ResultConverter(Anon);

		HashMap<String, Object> data= new HashMap<String,Object>();
		data.put("PatientName", NAME);
		data.put("PatientID", MAPID);
		data.put("AccessionNumber", ACCESSMAPID);

		SearchResult rs=new SearchResult(location, 0, data );

		SearchResult rs1 = rC.transform(rs);
		
		Assert.assertTrue(rs1.get("PatientName") instanceof String);
		Assert.assertTrue(rs1.get("PatientID") instanceof String);
		Assert.assertTrue(rs1.get("AccessionNumber") instanceof String);

		Assert.assertEquals(NAME, rs1.get("PatientName"));
		Assert.assertEquals(ID, rs1.get("PatientID"));
		Assert.assertEquals(ACCESSNUMB, rs1.get("AccessionNumber"));
		Assert.assertEquals(location, rs1.getURI());
		Assert.assertEquals(0, rs1.getScore(),1e-8);
	}













}
