package de.martcre.roxy.desktop;

import com.vaadin.ui.Component;

/**
 * A component which is wrappable by the Roxy Desktop.
 * 
 * @author martin
 *
 */
public interface Wrappable extends Component {

	/**
	 * Get the caption shown in the wrapper.
	 * 
	 * @return the caption
	 */
	public String getWrapperCaption();
}
