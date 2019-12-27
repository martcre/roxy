package de.martcre.roxy.desktop;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class UndockedWrapper extends Window {

	private UndockedWrapperDesign design;

	private Wrappable subject;
	private ComponentContainer parentComponentContainer;
	private WrapperManager wrapperManager;

	
	
	public UndockedWrapper(Wrappable subject, ComponentContainer parentComponentContainer, WrapperManager wrapperManager) {
		this(subject, parentComponentContainer);
		this.wrapperManager = wrapperManager;
		if (this.wrapperManager != null) {
			wrapperManager.registerWrapper(this);
		}
	}
	
	
	public UndockedWrapper(Wrappable subject, ComponentContainer parentComponentContainer) {

		this.subject = subject;
		this.parentComponentContainer = parentComponentContainer;

		setContent(getDesign());
		getDesign().setSizeFull();
		setHeight("60%");
		setWidth("60%");
		center();
		setCaption(((subject.getWrapperCaption() == null || subject.getWrapperCaption().isEmpty() ? "Undefined Wrapper Caption"
				: subject.getWrapperCaption())));

		getDesign().replaceComponent(getDesign().getPlaceholder(), this.subject);
		getDesign().setExpandRatio(subject, 1.0f);

		this.addCloseListener(c -> {
			dock();
		});
	}

	private UndockedWrapperDesign getDesign() {
		if (design == null) {
			design = new UndockedWrapperDesign();
		}
		return design;
	}

	public void dock() {
		parentComponentContainer.addComponent(new DockedWrapper(this.subject, this.parentComponentContainer, this.wrapperManager));
		UI.getCurrent().removeWindow(this);
	}
	
	public Wrappable getSubject() {
		return subject;
	}

}
