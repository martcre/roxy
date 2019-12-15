package de.martcre.roxy.objectshaper;

import javax.inject.Inject;
import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

import de.martcre.roxy.objectshaper.home.HomeView;
import de.martcre.roxy.objectshaper.root.ObjectshaperDesign;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("roxy")
@SuppressWarnings("serial")
@CDIUI("")
public class ObjectshaperUI extends UI {
	
	@Inject
	CDIViewProvider viewProvider;
	
	ObjectshaperDesign rootDesign;
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(getRootDesign());
        setNavigator(new Navigator(this, getRootDesign().getViewport()));
        getNavigator().addProvider(viewProvider);
//        getNavigator().navigateTo(HomeView.VIEW_NAME);
    }
    
    private ObjectshaperDesign getRootDesign() {
    	if (rootDesign == null) {
    		rootDesign = new ObjectshaperDesign();
    	}
		return rootDesign;
	}
}
