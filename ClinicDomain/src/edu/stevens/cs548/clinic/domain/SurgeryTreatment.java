package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: SurgeryTreatment
 * 
 */
@Entity
public class SurgeryTreatment extends Treatment implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	
	private Date surgeryDate;

	public Date getSurgeryDate()
	{
		return surgeryDate;
	}

	public void setSurgeryDate(Date surgeryDate)
	{
		this.surgeryDate = surgeryDate;
	}

	@Override
	public void visit(ITreatmentVisitor visitor)
	{
		visitor.visitSurgery(this.getId(),
				this.getPatient().getId(),
				this.getProvider().getId(),
				this.getDiagnosis(),
				this.surgeryDate);
	}
	
	public SurgeryTreatment()
	{
		super();
		super.setTreatmentType("surgery treatment");
	}

}
