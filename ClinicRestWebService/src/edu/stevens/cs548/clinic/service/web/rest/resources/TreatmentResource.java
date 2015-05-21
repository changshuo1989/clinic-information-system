package edu.stevens.cs548.clinic.service.web.rest.resources;

import java.util.logging.Logger;
import java.util.Date;
import java.util.List;
import java.net.URI;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.UriBuilder;

import edu.stevens.cs548.clinic.service.dto.treatment.ObjectFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentDAO;
import edu.stevens.cs548.clinic.service.web.rest.TreatmentRepresentation;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;
import edu.stevens.cs548.clinic.service.ejb.TreatmentPDOtoDTO;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.treatment.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.treatment.RadiologyType;

@RequestScoped
@Path("/treatment")
public class TreatmentResource
{
	final static Logger logger = Logger.getLogger(TreatmentResource.class
			.getCanonicalName());
	
	@Context
	private UriInfo context;
	
	private TreatmentDAO treatmentDAO;
	
	@EJB(beanName = "ProviderServiceBean")
	private IProviderServiceLocal providerService;
	
	@PersistenceContext(unitName = "ClinicDomain")
	private EntityManager em;
	
	@PostConstruct
	private void onInitialize()
	{
		treatmentDAO = new TreatmentDAO(em);
	}
	
	
	
	
	@POST
	@Consumes("application/xml")
	public Response addTreatment(TreatmentRepresentation treatmentRep)
	{
		try
		{
			int patDbIdIndex = treatmentRep.getPatient().getUrl().lastIndexOf('/') + 1;
			long patKey = Long.parseLong(treatmentRep.getPatient().getUrl()
					.substring(patDbIdIndex));

			int provDbIdIndex = treatmentRep.getProvider().getUrl().lastIndexOf('/') + 1;
			long provKey = Long.parseLong(treatmentRep.getProvider().getUrl()
					.substring(provDbIdIndex));

			ObjectFactory factory = new ObjectFactory();
			TreatmentDto trmtDto = factory.createTreatmentDto();

			trmtDto.setDiagnosis(treatmentRep.getDiagnosis());
			
			if (treatmentRep.getDrugTreatment() != null)
			{
				DrugTreatmentType drug = factory.createDrugTreatmentType();
				drug.setName(treatmentRep.getDrugTreatment().getName());
				drug.setDosage(treatmentRep.getDrugTreatment().getDosage());
				trmtDto.setDrugTreatment(drug);
			}
			if (treatmentRep.getSurgery() != null)
			{
				SurgeryType surgery = factory.createSurgeryType();
				surgery.setDate(treatmentRep.getSurgery().getDate());
				trmtDto.setSurgery(surgery);
			}
			if (treatmentRep.getRadiology() != null)
			{
				RadiologyType radiology = factory.createRadiologyType();
				List<Date> dates = radiology.getDate();
				for (Date date : treatmentRep.getRadiology().getDate())
				{
					dates.add(date);
				}
				trmtDto.setRadiology(radiology);
			}

			long trmtKey = providerService.addTreatmentByDbId(patKey, provKey, trmtDto);
			
			UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
			URI url = ub.build(Long.toString(trmtKey));
			return Response.created(url).build();
		}
		catch (ProviderServiceExn e)
		{
			throw new WebApplicationException();
		}
	}
	
	
	
	
	
	@GET
	@Path("{id}")
	@Produces("application/xml")
	public TreatmentRepresentation getTreatment(@PathParam("id") String tid)
	{
		try
		{
			long tidStr = Long.parseLong(tid);
			Treatment trmt = treatmentDAO.getTreatmentByDbId(tidStr);
			TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();
			trmt.visit(visitor);
			TreatmentRepresentation treatmentRep = new TreatmentRepresentation(
					visitor.getDTO(), context.getBaseUriBuilder());
			return treatmentRep;
		}
		catch (TreatmentExn e)
		{
			throw new WebApplicationException();
		}
	}	
}
