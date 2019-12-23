package de.martcre.roxy.tabber;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.query.FilterItem;
import org.apache.metamodel.query.OperatorType;
import org.apache.metamodel.query.SelectItem;
import org.apache.metamodel.schema.Column;

import com.vaadin.data.Binder;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.HeaderRow;

import de.martcre.roxy.backend.TabberDataService;

@SuppressWarnings("serial")
public class TabberDataExplorer extends CustomComponent {

	protected static Logger logger = LogManager.getLogger(TabberDataExplorer.class);

	private TabberDataExplorerDesign design;

	public TabberDataExplorer(TabberDataService tabberDataService) {
		setCompositionRoot(getDesign());
		setSizeFull();

		/*
		 * Obtain a new DataProvider from the TabberDataService and put it into the Grid
		 * to retrieve the Data and generate a Header Row to host the Filter fields.
		 */
		ConfigurableFilterDataProvider<Row, Void, Set<FilterItem>> dataProvider = tabberDataService
				.createDataProvider();
		Grid<Row> tabberGrid = getDesign().getGrid();
		tabberGrid.setDataProvider(dataProvider);
		HeaderRow filterRow = tabberGrid.appendHeaderRow();
		HeaderRow operatorRow = tabberGrid.appendHeaderRow();

		/*
		 * Setup a Binder Object to collect data from all filter and operator fields and
		 * configure a ValueChangeListener to update the dataProvider with the current
		 * Filter setup.
		 */
		Binder<Map<Column, FilterItemParameter>> filterBinder = new Binder<>();
		filterBinder.setBean(new HashMap<>());
		filterBinder.addValueChangeListener(event -> {
			/*
			 * On any change of any filter, set the Set of FilterItem on the dataProvider.
			 * If the OperatorType is IN or NOT_IN, split the operand on regex "\W*,\W*"
			 */
			Set<FilterItem> filterItems = new HashSet<>();
			filterBinder.getBean().values().stream().filter(item -> !(item.operand == null || item.operand.isEmpty()))
					.forEach(params -> filterItems.add(new FilterItem(params.selectItem,
							params.operator.getOperatorType(),
							((params.operator == TabberOperatorType.IN || params.operator == TabberOperatorType.NOT_IN)
									? params.operand.split("\\W*,\\W*")
									: params.operand))));
			dataProvider.setFilter(filterItems);
		});

		/*
		 * Retrieve all Columns from the TabberDataService to generate Columns in the
		 * Grid and corresponding Filter fields.
		 */
		for (Column column : tabberDataService.getColumns()) {
			Grid.Column<Row, Object> gridColumn = tabberGrid.addColumn(row -> row.getValue(column))
					.setSortProperty(column.getName()).setCaption(column.getName());

			if (column.getType().isLiteral() || column.getType().isNumber() || column.getType().isBinary()
					|| column.getType().isTimeBased()) {

				/*
				 * The fields required for the Filtering and Operator.
				 */
				TextField filterField = new TextField();
				ComboBox<TabberOperatorType> operatorField = new ComboBox<TabberOperatorType>();

				/*
				 * Bind the filterField and operatorField to the filterBinder into a custom
				 * FilterItemParameter structure.
				 */
				if (!filterBinder.getBean().containsKey(column)) {
					filterBinder.getBean().put(column, new FilterItemParameter());
					filterBinder.getBean().get(column).selectItem = new SelectItem(column);
				}
				filterBinder.forField(filterField).bind(map -> map.get(column).operand,
						(map, value) -> map.get(column).operand = value);
				filterBinder.forField(operatorField).bind(map -> map.get(column).operator,
						(map, value) -> map.get(column).operator = value);

				/*
				 * Setup of the filterField.
				 */
				filterField.setPlaceholder("Filter");
				filterField.setWidth("100%");
				filterRow.getCell(gridColumn).setComponent(filterField);

				/*
				 * Setup of the operatorField.
				 */
				operatorField.setWidth("100%");
				/*
				 * Set valid OperatorTypes for Literals:
				 */
				if (column.getType().isLiteral()) {
					operatorField.setDataProvider(new ListDataProvider<TabberOperatorType>(Arrays.asList(
							TabberOperatorType.LIKE, TabberOperatorType.NOT_LIKE, TabberOperatorType.EQUALS_TO,
							TabberOperatorType.DIFFERENT_FROM, TabberOperatorType.IN, TabberOperatorType.NOT_IN)));
					operatorField.setValue(TabberOperatorType.LIKE);
				}
				/*
				 * Set valid OperatorTypes for Numbers:
				 */
				else if (column.getType().isNumber()) {
					operatorField.setDataProvider(new ListDataProvider<TabberOperatorType>(
							Arrays.asList(TabberOperatorType.EQUALS_TO, TabberOperatorType.DIFFERENT_FROM,
									TabberOperatorType.LESS_THAN, TabberOperatorType.GREATER_THAN,
									TabberOperatorType.LESS_THAN_OR_EQUAL, TabberOperatorType.GREATER_THAN_OR_EQUAL,
									TabberOperatorType.IN, TabberOperatorType.NOT_IN)));
					operatorField.setValue(TabberOperatorType.EQUALS_TO);
				}
				/*
				 * Set valid OperatorTypes for Booleans:
				 */
				else if (column.getType().isBoolean()) {
					operatorField.setDataProvider(new ListDataProvider<TabberOperatorType>(
							Arrays.asList(TabberOperatorType.EQUALS_TO, TabberOperatorType.DIFFERENT_FROM)));
					operatorField.setValue(TabberOperatorType.EQUALS_TO);
				}
				/*
				 * Set valid OperatorTypes for TimeBased:
				 */
				else if (column.getType().isTimeBased()) {
					operatorField.setDataProvider(new ListDataProvider<TabberOperatorType>(
							Arrays.asList(TabberOperatorType.LIKE, TabberOperatorType.NOT_LIKE,
									TabberOperatorType.EQUALS_TO, TabberOperatorType.DIFFERENT_FROM,
									TabberOperatorType.LESS_THAN, TabberOperatorType.GREATER_THAN,
									TabberOperatorType.LESS_THAN_OR_EQUAL, TabberOperatorType.GREATER_THAN_OR_EQUAL,
									TabberOperatorType.IN, TabberOperatorType.NOT_IN)));
					operatorField.setValue(TabberOperatorType.LIKE);
				}

				operatorRow.getCell(gridColumn).setComponent(operatorField);
			}
		}
	}

	/*
	 * Helper Structure used by the filterBinder to preserve data before FilterItems
	 * are generated.
	 */
	private class FilterItemParameter {
		protected SelectItem selectItem;
		protected TabberOperatorType operator;
		protected String operand;
	}

	/*
	 * Helper Structure used in the Frontend to show the possible Operators.
	 */
	private enum TabberOperatorType {
		EQUALS_TO(OperatorType.EQUALS_TO, "EQUALS TO"), DIFFERENT_FROM(OperatorType.DIFFERENT_FROM, "DIFFERENT FROM"),
		LIKE(OperatorType.LIKE, "LIKE"), NOT_LIKE(OperatorType.NOT_LIKE, "NOT LIKE"),
		GREATER_THAN(OperatorType.GREATER_THAN, "GREATER THAN"),
		GREATER_THAN_OR_EQUAL(OperatorType.GREATER_THAN_OR_EQUAL, "GREATER_THAN_OR_EQUAL"),
		LESS_THAN(OperatorType.LESS_THAN, "LESS THAN"),
		LESS_THAN_OR_EQUAL(OperatorType.LESS_THAN_OR_EQUAL, "LESS THAN OR EQUAL"), IN(OperatorType.IN, "IN"),
		NOT_IN(OperatorType.NOT_IN, "NOT_IN");

		private final OperatorType operatorType;
		private final String caption;

		TabberOperatorType(OperatorType operatorType, String caption) {
			this.operatorType = operatorType;
			this.caption = caption;
		}

		@Override
		public String toString() {
			return this.caption;
		}

		public OperatorType getOperatorType() {
			return this.operatorType;
		}
	}

	private TabberDataExplorerDesign getDesign() {
		if (design == null) {
			design = new TabberDataExplorerDesign();
		}
		return design;
	}

}
