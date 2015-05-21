package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

/**
 * Entity implementation class for Entity: RadiologyTreatment
 *
 */
@Entity

public class RadiologyTreatment extends Treatment implements Serializable {

	
	private static final long serialVersionUID = 1L;

	private List<Date> radiologyDates;
	
	public List<Date> getRadiologyDates()
	{
		return radiologyDates;
	}
	
	public void setRadiologyDates(List<Date> radiologyDates)
	{
		this.radiologyDates = radiologyDates;
	}
	
	@Override
	public void visit(ITreatmentVisitor visitor)
	{
		visitor.visitRadiology(this.getId(),
				this.getPatient().getId(),
				this.getProvider().getId(),
				this.getDiagnosis(),
				this.radiologyDates);
	}
	
	public RadiologyTreatment() {
		super();
		super.setTreatmentType("radiology treatment");
		radiologyDates = new ArrayList<Date>();
	}
   
}
