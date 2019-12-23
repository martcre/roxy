package de.martcre.roxy.backend.dao;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import de.martcre.roxy.backend.dto.AdressDTO;
import de.martcre.roxy.backend.dto.DummyDTO;

public class DummyDAO {
	private static String[] firstNames = new String[] { "Evia", "Amalia", "Ali", "Jarred", "Nadia", "Hipolito", "Yon",
			"Detra", "Son", "Marcellus", "Yolonda", "Theo", "Jacqueline", "Mariela", "Johanne", "Kori", "Marchelle" };

	private static String[] lastNames = new String[] { "Ryan", "Hardin", "Clements", "Bradshaw", "Davenport", "Barker",
			"Proctor", "Graves", "Graham", "Sharp", "Farmer", "Sandoval", "Potts", "Reynolds", "Martinez", "Duffy",
			"Reilly", "Tyler", "Farrell", "Parks" };

	private static int dummyIdCounter;

	private static int getNextDummyIdCounter() {
		return (dummyIdCounter++);
	}

	public static int getRandom(int arrayLength) {
		return new Random().nextInt(arrayLength);
	}

	private static String getRandomFirstName() {
		return firstNames[getRandom(firstNames.length)];
	}

	private static String getRandomLastName() {
		return lastNames[getRandom(lastNames.length)];
	}

	public static DummyDTO randomDummyGenerator() {
		return new DummyDTO(BigInteger.valueOf(getNextDummyIdCounter()), getRandomFirstName(), getRandomLastName(),
				AdressDAO.randomAdressGenerator(),
				new HashSet<AdressDTO>(Arrays.asList(AdressDAO.randomAdressGenerator(),
						AdressDAO.randomAdressGenerator(), AdressDAO.randomAdressGenerator())));
	}
}
