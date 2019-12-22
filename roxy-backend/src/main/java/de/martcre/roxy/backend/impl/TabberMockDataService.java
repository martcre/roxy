package de.martcre.roxy.backend.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.pojo.ObjectTableDataProvider;
import org.apache.metamodel.pojo.PojoDataContext;
import org.apache.metamodel.query.OrderByItem.Direction;
import org.apache.metamodel.schema.Column;

import com.vaadin.data.provider.BackEndDataProvider;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import de.martcre.roxy.backend.TabberDataService;
import de.martcre.roxy.backend.dto.AdressDTO;
import de.martcre.roxy.backend.dto.DummyDTO;

@Stateless
public class TabberMockDataService implements TabberDataService {

	protected static Logger logger = LogManager.getLogger(TabberMockDataService.class);

	private ObjectTableDataProvider<DummyDTO> mmDataProvider;
	private PojoDataContext mmDataContext;
	
	private static List<DummyDTO> dummies = new ArrayList<>();
	

	public TabberMockDataService() {
		logger.info("tabberMockDataService constructed");
		
		if (dummies.isEmpty()) {
			for (int i = 0; i < 10000; i ++) {
				dummies.add(randomDummyGenerator());
			}
		}
		
		mmDataProvider = new ObjectTableDataProvider<DummyDTO>("dummy", DummyDTO.class, dummies);
		mmDataContext = new PojoDataContext("default", mmDataProvider);
	}

	
	@SuppressWarnings("serial")
	@Override
	public BackEndDataProvider<Row, ?> getDataProvider() {
		CallbackDataProvider<Row, ?> dataProvider = new CallbackDataProvider<Row, Object>(
				new CallbackDataProvider.FetchCallback<Row, Object>() {

					@Override
					public Stream<Row> fetch(Query<Row, Object> query) {
						org.apache.metamodel.query.Query mmQuery = new org.apache.metamodel.query.Query();
						mmQuery.from(mmDataContext.getTableByQualifiedLabel("dummy"));
						mmQuery.selectAll();
						
						
						
						for (QuerySortOrder querySortOrder : query.getSortOrders()) {
							Column orderByColumn = mmDataContext.getTableByQualifiedLabel("dummy").getColumnByName(querySortOrder.getSorted());
							mmQuery.orderBy(orderByColumn, ((querySortOrder.getDirection().equals(SortDirection.ASCENDING)) ? Direction.ASC : Direction.DESC));
						}
						
						mmQuery.setFirstRow(query.getOffset()+1);
						mmQuery.setMaxRows(query.getLimit());
						
						DataSet dataSet = mmDataContext.executeQuery(mmQuery);
						return dataSet.toRows().stream();
					}
				}, new CallbackDataProvider.CountCallback<Row, Object>() {

					@Override
					public int count(Query<Row, Object> query) {
						DataSet dataSet = mmDataContext.query().from("dummy").selectCount().execute();
						if (dataSet.next()) {
							return Math.toIntExact((Long) dataSet.getRow().getValue(0));
						} else {
							return 0;
						}
					}
				});

		return dataProvider;
	}

	@Override
	public List<Column> getColumns() {
		return mmDataContext.getTableByQualifiedLabel("dummy").getColumns();
	}

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

	private static DummyDTO randomDummyGenerator() {
		return new DummyDTO(BigInteger.valueOf(getNextDummyIdCounter()), getRandomFirstName(), getRandomLastName(),
				randomAdressGenerator(), new HashSet<AdressDTO>(
						Arrays.asList(randomAdressGenerator(), randomAdressGenerator(), randomAdressGenerator())));
	}

	private static int adressIdCounter;

	private static int getNextAdressIdCounter() {
		return (adressIdCounter++);
	}

	private static AdressDTO randomAdressGenerator() {
		return new AdressDTO(BigInteger.valueOf(getNextAdressIdCounter()), "street", "number", "city", "postalCode");
	}

}
