package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;

import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;

public interface IPatientService
{

	public class PatientServiceExn extends Exception
	{
		private static final long serialVersionUID = 1L;

		public PatientServiceExn(String m)
		{
			super(m);
		}
	}

	public class PatientNotFoundExn extends PatientServiceExn
	{
		private static final long serialVersionUID = 1L;

		public PatientNotFoundExn(String m)
		{
			super(m);
		}
	}

	public class TreatmentNotFoundExn extends PatientServiceExn
	{
		private static final long serialVersionUID = 1L;

		public TreatmentNotFoundExn(String m)
		{
			super(m);
		}
	}

	public long addPatient(String name, Date dob, long patientId, int age)
			throws PatientServiceExn;

	public PatientDto getPatientByDbId(long id) throws PatientServiceExn;

	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn;

	public PatientDto[] getPatientsByNameDob(String name, Date dob);

	public void deletePatient(String name, long id) throws PatientServiceExn;

	public TreatmentDto[] getTreatments(long id) throws PatientNotFoundExn,
			TreatmentNotFoundExn;

	public String siteInfo();

}
