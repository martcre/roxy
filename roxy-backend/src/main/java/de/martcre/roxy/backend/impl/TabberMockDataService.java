package de.martcre.roxy.backend.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import javax.ejb.Stateful;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.pojo.ObjectTableDataProvider;
import org.apache.metamodel.pojo.PojoDataContext;
import org.apache.metamodel.schema.Column;

import com.vaadin.data.provider.BackEndDataProvider;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.Query;

import de.martcre.roxy.backend.TabberDataService;
import de.martcre.roxy.backend.dto.AdressDTO;
import de.martcre.roxy.backend.dto.DummyDTO;

@Stateful
public class TabberMockDataService implements TabberDataService {
	
	protected static Logger logger = LogManager.getLogger(TabberMockDataService.class);
	
	private ObjectTableDataProvider<DummyDTO> mmDataProvider;
	private PojoDataContext mmDataContext;
	
	
	public TabberMockDataService() {
		logger.info("tabberMockDataService constructed");
		DummyDTO martin = new DummyDTO(
				BigInteger.valueOf(1),
				"Max",
				"Foo",
				new AdressDTO(BigInteger.valueOf(1), "Street X", "123", "MyCity", "12512"),
				new HashSet<AdressDTO>(Arrays.asList(
						new AdressDTO(BigInteger.valueOf(2), "MyWay", "55", "My Other City", "12451"))));
		
		DummyDTO sonni = new DummyDTO(
				BigInteger.valueOf(1),
				"Al",
				"Bundy",
				new AdressDTO(BigInteger.valueOf(3), "Hollywood Drv.", "22", "Some City", "25121"),
				new HashSet<AdressDTO>(Arrays.asList(
						new AdressDTO(BigInteger.valueOf(4), "Other Street.", "721", "North Pole", "98171"))));
		
		
		mmDataProvider = new ObjectTableDataProvider<DummyDTO>("dummy",
				DummyDTO.class, Arrays.asList(martin, sonni));
		mmDataContext = new PojoDataContext("default", mmDataProvider);
		
	}

	@SuppressWarnings("serial")
	@Override
	public BackEndDataProvider<Row, ?> getDataProvider() {
		CallbackDataProvider<Row, ?> dataProvider = new CallbackDataProvider<Row, Object>(new CallbackDataProvider.FetchCallback<Row, Object>() {

			@Override
			public Stream<Row> fetch(Query<Row, Object> query) {
				DataSet dataSet = mmDataContext.query().from("dummy").selectAll().execute();
				return dataSet.toRows().stream();
			}
		}, new CallbackDataProvider.CountCallback<Row, Object>() {

			@Override
			public int count(Query<Row, Object> query) {
				DataSet dataSet = mmDataContext.query().from("dummy").selectCount().execute();
				
				logger.info("count ", dataSet.toRows());
				return 2;
			}
		});
		
		return dataProvider;
	}

	@Override
	public List<Column> getColumns() {
		return mmDataContext.getTableByQualifiedLabel("dummy").getColumns();
	}

}
