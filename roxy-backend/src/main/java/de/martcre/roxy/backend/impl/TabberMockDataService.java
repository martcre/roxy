package de.martcre.roxy.backend.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.pojo.ObjectTableDataProvider;
import org.apache.metamodel.pojo.PojoDataContext;
import org.apache.metamodel.query.FilterItem;
import org.apache.metamodel.query.OrderByItem.Direction;
import org.apache.metamodel.schema.Column;
import org.apache.metamodel.schema.Table;

import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import de.martcre.roxy.backend.TabberDataService;
import de.martcre.roxy.backend.dao.DummyDAO;
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
			for (int i = 0; i < 10000; i++) {
				dummies.add(DummyDAO.randomDummyGenerator());
			}
		}

		mmDataProvider = new ObjectTableDataProvider<DummyDTO>("dummy", DummyDTO.class, dummies);
		mmDataContext = new PojoDataContext("default", mmDataProvider);
	}

	@SuppressWarnings("serial")
	@Override
	public ConfigurableFilterDataProvider<Row, Void, Set<FilterItem>> createDataProvider() {
		CallbackDataProvider<Row, Set<FilterItem>> dataProvider = new CallbackDataProvider<Row, Set<FilterItem>>(
				new CallbackDataProvider.FetchCallback<Row, Set<FilterItem>>() {

					@Override
					public Stream<Row> fetch(Query<Row, Set<FilterItem>> query) {
						/*
						 * Prepare the MetaModel Query, selecting all Columns.
						 */
						org.apache.metamodel.query.Query mmQuery = new org.apache.metamodel.query.Query();
						Table myTable = mmDataContext.getTableByQualifiedLabel("dummy");

						mmQuery.from(myTable);
						mmQuery.selectAll();

						/*
						 * Apply filtering:
						 */
						applyFiltering(mmQuery, query);

						/*
						 * Getting all sorting information from the query and translate it to the
						 * MetaModel orderBy clause.
						 */
						for (QuerySortOrder querySortOrder : query.getSortOrders()) {
							Column orderByColumn = myTable.getColumnByName(querySortOrder.getSorted());
							mmQuery.orderBy(orderByColumn,
									((querySortOrder.getDirection().equals(SortDirection.ASCENDING)) ? Direction.ASC
											: Direction.DESC));
						}

						/*
						 * Mapping the Offset and Limit of Vaadin query to the MetaModel Query.
						 */
						mmQuery.setFirstRow(query.getOffset() + 1);
						mmQuery.setMaxRows(query.getLimit());

						/*
						 * Execute the MetaModel query and return a stream of result rows.
						 */
						DataSet dataSet = mmDataContext.executeQuery(mmQuery);
						return dataSet.toRows().stream();
					}
				}, new CallbackDataProvider.CountCallback<Row, Set<FilterItem>>() {

					@Override
					public int count(Query<Row, Set<FilterItem>> query) {
						/*
						 * Prepare the MetaModel Query, counting all entries.
						 */
						org.apache.metamodel.query.Query mmQuery = new org.apache.metamodel.query.Query();
						Table myTable = mmDataContext.getTableByQualifiedLabel("dummy");

						mmQuery.from(myTable);
						mmQuery.selectCount();

						/*
						 * Apply filtering:
						 */
						applyFiltering(mmQuery, query);

						DataSet dataSet = mmDataContext.executeQuery(mmQuery);

						if (dataSet.next()) {
							return Math.toIntExact((Long) dataSet.getRow().getValue(0));
						} else {
							return 0;
						}
					}
				});

		return dataProvider.withConfigurableFilter();
	}

	/**
	 * Apply filtering on query. This is extracted to an own method as it has to be
	 * used on both the CountCallback and the FetchCallback.
	 * 
	 * Filter out all empty filters (=empty strings).
	 * 
	 * @param mmQuery the MetaModel Query
	 * @param query   the Vaadin Query
	 */
	private static void applyFiltering(org.apache.metamodel.query.Query mmQuery, Query<Row, Set<FilterItem>> query) {
		if (query.getFilter().isPresent()) {
			query.getFilter().get().stream()
					.filter(item -> (item.getOperand() instanceof String && !((String) item.getOperand()).isEmpty())
							|| (item.getOperand() instanceof String[] && ((String[]) item.getOperand()).length > 0))
					.forEach(c -> {
						mmQuery.where(c);
					});
		}
	}

	@Override
	public List<Column> getColumns() {
		return mmDataContext.getTableByQualifiedLabel("dummy").getColumns();
	}
}
