package de.martcre.roxy.tabber;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.query.FilterItem;
import org.apache.metamodel.query.SelectItem;
import org.apache.metamodel.schema.Column;

import com.vaadin.data.Binder;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

import de.martcre.roxy.backend.tabber.TabberDataService;
import de.martcre.roxy.desktop.Wrappable;

/**
 * TabberDataExplorer is a generic component to show Data in a Grid. It provides
 * Filtering and Sorting.
 * 
 * @author martin
 *
 */
@SuppressWarnings("serial")
public class TabberDataExplorer extends CustomComponent implements Wrappable {

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
				 * The generic Filter field and Operator field, which are set into the layout.
				 */
				AbstractComponent genericFilterField;
				AbstractComponent genericOperatorField;

				/*
				 * Initialize the map entry for the column:
				 */
				if (!getFilterBinder().getBean().containsKey(column)) {
					getFilterBinder().getBean().put(column, new FilterItemParameter());
					getFilterBinder().getBean().get(column).selectItem = new SelectItem(column);
				}

				/*
				 * Set filter for Literals:
				 */
				if (column.getType().isLiteral()) {
					/*
					 * Create specific fields:
					 */
					TextField filterField = new TextField();
					ComboBox<TabberOperatorType> operatorField = new ComboBox<>();

					/*
					 * Bind with validation, if required:
					 */
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

					/*
					 * Setup of the operatorField.
					 */
					operatorField.setWidth("100%");
					operatorField.addStyleNames(ValoTheme.COMBOBOX_TINY);

					operatorField.setDataProvider(
							new ListDataProvider<>(TabberOperatorType.getListOfLiteralTabberOperatorTypes()));
					operatorField.setValue(TabberOperatorType.LIKE);

					/*
					 * Pass the literal specific fields to the generic fields:
					 */
					genericFilterField = filterField;
					genericOperatorField = operatorField;
				}
				/*
				 * Set filter for Numbers:
				 */
				else if (column.getType().isNumber()) {
					/*
					 * Create specific fields:
					 */
					TextField filterField = new TextField();
					ComboBox<TabberOperatorType> operatorField = new ComboBox<>();

					/*
					 * Bind with validation, if required:
					 */
					getFilterBinder().forField(filterField)
							.withValidator(new RegexpValidator("Only Numbers allowed.", "[\\d|\\.]*"))
							.bind(map -> map.get(column).operand, (map, value) -> map.get(column).operand = value);
					getFilterBinder().forField(operatorField).bind(map -> map.get(column).operator,
							(map, value) -> map.get(column).operator = value);

					/*
					 * Setup of the filterField.
					 */
					filterField.setPlaceholder("Filter");
					filterField.setWidth("100%");
					filterField.addStyleNames(ValoTheme.TEXTFIELD_TINY);

					/*
					 * Setup of the operatorField.
					 */
					operatorField.setWidth("100%");
					operatorField.addStyleNames(ValoTheme.COMBOBOX_TINY);

					operatorField.setDataProvider(
							new ListDataProvider<>(TabberOperatorType.getListOfNumberTabberOperatorTypes()));
					operatorField.setValue(TabberOperatorType.EQUALS_TO);

					/*
					 * Pass the literal specific fields to the generic fields:
					 */
					genericFilterField = filterField;
					genericOperatorField = operatorField;
				}
				/*
				 * Set filter for Booleans:
				 */
				else if (column.getType().isBoolean()) {
					/*
					 * Create specific fields:
					 */
					ComboBox<String> filterField = new ComboBox<>();
					ComboBox<TabberOperatorType> operatorField = new ComboBox<>();

					/*
					 * Bind with validation, if required:
					 */
					getFilterBinder().forField(filterField).bind(map -> map.get(column).operand,
							(map, value) -> map.get(column).operand = value);
					getFilterBinder().forField(operatorField).bind(map -> map.get(column).operator,
							(map, value) -> map.get(column).operator = value);

					/*
					 * Setup of the filterField.
					 */
					filterField.setPlaceholder("Filter");
					filterField.setWidth("100%");
					filterField.addStyleNames(ValoTheme.COMBOBOX_TINY);
					filterField.setDataProvider(new ListDataProvider<>(Arrays.asList("", "true", "false")));
					filterField.setValue("");

					/*
					 * Setup of the operatorField.
					 */
					operatorField.setWidth("100%");
					operatorField.addStyleNames(ValoTheme.COMBOBOX_TINY);

					operatorField.setDataProvider(
							new ListDataProvider<>(TabberOperatorType.getListOfBooleanTabberOperatorTypes()));
					operatorField.setValue(TabberOperatorType.EQUALS_TO);

					/*
					 * Pass the literal specific fields to the generic fields:
					 */
					genericFilterField = filterField;
					genericOperatorField = operatorField;
				}
				/*
				 * Set filter for TimeBased:
				 */
				else if (column.getType().isTimeBased()) {
					/*
					 * Create specific fields:
					 */
					DateTimeField filterField = new DateTimeField();
					filterField.setResolution(DateTimeResolution.SECOND);

					ComboBox<TabberOperatorType> operatorField = new ComboBox<>();

					/*
					 * Bind with validation, if required:
					 */
					getFilterBinder().forField(filterField)
							.bind(map -> ((map.get(column).operand != null)
									? LocalDateTime.parse(map.get(column).operand)
									: null), (map, value) -> map.get(column).operand = value.toString());
					getFilterBinder().forField(operatorField).bind(map -> map.get(column).operator,
							(map, value) -> map.get(column).operator = value);

					/*
					 * Setup of the filterField.
					 */
					filterField.setPlaceholder("Filter");
					filterField.setWidth("100%");
					filterField.addStyleNames(ValoTheme.DATEFIELD_TINY);

					/*
					 * Setup of the operatorField.
					 */
					operatorField.setWidth("100%");
					operatorField.addStyleNames(ValoTheme.COMBOBOX_TINY);

					operatorField.setDataProvider(
							new ListDataProvider<>(TabberOperatorType.getListOfTimeBasedTabberOperatorTypes()));
					operatorField.setValue(TabberOperatorType.EQUALS_TO);

					/*
					 * Pass the literal specific fields to the generic fields:
					 */
					genericFilterField = filterField;
					genericOperatorField = operatorField;
				}
				/*
				 * This section should never get invoked as only one of the above sections
				 * should be invoked due to the outer if section.
				 */
				else {
					genericFilterField = new TextField();
					genericOperatorField = new ComboBox<>();
				}

				/*
				 * Add the Update Filter on Enter Listener to all fields:
				 */
				genericFilterField.addShortcutListener(getUpdateFilterOnEnter());
				genericOperatorField.addShortcutListener(getUpdateFilterOnEnter());

				/*
				 * Add the fields to the layout:
				 */
				filterRow.getCell(gridColumn).setComponent(genericFilterField);
				operatorRow.getCell(gridColumn).setComponent(genericOperatorField);
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
			 * If the OperatorType is LIKE or NOT_LIKE, check it Wildcards should be added
			 * automatically.
			 */
			else if (parameter.operator == TabberOperatorType.LIKE
					|| parameter.operator == TabberOperatorType.NOT_LIKE) {
				if (getDesign().getEnableAutomaticWildcards().getValue()) {
					operand = "%" + parameter.operand + "%";
				} else {
					operand = parameter.operand;
				}
			}
			/*
			 * In case of TimeBased Column type, convert the ISO Encoded String to
			 * java.util.Date.
			 */
			else if (parameter.selectItem.getColumn().getType().isTimeBased()) {
				if (parameter.operand != null) {
					operand = Date
							.from(LocalDateTime.parse(parameter.operand).atZone(ZoneId.systemDefault()).toInstant());
				} else {
					operand = null;
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
			CallbackDataProvider<Row, Set<FilterItem>> callbackDataProvider = new CallbackDataProvider<Row, Set<FilterItem>>(
					new CallbackDataProvider.FetchCallback<Row, Set<FilterItem>>() {

						@Override
						public Stream<Row> fetch(Query<Row, Set<FilterItem>> query) {
							return getTabberDataService().fetch(query);
						}
					}, new CallbackDataProvider.CountCallback<Row, Set<FilterItem>>() {

						@Override
						public int count(Query<Row, Set<FilterItem>> query) {
							return getTabberDataService().count(query);
						}
					});

			dataProvider = callbackDataProvider.withConfigurableFilter();
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

	@Override
	public String getWrapperCaption() {
		return "Tabber Data Explorer";
	}

}
