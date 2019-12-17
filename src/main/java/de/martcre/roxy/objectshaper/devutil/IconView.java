package de.martcre.roxy.objectshaper.devutil;

import java.util.stream.Stream;

import com.vaadin.cdi.CDIView;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@CDIView(IconView.VIEW_NAME)
@SuppressWarnings("serial")
public class IconView extends CustomComponent implements View {
	
	public static final String VIEW_NAME = "icons";
	
	private VerticalLayout mainLayout;
	private Layout iconLayout;
	
	public IconView() {
		setCompositionRoot(getMainLayout());
		setSizeFull();
	}
	
	private VerticalLayout getMainLayout() {
		if (mainLayout == null) {
			mainLayout = new VerticalLayout();
			mainLayout.setSizeFull();
			mainLayout.setMargin(true);
			Label header = new Label("Icon Explorer");
			header.addStyleName(ValoTheme.LABEL_H2);
			mainLayout.addComponent(header);
			
			
			
			TextField query = new TextField();
			query.setWidth("100%");
			query.focus();
			Button filter = new Button("Filter");
			filter.setStyleName(ValoTheme.BUTTON_PRIMARY);
			filter.setClickShortcut(KeyCode.ENTER);
			
			filter.addClickListener(c -> {
				
				renderIcons(query.getValue());
				
			});
			
			HorizontalLayout queryLayout = new HorizontalLayout(query, filter);
			queryLayout.setWidth("100%");
			queryLayout.setExpandRatio(query, 1.0f);
			mainLayout.addComponent(queryLayout);
			
			Panel scrollPanel = new Panel();
			scrollPanel.setSizeFull();
			mainLayout.addComponent(scrollPanel);
			mainLayout.setExpandRatio(scrollPanel, 1.0f);
			
			scrollPanel.setContent(getIconLayout());
			renderIcons("");
		}
		return mainLayout;
	}
	
	private Layout getIconLayout() {
		if (iconLayout == null) {
			iconLayout = new CssLayout();
		}
		return iconLayout;
	}
	
	private void renderIcons(String aFilter) {
		String filter;
		if (aFilter == null || aFilter.isEmpty()) {
			filter = ".*";
		} else {
			filter = ".*" + aFilter + ".*";
		}
		getIconLayout().removeAllComponents();
		for (VaadinIcons icon : Stream.of(VaadinIcons.values())
				.filter(icon -> icon.name().matches(filter)).toArray(VaadinIcons[]::new)) {
			Button button = new Button(icon.name(), icon);
			button.setStyleName(ValoTheme.BUTTON_BORDERLESS);
			button.setDescription("Name: " + icon.name() + " HTML: " + Integer.toHexString(icon.getCodepoint()));
			getIconLayout().addComponent(button);
		}
	}
	

}
