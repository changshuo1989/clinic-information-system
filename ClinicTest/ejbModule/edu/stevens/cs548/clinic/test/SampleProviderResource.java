package edu.stevens.cs548.clinic.test;

import java.net.URI;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;
import edu.stevens.cs548.clinic.service.web.rest.ProviderRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.TreatmentRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;

public class SampleProviderResource
{
	final static Logger logger = Logger.getLogger(SampleProviderResource.class
			.getCanonicalName());

	private UriBuilder baseUb;
	
	public UriBuilder getBaseUb()
	{
		return baseUb;
	}

	private IProviderServiceLocal providerService;

	/**
	 * Default constructor.
	 */
	public SampleProviderResource(UriBuilder ub,
			IProviderServiceLocal provService)
	{
		baseUb = ub.clone().path("provider");
		providerService = provService;
	}

	public String getSiteInfo()
	{
		return providerService.siteInfo();
	}

	public Response addProvider(ProviderRepresentation provRep)
	{
		try
		{
			Long key = providerService.addProvider(provRep.getNpi(),
					provRep.getName(), provRep.getProviderType());
			UriBuilder ub = baseUb.clone().path("{id}");
			URI url = ub.build(Long.toString(key));
			return Response.created(url).build();
		}
		catch (ProviderServiceExn e)
		{
			throw new WebApplicationException();
		}
	}

	public ProviderRepresentation getProviderByNPI(String provNpi)
	{
		try
		{
			long lProvNpi = Long.parseLong(provNpi);
			logger.info("Npi = " + lProvNpi);
			ProviderDto provDto = providerService.getProviderByNPI(lProvNpi);
			logger.info("provDto"+ provDto.getNpi());
			ProviderRepresentation provRep = new ProviderRepresentation(
					provDto, baseUb);
			logger.info("provRep: "+ provRep.toString());
			return provRep;
		}
		catch (ProviderServiceExn e)
		{
			throw new WebApplicationException();
		}
	}

	public LinkType[] getTreatments(String prid, String patURI)
	{
		try
		{
			long lPrid = Long.parseLong(prid);
			long lPatid = Long.parseLong(patURI.substring(patURI
					.lastIndexOf('/')));

			TreatmentDto[] trmtDtos = providerService.getTreatments(lPatid,
					lPrid);
			LinkType[] links = new LinkType[trmtDtos.length];
			for (int i = 0; i < trmtDtos.length; i++)
			{
				links[i] = TreatmentRepresentation.getTreatmentLink(
						trmtDtos[i].getTreatmentId(),
						baseUb);
			}

			return links;
		}
		catch (ProviderServiceExn e)
		{
			throw new WebApplicationException();
		}
	}
}
