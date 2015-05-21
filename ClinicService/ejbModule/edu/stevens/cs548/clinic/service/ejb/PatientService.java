package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IPatientFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;

/**
 * Session Bean implementation class PatientService
 */
@Stateless(name = "PatientServiceBean")
public class PatientService implements IPatientServiceLocal,
		IPatientServiceRemote
{
	private static Logger logger = Logger.getLogger(PatientService.class
			.getCanonicalName());

	private IPatientFactory patientFactory;

	private PatientDtoFactory patientDtoFactory;

	private IPatientDAO patientDAO;

	/*
	 * TODO: Use constructor, DI and lifecycle methods to initialize the above
	 * fields.
	 */
	public PatientService()
	{
		patientFactory = new PatientFactory();
		patientDtoFactory = new PatientDtoFactory();
	}

	/*
	 * Dependency Injection occurs after the constructor to be called
	 */
	@PersistenceContext(unitName = "ClinicDomain")
	private EntityManager em;

	@PostConstruct
	private void onInitialize()
	{
		patientDAO = new PatientDAO(em);
	}

	/**
	 * @see IPatientService#addPatient(String, Date, long)
	 */
	public long addPatient(String name, Date dob, long patientId, int age)
			throws PatientServiceExn
	{
		try
		{
			logger.info("addPatient()...");
			Patient patient = this.patientFactory.createPatient(patientId,
					name, dob, age);
			return this.patientDAO.addPatient(patient);
		}
		catch (PatientExn e)
		{
			throw new PatientServiceExn(e.toString());
		}
	}

	/**
	 * @see IPatientService#getPatientByDbId(long)
	 */
	public PatientDto getPatientByDbId(long id) throws PatientServiceExn
	{
		try
		{
			logger.info("getPatientByDbId()...");
			Patient patient = patientDAO.getPatientByDbId(id);
			return patientDtoFactory.createPatientDto(patient);
		}
		catch (PatientExn e)
		{
			throw new PatientServiceExn(e.toString());
		}
	}

	/**
	 * @see IPatientService#getPatientByPatId(long)
	 */
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn
	{
		try
		{
			logger.info("getPatientByPatId()...");
			Patient patient = patientDAO.getPatientByPatientId(pid);
			return patientDtoFactory.createPatientDto(patient);
		}
		catch (PatientExn e)
		{
			throw new PatientServiceExn(e.toString());
		}
	}

	/**
	 * @see IPatientService#getPatientsByNameDob(String, Date)
	 */
	public PatientDto[] getPatientsByNameDob(String name, Date dob)
	{
		logger.info("getPatientsByNameDob()...");
		
		List<Patient> patients = patientDAO.getPatientByNameDob(name, dob);
		PatientDto[] dto = new PatientDto[patients.size()];
		for (int i = 0; i < dto.length; i++)
		{
			dto[i] = patientDtoFactory.createPatientDto(patients.get(i));
		}
		return dto;
	}

	/**
	 * @see IPatientService#deletePatient(String, long)
	 */
	public void deletePatient(String name, long id) throws PatientServiceExn
	{
		try
		{
			logger.info("deletePatient()...");
			
			Patient patient = patientDAO.getPatientByDbId(id);
			if (!name.equals(patient.getName()))
			{
				throw new PatientServiceExn(
						"Tried to delete wrong patient: name = " + name
								+ " , id = " + id);
			}
			else
			{
				patientDAO.deletePatient(patient);
			}
		}
		catch (PatientExn e)
		{
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public TreatmentDto[] getTreatments(long id) throws PatientNotFoundExn,
			TreatmentNotFoundExn
	{
		try
		{
			logger.info("getTreatments()...");
			
			Patient patient;
			List<Long> tids;
			/*
			 * TODO: Retrieve treatment ids for this patient using the domain
			 * model.
			 */
			patient = patientDAO.getPatientByDbId(id);
			tids = patient.getTreatmentIds();

			TreatmentDto[] treatments = new TreatmentDto[tids.size()];
			TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();
			for (int i = 0; i < tids.size(); i++)
			{
				patient.visitTreatment(tids.get(i), visitor);
				treatments[i] = visitor.getDTO();
			}
			return treatments;
		}
		catch (PatientExn e)
		{
			throw new PatientNotFoundExn(e.toString());
		}
		catch (TreatmentExn e)
		{
			throw new TreatmentNotFoundExn(e.toString());
		}
	}

	/*
	 * TODO: Site information should be specified in the deployment descriptor
	 * (META-INF/ejb-jar.xml).
	 */
	@Resource(name = "SiteInfo")
	private String siteInformation;

	@Override
	public String siteInfo()
	{
		return siteInformation;
	}

}
