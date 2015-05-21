package edu.stevens.cs548.clinic.service.web.rest.resources;

import java.net.URI;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;
import edu.stevens.cs548.clinic.service.web.rest.ProviderRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.TreatmentRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;

@RequestScoped
@Path("/provider")
public class ProviderResource
{
	final static Logger logger = Logger.getLogger(ProviderResource.class
			.getCanonicalName());

	@Context
	private UriInfo context;

	@EJB(beanName = "ProviderServiceBean")
	private IProviderServiceLocal providerService;

	/**
	 * Default constructor.
	 */
	public ProviderResource()
	{
	}

	@GET
	@Path("siteinfo")
	@Produces("text/plain")
	public String getSiteInfo()
	{
		return providerService.siteInfo();
	}

	@POST
	@Consumes("application/xml")
	public Response addProvider(ProviderRepresentation provRep)
	{
		try
		{
			// TODO add patient record;
			Long key = providerService.addProvider(provRep.getNpi(),
					provRep.getName(), provRep.getProviderType());
			UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
			URI url = ub.build(Long.toString(key));
			return Response.created(url).build();
		}
		catch (ProviderServiceExn e)
		{
			throw new WebApplicationException();
		}
	}

	
	
	@GET
	@Path("ByNPI/{npi}")
	@Produces("application/xml")
	public ProviderRepresentation getProviderByNPI(
			@PathParam("npi") String npi)
	{
		try
		{
			long providerNpi = Long.parseLong(npi);
			ProviderDto provDto = providerService.getProviderByNPI(providerNpi);
			ProviderRepresentation providerRep = new ProviderRepresentation(
					provDto, context.getBaseUriBuilder());
			return providerRep;
		}
		catch (ProviderServiceExn e)
		{
			throw new WebApplicationException();
		}
	}

	
	@GET
	@Path("{id}/treatments")
	@Produces("application/xml")
	public LinkType[] getTreatments(@HeaderParam("X-Patient") String patURI, @PathParam("id") String prid
			)
	{
		try
		{
			long lPrid = Long.parseLong(prid);
			logger.info("providerDbId = "+ lPrid);
			
			String patIdStr=patURI.substring(patURI.lastIndexOf('/')+1);
			logger.info(patIdStr);
			long lPatid = Long.parseLong(patIdStr);
			logger.info("patientDbId = "+ lPatid);
			
			TreatmentDto[] trmtDtos = providerService.getTreatmentsByDbId(lPatid,
					lPrid);
			logger.info("get trmtDtos sucess!");
			LinkType[] links = new LinkType[trmtDtos.length];
			logger.info("get links sucess!");
			for (int i = 0; i < trmtDtos.length; i++)
			{
				logger.info("in the loop...");
				links[i] = TreatmentRepresentation.getTreatmentLink(
						trmtDtos[i].getTreatmentId(),
						context.getBaseUriBuilder());
				logger.info("get links[i] sucess!");
			}
			
			return links;
		}
		catch (ProviderServiceExn e)
		{
			throw new WebApplicationException();
		}
	}

}
