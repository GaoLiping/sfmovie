package dk.shx.service.search;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import dk.shx.model.search.AutocompleteQuery;
import dk.shx.model.search.CombinedAutocompleteResult;
import dk.shx.model.search.Query;
import dk.shx.model.search.QueryResult;
import dk.shx.search.dao.SearchDao;

@Service
public class SearchServiceImpl implements SearchService {

	@Inject
	private SearchDao searchDao;
	
	@Override
	public CombinedAutocompleteResult autocomplete(AutocompleteQuery q) {
		return searchDao.autocomplete(q);
	}
	
	@Override
	public QueryResult query(Query q) {
		return searchDao.query(q);
	}
}