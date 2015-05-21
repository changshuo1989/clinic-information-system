package edu.stevens.cs548.clinic.service.dto.provider;

import java.util.List;

import edu.stevens.cs548.clinic.domain.Provider;

public class ProviderDtoFactory
{
	ObjectFactory objFactory;
	
	public ProviderDtoFactory()
	{
		objFactory = new ObjectFactory();
	}
	
	public ProviderDto createProviderDto(Provider prov)
	{
		ProviderDto provDto = objFactory.createProviderDto();
		
		provDto.setProviderId(prov.getId());
		provDto.setNpi(prov.getNpi());
		provDto.setName(prov.getName());
		provDto.setProviderType(prov.getProviderType());
		List<Long> trmtIds = provDto.getTreatments();
		for (Long tid : prov.getTreatmentIds())
		{
			trmtIds.add(tid);
		}
		
		return provDto;
	}
}
