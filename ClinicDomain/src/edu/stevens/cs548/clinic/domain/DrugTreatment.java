package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Entity implementation class for Entity: DrugTreatment
 * 
 */
@Entity
public class DrugTreatment extends Treatment implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	@Transient
	private Logger logger = Logger
			.getLogger("edu.stevens.cs548.clinic.domain.DrugTreatment");
	
	@Column(name = "DRUG")

	private String drug;

	public String getDrug()
	{
		return drug;
	}

	public void setDrug(String drug)
	{
		this.drug = drug;
	}

	@Column(name = "DOSAGE")
	private float dosage;

	public float getDosage()
	{
		return dosage;
	}

	public void setDosage(float dosage)
	{
		this.dosage = dosage;
	}

	@Override
	public void visit(ITreatmentVisitor visitor)
	{
		visitor.visitDrugTreatment(this.getId(),
				this.getPatient().getId(),
				this.getProvider().getId(),
				this.getDiagnosis(),
				this.drug, this.dosage);
	}

	public DrugTreatment()
	{
		super();
		/*
		 * TODO: Finish with discriminator.
		 */
		super.setTreatmentType("drug treatment");
	}

}
