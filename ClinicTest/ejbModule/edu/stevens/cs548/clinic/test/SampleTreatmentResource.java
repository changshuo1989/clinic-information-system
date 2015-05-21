package edu.stevens.cs548.clinic.test;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentDAO;
import edu.stevens.cs548.clinic.service.ejb.TreatmentPDOtoDTO;
import edu.stevens.cs548.clinic.service.web.rest.TreatmentRepresentation;

public class SampleTreatmentResource
{
	final static Logger logger = Logger.getLogger(SampleTreatmentResource.class
			.getCanonicalName());
	
	private UriBuilder baseUb;
	
	public UriBuilder getBaseUb()
	{
		return baseUb;
	}
	
	private TreatmentDAO treatmentDAO;
	
	private EntityManager em;
	
	public SampleTreatmentResource(UriBuilder baseUb, EntityManager em)
	{
		this.baseUb = baseUb;
		this.em = em;
		treatmentDAO=new TreatmentDAO(em);
	}
	
	@PostConstruct
	private void onInitialize()
	{
		treatmentDAO = new TreatmentDAO(em);
	}
	
	public TreatmentRepresentation getTreatment(String tid)
	{
		try
		{
			long lTid = Long.parseLong(tid);
			Treatment trmt = treatmentDAO.getTreatmentByDbId(lTid);
			TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();
			trmt.visit(visitor);
			TreatmentRepresentation trmtRep = new TreatmentRepresentation(
					visitor.getDTO(), baseUb);
			return trmtRep;
		}
		catch (TreatmentExn e)
		{
			throw new WebApplicationException();
		}
	}
}
