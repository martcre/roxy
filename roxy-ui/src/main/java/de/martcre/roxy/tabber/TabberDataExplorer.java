package de.martcre.roxy.tabber;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.pojo.ObjectTableDataProvider;
import org.apache.metamodel.pojo.PojoDataContext;
import org.apache.metamodel.schema.Table;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.CustomComponent;

@SuppressWarnings("serial")
public class TabberDataExplorer extends CustomComponent {
	
	private TabberDataExplorerDesign design;
	
	
	public TabberDataExplorer() {
		setCompositionRoot(getDesign());
		setSizeFull();
		
		DummyDTO martin = new DummyDTO(
				BigInteger.valueOf(1),
				"Max",
				"Foo",
				new AdressDTO(BigInteger.valueOf(1), "Street X", "123", "MyCity", "12512"),
				new HashSet<TabberDataExplorer.AdressDTO>(Arrays.asList(
						new AdressDTO(BigInteger.valueOf(2), "MyWay", "55", "My Other City", "12451"))));
		DummyDTO sonni = new DummyDTO(
				BigInteger.valueOf(1),
				"Al",
				"Bundy",
				new AdressDTO(BigInteger.valueOf(3), "Hollywood Drv.", "22", "Some City", "25121"),
				new HashSet<TabberDataExplorer.AdressDTO>(Arrays.asList(
						new AdressDTO(BigInteger.valueOf(4), "Other Street.", "721", "North Pole", "98171"))));
		
		
		ObjectTableDataProvider<DummyDTO> mmDataProvider = new ObjectTableDataProvider<DummyDTO>("dummy",
				DummyDTO.class, Arrays.asList(martin, sonni));
		
		PojoDataContext dataContext = new PojoDataContext("default", mmDataProvider);
		
		DataSet dataSet = dataContext.query().from("dummy").selectAll().execute();
		
		
		ListDataProvider<Row> dataProvider = new ListDataProvider<Row>(dataSet.toRows());
		getDesign().getGrid().setDataProvider(dataProvider);
		
		Table myTable = dataContext.getTableByQualifiedLabel("dummy");
		
		myTable.getColumns().stream().forEach(c -> getDesign().getGrid().addColumn(row -> row.getValue(c)).setCaption(c.getName()));
		
		
		
		
	}
	
	private TabberDataExplorerDesign getDesign() {
		if (design == null) {
			design = new TabberDataExplorerDesign();
		}
		return design;
	}
	
	
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
}
