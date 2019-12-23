package de.martcre.roxy.tabber;

import java.util.Arrays;
import java.util.List;

import org.apache.metamodel.query.OperatorType;

/**
 * Helper Structure used in the Frontend to show the possible Operators.
 * 
 * @author martin
 *
 */
public enum TabberOperatorType {
	EQUALS_TO(OperatorType.EQUALS_TO, "EQUALS TO"), DIFFERENT_FROM(OperatorType.DIFFERENT_FROM, "DIFFERENT FROM"),
	LIKE(OperatorType.LIKE, "LIKE"), NOT_LIKE(OperatorType.NOT_LIKE, "NOT LIKE"),
	GREATER_THAN(OperatorType.GREATER_THAN, "GREATER THAN"),
	GREATER_THAN_OR_EQUAL(OperatorType.GREATER_THAN_OR_EQUAL, "GREATER_THAN_OR_EQUAL"),
	LESS_THAN(OperatorType.LESS_THAN, "LESS THAN"),
	LESS_THAN_OR_EQUAL(OperatorType.LESS_THAN_OR_EQUAL, "LESS THAN OR EQUAL"), IN(OperatorType.IN, "IN"),
	NOT_IN(OperatorType.NOT_IN, "NOT_IN");

	private final OperatorType operatorType;
	private final String caption;

	TabberOperatorType(OperatorType operatorType, String caption) {
		this.operatorType = operatorType;
		this.caption = caption;
	}

	@Override
	public String toString() {
		return this.caption;
	}

	/**
	 * Get the underlying MetaModel OperatorType.
	 * 
	 * @return the OperatorType
	 */
	public OperatorType getOperatorType() {
		return this.operatorType;
	}
	
	/**
	 * Get a list of TabberOperatorTypes valid for Literals.
	 * 
	 * @return the list
	 */
	public static List<TabberOperatorType> getListOfLiteralTabberOperatorTypes() {
		return Arrays.asList(TabberOperatorType.LIKE, TabberOperatorType.NOT_LIKE, TabberOperatorType.EQUALS_TO,
				TabberOperatorType.DIFFERENT_FROM, TabberOperatorType.IN, TabberOperatorType.NOT_IN);
	}

	/**
	 * Get a list of TabberOperatorTypes valid for Numbers.
	 * 
	 * @return the list
	 */
	public static List<TabberOperatorType> getListOfNumberTabberOperatorTypes() {
		return Arrays.asList(TabberOperatorType.EQUALS_TO, TabberOperatorType.DIFFERENT_FROM,
				TabberOperatorType.LESS_THAN, TabberOperatorType.GREATER_THAN, TabberOperatorType.LESS_THAN_OR_EQUAL,
				TabberOperatorType.GREATER_THAN_OR_EQUAL, TabberOperatorType.IN, TabberOperatorType.NOT_IN);
	}

	/**
	 * Get a list of TabberOperatorTypes valid for Boolean.
	 * 
	 * @return the list
	 */
	public static List<TabberOperatorType> getListOfBooleanTabberOperatorTypes() {
		return Arrays.asList(TabberOperatorType.EQUALS_TO, TabberOperatorType.DIFFERENT_FROM);
	}

	/**
	 * Get a list of TabberOperatorTypes valid for TimeBased.
	 * 
	 * @return the list
	 */
	public static List<TabberOperatorType> getListOfTimeBasedTabberOperatorTypes() {
		return Arrays.asList(TabberOperatorType.LIKE, TabberOperatorType.NOT_LIKE, TabberOperatorType.EQUALS_TO,
				TabberOperatorType.DIFFERENT_FROM, TabberOperatorType.LESS_THAN, TabberOperatorType.GREATER_THAN,
				TabberOperatorType.LESS_THAN_OR_EQUAL, TabberOperatorType.GREATER_THAN_OR_EQUAL, TabberOperatorType.IN,
				TabberOperatorType.NOT_IN);
	}
}