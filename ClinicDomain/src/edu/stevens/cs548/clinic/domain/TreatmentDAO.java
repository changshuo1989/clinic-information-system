package edu.stevens.cs548.clinic.domain;

import java.util.logging.Logger;

import javax.persistence.EntityManager;

public class TreatmentDAO implements ITreatmentDAO
{

	private Logger logger = Logger
			.getLogger("edu.stevens.cs548.clinic.domain.TreatmentDAO");
	
	public TreatmentDAO(EntityManager em)
	{
		this.em = em;
	}

	private EntityManager em;

	@Override
	public Treatment getTreatmentByDbId(long id) throws TreatmentExn
	{
		Treatment t = em.find(Treatment.class, id);
		if (t == null)
		{
			throw new TreatmentExn("Missing treatment: id = " + id);
		}
		else
		{
			logger.info("getTreatmentByDbId() succ: id=" + id);
			return t;
		}
	}

	@Override
	public long addTreatment(Treatment t)
	{
		em.persist(t);
		logger.info("addTreatment() succ: "
				+ "id=" + t.getId() + ", "
				+ "Diagnosis=" + t.getDiagnosis() + ", "
				+ "TreatmentType=" + t.getTreatmentType());
		return t.getId();
	}

	@Override
	public void deleteTreatment(Treatment t)
	{
		em.remove(t);
		logger.info("deleteTreatment() succ: "
				+ "id=" + t.getId() + ", "
				+ "Diagnosis=" + t.getDiagnosis() + ", "
				+ "TreatmentType=" + t.getTreatmentType());
	}

}
