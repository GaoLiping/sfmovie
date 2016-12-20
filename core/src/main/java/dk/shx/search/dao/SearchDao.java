package dk.shx.search.dao;

import dk.shx.model.search.AutocompleteQuery;
import dk.shx.model.search.CombinedAutocompleteResult;
import dk.shx.model.search.Query;
import dk.shx.model.search.QueryResult;

public interface SearchDao {

	CombinedAutocompleteResult autocomplete(AutocompleteQuery q);
	
	QueryResult query(Query q);
}