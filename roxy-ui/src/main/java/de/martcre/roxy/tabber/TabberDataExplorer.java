package de.martcre.roxy.tabber;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.query.FilterItem;
import org.apache.metamodel.query.SelectItem;
import org.apache.metamodel.schema.Column;

import com.vaadin.data.Binder;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

import de.martcre.roxy.backend.tabber.TabberDataService;

/**
 * TabberDataExplorer is a generic component to show Data in a Grid. It provides
 * Filtering and Sorting.
 * 
 * @author martin
 *
 */
@SuppressWarnings("serial")
public class TabberDataExplorer extends CustomComponent {

	protected static Logger logger = LogManager.getLogger(TabberDataExplorer.class);

	private TabberDataExplorerDesign design;
	private TabberDataService tabberDataService;
	private ConfigurableFilterDataProvider<Row, Void, Set<FilterItem>> dataProvider;
	private Binder<Map<Column, FilterItemParameter>> filterBinder;
	private ShortcutListener updateFilterOnEnter;

	/*
	 * Helper Structure used by the filterBinder to preserve data before FilterItems
	 * are generated.
	 */
	private class FilterItemParameter {
		protected SelectItem selectItem;
		protected TabberOperatorType operator;
		protected String operand;
	}

	/**
	 * Build a new TabberDataExplorer.
	 * 
	 * @param tabberDataService not null
	 */
	public TabberDataExplorer(TabberDataService tabberDataService) {
		this.tabberDataService = tabberDataService;
		setCompositionRoot(getDesign());
		setSizeFull();

		/*
		 * Build the Grid based on the information of TabberDataService:
		 */
		buildGrid();

		/*
		 * Update the Filtering on each change of a Filter if the LiveFilteringMode is
		 * active:
		 */
		getFilterBinder().addValueChangeListener(e -> {
			if (getDesign().getLiveFilteringMode().getValue()) {
				updateFiltering();
			}
		});
	}

	/**
	 * Build the content of the Grid.
	 */
	private void buildGrid() {
		/*
		 * Obtain a new DataProvider from the TabberDataService and put it into the Grid
		 * to retrieve the Data and generate a Header Row to host the Filter fields.
		 */
		Grid<Row> tabberGrid = getDesign().getGrid();
		tabberGrid.setDataProvider(getDataProvider());
		HeaderRow filterRow = tabberGrid.appendHeaderRow();
		HeaderRow operatorRow = tabberGrid.appendHeaderRow();

		/*
		 * Retrieve all Columns from the TabberDataService to generate Columns in the
		 * Grid and corresponding Filter fields.
		 */
		for (Column column : getTabberDataService().getColumns()) {
			Grid.Column<Row, Object> gridColumn = tabberGrid.addColumn(row -> row.getValue(column))
					.setSortProperty(column.getName()).setCaption(column.getName());

			if (column.getType().isLiteral() || column.getType().isNumber() || column.getType().isBinary()
					|| column.getType().isTimeBased()) {

				/*
				 * The fields required for the Filtering and Operator. Add the general Shortcut
				 * Listener to enable updating the filtering on ENTER.
				 */
				TextField filterField = new TextField();
				filterField.addShortcutListener(getUpdateFilterOnEnter());
				ComboBox<TabberOperatorType> operatorField = new ComboBox<TabberOperatorType>();
				operatorField.addShortcutListener(getUpdateFilterOnEnter());

				/*
				 * Bind the filterField and operatorField to the filterBinder into a custom
				 * FilterItemParameter structure.
				 */
				if (!getFilterBinder().getBean().containsKey(column)) {
					getFilterBinder().getBean().put(column, new FilterItemParameter());
					getFilterBinder().getBean().get(column).selectItem = new SelectItem(column);
				}
				getFilterBinder().forField(filterField).bind(map -> map.get(column).operand,
						(map, value) -> map.get(column).operand = value);
				getFilterBinder().forField(operatorField).bind(map -> map.get(column).operator,
						(map, value) -> map.get(column).operator = value);

				/*
				 * Setup of the filterField.
				 */
				filterField.setPlaceholder("Filter");
				filterField.setWidth("100%");
				filterField.addStyleNames(ValoTheme.TEXTFIELD_TINY);
				filterRow.getCell(gridColumn).setComponent(filterField);

				/*
				 * Setup of the operatorField.
				 */
				operatorField.setWidth("100%");
				operatorField.addStyleNames(ValoTheme.COMBOBOX_TINY);
				/*
				 * Set valid OperatorTypes for Literals:
				 */
				if (column.getType().isLiteral()) {
					operatorField.setDataProvider(new ListDataProvider<TabberOperatorType>(
							TabberOperatorType.getListOfLiteralTabberOperatorTypes()));
					operatorField.setValue(TabberOperatorType.LIKE);
				}
				/*
				 * Set valid OperatorTypes for Numbers:
				 */
				else if (column.getType().isNumber()) {
					operatorField.setDataProvider(new ListDataProvider<TabberOperatorType>(
							TabberOperatorType.getListOfNumberTabberOperatorTypes()));
					operatorField.setValue(TabberOperatorType.EQUALS_TO);
				}
				/*
				 * Set valid OperatorTypes for Booleans:
				 */
				else if (column.getType().isBoolean()) {
					operatorField.setDataProvider(new ListDataProvider<TabberOperatorType>(
							TabberOperatorType.getListOfBooleanTabberOperatorTypes()));
					operatorField.setValue(TabberOperatorType.EQUALS_TO);
				}
				/*
				 * Set valid OperatorTypes for TimeBased:
				 */
				else if (column.getType().isTimeBased()) {
					operatorField.setDataProvider(new ListDataProvider<TabberOperatorType>(
							TabberOperatorType.getListOfTimeBasedTabberOperatorTypes()));
					operatorField.setValue(TabberOperatorType.LIKE);
				}

				operatorRow.getCell(gridColumn).setComponent(operatorField);
			}
		}
	}

	/**
	 * The general ShortcutListener to update the filtering.
	 * 
	 * @return the ShortcutListener
	 */
	private ShortcutListener getUpdateFilterOnEnter() {
		if (updateFilterOnEnter == null) {
			updateFilterOnEnter = new ShortcutListener("Update Filter", ShortcutAction.KeyCode.ENTER, null) {
				@Override
				public void handleAction(Object sender, Object target) {
					if (!getDesign().getLiveFilteringMode().getValue()) {
						updateFiltering();
					}
				}
			};
		}
		return updateFilterOnEnter;
	}

	/**
	 * Get the filterBinder. FilterBinder is create if null.
	 * 
	 * @return the FilterBinder
	 */
	private Binder<Map<Column, FilterItemParameter>> getFilterBinder() {
		if (filterBinder == null) {
			/*
			 * Setup a Binder Object to collect data from all filter and operator fields and
			 * configure a ValueChangeListener to update the dataProvider with the current
			 * Filter setup.
			 */
			filterBinder = new Binder<>();
			filterBinder.setBean(new HashMap<>());
		}
		return filterBinder;
	}

	/**
	 * Updates the Filter definition based on the current settings.
	 */
	private void updateFiltering() {
		/*
		 * On any change of any filter, set the Set of FilterItem on the dataProvider.
		 */
		Set<FilterItem> filterItems = new HashSet<>();

		for (FilterItemParameter parameter : getFilterBinder().getBean().values().stream()
				.filter(item -> !(item.operand == null || item.operand.isEmpty()))
				.toArray(FilterItemParameter[]::new)) {
			Object operand;
			
			/*
			 * If the OperatorType is IN or NOT_IN, split the operand on regex "\W*,\W*".
			 */
			if (parameter.operator == TabberOperatorType.IN || parameter.operator == TabberOperatorType.NOT_IN) {
				operand = parameter.operand.split("\\W*,\\W*");
			}
			/*
			 * If the OperatorType is LIKE or NOT_LIKE, check it Wildcards should be added automatically.
			 */
			else if (parameter.operator == TabberOperatorType.LIKE || parameter.operator == TabberOperatorType.NOT_LIKE) {
				if (getDesign().getEnableAutomaticWildcards().getValue()) {
					operand = "%" + parameter.operand + "%";
				} else {
					operand = parameter.operand;
				}
			} else {
				operand = parameter.operand;
			}
			
			filterItems.add(new FilterItem(parameter.selectItem, parameter.operator.getOperatorType(), operand));
		}

		getDataProvider().setFilter(filterItems);
	}

	/**
	 * Get the dataProvider. DataProvider is created if null.
	 * 
	 * @return the DataProvider
	 */
	private ConfigurableFilterDataProvider<Row, Void, Set<FilterItem>> getDataProvider() {
		if (dataProvider == null) {
			dataProvider = tabberDataService.createDataProvider();
		}
		return dataProvider;
	}

	/**
	 * Get the tabberDataService.
	 * 
	 * @return the TabberDataService
	 */
	private TabberDataService getTabberDataService() {
		return tabberDataService;
	}

	private TabberDataExplorerDesign getDesign() {
		if (design == null) {
			design = new TabberDataExplorerDesign();
		}
		return design;
	}

}
