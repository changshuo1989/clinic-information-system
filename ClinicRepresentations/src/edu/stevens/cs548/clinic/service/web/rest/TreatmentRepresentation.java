package edu.stevens.cs548.clinic.service.web.rest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.ObjectFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.TreatmentType;

@XmlRootElement
public class TreatmentRepresentation extends TreatmentType
{
	private ObjectFactory repFactory = new ObjectFactory();

	public static LinkType getTreatmentLink(long tid, UriBuilder baseUb)
	{
		UriBuilder ub = baseUb.clone();
		ub.path("treatment");
		UriBuilder ubTreatment = ub.clone().path("{tid}");
		String treatmentURI = ubTreatment.build(Long.toString(tid)).toString();

		LinkType link = new LinkType();
		link.setUrl(treatmentURI);
		link.setRelation(Representation.RELATION_TREATMENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
	
	public TreatmentRepresentation()
	{
		super();
	}

	public TreatmentRepresentation(TreatmentDto dto, UriBuilder baseUb)
	{
		this.id = dto.getTreatmentId();
		this.patient = PatientRepresentation.getPatientLink(dto.getPatientId(),
				baseUb);
		/*
		 * TODO: Need to fill in provider information.
		 */
		this.provider = ProviderRepresentation.getProviderLink(
				dto.getProviderId(), baseUb);

		this.diagnosis = dto.getDiagnosis();

		if (dto.getDrugTreatment() != null)
		{
			this.drugTreatment = repFactory.createDrugTreatmentType();
			this.drugTreatment.setName(dto.getDrugTreatment().getName());
			this.drugTreatment.setDosage(dto.getDrugTreatment().getDosage());
		}
		else if (dto.getSurgery() != null)
		{
			/*
			 * TODO finish this
			 */
			this.surgery = repFactory.createSurgeryType();
			this.surgery.setDate(dto.getSurgery().getDate());
		}
		else if (dto.getRadiology() != null)
		{
			/*
			 * TODO finish this
			 */
			this.radiology = repFactory.createRadiologyType();
			List<Date> dates = this.radiology.getDate();
			for (Date date : dto.getRadiology().getDate())
			{
				dates.add(date);
			}
		}
	}
	
	public LinkType getLinkPatient()
	{
		return this.getPatient();
	}

	public LinkType getLinkProvider()
	{
		return this.getProvider();
	}
	
	@Override
	public String toString()
	{		
		String rt = "{"
			+ "id=" + this.id + ", "
			+ "patient=" + this.patient.getUrl() + ", "
			+ "provider=" + this.provider.getUrl() + ", "
			+ "dignosis=" + this.diagnosis + ", ";
		
		if (this.getDrugTreatment() != null)
		{
			rt += "DrugTreatment={"
				+ this.getDrugTreatment().getName() + ", "
				+ this.getDrugTreatment().getDosage() + "}";
		}
		
		if (this.getSurgery() != null)
		{
			rt += "Surgery={"
				+ this.getSurgery().getDate() + "}";
		}
		
		if (this.getRadiology() != null)
		{
			rt += "Radiology={"
				+ Arrays.toString(this.getRadiology().getDate().toArray())
				+ "}";
		}
		
		rt	+= "}";
		
		return rt;
	}
}
