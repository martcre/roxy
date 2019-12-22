package de.martcre.roxy.tabber;

import com.vaadin.ui.CustomComponent;

import de.martcre.roxy.backend.TabberDataService;

@SuppressWarnings("serial")
public class TabberDataExplorer extends CustomComponent {
	
	private TabberDataExplorerDesign design;
	
	private TabberDataService tabberDataService;
	
	
	public TabberDataExplorer(TabberDataService tabberDataService) {
		this.tabberDataService = tabberDataService;
		setCompositionRoot(getDesign());
		setSizeFull();
		
		
		
		getDesign().getGrid().setDataProvider(tabberDataService.getDataProvider());
		tabberDataService.getColumns().stream().forEach(c -> getDesign().getGrid().addColumn(row -> row.getValue(c)).setCaption(c.getName()));
		
		
	}
	
	private TabberDataExplorerDesign getDesign() {
		if (design == null) {
			design = new TabberDataExplorerDesign();
		}
		return design;
	}
	
	
	
}
