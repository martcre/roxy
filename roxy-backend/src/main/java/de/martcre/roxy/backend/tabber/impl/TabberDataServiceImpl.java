package de.martcre.roxy.backend.tabber.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.metamodel.DataContext;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.jdbc.JdbcDataContext;
import org.apache.metamodel.pojo.ObjectTableDataProvider;
import org.apache.metamodel.pojo.PojoDataContext;
import org.apache.metamodel.query.FilterItem;
import org.apache.metamodel.query.OrderByItem.Direction;
import org.apache.metamodel.schema.Column;
import org.apache.metamodel.schema.Table;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import de.martcre.roxy.backend.dao.DummyDAO;
import de.martcre.roxy.backend.dto.DummyDTO;
import de.martcre.roxy.backend.tabber.TabberDataService;

@SuppressWarnings("serial")
@Stateless
public class TabberDataServiceImpl implements TabberDataService {

	protected static Logger logger = LogManager.getLogger(TabberDataServiceImpl.class);

	private DataContext mmDataContext;

	private String schemaName;
	private String tableName;

	public TabberDataServiceImpl() {
		initJdbcContext();
	}

	private void initJdbcContext() {
		logger.info("tabberMockDataService constructed with JdbcContext");
		try {
			schemaName = "fomshop";
			tableName = "ACT_HI_ACTINST";

			mmDataContext = new JdbcDataContext((DataSource) InitialContext.doLookup("jndi/fomshop"));

		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private void initPojoContext() {
		logger.info("tabberMockDataService constructed with PojoContext");

		schemaName = "default";
		tableName = "dummy";

		ObjectTableDataProvider<DummyDTO> mmDataProvider = new ObjectTableDataProvider<DummyDTO>(tableName,
				DummyDTO.class, DummyDAO.getData());
		mmDataContext = new PojoDataContext(schemaName, mmDataProvider);
	}

	@Override
	public int count(Query<Row, Set<FilterItem>> query) {
		/*
		 * Prepare the MetaModel Query, counting all entries.
		 */
		org.apache.metamodel.query.Query mmQuery = new org.apache.metamodel.query.Query();
		Table myTable = mmDataContext.getTableByQualifiedLabel(tableName);

		mmQuery.from(myTable);
		mmQuery.selectCount();

		/*
		 * Apply filtering:
		 */
		applyFiltering(mmQuery, query);

		logger.debug(mmQuery.toSql());

		DataSet dataSet = mmDataContext.executeQuery(mmQuery);

		if (dataSet.next()) {
			return Math.toIntExact((Long) dataSet.getRow().getValue(0));
		} else {
			return 0;
		}
	}

	@Override
	public Stream<Row> fetch(Query<Row, Set<FilterItem>> query) {
		/*
		 * Prepare the MetaModel Query, selecting all Columns.
		 */
		org.apache.metamodel.query.Query mmQuery = new org.apache.metamodel.query.Query();
		Table myTable = mmDataContext.getTableByQualifiedLabel(tableName);

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
					((querySortOrder.getDirection().equals(SortDirection.ASCENDING)) ? Direction.ASC : Direction.DESC));
		}

		/*
		 * Mapping the Offset and Limit of Vaadin query to the MetaModel Query.
		 */
		mmQuery.setFirstRow(query.getOffset() + 1);
		mmQuery.setMaxRows(query.getLimit());

		logger.debug(mmQuery.toSql());

		/*
		 * Execute the MetaModel query and return a stream of result rows.
		 */
		DataSet dataSet = mmDataContext.executeQuery(mmQuery);
		return dataSet.toRows().stream();
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
							|| (item.getOperand() instanceof String[] && ((String[]) item.getOperand()).length > 0)
							|| (item.getOperand() instanceof Date))
					.forEach(c -> {
						mmQuery.where(c);
					});
		}
	}

	@Override
	public List<Column> getColumns() {
		return mmDataContext.getTableByQualifiedLabel(tableName).getColumns();
	}
}
