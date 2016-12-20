package dk.shx.model.search;

import java.io.Serializable;
import java.util.EnumSet;

public enum SFMovieFieldEnum implements Serializable {
	TITLE("title"),
	RELEASEYEAR("release_year"),
	LOCATIONS("locations"),
	FUNFACTS("fun_facts"),
	PRODUCTION_COMPANY("production_company"),
	DISTRIBUTOR("distributor"),
	DIRECTOR("director"),
	WRITER("writer"),
	ACTOR1("actor_1"),
	ACTOR2("actor_2"),
	ACTOR3("actor_3");
	
	private final String name;
	
	private SFMovieFieldEnum(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
	
	public static final EnumSet<SFMovieFieldEnum> sfmoviefields = 
	  EnumSet.of(TITLE,
		  RELEASEYEAR,
		  LOCATIONS,
		  FUNFACTS,
		  PRODUCTION_COMPANY,
		  DISTRIBUTOR,
		  DIRECTOR,
		  WRITER,
		  ACTOR1,
		  ACTOR2,
		  ACTOR3);
}