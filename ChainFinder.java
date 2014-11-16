//ChainFinder.java
//This program is written by Tao Liu to apply the concept of six degree separation 
//by finding the connections between actress
//the data used here is from the open source iMDb database.
import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class ChainFinder {
    // movieList is used to store movie key and performers values where one movie points to all performers appearing in this movie.
	private static Hashtable<String, Movie> movie_to_performer =  new Hashtable<String, Movie>();
	//performerChain is used to store performer key and performers values where one performer points to all the other performers who was in the same movie.
    private static Hashtable<String, Performer> performer_to_movie = new Hashtable<String, Performer>();
    // we use this.name to refer to the performer when we read lines from input file.
    private static String name;
    // we use this.movieName to refer to the movie when we read lines from input file.
    private static String movieName;
    //we use the shortestPath to store the list of performers of shortest path.
    private static LinkedList<String[]> shortestPath = new LinkedList<String[]>();
    // we use the visited to store the info whether a performer has been visited when we do the BFS.
    private static Hashtable<String,String> visited = new Hashtable<String,String>();
    // we use the queue to do the BFS.
    private static LinkedList<String> queue = new LinkedList<String>();
    // we use the whereFrom to store the performer we visit as the key and what its last source(by which we find this performer)as the value. so after we have 
    //done with the BFS, we can trace back to the start by beginning at the end.
    private static Hashtable<String,String[]> whereFrom = new Hashtable<String,String[]>();

    public static int performerCount;

    public static int movieCount;

    //use this function to load movie. and store each movie as a key in the movieList, though it now only points to an empty list since we haven't added performers.
	public void loadMovie(String fileName){
		String inputFilePath = fileName;
        File inputFile = new File(inputFilePath);
		 BufferedReader br = null;
        movieCount = 0;
        try {
           
            br = new BufferedReader(new FileReader(inputFile));
            String line;
            String[] fields;
            String movieName;
            while((line = br.readLine()) != null) {
                if (line.contains("(")){
                    fields = line.split("[\\()]");
                    movieName = fields[0]+"("+fields[1]+")";
                    System.out.println("movie is "+movieName);
                    movie_to_performer.put(movieName,new Movie(movieName));
                    movieCount++;

                    
                }
            }
        } catch (Exception e) {
            System.err.println("Can't open " + inputFilePath);
            System.exit(1);
        }

        
	}


    // we use this function  to read input performers list.
    public void loadPerformer(String fileName){
        String inputFilePath = fileName;
        File inputFile = new File(inputFilePath);
        BufferedReader br = null;
        
        StringTokenizer st;
        
        
        try {
           
            br = new BufferedReader(new FileReader(inputFile));
            String line;
            String[] fields;
            int emptyLine = 0;
            String name;
            String performerName1;
            String performerName2;
            String tempMovieName;
           performerCount = 0;
            
            
            mainLoop:
            while((line = br.readLine()) != null) {

                
                if ( line.trim().length() == 0 ) {
                    emptyLine = 1;  
                    continue;  // Skip blank lines  
                } // otherwise:  
                
                if (emptyLine == 1){
                    st = new StringTokenizer(line);
                    performerName1 = st.nextToken("[\\,\t ]");
                    performerName2 = st.nextToken("[\\,\t ]");
                    

                    name = performerName2+" "+performerName1;
                    this.name = name;
                    // we read performers and add them to the performerChain, though we haven't made it point to other performers in the same movie.
                    performer_to_movie.put(this.name,new Performer(this.name));
                    performerCount++;
                    
                    emptyLine = 2;
                }
                else{
                    
                    st = new StringTokenizer(line);
                    tempMovieName = st.nextToken("[\\)\t]");
                    tempMovieName = tempMovieName+")";

                    // if the movieList doesn't included the movie now we are loading from performer list under each performer's name, we add the movie to the 
                    // movieList .
                    if (!movie_to_performer.containsKey(tempMovieName)){
                        movie_to_performer.put(tempMovieName,new Movie(tempMovieName));
                         movieCount++;
                        continue mainLoop;
                    }
                    
                    // if the movie from the next line we are reading from is the repetitive movie shown in the last line, we won't add it to our list.
                    if ((!tempMovieName.equals(this.movieName)) ) {
                       
                        performer_to_movie.get(this.name).addMovie(tempMovieName);
                        //debugging code
                        System.out.println("get performer "+performer_to_movie.get(this.name).getPerformerName());

                        movie_to_performer.get(tempMovieName).addPerformer(this.name);  
                        //debugging code
                        System.out.println("get movie name "+movie_to_performer.get(tempMovieName).getMovieName());
                       
                    }
                    this.movieName = tempMovieName;
                   
                }  
                    
            }        
                
        } catch (Exception e) {
            System.err.println("Can't open " + inputFilePath);
            System.exit(1);
        }
    }
    //we use the BFS to find the shortest path.
    public boolean breadthFirstSearch(String start, String end){
        boolean truth = false;
        // if the performerChain doesn't include the start and end we want to search for at all, then return false.
        if (performer_to_movie.containsKey(start) && performer_to_movie.containsKey(end)){
            // we set the start to visited.
            visited.put(start,"a");
            //we add it to the queue.
            queue.addLast(start);
            // it's the basic algorithm of BFS ecept when add one step to store the performer we find the its source in the whereFrom so later we can trace 
            // back to the start.
            mainLoop:
            while (!queue.isEmpty()) {
                String dequeue = queue.remove(0);
                
                ArrayList<String> movieList = performer_to_movie.get(dequeue).getMovies();
                Collections.sort(movieList);
                for(String movie : movieList ){
                    
                    ArrayList<String> performerList = movie_to_performer.get(movie).getPerformers();
                    Collections.sort(performerList);
                    for (String performer : performerList){

                        
                        if (!performer.equals(end)){
                            if (!visited.containsKey(performer) ){
                                visited.put(performer,"a");
                                String[] edge = {movie,dequeue};
                                whereFrom.put(performer,edge);
                                queue.addLast(performer);
                            }
                        }else{
                            String[] edge = {movie,dequeue};
                            whereFrom.put(performer,edge);
                            truth = true;
                            break mainLoop;
                        }
                    }
                }
            }
        }
        return truth;
    }
    // we store the shortest path in the shortestPath in this function.
    public LinkedList<String[]> path_of_connection(String  start, String end){
        String[] pair;
        pair = whereFrom.get(end);
        
        shortestPath.addFirst(pair);
        //System.out.println("end is "+end );
        String source;
        
        source = whereFrom.get(end)[1];
        
        //System.out.println("the source is  "+source);
        
        while(!source.equals(start)){
            String temp = source;
            pair = whereFrom.get(temp);
            
            shortestPath.addFirst(pair);

            source = whereFrom.get(temp)[1];
            
            //System.out.println("the source is "+source );
            
        }
        return shortestPath;
        
        
    }

        
    
	public static void main(String args[]){
        Scanner in = new Scanner(System.in);
 
        

		ChainFinder chainFinder = new ChainFinder();
        chainFinder.loadMovie("movies.list");
	    
        chainFinder.loadPerformer("actress.list");
        
        
        System.out.println("Enter the name of an actress");
        String s1 = in.nextLine();

        System.out.println("Enter the name of the other actress");
        String s2 = in.nextLine();

        Boolean ifFind = chainFinder.breadthFirstSearch(s1,s2);
        System.out.println("true or false "+ifFind);

        LinkedList<String[]> result =  chainFinder.path_of_connection(s1,s2);
        System.out.println("the size is "+result.size());
       
        if(ifFind == true){
            
            
            forLoop:
            for(int i= 0; i<result.size(); i++){
                if (result.size() == 1){
                    System.out.println("actress "+result.get(i)[1]+" was in movie "+result.get(i)[0]+" with actress "+s2);
                    break forLoop;
                }

               if((i!=result.size()-1) && result.size()!=1){

                    
                
                System.out.println("actress "+result.get(i)[1]+" was in movie "+result.get(i)[0]+" with actress "+result.get(i+1)[1]);
               }

               
               else{
                System.out.println("actor "+result.get(i)[1]+" was in movie "+result.get(i)[0]+" with actor "+s2);
               }
                
            }
            
        
       
        }else{
            System.out.println("there is no connection between these two performers");
        }
         System.out.println("total number of actress in iMDB's database: "+ChainFinder.performerCount);
         System.out.println("total number of movies in iMDB's database: "+ChainFinder.movieCount);
        

	}

}
