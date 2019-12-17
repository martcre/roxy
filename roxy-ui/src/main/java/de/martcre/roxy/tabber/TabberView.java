package de.martcre.roxy.tabber;

import javax.inject.Inject;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;

import de.martcre.roxy.backend.DummyService;

@SuppressWarnings("serial")
@CDIView(TabberView.VIEW_NAME)
public class TabberView extends CustomComponent implements View {
	
	@Inject
	private DummyService dummyService;

	public static final String VIEW_NAME = "tabber";
	private TabberMainDesign design;
	
	public TabberView() {
		setCompositionRoot(getDesign());
		setSizeFull();
		
		getDesign().getViewport().addComponent(new Button("Test Hello World", c-> {
			
			String rtn = dummyService.helloWorld("Tabber");
			Notification.show(rtn);
			
		}));
		
		getDesign().getViewport().addComponent(new TabberDataExplorer());
		
		
	}
	
	private TabberMainDesign getDesign() {
		if (design == null) {
			design = new TabberMainDesign();
		}
		return design;
	}
	
}
