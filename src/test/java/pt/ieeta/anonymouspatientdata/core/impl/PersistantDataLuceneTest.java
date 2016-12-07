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
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */
public class PersistantDataLuceneTest {



	AnonDatabase lucy;
	static final String DEFAULT_ANON_PATH = System.getProperty("java.tmp.dir") + "/Anon_index/";
	PatientData pd;
	StudyData sd;
	StudyData sd2;
	final String PatientID="000";
	final String PatientName="Patient^Anonymous";
	final String MAPID= "1f46ddfb-73ed-4f12-b897-8f54354a8789";
	final String AccessionNumber="000";
	final String ACCESSMAPID="97b08c16-b688-4b03-a26f-4c4bc6d22f09";
	final String AccessionNumber2="111";
	final String ACCESSMAPID2="97b08c16-b688-4b03-a26f-4c4bc6d22f03";	

	@Before 
	public void create()throws IOException{
		//Patient1
		pd=new PatientData(PatientName, PatientID,MAPID);
		sd= new StudyData(AccessionNumber, ACCESSMAPID);
		sd2= new StudyData(AccessionNumber2, ACCESSMAPID2);

	}

	@Before
	public void setUp() throws Exception {
		Files.createDirectories(Paths.get(DEFAULT_ANON_PATH));
		lucy =new PersistantDataLucene(DEFAULT_ANON_PATH);
	}


	/**
	 * Test method for {@link pt.ieeta.anonymouspatientdata.core.impl.PersistantDataLucene#getStudyDataByAccessionNumber(java.lang.String)}.
	 * @throws IOException 
	 */
	
	@Test
	public void testGetStudyDataByAccessionNumber() throws IOException {
		Optional<StudyData> test=lucy.getStudyDataByAccessionNumber(AccessionNumber);
		Assert.assertFalse(test.isPresent());
		
		lucy.insertStudyData(this.sd);
		lucy.insertStudyData(this.sd2);
		Optional<StudyData> sdo=lucy.getStudyDataByAccessionNumber(AccessionNumber);
		Optional<StudyData> sdo2=lucy.getStudyDataByAccessionNumber(AccessionNumber2);
		Assert.assertEquals(sdo2.get(),sd2);
		Assert.assertEquals(sdo.get(),sd);
	}

	@Test
	public void testGetStudyDataByMapAccessionNumber() throws IOException {
		lucy.insertStudyData(this.sd);
		lucy.insertStudyData(this.sd2);
		Optional<String> sdo = lucy.getAccessionNumberByAccessionMapNumber(ACCESSMAPID);
		Assert.assertEquals(sdo.get(), AccessionNumber);

		Optional<String> sdo2 = lucy.getAccessionNumberByAccessionMapNumber(ACCESSMAPID2);
		Assert.assertEquals(sdo2.get(), AccessionNumber2);
	}
	
	@Test
	public void testGetPatientDataById() throws IOException {
		Optional<PatientData> test=lucy.getPatientDataById(PatientID);
		Assert.assertFalse(test.isPresent());
		
		lucy.insertPatientData(pd);
		Optional<PatientData> pdo=lucy.getPatientDataById(PatientID);
		Assert.assertTrue(pdo.isPresent());
		Assert.assertEquals(pdo.get(),pd);
	}

	@Test
	public void testGetPatientDataByMapId() throws IOException {
		Optional<PatientData> test=lucy.getPatientDataBypatientMapId(MAPID);
		Assert.assertFalse(test.isPresent());
		
		lucy.insertPatientData(pd);
		Optional<PatientData> pdo=lucy.getPatientDataBypatientMapId(MAPID);
		Assert.assertTrue(pdo.isPresent());
		Assert.assertEquals(pdo.get(),pd);
	}

	@Test
	public void testPNandIDByMapId() throws IOException {
		Optional<String> pn = lucy.getPatientNameByPatientMapId(MAPID);
		Assert.assertFalse(pn.isPresent());

		Optional<String> pid = lucy.getPatientIdByPatientMapId(MAPID);
		Assert.assertFalse(pid.isPresent());
		
		lucy.insertPatientData(pd);
		Optional<String> pdo = lucy.getPatientNameByPatientMapId(MAPID);
		Assert.assertTrue(pdo.isPresent());
		Assert.assertEquals(pdo.get(), PatientName);

		Optional<String> pdo2 = lucy.getPatientIdByPatientMapId(MAPID);
		Assert.assertTrue(pdo2.isPresent());
		Assert.assertEquals(pdo2.get(), PatientID);
	}

	@After
	public void cleanUp() throws IOException, InterruptedException {
		lucy.close();
		Path dbPath = Paths.get(DEFAULT_ANON_PATH);
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
