package edu.stevens.cs548.clinic.service.web.rest;

import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.ProviderType;

@XmlRootElement
public class ProviderRepresentation extends ProviderType
{
	public static LinkType getProviderLink(long id, UriBuilder baseUb)
	{
		UriBuilder ub = baseUb.clone();
		ub.path("provider").path("{id}");
		String provURI = ub.build(Long.toString(id)).toString();

		LinkType link = new LinkType();
		link.setUrl(provURI);
		link.setRelation(Representation.RELATION_PROVIDER);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
	
	public ProviderRepresentation()
	{
		super();
	}
	
	public ProviderRepresentation(ProviderDto dto, UriBuilder baseUb)
	{
		this.id = dto.getProviderId();
		this.npi = dto.getNpi();
		this.name = dto.getName();
		this.providerType = dto.getProviderType();
		
		/*
		 * Call getTreatments to initialize empty list.
		 */
		List<LinkType> links = this.getTreatments();
		UriBuilder ub = baseUb.clone().path("treatment");

		List<Long> tids = dto.getTreatments();
		for (Long tid : tids)
		{
			LinkType link = new LinkType();

			UriBuilder ubTrmt = ub.clone().path("{tid}");
			String trmtUri = ubTrmt.build(tid.toString()).toString();
			link.setUrl(trmtUri);
			link.setRelation(Representation.RELATION_TREATMENT);
			link.setMediaType(Representation.MEDIA_TYPE);

			links.add(link);
		}
	}
	
	public List<LinkType> getLinksTreatments()
	{
		return this.getTreatments();
	}
	
	@Override
	public String toString()
	{		
		String rt = "{"
			+ "id=" + this.id + ", "
			+ "npi=" + this.npi + ", "
			+ "name=" + this.name + ", "
			+ "providerType=" + this.providerType + ", ";
		
		for (LinkType link: this.getTreatments())
		{
			rt += link.getUrl() + ", ";
		}
		
		rt += "}";
		
		return rt;
	}
}
