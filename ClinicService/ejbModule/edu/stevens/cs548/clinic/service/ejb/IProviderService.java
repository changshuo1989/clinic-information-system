package edu.stevens.cs548.clinic.service.ejb;

import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;

public interface IProviderService
{
	public class ProviderServiceExn extends Exception
	{
		private static final long serialVersionUID = 1L;

		public ProviderServiceExn(String m)
		{
			super(m);
		}
	}

	public class PatientNotFoundExn extends ProviderServiceExn
	{
		private static final long serialVersionUID = 1L;

		public PatientNotFoundExn(String m)
		{
			super(m);
		}
	}
	
	public class ProviderNotFoundExn extends ProviderServiceExn
	{
		private static final long serialVersionUID = 1L;

		public ProviderNotFoundExn(String m)
		{
			super(m);
		}
	}
	
	public class TreatmentNotFoundExn extends ProviderServiceExn
	{
		private static final long serialVersionUID = 1L;

		public TreatmentNotFoundExn(String m)
		{
			super(m);
		}
	}
	
	public long addProvider(long npi, String name, String spec)
		throws ProviderServiceExn;
	
	public ProviderDto[] getProviderByName(String name);
	
	public ProviderDto getProviderByNPI(long npi)
		throws ProviderServiceExn;
	
	public long addTreatment(long patId,
			long provNpi,
			TreatmentDto trmtDto)
		throws PatientNotFoundExn, ProviderNotFoundExn, ProviderServiceExn;
	
	public long addTreatmentByDbId(long patDbId,
			long provDbId,
			TreatmentDto trmtDto)
		throws PatientNotFoundExn, ProviderNotFoundExn, ProviderServiceExn;
	
	public void deleteTreatment(long tid, long patId, long provNpi)
			throws ProviderNotFoundExn, ProviderServiceExn;
	
	public TreatmentDto[] getTreatments(long patId, long provNpi)
		throws ProviderNotFoundExn, PatientNotFoundExn, TreatmentNotFoundExn;
	
	public TreatmentDto[] getTreatmentsByDbId(long patDbId, long provDbId) 
			throws ProviderNotFoundExn, PatientNotFoundExn, TreatmentNotFoundExn;
	
	public TreatmentDto[] getTreatments(long provNpi)
		throws ProviderNotFoundExn, TreatmentNotFoundExn;

	public String siteInfo();
}
