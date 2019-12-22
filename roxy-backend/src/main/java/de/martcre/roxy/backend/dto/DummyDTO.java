package de.martcre.roxy.backend.dto;

import java.math.BigInteger;
import java.util.Set;

public class DummyDTO {
	private BigInteger id;
	private String firstName;
	private String lastName;
	private AdressDTO homeAdress;
	private Set<AdressDTO> otherAdresses;
	
	public DummyDTO(BigInteger id, String firstName, String lastName, AdressDTO homeAdress,
			Set<AdressDTO> otherAdresses) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.homeAdress = homeAdress;
		this.otherAdresses = otherAdresses;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public AdressDTO getHomeAdress() {
		return homeAdress;
	}
	public void setHomeAdress(AdressDTO homeAdress) {
		this.homeAdress = homeAdress;
	}
	public Set<AdressDTO> getOtherAdresses() {
		return otherAdresses;
	}
	public void setOtherAdresses(Set<AdressDTO> otherAdresses) {
		this.otherAdresses = otherAdresses;
	}
}