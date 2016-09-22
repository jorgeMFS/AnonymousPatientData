/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of MatchTables.
 *
 *  MatchTables is free software: you can redistribute it and/or modify
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
package pt.ieeta.anonymouspatientdata.core;

import java.io.File;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ieeta.anonymouspatientdata.core.impl.MatchTables;

/**
 * @author Jorge Miguel Ferreira da Silva
 */

public class AnonimizeDicomObjectTest {

	DicomObject dcmObjc;
	DicomInputStream dis;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MatchTables.getInstance().bootstrapDataBase("jdbc:sqlite:test.db");
		System.out.println("Setup Test");
		String str ="C:\\Users\\Miguel\\Dropbox\\UACMUPERITIC00282014\\1_1.dcm"; 
		dis =new DicomInputStream(new File(str));
		this.dcmObjc=dis.readDicomObject();
		dis.close();
	}

	/**
	 * Test method for {@link pt.ieeta.anonymouspatientdata.core.AnonimizeDicomObject#anonymizeObject(org.dcm4che2.data.DicomObject)}.
	 */
	@Test
	public final void testAnonymizeObject() {
		String patientName=dcmObjc.getString(Tag.PatientName);
		String patientID=dcmObjc.getString(Tag.PatientID);
		String accessionNumber=dcmObjc.getString(Tag.AccessionNumber);
		AnonimizeDicomObject.anonymizeObject(dcmObjc);
		String patientNameAnon=dcmObjc.getString(Tag.PatientName);
		String patientIDAnon=dcmObjc.getString(Tag.PatientID);
		String accessionNumberAnon=dcmObjc.getString(Tag.AccessionNumber);
		Assert.assertEquals(patientNameAnon, patientIDAnon);
		System.out.println("patientName :"+ patientName);
		System.out.println("patientID :"+ patientID);
		System.out.println("accessionNumber :"+ accessionNumber);
		System.out.println("patientNameAnon :"+ patientNameAnon);
		System.out.println("patientIDAnon :"+ patientIDAnon);
		System.out.println("accessionNumberAnon :"+ accessionNumberAnon);
		
	}

}
