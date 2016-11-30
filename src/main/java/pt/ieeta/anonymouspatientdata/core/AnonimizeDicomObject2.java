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

public class AnonimizeDicomObject2 {
/**
 * Transforms the DicomObject by Anonymizing the Tag patientName, patientId and AccessionNumber
 * @param DICOMObj
 * @return  
 * @throws IOException 
 */
	public static DicomObject anonymizeObject(DicomObject DICOMObj) throws IOException {
		
		
		
		return DICOMObj.exclude(new int [] {Tag.ReferringPhysicianAddress,
				Tag.ReferringPhysicianTelephoneNumbers,
				Tag.StationName,
				Tag.StudyDescription,
				Tag.SeriesDescription,
				Tag.InstitutionalDepartmentName,
				Tag.PhysiciansOfRecord,
				Tag.PerformingPhysicianName,
				Tag.NameOfPhysiciansReadingStudy,
				Tag.OperatorsName,
				Tag.AdmittingDiagnosesDescription,
				Tag.DerivationDescription,
				Tag.MedicalRecordLocator,
				Tag.EthnicGroup,
				Tag.Occupation,
				Tag.AdditionalPatientHistory,
				Tag.PatientComments,
				Tag.ProtocolName,
				Tag.ImageComments,
				Tag.RequestAttributesSequence,
				Tag.ContentSequence,Tag.InstanceCreatorUID,
				Tag.SOPInstanceUID,
				Tag.ReferencedSOPInstanceUID,
				Tag.StudyInstanceUID,
				Tag.SeriesInstanceUID,
				Tag.FrameOfReferenceUID,
				Tag.SynchronizationFrameOfReferenceUID,
				Tag.UID,
				Tag.StorageMediaFileSetUID,
				Tag.ReferencedFrameOfReferenceUID,
				Tag.RelatedFrameOfReferenceUID,
				Tag.InstitutionName,
				Tag.InstitutionAddress,
				Tag.ReferringPhysicianName,
				Tag.PatientBirthDate,
				Tag.PatientBirthTime,
				Tag.PatientSex,
				Tag.OtherPatientIDs,
				Tag.OtherPatientNames,
				Tag.PatientAge,
				Tag.PatientSize,
				Tag.PatientWeight,
				Tag.DeviceSerialNumber,
				Tag.AccessionNumber,
				Tag.StudyID});
		

	}
}
