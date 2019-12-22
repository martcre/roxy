package de.martcre.roxy.tabber;

import org.apache.metamodel.data.Row;
import org.apache.metamodel.schema.Column;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.HeaderRow;

import de.martcre.roxy.backend.TabberDataService;

@SuppressWarnings("serial")
public class TabberDataExplorer extends CustomComponent {

	private TabberDataExplorerDesign design;

	public TabberDataExplorer(TabberDataService tabberDataService) {
		setCompositionRoot(getDesign());
		setSizeFull();

		/*
		 * Set a DataProvider provided by the TabberDataService into the Grid to
		 * retrieve the Data and generate a Header Row to host the Filter fields.
		 */
		Grid<Row> tabberGrid = getDesign().getGrid();
		ConfigurableFilterDataProvider<Row, Void, ?> dataProvider = tabberDataService.getDataProvider()
				.withConfigurableFilter();
		tabberGrid.setDataProvider(dataProvider);
		HeaderRow filterRow = tabberGrid.appendHeaderRow();

		/*
		 * Retrieve all Columns from the TabberDataService to generate Columns in the
		 * Grid and corresponding Filter fields.
		 */
		for (Column column : tabberDataService.getColumns()) {
			Grid.Column<Row, Object> gridColumn = tabberGrid.addColumn(row -> row.getValue(column))
					.setSortProperty(column.getName()).setCaption(column.getName());

			TextField filterField = new TextField();
			filterField.setPlaceholder("Filter");
			filterField.setWidth("100%");
			filterRow.getCell(gridColumn).setComponent(filterField);
			
//			dataProvider.setFilter(filter);

		}

	}

	private TabberDataExplorerDesign getDesign() {
		if (design == null) {
			design = new TabberDataExplorerDesign();
		}
		return design;
	}

}
