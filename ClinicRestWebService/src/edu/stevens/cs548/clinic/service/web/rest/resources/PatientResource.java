package edu.stevens.cs548.clinic.service.web.rest.resources;

import java.net.URI;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.web.rest.PatientRepresentation;

//TODO
@RequestScoped
@Path("/patient")
public class PatientResource
{
	final static Logger logger = Logger.getLogger(PatientResource.class
			.getCanonicalName());

	@Context
	private UriInfo context;

	// TODO
	@EJB(beanName = "PatientServiceBean")
	private IPatientServiceLocal patientService;

	/**
	 * Default constructor.
	 */
	public PatientResource()
	{
	}

	// TODO
	@GET
	@Path("siteinfo")
	@Produces("text/plain")
	public String getSiteInfo()
	{
		return patientService.siteInfo();
	}

	// TODO
	@POST
	@Consumes("application/xml")
	public Response addPatient(PatientRepresentation patientRep)
	{
		try
		{
			// TODO add patient record;
			Long key = patientService.addPatient(patientRep.getName(),
					patientRep.getDob(), patientRep.getPatientId(),
					patientRep.getAge());
			UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
			URI url = ub.build(Long.toString(key));
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
	// TODO
	@GET
	@Path("{id}")
	@Produces("application/xml")
	public PatientRepresentation getPatient(@PathParam("id") String id)
	{
		try
		{
			long key = Long.parseLong(id);
			PatientDto patientDTO = patientService.getPatientByDbId(key);
			// TODO return patient representation;
			PatientRepresentation patientRep = new PatientRepresentation(
					patientDTO, context.getBaseUriBuilder());
			return patientRep;
		}
		catch (PatientServiceExn e)
		{
			throw new WebApplicationException();
		}
	}
	
	@GET
	@Path("patientid")
	@Produces("application/xml")
	public PatientRepresentation getPatientByPatientId(@QueryParam("id")
			String patid)
	{
		try
		{
			long lPatid = Long.parseLong(patid);
			PatientDto patientDTO = patientService.getPatientByPatId(lPatid);
			// TODO return patient representation;
			PatientRepresentation patientRep = new PatientRepresentation(
					patientDTO, context.getBaseUriBuilder());
			return patientRep;
		}
		catch (PatientServiceExn e)
		{
			throw new WebApplicationException();
		}
	}
	
}