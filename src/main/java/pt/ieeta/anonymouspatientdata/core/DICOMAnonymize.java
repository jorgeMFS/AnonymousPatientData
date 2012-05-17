/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ieeta.anonymouspatientdata.core;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import pt.ieeta.anonymouspatientdata.core.impl.MatchTable;

/**
 *
 * @author eriksson
 */
public class DICOMAnonymize {
    public static DicomObject anonymize(DicomObject DICOMObj) {
       String val;

       val = DICOMObj.getString(Tag.PatientName);
       if (val != null) {
           DICOMObj.remove(Tag.PatientName);

           if (val.contains("*")) {
               val = val.replaceAll("\\*", "");
               DICOMObj.putString(Tag.PatientName, VR.PN, MatchTable.getInstance().getName(val) + "*");
           } else {
               DICOMObj.putString(Tag.PatientName, VR.PN, MatchTable.getInstance().getName(val));
           }
       }

       val = DICOMObj.getString(Tag.PatientID);
       if (val != null) {
           DICOMObj.remove(Tag.PatientID);
           DICOMObj.putString(Tag.PatientID, VR.LO, MatchTable.getInstance().getPatientID(val));
       }

       val = DICOMObj.getString(Tag.AccessionNumber);
       if (val != null) {
           DICOMObj.remove(Tag.AccessionNumber);
           DICOMObj.putString(Tag.AccessionNumber, VR.SH, MatchTable.getInstance().getAccessionNumber(val));
       }

       return DICOMObj;
   }
}
