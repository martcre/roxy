package de.martcre.roxy.desktop;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;

@SuppressWarnings("serial")
public class WrapperManager extends CustomComponent {

	private WrapperManagerDesign design;
	private ComponentContainer viewport;
	private Map<Wrappable, WrapperMapping> wrapperMappings = new HashMap<>();

	private class WrapperMapping {
		DockedWrapper dockedWrapper;
		UndockedWrapper undockedWrapper;
		Wrappable subject;
	}
	
	public WrapperManager() {
		setCompositionRoot(getDesign());
		setWidth("100%");
		setHeightUndefined();
	}
	
	public void setViewport(ComponentContainer viewport) {
		this.viewport = viewport;
	}

	public WrapperManagerDesign getDesign() {
		if (design == null) {
			design = new WrapperManagerDesign();
		}
		return design;
	}

	private void addLink(WrapperMapping wrapperMapping) {
		getDesign().addComponent(new Button(((wrapperMapping.subject.getWrapperCaption() == null
				|| wrapperMapping.subject.getWrapperCaption().isEmpty() ? "Undefined Wrapper Caption"
						: wrapperMapping.subject.getWrapperCaption())),
				c -> {
					putToForeground(wrapperMapping);
				}));
	}

	private void putToForeground(WrapperMapping wrapperMapping) {
		if (wrapperMapping.dockedWrapper != null && viewport != null) {
			viewport.removeAllComponents();
			viewport.addComponent(wrapperMapping.dockedWrapper);
		} else if (wrapperMapping.undockedWrapper != null){
			wrapperMapping.undockedWrapper.focus();
		} else {
			throw new RuntimeException("viewport has to be set before usage.");
		}
	}

	public void registerWrapper(DockedWrapper dockedWrapper) {
		/*
		 * Switch to decide if a new Link should be added after initialization/update of
		 * WrapperMapping.
		 */
		boolean addLink = false;

		if (!wrapperMappings.containsKey(dockedWrapper.getSubject())) {
			wrapperMappings.put(dockedWrapper.getSubject(), new WrapperMapping());
			wrapperMappings.get(dockedWrapper.getSubject()).subject = dockedWrapper.getSubject();
			addLink = true;
		}

		wrapperMappings.get(dockedWrapper.getSubject()).dockedWrapper = dockedWrapper;
		wrapperMappings.get(dockedWrapper.getSubject()).undockedWrapper = null;

		if (addLink) {
			addLink(wrapperMappings.get(dockedWrapper.getSubject()));
		}
	}

	public void registerWrapper(UndockedWrapper undockedWrapper) {
		/*
		 * Switch to decide if a new Link should be added after initialization/update of
		 * WrapperMapping.
		 */
		boolean addLink = false;

		if (!wrapperMappings.containsKey(undockedWrapper.getSubject())) {
			wrapperMappings.put(undockedWrapper.getSubject(), new WrapperMapping());
			wrapperMappings.get(undockedWrapper.getSubject()).subject = undockedWrapper.getSubject();
			addLink = true;
		}

		wrapperMappings.get(undockedWrapper.getSubject()).undockedWrapper = undockedWrapper;
		wrapperMappings.get(undockedWrapper.getSubject()).dockedWrapper = null;

		if (addLink) {
			addLink(wrapperMappings.get(undockedWrapper.getSubject()));
		}
	}

}
