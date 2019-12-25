package de.martcre.roxy.desktop;

import com.vaadin.ui.CustomComponent;

@SuppressWarnings("serial")
public class WrapperManager extends CustomComponent {

	private WrapperManagerDesign design;
	
	public WrapperManager() {
		setCompositionRoot(getDesign());
		setWidth("100%");
		setHeightUndefined();
	}
	
	public WrapperManagerDesign getDesign() {
		if (design == null) {
			design = new WrapperManagerDesign();
		}
		return design;
	}
	
}
