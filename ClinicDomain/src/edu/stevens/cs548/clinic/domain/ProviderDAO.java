package edu.stevens.cs548.clinic.domain;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ProviderDAO implements IProviderDAO
{
	private EntityManager em;
	private TreatmentDAO treatmentDAO;
	
	private Logger logger = Logger
			.getLogger("edu.stevens.cs548.clinic.domain.ProviderDAO");
	
	@Override
	public long addProvider(Provider prov) throws ProviderExn
	{
		long npi = prov.getNpi();
		TypedQuery<Provider> query = em.createNamedQuery(
				"SearchProviderByNPI", Provider.class).setParameter("npi", npi);
		List<Provider> providers = query.getResultList();
		if (providers.size() < 1)
		{
			em.persist(prov);
			prov.setTreatmentDAO(treatmentDAO);
			
			logger.info("addProvider(): "
					+ "id=" + prov.getId() + ", "
					+ "npi=" + prov.getNpi() + ", "
					+ "name=" + prov.getName());
			
			return prov.getId();
		}
		else
		{
			//the npi already exists
			Provider provider2 = providers.get(0);
			throw new ProviderExn("Insertion: Provider with npi (" + npi
					+ ") already exists.\n** Name: " + provider2.getName());
		}
	}
	
	public Provider getProviderByNPI(long npi) throws ProviderExn
	{
		TypedQuery<Provider> query = em.createNamedQuery(
				"SearchProviderByNPI", Provider.class).setParameter("npi", npi);
		logger.info("search database sucess!");
		List<Provider> providers = query.getResultList();
		logger.info("get providers sucess!");
		if (providers.size() < 1)
		{
			logger.info("getProviderByNPI() Provider Not Found: npi=" + npi);
			throw new ProviderExn("getProviderByNPI() Provider Not Found: provider npi = "
					+ npi);
		}
		else if (providers.size() > 1)
		{
			logger.info("Duplicate provider records: provider npi = " + npi);
			throw new ProviderExn("Duplicate provider records: provider npi = "
					+ npi);
		}
		else
		{
			Provider p = providers.get(0);
			logger.info(p.toString());
			p.setTreatmentDAO(this.treatmentDAO);		
			logger.info("getProviderByNPI() succ: npi=" + npi);
			
			return p;
		}
	}
	
	@Override
	public Provider getProviderByDbId(long id) throws ProviderExn {
		TypedQuery<Provider> query = em.createNamedQuery(
				"SearchProviderById", Provider.class).setParameter("id", id);
		List<Provider> providers = query.getResultList();
		
		if (providers.size() < 1)
		{
			logger.info("getProviderById() Provider Not Found: Id=" + id);
			throw new ProviderExn("getProviderById() Provider Not Found: provider Id = "
					+ id);
		}
		else if (providers.size() > 1)
		{
			logger.info("Duplicate provider records: provider id = " + id);
			throw new ProviderExn("Duplicate provider records: provider id = "
					+ id);
		}
		else
		{
			Provider p = providers.get(0);
			logger.info(p.toString());
			p.setTreatmentDAO(this.treatmentDAO);		
			logger.info("getProviderById() succ: Id=" + id);
			
			return p;
		}
	}
	
	
	
	public List<Provider> getProviderByName(String name)
	{
		TypedQuery<Provider> query = em
				.createNamedQuery("SearchProviderByName", Provider.class)
				.setParameter("name", name);
		List<Provider> providers = query.getResultList();
		for (Provider p : providers)
		{
			p.setTreatmentDAO(this.treatmentDAO);
		}
		return providers;
	}
	
	public void deleteProvider(Provider prov) throws ProviderExn
	{
		em.remove(prov);
		logger.info("deleteProvider() succ: " 
				+ "id=" + prov.getId() + ", "
				+ "npi=" + prov.getNpi() + ", "
				+ "name=" + prov.getName() + "}");
	}
	
	public ProviderDAO(EntityManager em)
	{
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}



}
