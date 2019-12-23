package de.martcre.roxy.tabber;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.ui.CustomComponent;

import de.martcre.roxy.backend.tabber.TabberDataService;

@SuppressWarnings("serial")
@CDIView(TabberView.VIEW_NAME)
public class TabberView extends CustomComponent implements View {

	protected static Logger logger = LogManager.getLogger(TabberView.class);

	@Inject
	private TabberDataService tabberDataService;

	public static final String VIEW_NAME = "tabber";
	private TabberMainDesign design;

	/**
	 * Constructs a new TabberView.
	 */
	public TabberView() {
		setCompositionRoot(getDesign());
		setSizeFull();
	}

	/**
	 * Initializes the View after it was created by CDI. At this point of time,
	 * Dependencies were injected and can be used in the application.
	 */
	@PostConstruct
	private void initialize() {
		getDesign().getViewport().addComponent(new TabberDataExplorer(tabberDataService));
	}

	private TabberMainDesign getDesign() {
		if (design == null) {
			design = new TabberMainDesign();
		}
		return design;
	}

}
