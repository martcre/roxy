package de.martcre.roxy.backend;

import javax.ejb.Stateless;

@Stateless
public class DummyService {
	
	public String helloWorld(String name) {
		return "Hello " + name;
	}

}
