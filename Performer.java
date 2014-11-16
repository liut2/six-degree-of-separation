import java.util.*;
public class Performer{
	private String performerName;
	private ArrayList<String> movies;

	public Performer(String performerName){
		this.performerName = performerName;
		movies = new ArrayList<String>();
	} 
	public void addMovie(String movie){
		movies.add(movie);
	}
	public String getPerformerName(){
		return this.performerName;
	}
	public ArrayList<String> getMovies(){
		return movies;
	}

}
