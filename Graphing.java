//This program wraps the result from the ChainFinder.java and present the chain visually
//This program is written by Tao Liu
import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Graphing extends JFrame{
	Wdmb mb = null;
	public Graphing(){
		Wdmb mb = new Wdmb();
		this.add(mb);
		this.setSize(800,800);
		this.setLocation(100,100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public static void main(String[] args){
		Graphing graph = new Graphing();
	}

}
class Wdmb extends JPanel{
	public void paint(Graphics g){
		Scanner in = new Scanner(System.in);
 
        

		ChainFinder chainFinder = new ChainFinder();
        chainFinder.loadMovie("movies.txt");
	   
        chainFinder.loadPerformer("actress.txt");
        
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
                    	g.setColor(Color.cyan);
		 				g.fillOval(50,50,400,100);
		 				g.fillOval(50,200,400,100);
		 				g.fillOval(50,350,400,100);


		 				g.setColor(Color.black);
						Font ft = new Font("Times New Roman",Font.BOLD,30);
						g.setFont(ft);
						g.drawString(result.get(i)[1],80,100);
						g.drawString("was in movie",80,180);
						g.drawString(result.get(i)[0],80,250);
						g.drawString("with", 80,330);
						g.drawString(s2,80,400);
						g.drawString("total number of actress in iMDB's database: ",50,500);
						g.drawString(""+ChainFinder.performerCount,50,550);
						g.drawString("total number of movies in iMDB's database: ",50,600);
						g.drawString(""+ChainFinder.movieCount,50,650);


                        System.out.println("actor "+result.get(i)[1]+" was in movie "+result.get(i)[0]+" with actor "+s2);
                        break forLoop;
                    }

                   if((i!=result.size()-1) && result.size()!=1){
      //              		g.setColor(Color.cyan);
		 			// 	g.fillOval(50,50+150*i,400,100);
		 			// 	g.fillOval(50,200,400,100);
		 			// 	g.fillOval(50,350,400,100);

		 			// 	g.setColor(Color.black);
						// Font ft = new Font("Times New Roman",Font.BOLD,30);
						// g.setFont(ft);
						// g.drawString(result.get(i)[1],80,50+50*i);
						// g.drawString("was in movie",80,180);
						// g.drawString(result.get(i)[0],80,250);
						// g.drawString("with", 80,330);
						// g.drawString(s2,80,400);


                        
                    
                    System.out.println("actor "+result.get(i)[1]+" was in movie "+result.get(i)[0]+" with actor "+result.get(i+1)[1]);
                   }

                   
                   else{
                    System.out.println("actor "+result.get(i)[1]+" was in movie "+result.get(i)[0]+" with actor "+s2);
                   }
                    
                }
                
            
           
            }else{
                System.out.println("there is no connection between these two performers");
            }

		

	}
}
