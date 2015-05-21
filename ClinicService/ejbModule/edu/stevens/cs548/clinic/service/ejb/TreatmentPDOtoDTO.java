package edu.stevens.cs548.clinic.service.ejb;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.treatment.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.treatment.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;

public class TreatmentPDOtoDTO implements ITreatmentVisitor
{

	private static Logger logger = Logger.getLogger(TreatmentPDOtoDTO.class
			.getCanonicalName());

	private TreatmentDto dto;

	public TreatmentDto getDTO()
	{
		return dto;
	}

	@Override
	public void visitDrugTreatment(long tid, long patid, long prid,
			String diagnosis, String drug, float dosage)
	{
		logger.info("visitDrugTreatment(): " + "tid=" + tid + ", " + "patid="
				+ patid + ", " + "prid=" + prid + ", " + "diagnosis="
				+ diagnosis + ", " + "drug=" + drug + ", " + "dosage=" + dosage);

		dto = new TreatmentDto();
		dto.setTreatmentId(tid);
		dto.setPatientId(patid);
		dto.setProviderId(prid);
		dto.setDiagnosis(diagnosis);

		DrugTreatmentType drugInfo = new DrugTreatmentType();
		drugInfo.setDosage(dosage);
		drugInfo.setName(drug);
		dto.setDrugTreatment(drugInfo);
	}

	@Override
	public void visitRadiology(long tid, long patid, long prid,
			String diagnosis, List<Date> dates)
	{
		logger.info("visitRadiology(): " + "tid=" + tid + ", " + "patid="
				+ patid + ", " + "prid=" + prid + ", " + "diagnosis="
				+ diagnosis + ", " + "dates="
				+ Arrays.toString(dates.toArray()));

		// TODO Auto-generated method stub
		dto = new TreatmentDto();
		dto.setTreatmentId(tid);
		dto.setPatientId(patid);
		dto.setProviderId(prid);
		dto.setDiagnosis(diagnosis);

		RadiologyType radiologyInfo = new RadiologyType();
		radiologyInfo.setRadiologist(diagnosis);
		List<Date> radioDates = radiologyInfo.getDate();
		for (int i = 0; i < dates.size(); i++)
		{
			radioDates.add(dates.get(i));
		}
		dto.setRadiology(radiologyInfo);
	}

	@Override
	public void visitSurgery(long tid, long patid, long prid, String diagnosis,
			Date date)
	{
		logger.info("visitSurgery(): " + "tid=" + tid + ", " + "patid=" + patid
				+ ", " + "prid=" + prid + ", " + "diagnosis=" + diagnosis
				+ ", " + "date=" + date);

		// TODO Auto-generated method stub
		dto = new TreatmentDto();
		dto.setTreatmentId(tid);
		dto.setPatientId(patid);
		dto.setProviderId(prid);
		dto.setDiagnosis(diagnosis);

		SurgeryType surgeryInfo = new SurgeryType();
		surgeryInfo.setDate(date);
		dto.setSurgery(surgeryInfo);
	}

}
