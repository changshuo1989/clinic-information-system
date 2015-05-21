package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: Treatment
 * 
 */
@Entity

@Table(name = "TREATMENT")
public abstract class Treatment implements Serializable
{

	private static final long serialVersionUID = 1L;
	
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

	/*
	 * TODO: Use this in defining the discriminator column.
	 */
	@Column(name = "DISCRIMINATOR")
	private String treatmentType;

	public String getTreatmentType()
	{
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType)
	{
		this.treatmentType = treatmentType;
	}

	private String diagnosis;

	public String getDiagnosis()
	{
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis)
	{
		this.diagnosis = diagnosis;
	}

	/*
	 * TODO: Add backward references to patient and provider
	 */
	@ManyToOne
	@JoinColumn(name = "patient_fk", referencedColumnName = "id")
	private Patient patient;

	public Patient getPatient()
	{
		/*
		 * TODO: return backward reference to patient.
		 */
		return patient;
	}

	public void setPatient(Patient patient)
	{
		/*
		 * TODO: patch forward and backward references, where necessary
		 */
		this.patient = patient;
	}
	
	@ManyToOne
	@JoinColumn(name = "provider_fk", referencedColumnName = "id")
	private Provider provider;

	public Provider getProvider()
	{
		/*
		 * TODO: return backward reference to patient.
		 */
		return provider;
	}

	public void setProvider(Provider provider)
	{
		/*
		 * TODO: patch forward and backward references, where necessary
		 */
		this.provider = provider;
	}
	
	public abstract void visit (ITreatmentVisitor visitor);
	
	public Treatment()
	{
		super();
	}

}
