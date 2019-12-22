package de.martcre.roxy.tabber;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.ui.CustomComponent;

import de.martcre.roxy.backend.DummyService;
import de.martcre.roxy.backend.TabberDataService;

@SuppressWarnings("serial")
@CDIView(TabberView.VIEW_NAME)
public class TabberView extends CustomComponent implements View {
	
	protected static Logger logger = LogManager.getLogger(TabberView.class);
	
	@Inject
	private TabberDataService tabberDataService;
	
	@Inject
	private DummyService dummyService;

	public static final String VIEW_NAME = "tabber";
	private TabberMainDesign design;
	
	public TabberView() {
		setCompositionRoot(getDesign());
		setSizeFull();
	}
	
	@PostConstruct
	private void initialize() {
		logger.info("tabberDataService " + ((tabberDataService == null) ? "null" : "set"));
		logger.info("dummyService " + ((dummyService == null) ? "null" : "set"));
		
		getDesign().getViewport().addComponent(new TabberDataExplorer(tabberDataService));
	}
	
	private TabberMainDesign getDesign() {
		if (design == null) {
			design = new TabberMainDesign();
		}
		return design;
	}
	
}
