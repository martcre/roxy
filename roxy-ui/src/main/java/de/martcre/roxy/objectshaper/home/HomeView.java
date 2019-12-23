package de.martcre.roxy.objectshaper.home;

import org.jsoup.nodes.Element;

import com.vaadin.cdi.CDIView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.DesignContext;

@CDIView(HomeView.VIEW_NAME)
@SuppressWarnings("serial")
public class HomeView extends CustomComponent implements View {

	public static final String VIEW_NAME = "";

	public HomeView() {
		VerticalLayout layout = new VerticalLayout();
		setCompositionRoot(layout);

		Label lbl = new Label();
		MenuBar menuBar = new MenuBar();
		menuBar.addItem("", VaadinIcons.ACCORDION_MENU, null);
		menuBar.addItem("one").addItem("sub");
		menuBar.addItem("two", new Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				Notification.show("two");
			}
		});
		Element e = new Element("p");
		menuBar.writeDesign(e, new DesignContext());
		lbl.setValue(e.toString());

		layout.addComponent(lbl);
		layout.addComponent(menuBar);
		
		
		CheckBox cb = new CheckBox("Live Filtering");
		cb.setIcon(VaadinIcons.SLIDERS);
		PopupView popupView = new PopupView("Settings", new VerticalLayout(cb));
		popupView.setIcon(VaadinIcons.SLIDERS);
		popupView.setDescription("Settings");
		
		Label l = new Label(VaadinIcons.ABACUS.getHtml());
		l.setDescription("Settings");
		
		VerticalLayout pane = new VerticalLayout(popupView, l);
		
		Element p = new Element("p");
		pane.writeDesign(p, new DesignContext());
		layout.addComponents(pane, new Label(p.toString()));

	}

}
