import java.util.*;
public class Movie{
	private String movieName;
	private ArrayList<String> performers;

	public Movie(String movie){
		this.movieName = movie;
		performers = new ArrayList<String>();
	}
	public void addPerformer(String performer){
		performers.add(performer);
	}
	public String getMovieName(){
		return this.movieName;
	}
	public ArrayList<String> getPerformers(){
		return performers;
	}
}
