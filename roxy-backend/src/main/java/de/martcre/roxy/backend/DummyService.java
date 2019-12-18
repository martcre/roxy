package de.martcre.roxy.backend;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class DummyService {
	
	protected static Logger logger = LogManager.getLogger(DummyService.class);
	
	
	public String helloWorld(String name) {
		return "Hello " + name;
	}

}
