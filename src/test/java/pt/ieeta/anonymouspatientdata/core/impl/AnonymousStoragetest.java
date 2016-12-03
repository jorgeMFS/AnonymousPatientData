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
package pt.ieeta.anonymouspatientdata.core.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ieeta.anonymouspatientdata.core.AnonimizeDicomObject;
import pt.ieeta.anonymouspatientdata.core.impl.MatchTables;
import pt.ieeta.anonymouspatientdata.pluginset.storage.AnonymousStorage;


/**
 *  @author Jorge Miguel Ferreira da Silva
 *
 */


public class AnonymousStoragetest {
	final String Location="./Test_index2/";
	AnonymousStorage storage;
	DicomObject dcmObj;
	DicomInputStream dis;
	URI uri;

	@Before
	public void setUp() throws Exception {
		this.storage= new AnonymousStorage();
		System.out.println("Setup Test");
		String str ="./src/data/test.dcm"; 
		dis =new DicomInputStream(new File(str));
		this.dcmObj=dis.readDicomObject();
		dis.close();
		MatchTables.getInstance().bootstrapDataBase(Location);
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
		Assert.assertSame("Anonymous-Wrapper-Plugin",this.storage.getName());
	}

	@Test
	public void validateStoreOutput() throws IOException{
		String patientNameexpected=dcmObj.getString(Tag.PatientName);
		AnonimizeDicomObject.anonymizeObject(this.dcmObj);
		Assert.assertEquals(dcmObj.get(Tag.SOPInstanceUID).toString(), dcmObj.get(Tag.SOPInstanceUID).toString());
		System.out.println("Anonymize DICOM Test");
		System.out.println(patientNameexpected);
		System.out.println(dcmObj.get(Tag.PatientName));
		Assert.assertNotEquals(patientNameexpected, dcmObj.get(Tag.PatientName));
	}

	@After
	public void cleanUp() throws IOException, InterruptedException {
		MatchTables.getInstance().close();
		Path dbPath = Paths.get(Location);
		Files.walkFileTree(dbPath, new FileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.deleteIfExists(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
				if (e == null) {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				} else {
					throw e;
				}
			}
		});
	}
}