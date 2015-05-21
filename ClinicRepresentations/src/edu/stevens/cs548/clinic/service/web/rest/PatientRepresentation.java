package edu.stevens.cs548.clinic.service.web.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.PatientType;


@XmlRootElement
public class PatientRepresentation extends PatientType
{
	final static Logger logger = Logger.getLogger(PatientRepresentation.class
			.getCanonicalName());
	
	public static LinkType getPatientLink(long id, UriBuilder baseUb)
	{
		//clone;
		UriBuilder ub = baseUb.clone();
		ub.path("patient").path("{id}");
		String patientURI = ub.build(Long.toString(id)).toString();

		LinkType link = new LinkType();
		link.setUrl(patientURI);
		link.setRelation(Representation.RELATION_PATIENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}

	public PatientRepresentation()
	{
		super();
	}

	public PatientRepresentation(PatientDto dto, UriBuilder baseUb)
	{
		// TODO initialize representation from DTO
		
		this.id = dto.getId();
		this.patientId = dto.getPatientId();
		this.name = dto.getName();
		this.dob = dto.getDob();
		this.age = dto.getAge();
		
		/*
		 * Call getTreatments to initialize empty list.
		 */
		
		List<LinkType> links = this.getTreatments();
		// TODO add treatment representations
		
		UriBuilder ub = baseUb.clone().path("treatment");
		
		List<Long> tids = dto.getTreatments();
		
		for (Long tid : tids)
		{
			
			LinkType link = new LinkType();
			
			UriBuilder ubTrmt = ub.clone().path("{tid}");
			
			logger.info("tid = "+ubTrmt.toString());
			
			String trmtUri = ubTrmt.build(String.valueOf(tid)).toString();
			
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
			+ "key=" + this.id + ", "
			+ "patientId=" + this.patientId + ", "
			+ "name=" + this.name + ", "
			+ "dob=" + this.dob + ", "
			+ "age=" + this.age + ", ";
		
		for (LinkType link: this.getTreatments())
		{
			rt += link.getUrl() + ", ";
		}
		
		rt += "}";
		
		return rt;
	}

}
