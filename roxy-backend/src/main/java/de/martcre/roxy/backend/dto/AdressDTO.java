package de.martcre.roxy.backend.dto;

import java.math.BigInteger;

public class AdressDTO {
		private BigInteger id;
		private String street;
		private String number;
		private String city;
		private String postalCode;
		
		public AdressDTO(BigInteger id, String street, String number, String city, String postalCode) {
			super();
			this.id = id;
			this.street = street;
			this.number = number;
			this.city = city;
			this.postalCode = postalCode;
		}
		public BigInteger getId() {
			return id;
		}
		public void setId(BigInteger id) {
			this.id = id;
		}
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getPostalCode() {
			return postalCode;
		}
		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}
	}