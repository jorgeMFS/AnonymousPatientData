/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousStoragetest.
 *
 *  AnonymousStoragetest is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AnonymousPluginSet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ieeta.anonymouspatientdata.pluginset.storage;

import java.io.File;
import java.net.URI;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ieeta.anonymouspatientdata.core.AnonimizeDicomObject;


/**
 *  @author Jorge Miguel Ferreira da Silva
 *
 */


public class AnonymousStoragetest {

	AnonymousStorage storage;
	DicomObject dcmObj;
	DicomInputStream dis;
	URI uri;

	@Before
	public void setUp() throws Exception {
		this.storage= new AnonymousStorage();
		System.out.println("Setup Test");
		String str ="file:/Users/Miguel/Dropbox/UACMUPERITIC00282014/1_1.dcm"; 
		this.uri =new URI(str); 
		dis =new DicomInputStream(new File(uri));
		this.dcmObj=dis.readDicomObject();
		dis.close();
	}

	@Test
	public void enableValidOutput(){
		System.out.println("enable Anonymous Test");
		Assert.assertFalse(this.storage.disable());
		Assert.assertTrue(this.storage.enable());
		Assert.assertEquals(true, storage.isEnabled());

	}


	@Test
	public void ValidOutput(){
		System.out.println("URI handle Anonymous Test");
		Assert.assertFalse(storage.handles(uri));
		Assert.assertSame("Anonymous-Filesystem-dgPlugin",this.storage.getName());
	}

	@Test
	public void validateStoreOutput(){
		String patientNameexpected=dcmObj.getString(Tag.PatientName);
		AnonimizeDicomObject.anonymizeObject(this.dcmObj);
		Assert.assertEquals(dcmObj.get(Tag.SOPInstanceUID).toString(), dcmObj.get(Tag.SOPInstanceUID).toString());
		System.out.println("Anonymize DICOM Test");
		System.out.println(patientNameexpected);
		System.out.println(dcmObj.get(Tag.PatientName));
		Assert.assertNotEquals(patientNameexpected, dcmObj.get(Tag.PatientName));
	}






}
