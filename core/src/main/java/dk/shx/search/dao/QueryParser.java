package dk.shx.search.dao;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import dk.shx.model.search.NameValuePair;
import dk.shx.model.search.Query;

public class QueryParser {

	public org.apache.lucene.search.Query parse(Query q) {
		BooleanQuery bq = new BooleanQuery();
		for (NameValuePair<String> pair : q.getFq()) {
			TermQuery tq = new TermQuery(new Term(pair.getName(), pair.getValue().toLowerCase()));
			bq.add(tq, Occur.SHOULD);
		}
		return bq;
	}
}