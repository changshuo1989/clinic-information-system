package edu.stevens.cs548.clinic.domain;

public interface IClinicGateway {
	
	public IPatientFactory getPatientFactory ();
	
	public IPatientDAO getPatientDAO();
	
	/*The provider factory*/
	public IProviderFactory getProviderFactory ();
	
	public IProviderDAO getProviderDAO();
	
}
