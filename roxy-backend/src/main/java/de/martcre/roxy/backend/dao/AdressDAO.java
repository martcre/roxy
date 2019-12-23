package de.martcre.roxy.backend.dao;

import java.math.BigInteger;

import de.martcre.roxy.backend.dto.AdressDTO;

public class AdressDAO {
	private static int adressIdCounter;

	private static int getNextAdressIdCounter() {
		return (adressIdCounter++);
	}

	public static AdressDTO randomAdressGenerator() {
		return new AdressDTO(BigInteger.valueOf(getNextAdressIdCounter()), "street", "number", "city", "postalCode");
	}
}
