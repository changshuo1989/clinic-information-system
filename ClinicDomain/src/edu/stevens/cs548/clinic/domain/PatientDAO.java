package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class PatientDAO implements IPatientDAO
{
	private EntityManager em;
	private TreatmentDAO treatmentDAO;

	
	private Logger logger = Logger
			.getLogger("edu.stevens.cs548.clinic.domain.PatientDAO");

	@Override
	public Patient getPatientByDbId(long id) throws PatientExn
	{
		Patient p = em.find(Patient.class, id);
		if (p == null)
		{
			throw new PatientExn("Patient not found: primary key = " + id);
		}
		else
		{
			logger.info("getPatientByDbId() succ: " + id);
			p.setTreatmentDAO(this.treatmentDAO);
			
			logger.info("success setTreatmentDAO()");
			return p;
		}
	}

	@Override
	public Patient getPatientByPatientId(long pid) throws PatientExn
	{
		TypedQuery<Patient> query = em.createNamedQuery(
				"SearchPatientByPatientID", Patient.class).setParameter("pid",
				pid);
		List<Patient> patients = query.getResultList();
		if (patients.size() > 1)
		{
			logger.info("Duplicate patient records: patient id = " + pid);
			throw new PatientExn("Duplicate patient records: patient id = "
					+ pid);
		}
		else if (patients.size() < 1)
		{
			return null;
		}
		else
		{
			Patient p = patients.get(0);
			p.setTreatmentDAO(this.treatmentDAO);
			logger.info("getPatientByPatientId() succ: " + pid);
			return p;
		}
	}

	@Override
	public List<Patient> getPatientByNameDob(String name, Date dob)
	{
		TypedQuery<Patient> query = em
				.createNamedQuery("SearchPatientByNameDOB", Patient.class)
				.setParameter("name", name).setParameter("dob", dob);
		List<Patient> patients = query.getResultList();
		for (Patient p : patients)
		{
			p.setTreatmentDAO(this.treatmentDAO);
		}
		return patients;
	}

	@Override
	public long addPatient(Patient patient) throws PatientExn
	{
		if (null == patient)
		{
			/* PatientFactory.createPatient returns null 
			 * only if age is not consistent with DOB
			 */
			throw new PatientExn("Insertion: Target patient is null or"
					+ "Input age is not consistent with DOB.");
		}
		
		long pid = patient.getPatientId();
		TypedQuery<Patient> query = em.createNamedQuery(
				"SearchPatientByPatientID", Patient.class).setParameter("pid",
				pid);
		List<Patient> patients = query.getResultList();
		if (patients.size() < 1)
		{
			//the pid is valid, so add the new patient to the system
			em.persist(patient);
			patient.setTreatmentDAO(this.treatmentDAO);
			
			logger.info("addPatient() succ: "
					+ "id=" + patient.getId() + ", "
					+ "pid=" + patient.getPatientId() + ", "
					+ "name=" + patient.getName());
			
			return patient.getId();
		}
		else
		{
			//the pid already exists
			Patient patient2 = patients.get(0);
			throw new PatientExn("Insertion: Patient with patient id (" + pid
					+ ") already exists.\n** Name: " + patient2.getName());
		}
	}

	@Override
	public void deletePatientByDbId (long id) throws PatientExn
	{
		Patient patient = getPatientByDbId(id);
		if (null == patient)
		{
			throw new PatientExn("Deletion: Patient with id (" + id
					+ ") not found.");
		}
		deletePatient(patient);
		logger.info("deletePatientByDbId(): id=" + id);
	}
	
	@Override
	public void deletePatient(Patient patient) throws PatientExn
	{
		em.remove(patient);
	}

	public PatientDAO(EntityManager em)
	{
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}

}
