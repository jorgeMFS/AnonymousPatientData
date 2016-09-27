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
import org.sql2o.Sql2oException;

/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */
public class MatchTablesTest {
	final String patientName= "Jorge Miguel";
	final String patientId = "234643501";
	final String accessionNumber ="836492346234987";
	final String Location="./Test_index/";
	PatientData patientData=PatientData.createWithMapping(patientName, patientId);
	StudyData studyData =StudyData.createWithMapping(accessionNumber);
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MatchTables.getInstance().bootstrapDataBase(Location);
	}

	/**
	 * Test method for {@link pt.ieeta.anonymouspatientdata.core.impl.MatchTables#createMatch(java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 * @throws Sql2oException 
	 */
	@Test
	public final void testCreateMatch() throws IOException {

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
	 * @throws IOException 
	 * @throws Sql2oException 
	 */
	@Test
	public final void testGetMatch() throws IOException {

		Optional<PatientStudy> ps =MatchTables.getInstance().getMatch(patientId, accessionNumber);
		Assert.assertNotNull(ps);
		Assert.assertTrue(ps.isPresent());
		Assert.assertNotNull(ps.get().getPatientData());
		Assert.assertNotNull(ps.get().getPatientData().getPatientId());
		Assert.assertNotNull(ps.get().getStudyData());
		Assert.assertNotNull(ps.get().getStudyData().getAccessionNumber());

		// this one must not be in the index
		Optional<PatientStudy> ps1 =MatchTables.getInstance().getMatch("1234588787815212145","00987654325364636324");
		Assert.assertNotNull(ps);
		Assert.assertFalse(ps1.isPresent());
	}




	@After
	public void cleanUp() throws IOException {
		MatchTables.getInstance().close();
		Path dbPath = Paths.get(Location);
		Files.walkFileTree(dbPath, new FileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
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
