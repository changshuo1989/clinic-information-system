package edu.stevens.cs548.clinic.service.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.IProviderFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentDAO;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;

@Stateless(name = "ProviderServiceBean")
public class ProviderService implements IProviderServiceLocal,
		IProviderServiceRemote
{
	private static Logger logger = Logger.getLogger(ProviderService.class
			.getCanonicalName());

	private IProviderFactory provFactory;
	private ProviderDtoFactory provDtoFactory;

	private IProviderDAO provDAO;
	private IPatientDAO patDAO;
	private ITreatmentDAO trmtDAO;

	public ProviderService()
	{
		provFactory = new ProviderFactory();
		provDtoFactory = new ProviderDtoFactory();
	}

	@PersistenceContext(unitName = "ClinicDomain")
	private EntityManager em;

	@PostConstruct
	private void onInitialize()
	{
		provDAO = new ProviderDAO(em);
		patDAO = new PatientDAO(em);
		trmtDAO = new TreatmentDAO(em);
	}

	@Override
	public long addProvider(long npi, String name, String spec) throws ProviderServiceExn
	{
		try
		{
			Provider prov = provFactory.createProvider(npi, name, spec);
			return provDAO.addProvider(prov);
		}
		catch (ProviderExn e)
		{
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public ProviderDto[] getProviderByName(String name)
	{
		List<Provider> providers = provDAO.getProviderByName(name);
		ProviderDto[] provDtos = new ProviderDto[providers.size()];
		for (int i = 0; i < providers.size(); i++)
		{
			provDtos[i] = provDtoFactory.createProviderDto(providers.get(i));
		}

		return provDtos;
	}

	@Override
	public ProviderDto getProviderByNPI(long npi) throws ProviderServiceExn
	{
		try
		{
			
			Provider prov = provDAO.getProviderByNPI(npi);
			logger.info("provider =" + prov.toString());
			return provDtoFactory.createProviderDto(prov);
		}
		catch (ProviderExn e)
		{
			throw new ProviderServiceExn(e.toString());
		}
	}

	
	@Override
	public long addTreatment(long patId, long provNpi, TreatmentDto trmtDto)
			throws PatientNotFoundExn, ProviderNotFoundExn, ProviderServiceExn
	{
		try
		{
			Patient pat = patDAO.getPatientByPatientId(patId);
			Provider prov = provDAO.getProviderByNPI(provNpi);
			if (trmtDto.getDrugTreatment() != null)
			{
				return prov.addDrugTreatment(pat, trmtDto.getDiagnosis(),
						trmtDto.getDrugTreatment().getName(), trmtDto
								.getDrugTreatment().getDosage());
			}
			else if (trmtDto.getRadiology() != null)
			{
				return prov.addRadiologyTreatment(pat, trmtDto.getDiagnosis(),
						trmtDto.getRadiology().getDate());
			}
			else if (trmtDto.getSurgery() != null)
			{
				return prov.addSurgeryTreatment(pat, trmtDto.getDiagnosis(),
						trmtDto.getSurgery().getDate());
			}

			throw new ProviderServiceExn("TreatmentDto is invalid.");
		}
		catch (PatientExn e)
		{
			throw new PatientNotFoundExn(e.toString());
		}
		catch (ProviderExn e)
		{
			throw new ProviderNotFoundExn(e.toString());
		}

	}
	
	
	@Override
	public long addTreatmentByDbId(long patDbId, long provDbId, TreatmentDto trmtDto)
			throws PatientNotFoundExn, ProviderNotFoundExn, ProviderServiceExn
	{
		try
		{
			Patient pat = patDAO.getPatientByDbId(patDbId);
			Provider prov = provDAO.getProviderByDbId(provDbId);
			if (trmtDto.getDrugTreatment() != null)
			{
				return prov.addDrugTreatment(pat, trmtDto.getDiagnosis(),
						trmtDto.getDrugTreatment().getName(), trmtDto
								.getDrugTreatment().getDosage());
			}
			else if (trmtDto.getRadiology() != null)
			{
				return prov.addRadiologyTreatment(pat, trmtDto.getDiagnosis(),
						trmtDto.getRadiology().getDate());
			}
			else if (trmtDto.getSurgery() != null)
			{
				return prov.addSurgeryTreatment(pat, trmtDto.getDiagnosis(),
						trmtDto.getSurgery().getDate());
			}

			throw new ProviderServiceExn("TreatmentDto is invalid.");
		}
		catch (PatientExn e)
		{
			throw new PatientNotFoundExn(e.toString());
		}
		catch (ProviderExn e)
		{
			throw new ProviderNotFoundExn(e.toString());
		}

	}
	

	@Override
	public void deleteTreatment(long tid, long patId, long provNpi)
			throws ProviderNotFoundExn, ProviderServiceExn
	{
		try
		{
			Provider prov = provDAO.getProviderByNPI(provNpi);
			Treatment trmt = trmtDAO.getTreatmentByDbId(tid);
			if (trmt.getPatient().getPatientId() != patId)
			{
				throw new ProviderServiceExn("Treatment (id=" + tid
						+ ") does not conform to the Patient (pid=" + patId
						+ ").");
			}
			logger.info("invoking Provider.deleteTreatment()...");
			prov.deleteTreatment(tid);
			logger.info("returning from deleteTreatment()...");
		}
		catch (ProviderExn e)
		{
			throw new ProviderNotFoundExn(e.toString());
		}
		catch (TreatmentExn e)
		{
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public TreatmentDto[] getTreatments(long patId, long provNpi)
			throws ProviderNotFoundExn, PatientNotFoundExn,
			TreatmentNotFoundExn
	{
		try
		{
			Patient pat = patDAO.getPatientByPatientId(patId);
			Provider prov = provDAO.getProviderByNPI(provNpi);
			List<Long> trmtIds = prov.getTreatmentIds(pat);
			TreatmentDto[] trmtDtos = new TreatmentDto[trmtIds.size()];
			TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();

			Treatment trmt;
			for (int i = 0; i < trmtIds.size(); i++)
			{
				trmt = trmtDAO.getTreatmentByDbId(trmtIds.get(i));
				trmt.visit(visitor);
				trmtDtos[i] = visitor.getDTO();
			}
			
			logger.info("returning from getTreatments()...");			
			return trmtDtos;
		}
		catch (PatientExn e)
		{
			throw new PatientNotFoundExn(e.toString());
		}
		catch (ProviderExn e)
		{
			throw new ProviderNotFoundExn(e.toString());
		}
		catch (TreatmentExn e)
		{
			throw new TreatmentNotFoundExn(e.toString());
		}
	}

	@Override
	public TreatmentDto[] getTreatmentsByDbId(long patDbId, long provDbId) 
			throws ProviderNotFoundExn, PatientNotFoundExn,
			TreatmentNotFoundExn
	{
	try{
		Patient pat= patDAO.getPatientByDbId(patDbId);
		Provider prov=provDAO.getProviderByDbId(provDbId);
		List<Long> trmtIds = prov.getTreatmentIds(pat);
		TreatmentDto[] trmtDtos = new TreatmentDto[trmtIds.size()];
		TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();
		
		Treatment trmt;
		for (int i = 0; i < trmtIds.size(); i++)
		{
			trmt = trmtDAO.getTreatmentByDbId(trmtIds.get(i));
			trmt.visit(visitor);
			trmtDtos[i] = visitor.getDTO();
		}
		
		return trmtDtos;
	}
	catch (PatientExn e)
	{
		throw new PatientNotFoundExn(e.toString());
	}
	catch (ProviderExn e)
	{
		throw new ProviderNotFoundExn(e.toString());
	}
	catch (TreatmentExn e)
	{
		throw new TreatmentNotFoundExn(e.toString());
	}
	}
	
	
	@Override
	public TreatmentDto[] getTreatments(long provNpi)
			throws ProviderNotFoundExn, TreatmentNotFoundExn
	{
		try
		{
			Provider prov = provDAO.getProviderByNPI(provNpi);
			List<Long> trmtIds = prov.getTreatmentIds();
			TreatmentDto[] trmtDtos = new TreatmentDto[trmtIds.size()];
			TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();

			Treatment trmt;
			for (int i = 0; i < trmtIds.size(); i++)
			{
				trmt = trmtDAO.getTreatmentByDbId(trmtIds.get(i));
				trmt.visit(visitor);
				trmtDtos[i] = visitor.getDTO();
			}

			return trmtDtos;
		}
		catch (ProviderExn e)
		{
			throw new ProviderNotFoundExn(e.toString());
		}
		catch (TreatmentExn e)
		{
			throw new TreatmentNotFoundExn(e.toString());
		}
	}

	@Resource(name = "SiteInfo")
	private String siteInformation;

	@Override
	public String siteInfo()
	{
		return siteInformation;
	}
}
