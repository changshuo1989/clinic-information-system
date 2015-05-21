package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public interface ITreatmentVisitor
{
	public void visitDrugTreatment(long tid,
			long patid,
			long prid,
			String diagnosis,
			String drug,
			float dosage);

	public void visitRadiology(long tid, 
			long patid,
			long prid, 
			String diagnosis,
			List<Date> dates);

	public void visitSurgery(long tid, 
			long patid,
			long prid, 
			String diagnosis, 
			Date date);
}
