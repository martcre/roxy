package de.martcre.roxy.backend.tabber;

import java.util.List;
import java.util.Set;

import org.apache.metamodel.data.Row;
import org.apache.metamodel.query.FilterItem;
import org.apache.metamodel.schema.Column;

import com.vaadin.data.provider.CallbackDataProvider;

public interface TabberDataService extends CallbackDataProvider.FetchCallback<Row, Set<FilterItem>>,
		CallbackDataProvider.CountCallback<Row, Set<FilterItem>> {

	public List<Column> getColumns();

}
