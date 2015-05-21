
package edu.stevens.cs548.clinic.service.web.rest.resources;

import javax.ws.rs.ApplicationPath;

import com.sun.jersey.api.core.PackagesResourceConfig;

@ApplicationPath("/resources")
public class WSConfiguration extends PackagesResourceConfig {

	public WSConfiguration() {
		super("edu.stevens.cs548.clinic.service.web.rest.resources");
	}

}
