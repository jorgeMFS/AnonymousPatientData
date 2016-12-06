/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  AnonCache is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AnonCache is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */

package pt.ieeta.anonymouspatientdata.core.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import pt.ieeta.anonymouspatientdata.core.util.RuntimeIOException;
/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */
public class AnonCache implements AnonDatabase {


	private final AnonDatabase anonDb;

	private  Map<String, PatientData> patientDataMap=new HashMap<String, PatientData>();
	private  Map<String, String> studyDataMap=new HashMap<String, String>();
	//private final int MAXSIZEPATIENTDATA=50;
	//private final int MAXSIZESTUDYDATA=1000;


	public AnonCache(AnonDatabase anonDb) {
		this.anonDb= anonDb;
	}



	@Override
	public void insertStudyData(StudyData studyData) throws IOException {
		this.anonDb.insertStudyData(studyData);
	}


	@Override
	public void insertPatientData(PatientData patientData) throws IOException {
		this.anonDb.insertPatientData(patientData);
	}


	@Override
	public Optional<StudyData> getStudyDataByAccessionNumber(String accessionNumber) throws IOException {
		return this.anonDb.getStudyDataByAccessionNumber(accessionNumber);
	}


	@Override
	public Optional<PatientData> getPatientDataById(String id) throws IOException {
		return this.anonDb.getPatientDataById(id);
	}


	@Override
	public Optional<String> getmapAccessionNumber(String accessionNumber) throws IOException {
		return this.anonDb.getmapAccessionNumber(accessionNumber);
	}


	@Override
	public Optional<String> getmapIdbyPatientId(String patientId) throws IOException {
		return this.anonDb.getmapIdbyPatientId(patientId);
	}


	@Override
	public Optional<String> getmapIdbyPatientName(String patientName) throws IOException {
		return this.anonDb.getmapIdbyPatientName(patientName);

	}


	@Override
	public void close() throws IOException {
		this.anonDb.close();
	}


	@Override
	public Optional<String> getPatientNameByPatientMapId(String patientMapId) throws IOException {

		if(this.patientDataMap.containsKey(patientMapId)) {
			return Optional.of(this.patientDataMap.get(patientMapId).getPatientName());
		}

		try{
			Optional<PatientData>  pd = Optional.ofNullable(patientDataMap.computeIfAbsent(patientMapId, (k) -> {try
			{return this.anonDb.getPatientDataBypatientMapId(k).orElse(null);
			}
			catch (IOException e) {
				throw new RuntimeIOException(e);}

			}));
			return pd.map(PatientData::getPatientName);
		}
		catch(RuntimeIOException e){
			throw new IOException(e);
		}

	}


	@Override
	public Optional<String> getPatientIdByPatientMapId(String patientMapId) throws IOException {
		if(this.patientDataMap.containsKey(patientMapId)) {
			return Optional.of(this.patientDataMap.get(patientMapId).getPatientId());
		}

		try{
			Optional<PatientData> pd = Optional.ofNullable(patientDataMap.computeIfAbsent(patientMapId, (k) -> {try{
				return this.anonDb.getPatientDataBypatientMapId(k).orElse(null);
			}
			catch (IOException e) {
				throw new RuntimeIOException(e);}
			}));
			return pd.map(PatientData::getPatientId);
		}
		catch(RuntimeIOException e){
			throw new IOException(e);
		}
	}


	@Override
	public Optional<String> getAccessionNumberByAccessionMapNumber(String mapAccessionNumber) throws IOException {
		if(this.studyDataMap.containsKey(mapAccessionNumber)) {
			return Optional.of(this.studyDataMap.get(mapAccessionNumber));
		}
		try{
			Optional<String> pd = Optional.ofNullable( studyDataMap.computeIfAbsent(mapAccessionNumber, (k) -> {try
			{return this.anonDb.getAccessionNumberByAccessionMapNumber(k).orElse(null);
			}

			catch (IOException e) {
				throw new RuntimeIOException(e);}
			}));
			
			return pd;

		}
		catch(RuntimeIOException e){
			throw new IOException(e);
		}
	}


	@Override
	public Optional<PatientData> getPatientDataBypatientMapId(String patientMapId) throws IOException {
		if(this.patientDataMap.containsKey(patientMapId)) {
			return Optional.of(this.patientDataMap.get(patientMapId));
		}
		try{
			Optional<PatientData> pd = Optional.ofNullable(patientDataMap.computeIfAbsent(patientMapId, (k) -> {try{return this.anonDb.getPatientDataBypatientMapId(k).orElse(null);
			}
			catch (IOException e) {
				throw new RuntimeIOException(e);}
			}));
			return pd;
		}
		catch(RuntimeIOException e){
			throw new IOException(e);
		}
	}

	@Override
	public String toString() {
		return "AnonCache [anonDb=" + anonDb + ", patientDataMap=" + patientDataMap + ", studyDataMap=" + studyDataMap
				+ "]";
	}
	
}
