package dk.shx.search.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.suggest.Lookup.LookupResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dk.shx.model.search.AutocompleteQuery;
import dk.shx.model.search.AutocompleteResult;
import dk.shx.model.search.CombinedAutocompleteResult;
import dk.shx.model.search.Hit;
import dk.shx.model.search.NameValuePair;
import dk.shx.model.search.Query;
import dk.shx.model.search.QueryResult;
import dk.shx.model.search.SFMovieFieldEnum;

@Component
public class SearchDaoImpl implements SearchDao {

	protected Logger LOGGER = Logger.getLogger(SearchDaoImpl.class);

    private static final int MAX_RESULTS = 100000;
    @Inject
    private SearchIndexer context;

    public SearchDaoImpl() {
      LOGGER.info("new SearchDaoImpl instance created.");
    }
    
    private void reload() {
      context.reindex();
    }

    //always reload every 6 hours (3600000*6 ms)
    @Scheduled(fixedDelay=2100000)
    private void scheduledReload() {
      LOGGER.info("scheduled index reload");
      reload(); 
    }
    
    @Override
	public CombinedAutocompleteResult autocomplete(AutocompleteQuery q) {
    	CombinedAutocompleteResult ca = new CombinedAutocompleteResult();
    	ca.setQ(q);
    	try {
    	List<String> fls = q.getFls();
    	if (fls == null) {
    		fls = new ArrayList<String>();
    		for (SFMovieFieldEnum fieldenum : SFMovieFieldEnum.sfmoviefields) {
    			fls.add(fieldenum.toString());
    		}
    	}
    	for (String fl : fls) {
    		AutocompleteResult ar = subautocomplete(q.getQ(), fl, q.getRows());
    		ca.getResults().add(new NameValuePair(fl, ar));
    	}
    	} catch (IOException e) {
    		ca.setError(e.getMessage());
    	    LOGGER.error(e.getMessage());
    	}
		return ca;
	}

    private AutocompleteResult subautocomplete(String query, String field, int count) throws IOException {
    	AutocompleteResult ar = new AutocompleteResult();
    	List<LookupResult> results = context.getSuggester(field).lookup(query.toLowerCase(), false, count);
        for (LookupResult result : results) {
            ar.getValues().add(result.key.toString());
        }
    	return ar;
    }
    
	@Override
	public QueryResult query(Query q) {
      QueryResult qr = new QueryResult();
      qr.setQ(q);
	  try {
	      IndexReader reader = DirectoryReader.open(context.getIndexdir());
	      IndexSearcher searcher = new IndexSearcher(reader);
	
	      TopScoreDocCollector collector = TopScoreDocCollector.create(MAX_RESULTS, true);

	      QueryParser qp = new QueryParser();
	      org.apache.lucene.search.Query query = qp.parse(q);
	      
	      searcher.search(query, collector);
	      int totalhits = collector.getTotalHits();
	      qr.setTotalHits(totalhits);
	      
	      if (totalhits > 0) {
	          List<Hit> hits = new ArrayList<Hit>();
	          qr.setHits(hits);

	          TopDocs topDocs = collector.topDocs(q.getStart(), q.getRows());
	          ScoreDoc[] scoredDocs = topDocs.scoreDocs;
	          
	          for (int i = 0; i < scoredDocs.length; i++) {
	        	  int docId = scoredDocs[i].doc;
		          Document d = searcher.doc(docId);
		          Hit hit = buildHit(d, q.getFl());
		          hits.add(hit);
	          }
	      }
	      try {
	          reader.close();
          } catch (Exception e) {
              throw e;
          }
	  } catch (Exception e) {
		  qr.setError(e.getMessage());
	      LOGGER.error(e.getMessage());
	  }
	  return qr;
	}
	
  private Hit buildHit(Document d, List<String> fl) {
    Hit hit = new Hit();
    List<NameValuePair<?>> values = new ArrayList<NameValuePair<?>>();
    hit.setValues(values);
    for (IndexableField f : d.getFields()) {
      if ((fl == null) || (fl.contains(f.name()))) {
        NameValuePair<String> p = new NameValuePair<String>(f.name(), d.get(f.name()));
        values.add(p);
      }
    }
    return hit;
  }
}