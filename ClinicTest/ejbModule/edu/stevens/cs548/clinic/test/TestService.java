package edu.stevens.cs548.clinic.test;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import edu.stevens.cs548.clinic.domain.IPatientFactory;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderFactory;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;
import edu.stevens.cs548.clinic.service.web.rest.PatientRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.ProviderRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.TreatmentRepresentation;

/**
 * Session Bean implementation class TestService
 */
@Singleton
@LocalBean
@Startup
public class TestService
{
	private static String BASE_PATH = "test-host-base";
	
	private static Logger logger = Logger.getLogger(TestService.class
			.getCanonicalName());

	IPatientFactory patFactory;
	IProviderFactory provFactory;

	PatientDtoFactory patDtoFactory;
	ProviderDtoFactory provDtoFactory;

	SamplePatientResource patResource;
	SampleProviderResource provResource;
	SampleTreatmentResource trmtResource;

	IProviderDAO provDAO;
	
	@PersistenceContext(unitName = "ClinicDomain")
	private EntityManager em;

	@EJB(beanName = "PatientServiceBean")
	private IPatientServiceLocal patientService;

	@EJB(beanName = "ProviderServiceBean")
	private IProviderServiceLocal providerService;
	
	@PostConstruct
	public void init()
	{
		logger.info("Initializing...");

		patFactory = new PatientFactory();
		provFactory = new ProviderFactory();

		patDtoFactory = new PatientDtoFactory();
		provDtoFactory = new ProviderDtoFactory();
		
		patResource = new SamplePatientResource(
				UriBuilder.fromPath(BASE_PATH), patientService);
		
		provResource = new SampleProviderResource(
				UriBuilder.fromPath(BASE_PATH), providerService);
		trmtResource = new SampleTreatmentResource(
				UriBuilder.fromPath(BASE_PATH), em);
		
		provDAO = new ProviderDAO(em);
		
		try
		{
			logger.info("add a patient...");
			Patient pat = patFactory.createPatient(1234, "David",
					new SimpleDateFormat("yyyy-MM-dd").parse("1989-01-01"), 25);
			PatientDto patDto = patDtoFactory.createPatientDto(pat);
			PatientRepresentation patRep = new PatientRepresentation(patDto,
					patResource.getBaseUb());
			logger.info("patRep to be added: " + patRep.toString());
			Response resp = patResource.addPatient(patRep);
			logger.info("addPatient() Response: " + resp);

			logger.info("add a provider...");
			Provider prov = provFactory.createProvider(9876, "Joe","Surgery");
			ProviderDto provDto = provDtoFactory.createProviderDto(prov);
			ProviderRepresentation provRep = new ProviderRepresentation(
					provDto, provResource.getBaseUb());
			logger.info("provRep to be added: " + provRep.toString());
			resp = provResource.addProvider(provRep);
			logger.info("addProvider() Response: " + resp);
			
			logger.info("add a treatment...");
			TreatmentDto trmtDto = new TreatmentDto();
			trmtDto.setDiagnosis("fever");
			DrugTreatmentType drugTrmt = new DrugTreatmentType();
			drugTrmt.setDosage(20);
			drugTrmt.setName("Apsrin");
			drugTrmt.setPhysician("Joe");
			trmtDto.setDrugTreatment(drugTrmt);
			Long tid = providerService.addTreatment(1234, 9876, trmtDto);
			logger.info("tid = "+tid);
			
			logger.info("get a patient...");
			Long patKey = patResource.getPatKey();
			logger.info("get patkey success!: "+ patKey);
			patRep = patResource.getPatient(patKey.toString());
			logger.info("getPatient(): " + patRep.toString());
			
			logger.info("get a provider...");
			provRep = provResource.getProviderByNPI("9876");
			logger.info("getProviderByNPI(): " + provRep.toString());
			
			logger.info("get a treatment...");
			TreatmentRepresentation trmtRep
				= trmtResource.getTreatment(tid.toString());
			logger.info("getTreatment(): " + trmtRep.toString());
			/*
			//clear data
			patientService.deletePatient("Mike", patKey);
			logger.info("patient has been deleted!");
			logger.info("treatment has been deleted!");
			provDAO.deleteProvider(prov);
			logger.info("provider has been deleted!");
			*/
		}
		catch (Exception e)
		{
			logger.info(e.toString());
		}
	}

}