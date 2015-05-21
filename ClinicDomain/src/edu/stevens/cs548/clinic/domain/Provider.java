package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;

@Entity
@NamedQueries(
{
	@NamedQuery(name = "SearchProviderByNPI",
		query = "select p from Provider p "
			+ "where p.npi = :npi"),
	@NamedQuery(name = "SearchProviderByName",
		query = "select p from Provider p "
			+ "where p.name = :name"),
	@NamedQuery(name = "SearchProviderById",
			query = "select p from Provider p "
				+ "where p.id = :id")		

})

/**
 * Entity implementation class for Entity: Provider
 * 
 */

@Table(name = "PROVIDER")
public class Provider implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Transient
	private Logger logger = Logger
			.getLogger("edu.stevens.cs548.clinic.domain.Provider");
	
	@Id @GeneratedValue
	private long id;
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Column(name = "NPI")
	private long npi;
	
	public long getNpi()
	{
		return npi;
	}

	public void setNpi(long npi)
	{
		this.npi = npi;
	}
	
	@Column(name = "NAME")
	private String name;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Column(name = "DISCRIMINATOR")
	private String providerType;

	public String getProviderType()
	{
		return providerType;
	}

	public void setProviderType(String providerType)
	{
		this.providerType = providerType;
	}
	
	@OneToMany(mappedBy = "provider")
	@OrderBy
	private List<Treatment> treatments;

	public List<Treatment> getTreatments()
	{
		return treatments;
	}

	public void setTreatments(List<Treatment> treatments)
	{
		this.treatments = treatments;
	}
	
	@Transient
	private ITreatmentDAO treatmentDAO;

	public void setTreatmentDAO(ITreatmentDAO tdao)
	{
		this.treatmentDAO = tdao;
	}
	
	public Provider()
	{
		super();
		treatments = new ArrayList<Treatment>();
	}
	
	public long addDrugTreatment(Patient patient, String diagnosis,
			String drug, float dosage)
	{
		DrugTreatment t = new DrugTreatment();
		t.setDiagnosis(diagnosis);
		t.setDrug(drug);
		t.setDosage(dosage);
		t.setPatient(patient);
		t.setProvider(this);
		treatments.add(t);
		
		return patient.addTreatment(t);
	}
	
	public long addSurgeryTreatment(Patient patient, String diagnosis,
			Date surgeryDate)
	{
		SurgeryTreatment t = new SurgeryTreatment();
		t.setDiagnosis(diagnosis);
		t.setSurgeryDate(surgeryDate);
		t.setPatient(patient);
		t.setProvider(this);
		treatments.add(t);
		
		return patient.addTreatment(t);
	}
	
	public long addRadiologyTreatment(Patient patient, String diagnosis,
			List<Date> radiologyDates)
	{
		RadiologyTreatment t = new RadiologyTreatment();
		t.setDiagnosis(diagnosis);
		t.setRadiologyDates(radiologyDates);
		t.setPatient(patient);
		t.setProvider(this);
		treatments.add(t);
		
		return patient.addTreatment(t);
	}
	
	public List<Long> getTreatmentIds()
	{
		List<Long> tids = new ArrayList<Long>();
		for (Treatment t : treatments)
		{
			tids.add(t.getId());
		}
		logger.info("getTreatmentIds(): " + Arrays.toString(tids.toArray()));
		return tids;
	}
	
	public List<Long> getTreatmentIds(Patient patient)
	{
		List<Long> tids = new ArrayList<Long>();
		for (Treatment t : treatments)
		{
			if (t.getPatient() == patient)
			{
				tids.add(t.getId());
			}
		}
		return tids;
	}
	
	public void visitTreatment(long tid, ITreatmentVisitor visitor)
			throws TreatmentExn
	{		
		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		if (null == t)
		{
			logger.info("failed to find treatment by id = " + tid);
			throw new TreatmentExn("Inappropriate treatment access: provider = "
					+ id + ", treatment = " + tid);
		}
		
		if (t.getProvider() != this)
		{
			logger.info("Inappropriate treatment access: provider = "
					+ id + ", treatment = " + tid);
			throw new TreatmentExn("Inappropriate treatment access: provider = "
					+ id + ", treatment = " + tid);
		}
		
		if (null == visitor)
		{
			logger.info("Treatment Visitor is null.");
			throw new TreatmentExn("Treatment Visitor is null: provider = "
					+ id + ", treatment = " + tid);
		}
		t.visit(visitor);
	}
	
	public void visitTreatments(ITreatmentVisitor visitor)
	{
		for (Treatment t : this.getTreatments())
		{
			t.visit(visitor);
		}
	}
	
	public void deleteTreatment(long tid)
			throws TreatmentExn
	{
		//get the treatment from DB
		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		//verify the treatment authority
		if (t.getProvider() != this)
		{
			logger.info("Inappropriate treatment deletion: provider = "
					+ id + ", treatment = " + tid);
			throw new TreatmentExn("Inappropriate treatment deletion: provider = "
					+ id + ", treatment = " + tid);
		}
		//delete the treatment from the local list
		for (int i = 0; i < treatments.size(); i++)
		{
			if (treatments.get(i).getId() == tid)
			{
				treatments.remove(i);
				break;
			}
		}
		
		//ask the responsible patient to delete the treatment 
		Patient p = t.getPatient();
		p.deleteTreatment(t);
		
		logger.info("returning from deleteTreatment()...");
	}
	
	@Override
	public String toString()
	{
		return "{"
			+ "id=" + id
			+ " npi=" + npi
			+ " name=" + name
			+ "}";
	}
	
}
