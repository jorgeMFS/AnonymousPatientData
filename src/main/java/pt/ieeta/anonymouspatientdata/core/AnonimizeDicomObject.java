/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of PersistantDataLiteSQL.
 *
 *  PersistantDataLiteSQL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PersistantDataLiteSQL is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */

package pt.ieeta.anonymouspatientdata.core;

import java.io.IOException;

/**
 * @author Jorge Miguel Ferreira da Silva
 */

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;

import pt.ieeta.anonymouspatientdata.core.impl.MatchTables;
import pt.ieeta.anonymouspatientdata.core.impl.PatientStudy;

public class AnonimizeDicomObject {
/**
 * Transforms the DicomObject by Anonymizing the Tag patientName, patientId and AccessionNumber
 * @param DICOMObj
 * @return  
 * @throws IOException 
 */
	public static void anonymizeObject(DicomObject DICOMObj) throws IOException {
		
		DICOMObj.remove(Tag.PatientID);
		DICOMObj.remove(Tag.PatientName);
		DICOMObj.remove(Tag.AccessionNumber);
		
		String patientName = DICOMObj.getString(Tag.PatientName);
		String patientId =DICOMObj.getString(Tag.PatientID);
		String accessionNumber=DICOMObj.getString(Tag.AccessionNumber);
		PatientStudy patientStudy =MatchTables.getInstance().createMatch(patientId, patientName, accessionNumber);
		String keyPatient = patientStudy.getPatientData().getMapId();
		String keyStudy =patientStudy.getStudyData().getMapAccessionNumber();		
		
		DICOMObj.remove(Tag.PatientID);
		DICOMObj.remove(Tag.PatientName);
		DICOMObj.remove(Tag.AccessionNumber);
		DICOMObj.putString(Tag.PatientID, VR.LO,keyPatient);
		DICOMObj.putString(Tag.PatientName, VR.PN,keyPatient);
		DICOMObj.putString(Tag.AccessionNumber, VR.SH,keyStudy);

	}
}
