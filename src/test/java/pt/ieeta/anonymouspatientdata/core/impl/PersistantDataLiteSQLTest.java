/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  PersistantDataLiteSQLTest is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PersistantDataLiteSQLTest is distributed in the hope that it will be useful,
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
import java.io.File;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;



public class PersistantDataLiteSQLTest {

	String patientName= "Jorge Miguel Ferreira da Silva";
	String patientId = "12345689656";
	String accessionNumber ="836492346234987";
	PatientData patientData=PatientData.createWithMapping(patientName, patientId);
	StudyData studyData =StudyData.createWithMapping(accessionNumber, patientId);
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		String tst="C:\\Users\\Miguel\\Desktop\\AnonymousPatientData\\test.db";
		File file = new File(tst);
		file.delete();
	}

	/**
	 * Test method for {@link pt.ieeta.anonymouspatientdata.core.impl.PersistantDataLiteSQL#CreateTable()}.
	 */
	@Test
	public final void testCreateTable() {
		PersistantDataLiteSQL sql1 = new PersistantDataLiteSQL("jdbc:sqlite:test.db");
		sql1.CreateTable();
	}

	/**
	 * Test method for {@link pt.ieeta.anonymouspatientdata.core.impl.PersistantDataLiteSQL#getPatientDataById(java.lang.String)}.
	 */
	@Test
	public final void testGetPatientDataById() {

		PersistantDataLiteSQL sql1 = new PersistantDataLiteSQL("jdbc:sqlite:test.db");
		sql1.CreateTable();
		sql1.insertPatientData(this.patientData);
		Optional<PatientData> pd= sql1.getPatientDataById(this.patientId);
		System.out.println(pd.get().getMapId().toString());
		System.out.println(pd.get().getPatientId().toString());
		System.out.println(pd.get().getPatientName().toString());
	}

	/**
	 * Test method for {@link pt.ieeta.anonymouspatientdata.core.impl.PersistantDataLiteSQL#getStudyDataByAccessionNumber(java.lang.String)}.
	 */
	@Test
	public final void testGetStudyDataByAccessionNumber() {
		PersistantDataLiteSQL sql1 = new PersistantDataLiteSQL("jdbc:sqlite:test.db");
		sql1.CreateTable();
		sql1.insertStudyData(this.studyData);
		Optional<StudyData> sd = sql1.getStudyDataByAccessionNumber(accessionNumber);
		
		}

	/**
	 * Test method for {@link pt.ieeta.anonymouspatientdata.core.impl.PersistantDataLiteSQL#insertPatientData(pt.ieeta.anonymouspatientdata.core.impl.PatientData)}.
	 */
	@Test
	public final void testInsertPatientData() {
		PersistantDataLiteSQL sql1 = new PersistantDataLiteSQL("jdbc:sqlite:test.db");
		sql1.CreateTable();
		sql1.insertPatientData(this.patientData);
	}

	/**
	 * Test method for {@link pt.ieeta.anonymouspatientdata.core.impl.PersistantDataLiteSQL#insertStudyData(pt.ieeta.anonymouspatientdata.core.impl.StudyData)}.
	 */
	@Test
	public final void testInsertStudyData() {
		PersistantDataLiteSQL sql1 = new PersistantDataLiteSQL("jdbc:sqlite:test.db");
		sql1.CreateTable();
		sql1.insertStudyData(this.studyData);
	}

}
