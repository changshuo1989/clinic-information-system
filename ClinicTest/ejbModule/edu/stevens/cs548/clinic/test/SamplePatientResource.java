package edu.stevens.cs548.clinic.test;

import java.net.URI;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.web.rest.PatientRepresentation;

public class SamplePatientResource
{
	final static Logger logger = Logger.getLogger(SamplePatientResource.class
			.getCanonicalName());
	
	private IPatientServiceLocal patientService;
	
	private UriBuilder baseUb;
	
	public UriBuilder getBaseUb()
	{
		return baseUb;
	}
	
	/**
	 * Default constructor.
	 */
	public SamplePatientResource(UriBuilder ub,
			IPatientServiceLocal patService)
	{
		this.baseUb = ub.clone().path("patient");
		this.patientService = patService;
	}
	
	private long patKey;
	
	public long getPatKey()
	{
		return patKey;
	}

	public String getSiteInfo()
	{
		return patientService.siteInfo();
	}

	public Response addPatient(PatientRepresentation patientRep)
	{
		try
		{
			logger.info("addPatient()...");
			
			Long key = patientService.addPatient(patientRep.getName(),
					patientRep.getDob(), patientRep.getPatientId(),
					patientRep.getAge());
			patKey = key;
			
			UriBuilder ub = baseUb.clone().path("{id}");
			URI url = ub.build(Long.toString(key));
			
			logger.info("return...");
			return Response.created(url).build();
		}
		catch (PatientServiceExn e)
		{
			throw new WebApplicationException();
		}
	}

	/**
	 * Query methods for patient resources.
	 */
	public PatientRepresentation getPatient(String id)
	{
		try
		{
			long key = Long.parseLong(id);
			PatientDto patientDTO = patientService.getPatientByDbId(key);
			logger.info("success get patientDTO");
			PatientRepresentation patientRep = new PatientRepresentation(
					patientDTO,baseUb); 
			logger.info("success get patientRep");
			return patientRep;
		}
		catch (PatientServiceExn e)
		{
			throw new WebApplicationException();
		}
	}
	
}
