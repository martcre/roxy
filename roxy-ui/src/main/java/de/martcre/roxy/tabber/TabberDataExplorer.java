package de.martcre.roxy.tabber;

import com.vaadin.ui.CustomComponent;

@SuppressWarnings("serial")
public class TabberDataExplorer extends CustomComponent {
	
	private TabberDataExplorerDesign design;
	
	public TabberDataExplorer() {
		setCompositionRoot(getDesign());
		setSizeFull();
		
		
	}
	
	private TabberDataExplorerDesign getDesign() {
		if (design == null) {
			design = new TabberDataExplorerDesign();
		}
		return design;
	}
}
