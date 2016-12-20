package dk.shx.search.dao;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.HighFrequencyDictionary;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.tst.TSTLookup;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import dk.shx.model.search.SFMovieDocument;
import dk.shx.model.search.SFMovieFieldEnum;

@Component
@Scope("prototype")
public class SearchIndexer {
  protected Logger LOGGER = Logger.getLogger(SearchIndexer.class);

  @Inject String sfmoviejsonurl;
  
  public static final Version LUCENE_VERSION = Version.LUCENE_48;
  public static final Analyzer analyzer = new StandardAnalyzer(LUCENE_VERSION);
  private Directory indexdir = null;
  private Map<String, Lookup> suggesterMap = new HashMap<String, Lookup>();
  
  public SearchIndexer() {
    LOGGER.debug("DKSearchIndexer instance created");
  }

  public SearchIndexer(Directory indexdir, Map<String, Lookup> suggesterMap) {
    this.indexdir = indexdir;
    this.suggesterMap = suggesterMap;
  }

  public Directory getIndexdir() {
    return indexdir;
  }

  public void setIndexdir(Directory indexdir) {
    this.indexdir = indexdir;
  }
  
  public Map<String, Lookup> getSuggesterMap() {
	return suggesterMap;
  }
	
  public void setSuggesterMap(Map<String, Lookup> suggesterMap) {
	this.suggesterMap = suggesterMap;
  }
	
  public Lookup getSuggester(String name) {
    return suggesterMap.get(name);
  }

  public void setSuggester(String name, Lookup suggester) {
    this.suggesterMap.put(name, suggester);
  }
  
  public void reindex() {
    if (indexdir != null) {
      try {
        indexdir.close();
        LOGGER.info("Closing indexdir");
      } catch (IOException e) {
        LOGGER.error("Error closing index directory.");
        LOGGER.error(e.getMessage());
      }
    }
    /*
    try {
      indexdir = FSDirectory.open(new File("D:\\dev\\workspace\\sfmovie\\search\\idx"));
    } catch (Exception e) {
      LOGGER.error("open FSDirectory error");
      LOGGER.error(e.getMessage());
    }*/
    indexdir = new RAMDirectory();
    try {
    	List<Document> documents = loadMovies();
        index(documents);	
    } catch (Exception e) {
    	LOGGER.error("build movie directory error");
        LOGGER.error(e.getMessage());
    }
    
    try {
      for (SFMovieFieldEnum field : SFMovieFieldEnum.sfmoviefields) {
    	  buildSuggestionDictionary(field.toString());  
      }
    } catch (IOException e) {
      LOGGER.error("build suggestion directory error");
      LOGGER.error(e.getMessage());
    }
  }
  private List<Document> loadMovies() throws MalformedURLException, IOException {
    List<Document> documents = new ArrayList<Document>();
    try {
    	URL url = new URL(sfmoviejsonurl);
    	LOGGER.warn("loadMovies, sfmovie url = " + sfmoviejsonurl);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
          ObjectMapper mapper = new ObjectMapper();
          List<SFMovieDocument> sfMovieDocumentList = mapper.readValue(con.getInputStream(), new TypeReference<List<SFMovieDocument>>(){});
          con.getInputStream().close();
          for (SFMovieDocument sfMovieDocument : sfMovieDocumentList) {
        	  Document d = convertToDocument(sfMovieDocument);
        	  documents.add(d);
          }
        }
    } catch (MalformedURLException me) {
    	LOGGER.error("loadMovies MalformedURLException", me);
        throw me;
      } catch (IOException ioe) {
    	  LOGGER.error("loadMovies IOException", ioe);
        throw ioe;
      }
    return documents;
  }
  /*
  private List<Document> loadMovies() throws Exception {
	  List<Document> documents = new ArrayList<Document>();
	  FileInputStream fis = new FileInputStream("D:\\dev\\workspace\\sfmovie\\backend\\wwmu-gmzc.json");
      ObjectMapper mapper = new ObjectMapper();
      List<SFMovieDocument> sfMovieDocumentList = mapper.readValue(fis, new TypeReference<List<SFMovieDocument>>(){});
      fis.close();
      for (SFMovieDocument sfMovieDocument : sfMovieDocumentList) {
  	    Document d = convertToDocument(sfMovieDocument);
  	    documents.add(d);
      }
	  return documents;
  }*/
  
  private Document convertToDocument(SFMovieDocument sfMovieDocument) {
	  Document d = new Document();
	  FieldType type = new FieldType();
	  type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
	  type.setStored(true);
	  type.setIndexed(true);
	  type.setTokenized(false);
	  d.add(new Field(SFMovieFieldEnum.TITLE.toString(), lowercase(sfMovieDocument.getTitle()), type));
	  d.add(new Field(SFMovieFieldEnum.RELEASEYEAR.toString(), lowercase(sfMovieDocument.getRelease_year()), type));
	  d.add(new Field(SFMovieFieldEnum.LOCATIONS.toString(), lowercase(sfMovieDocument.getLocations()), type));
	  d.add(new Field(SFMovieFieldEnum.FUNFACTS.toString(), lowercase(sfMovieDocument.getFunfacts()), type));
	  d.add(new Field(SFMovieFieldEnum.PRODUCTION_COMPANY.toString(), lowercase(sfMovieDocument.getProduction_company()), type));
	  d.add(new Field(SFMovieFieldEnum.DISTRIBUTOR.toString(), lowercase(sfMovieDocument.getDistributor()), type));
	  d.add(new Field(SFMovieFieldEnum.DIRECTOR.toString(), lowercase(sfMovieDocument.getDirector()), type));
	  d.add(new Field(SFMovieFieldEnum.WRITER.toString(), lowercase(sfMovieDocument.getWriter()), type));
	  d.add(new Field(SFMovieFieldEnum.ACTOR1.toString(), lowercase(sfMovieDocument.getActor_1()), type));
	  d.add(new Field(SFMovieFieldEnum.ACTOR2.toString(), lowercase(sfMovieDocument.getActor_2()), type));
	  d.add(new Field(SFMovieFieldEnum.ACTOR3.toString(), lowercase(sfMovieDocument.getActor_3()), type));
	  return d;
  }
  
  private String lowercase(String value) {
	  if (value != null) {
		  return value.trim().toLowerCase().replaceAll("\"", "");
	  } else {
		  return "";
	  }
  }
  
  private void index(List<Document> docs) {
    try {
      IndexWriterConfig config = new IndexWriterConfig(LUCENE_VERSION, analyzer);
      IndexWriter writer = new IndexWriter(indexdir, config);
      if (writer != null) {
        for (Document doc : docs) {
          if (doc != null) {
            writer.addDocument(doc);
          }
        }
        writer.commit();
        writer.close();
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }
  }

  private void buildSuggestionDictionary(String name) throws IOException {
    Lookup suggester = new TSTLookup();
    HighFrequencyDictionary dict = new HighFrequencyDictionary(DirectoryReader.open(indexdir), name, 0.0f);
    suggester.build(dict);
    setSuggester(name, suggester);
  }
}