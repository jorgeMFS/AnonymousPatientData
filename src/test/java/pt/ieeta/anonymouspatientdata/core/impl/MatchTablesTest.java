/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  MatchTablesTest is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  MatchTablesTest is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ieeta.anonymouspatientdata.core.impl;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */
public class MatchTablesTest {
	String patientName= "Jorge Miguel";
	String patientId = "234643501";
	String accessionNumber ="836492346234987";
	String Location="jdbc:sqlite:test.db";
	PatientData patientData=PatientData.createWithMapping(patientName, patientId);
	StudyData studyData =StudyData.createWithMapping(accessionNumber, patientId);
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MatchTables.getInstance().bootstrapDataBase(Location);
	}
	
	/**
	 * Test method for {@link pt.ieeta.anonymouspatientdata.core.impl.MatchTables#createMatch(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testCreateMatch() {
		
		PatientStudy ps =MatchTables.getInstance().createMatch(patientId, patientName, accessionNumber);
		Assert.assertNotNull(ps);
		
		System.out.println("getMapId :"+ ps.getPatientData().getMapId());
		System.out.println("getPatientId :"+ ps.getPatientData().getPatientId());
		System.out.println("getPatientName :"+ ps.getPatientData().getPatientName());
		System.out.println("getAccessionNumber :"+ ps.getStudyData().getAccessionNumber());
		System.out.println("getMapAccessionNumber :"+ ps.getStudyData().getMapAccessionNumber());
		
		PatientStudy ps2 =MatchTables.getInstance().createMatch("15984753", "João Paulo Ferreira da Silva", "74123698789632145");
		Assert.assertNotNull(ps);
		Assert.assertNotNull(ps2);
	}

	/**
	 * Test method for {@link pt.ieeta.anonymouspatientdata.core.impl.MatchTables#getMatch(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testGetMatch() {
		Optional<PatientStudy> ps =MatchTables.getInstance().getMatch(patientId, accessionNumber);
		Assert.assertNotNull(ps);
		Optional<PatientStudy> ps1 =MatchTables.getInstance().getMatch("1234588787815212145","00987654325364636324");
		Assert.assertFalse(ps1.isPresent());
	}
}
