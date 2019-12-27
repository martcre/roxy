package de.martcre.roxy.desktop;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class DockedWrapper extends Panel {

	private DockedWrapperDesign design;

	private Wrappable subject;
	private ComponentContainer parentComponentContainer;
	private WrapperManager wrapperManager;

	public DockedWrapper(Wrappable subject, ComponentContainer parentComponentContainer,
			WrapperManager wrapperManager) {
		this(subject, parentComponentContainer);
		this.wrapperManager = wrapperManager;
		if (this.wrapperManager != null) {
			this.wrapperManager.registerWrapper(this);
		}
	}

	public DockedWrapper(Wrappable subject, ComponentContainer parentComponentContainer) {
		setContent(getDesign());
		getDesign().setSizeFull();
		setSizeFull();

		this.subject = subject;
		this.parentComponentContainer = parentComponentContainer;

		getDesign().getPanelCaption()
				.setValue(((subject.getWrapperCaption() == null || subject.getWrapperCaption().isEmpty()
						? "Undefined Wrapper Caption"
						: subject.getWrapperCaption())));
		getDesign().replaceComponent(getDesign().getPlaceholder(), subject);
		getDesign().setExpandRatio(subject, 1.0f);

		getDesign().getUndockButton().addClickListener(c -> {
			undock();
		});
	}

	public void undock() {
		if (getParent() instanceof ComponentContainer) {
			UI.getCurrent().addWindow(new UndockedWrapper(this.subject, this.parentComponentContainer, this.wrapperManager));
			this.parentComponentContainer.removeComponent(this);
		}
	}

	private DockedWrapperDesign getDesign() {
		if (design == null) {
			design = new DockedWrapperDesign();
		}
		return design;
	}
	
	public Wrappable getSubject() {
		return subject;
	}

}
