package de.martcre.roxy.objectshaper.root;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@SuppressWarnings("serial")
@DesignRoot
public class ObjectshaperDesign extends VerticalLayout {

	private VerticalLayout viewport;
	private MenuBar menu;
	private MenuBar infobar;
	private VerticalLayout sidebar;
	private MenuItem sidebarToggle;

	public ObjectshaperDesign() {
		Design.read(this);

		sidebarToggle = infobar.addItem("", VaadinIcons.TASKS, c -> {
			toggleSidebar();
		});

		// Init Sidebar collapsed
		collapseSidebar();

	}

	private void toggleSidebar() {
		if (sidebar.isVisible()) {
			collapseSidebar();
		} else {
			expandSidebar();
		}
	}

	private void expandSidebar() {
		sidebar.setVisible(true);
		sidebarToggle.setDescription("Collapse Sidebar");
	}

	private void collapseSidebar() {
		sidebar.setVisible(false);
		sidebarToggle.setDescription("Expand Sidebar");
	}

	/**
	 * Get the main Viewport of the layout, e. g. to let it be populated by the
	 * Navigator.
	 * 
	 * @return the viewport
	 */
	public VerticalLayout getViewport() {
		return viewport;
	}
	
	/**
	 * Get the main menu bar, e. g. to add menu items.
	 * 
	 * @return the main menu bar
	 */
	public MenuBar getMenu() {
		return menu;
	}
	
	/**
	 * Get the info bar.
	 * 
	 * @return the info bar
	 */
	public MenuBar getInfobar() {
		return infobar;
	}
	
	/**
	 * Get the side bar, which is collapsable.
	 * 
	 * @return the side bar
	 */
	public VerticalLayout getSidebar() {
		return sidebar;
	}

}
