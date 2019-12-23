package de.martcre.roxy.backend;

import java.util.List;
import java.util.Set;

import org.apache.metamodel.data.Row;
import org.apache.metamodel.query.FilterItem;
import org.apache.metamodel.schema.Column;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;

public interface TabberDataService {
	
	public ConfigurableFilterDataProvider<Row, Void, Set<FilterItem>> createDataProvider();
	
	public List<Column> getColumns();
	
}
