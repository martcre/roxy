package de.martcre.roxy.backend;

import java.util.List;

import org.apache.metamodel.data.Row;
import org.apache.metamodel.schema.Column;

import com.vaadin.data.provider.BackEndDataProvider;

public interface TabberDataService {
	
	public BackEndDataProvider<Row, ?> getDataProvider();
	
	public List<Column> getColumns();
	
}
