package de.martcre.roxy.tabber;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.ui.CustomComponent;

@SuppressWarnings("serial")
@CDIView(TabberView.VIEW_NAME)
public class TabberView extends CustomComponent implements View {

	public static final String VIEW_NAME = "tabber";
	private TabberMainDesign design;
	
	public TabberView() {
		setCompositionRoot(getDesign());
		setSizeFull();
		
		getDesign().getViewport().addComponent(new TabberDataExplorer());
	}
	
	private TabberMainDesign getDesign() {
		if (design == null) {
			design = new TabberMainDesign();
		}
		return design;
	}
	
}
