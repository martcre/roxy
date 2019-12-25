package de.martcre.roxy.desktop;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class DockedWrapper extends Panel {

	private DockedWrapperDesign design;

	private Wrappable subject;
	private ComponentContainer parentComponentContainer;

	public DockedWrapper(Wrappable subject, ComponentContainer parentComponentContainer) {
		setContent(getDesign());
		getDesign().setSizeFull();
		setSizeFull();

		this.subject = subject;
		this.parentComponentContainer = parentComponentContainer;

		getDesign().getPanelCaption()
				.setValue(((subject.getWrapperCaption() == null || subject.getWrapperCaption().isEmpty() ? "Undefined Wrapper Caption"
						: subject.getWrapperCaption())));
		getDesign().replaceComponent(getDesign().getPlaceholder(), subject);
		getDesign().setExpandRatio(subject, 1.0f);

		getDesign().getUndockButton().addClickListener(c -> {
			if (getParent() instanceof ComponentContainer) {
				UI.getCurrent().addWindow(new UndockedWrapper(this.subject, this.parentComponentContainer));
				this.parentComponentContainer.removeComponent(this);
			}
		});
	}

	public DockedWrapperDesign getDesign() {
		if (design == null) {
			design = new DockedWrapperDesign();
		}
		return design;
	}

}
