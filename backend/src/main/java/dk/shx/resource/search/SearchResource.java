package dk.shx.resource.search;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import dk.shx.model.search.AutocompleteQuery;
import dk.shx.model.search.CombinedAutocompleteResult;
import dk.shx.model.search.Query;
import dk.shx.model.search.QueryResult;
import dk.shx.service.search.SearchService;

@Service
@Path("/search")
public class SearchResource {

	@Inject SearchService searchService;
	
	@POST
	@Path("/autocomplete")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	public CombinedAutocompleteResult autocomplete(AutocompleteQuery q) {
		return searchService.autocomplete(q);
	}
	
	@POST
	@Path("/query")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	public QueryResult query(Query q) {
		return searchService.query(q);
	}
}