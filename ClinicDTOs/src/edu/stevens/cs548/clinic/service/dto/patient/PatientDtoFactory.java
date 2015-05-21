package edu.stevens.cs548.clinic.service.dto.patient;

import java.util.List;

import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientFactory;

public class PatientDtoFactory
{

	ObjectFactory factory;

	public PatientDtoFactory()
	{
		factory = new ObjectFactory();
	}

	public PatientDto createPatientDto(Patient p)
	{
		PatientDto dto = factory.createPatientDto();
		PatientFactory patFactory = new PatientFactory();
		/*
		 * TODO: Initialize the fields of the DTO.
		 */
		dto.setId(p.getId());
		dto.setPatientId(p.getPatientId());
		dto.setName(p.getName());
		dto.setDob(p.getBirthDate());
		dto.setAge(patFactory.computeAge(p.getBirthDate()));
		List<Long> trmtIds = dto.getTreatments();
		for (Long tid : p.getTreatmentIds())
		{
			trmtIds.add(tid);
		}
		
		return dto;
	}
}
