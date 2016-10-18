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
	 * Test method for {@link pt.ieeta.anonymouspatientdata.core.impl.PersistantDataLucene#getStudyDataByAccessionNumber(java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testGetStudyDataByAccessionNumber() throws IOException {
		System.out.println(" testGetStudyDataByAccessionNumber\n");

		lucy.insertStudyData(studyData);

		Optional<StudyData> sd=lucy.getStudyDataByAccessionNumber(accessionNumber);
		Assert.assertTrue(sd.isPresent());
	}
	
	@Test
	public void testGetPatientDataById() throws IOException {
		System.out.println("testGetPatientDataById\n");

		lucy.insertPatientData(patientData);
		Optional<PatientData> pd=lucy.getPatientDataById(patientId);
		Assert.assertTrue(pd.isPresent());

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
