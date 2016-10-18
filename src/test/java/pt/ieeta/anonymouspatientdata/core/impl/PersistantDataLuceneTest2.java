/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  PersistantDataLuceneTest is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PersistantDataLuceneTest is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */

package pt.ieeta.anonymouspatientdata.core.impl;

import java.io.IOException;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */
public class PersistantDataLuceneTest2 {


	String patientName= "Jorge Miguel Ferreira da Silva";
	String patientId = "123456896564";
	String accessionNumber ="83649234623498754";
	PatientData patientData=PatientData.createWithMapping(patientName, patientId);
	StudyData studyData =StudyData.createWithMapping(accessionNumber);
	AnonDatabase lucy;
	static final String DEFAULT_ANON_PATH = "./Anon_index/";
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		lucy =new PersistantDataLucene(DEFAULT_ANON_PATH);
		
	}
	/**
	 * Test method for {@link pt.ieeta.anonymouspatientdata.core.impl.PersistantDataLucene#getPatientDataById(java.lang.String)}.
	 */
	@Test
	public void testGetPatientDataById() {
		System.out.println("testGetPatientDataById\n");
		try {
			lucy.insertPatientData(patientData);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			Optional<PatientData> pd=lucy.getPatientDataById(patientId);
			Assert.assertTrue(pd.isPresent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}