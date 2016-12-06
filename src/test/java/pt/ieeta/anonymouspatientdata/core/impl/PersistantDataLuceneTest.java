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
import org.mockito.Mock;
import org.mockito.Mockito;

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
	final String ID="1";
	final String NAME="a1";
	final String MAPID= "123";
	final String ACCESSNUMB="19";
	final String ACCESSMAPID="321";
	final String ACCESSNUMB2="29";
	final String ACCESSMAPID2="4321";	

	@Before 
	public void create()throws IOException{
		//Patient1
		pd=new PatientData(NAME, ID,MAPID);
		sd= new StudyData(ACCESSNUMB, ACCESSMAPID);
		sd2= new StudyData(ACCESSNUMB2, ACCESSMAPID2);

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
		System.out.println(" testGetStudyDataByAccessionNumber\n");

		Optional<StudyData> test=lucy.getStudyDataByAccessionNumber(ACCESSNUMB);
		Assert.assertFalse(test.isPresent());
		
		lucy.insertStudyData(this.sd);
		lucy.insertStudyData(this.sd2);
		Optional<StudyData> sdo=lucy.getStudyDataByAccessionNumber(ACCESSNUMB);
		Optional<StudyData> sdo2=lucy.getStudyDataByAccessionNumber(ACCESSNUMB2);
		Assert.assertEquals(sdo2.get(),sd2);
		Assert.assertEquals(sdo.get(),sd);
	}


	@Test
	public void testGetPatientDataById() throws IOException {
		System.out.println("testGetPatientDataById\n");

		Optional<PatientData> test=lucy.getPatientDataById(ID);
		Assert.assertFalse(test.isPresent());
		
		
		lucy.insertPatientData(pd);
		Optional<PatientData> pdo=lucy.getPatientDataById(ID);
		Assert.assertEquals(pdo.get(),pd);

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
