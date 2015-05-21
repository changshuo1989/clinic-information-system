package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;

@Entity
@NamedQueries(
{
	@NamedQuery(name = "SearchPatientByNameDOB",
		query = "select p from Patient p "
			+ "where p.name = :name and p.birthDate = :dob"),
	@NamedQuery(name = "SearchPatientByPatientID",
		query = "select p from Patient p "
			+ "where p.patientId = :pid")
})

/**
 * Entity implementation class for Entity: Patient
 * 
 */

@Table(name = "PATIENT")

public class Patient implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Transient
	private Logger logger = Logger
			.getLogger("edu.stevens.cs548.clinic.domain.Patient");
	
	@Id
	@GeneratedValue
	private long id;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	private long patientId;

	public long getPatientId()
	{
		return patientId;
	}

	public void setPatientId(long patientId)
	{
		this.patientId = patientId;
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

	@Temporal(TemporalType.DATE)
	private Date birthDate;

	public Date getBirthDate()
	{
		return birthDate;
	}

	public void setBirthDate(Date birthDate)
	{
		this.birthDate = birthDate;
	}

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "patient")
	@OrderBy
	private List<Treatment> treatments;

	protected List<Treatment> getTreatments()
	{
		return treatments;
	}

	protected void setTreatments(List<Treatment> treatments)
	{
		this.treatments = treatments;
	}

	@Transient
	private ITreatmentDAO treatmentDAO;

	public void setTreatmentDAO(ITreatmentDAO tdao)
	{
		this.treatmentDAO = tdao;
	}
	
	public Patient()
	{
		super();
		treatments = new ArrayList<Treatment>();
	}
	
	protected long addTreatment(Treatment t)
	{
		treatmentDAO.addTreatment(t);
		treatments.add(t);
		if (t.getPatient() != this)
			t.setPatient(this);
		return t.getId();
	}

	protected void deleteTreatment(Treatment t)
	{
		treatmentDAO.deleteTreatment(t);
		
		logger.info("Deleting treatment from the local list...");
		for (int i = 0; i < treatments.size(); i++)
		{
			if (treatments.get(i).getId() == t.getId())
			{
				treatments.remove(i);
				break;
			}
		}
	}
		
	public List<Long> getTreatmentIds()
	{
		List<Long> tids = new ArrayList<Long>();
		for (Treatment t : treatments)
		{
			tids.add(t.getId());
		}
		return tids;
	}

	public void visitTreatment(long tid, ITreatmentVisitor visitor)
			throws TreatmentExn
	{
		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		if (t.getPatient() != this)
		{
			logger.info("Inappropriate treatment access: patient = "
					+ id + ", treatment = " + tid);
			throw new TreatmentExn("Inappropriate treatment access: patient = "
					+ id + ", treatment = " + tid);
		}
		t.visit(visitor);
	}

	public void visitTreatments(ITreatmentVisitor visitor)
	{
		for (Treatment t : treatments)
		{
			t.visit(visitor);
		}
	}

	@Override
	public String toString()
	{
		return "{"
			+ "id=" + id
			+ " name=" + name
			+ " birthDate=" + birthDate
			+ "}";
	}
}