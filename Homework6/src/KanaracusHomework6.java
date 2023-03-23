import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;

public class KanaracusHomework6 {

	public static void main(String[] args) {
		File file = new File("Dict.txt");
		Scanner sc=null;
		String currentWord;

		HashMap<String, Node>[] ary = new HashMap[29];
		for (int i = 0; i < ary.length; i++) {
			ary[i] = new HashMap<String, Node>();
		}
		try {
			sc = new Scanner(file);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//read in
		while (sc.hasNextLine()) {
			currentWord = sc.nextLine();
			ary[currentWord.length() - 1].put(currentWord, new Node(currentWord));
		}
		//build graphs
		for (int i = 0; i < 29; i++) {// each loop builds graph for a word length
			Set<Entry<String, Node>> outerSet = ary[i].entrySet();
			Iterator<Entry<String, Node>> outer = outerSet.iterator();
			// loop though all words of that length
			while (outer.hasNext()) {
				Node node = outer.next().getValue();
				String word = node.getWord();// current word
				char[] wordChars = word.toCharArray();// needed to change single character at a time
				char[] changedC;
				String changedS;
				// loops though all characters in word
				for (int j = 0; j < i + 1; j++) {
					changedC = wordChars.clone();// initilizes /fixes previously changed leters
					// tryes all words in alphebet for that charachter
					for (int k = 0; k < 26; k++) {
						if (k + 97 != (int) wordChars[j]) {// makes does not mach with self
							changedC[j] = (char) (k + 97);
							changedS = new String(changedC);
							if (ary[i].containsKey(changedS)) {
								int w = wordChars[j] - changedC[j];
								if (w < 0)
									w *= -1;
								node.addConnected(ary[i].get(changedS), w);
//								System.out.println("test");

							}
						}
					}
				}
			}
		}		
		// user input section
		boolean noEnd = true;
		Scanner keyboard = new Scanner(System.in);
		String word1;
		String word2;
		boolean check;
		
		while (noEnd) {
			//read inputs with checks
			check=true;
			do {
				do {//enter starting word with checks
					System.out.print("Enter Starting Word:");
					word1 = keyboard.nextLine();
					check=!ary[word1.length()-1].containsKey(word1);
					if(check) {
						System.out.println("Word does not exist please try agian");
					}
				}while(check);
				check=true;
				do {//enter ending word with checks
					System.out.print("Enter Ending Word:");
					word2 = keyboard.nextLine();
					check=!ary[word2.length()-1].containsKey(word2);
					if(check) {
						System.out.println("Word does not exist please try agian");
					}
				}while(check);
			
				if(word1.length()!=word2.length()) {
					check=true;
					System.out.println("Word Lengths Do Not Match");
				}
			}while(check);
			
			// function call
			dijkstra(word2,word1,ary);
			
			
			
			//check if exit
			System.out.print("Do you want to continue (y/n):");
			String endCheck=keyboard.nextLine();
			if(endCheck.charAt(0)=='n') {
				noEnd=false;
			}

		}
		keyboard.close();
	}
	
	public static void dijkstra(String w1, String w2, HashMap<String, Node>[] ary) {
		//set up S andd Q from dijkstras
		PriorityQueue<Node> pq=new PriorityQueue<Node>();
		ArrayList<Node> s= new ArrayList<Node>();
		ary[w1.length()-1].get(w1).setDistance(0);
		pq.add(ary[w1.length()-1].get(w1) );//add new connection that is to starting word with weight 0
		boolean found=false;
		while(!pq.isEmpty()&&!found) {
			Node current=pq.poll();
			s.add(current);
			if(current.getWord().equals(w2)) {
				System.out.println(current.getDistance());
				while(current.getParent()!=null) {
					System.out.println(current.getWord());
					current=current.getParent();
				}
				System.out.println(w1);
				return;
			}
			addToPQ(pq,current,current.getConnected(),s);
		}
		System.out.println("No Connection");
	}
	
	public static void addToPQ(PriorityQueue<Node> pq,Node current,ArrayList<NodeConnection> connected,ArrayList<Node> s) {//adds all non previous seen to que
		for(int i=0;i<connected.size();i++) {
			if(s.contains(connected.get(i).getLink())) {
				
			}else if(pq.contains(connected.get(i).getLink())) {
				Iterator<Node> iter =pq.iterator();
				Node c=iter.next();
				while(c.getWord()!=connected.get(i).getLink().getWord()) {
					c=iter.next();
				}
				if(c.getDistance()>connected.get(i).getWeight()+current.getDistance()) {
					pq.remove(c);
					c.setParent(current);
					c.setDistance(connected.get(i).getWeight()+current.getDistance());
					pq.add(c);
				}
			}else {
				connected.get(i).getLink().setParent(current);//set parrent to current
				connected.get(i).getLink().setDistance(connected.get(i).getWeight()+current.getDistance());//set distance to parrent distance plus weight to self
				pq.add(connected.get(i).getLink());
			}
		}
	}
	

}

class Node implements Comparable{
	private Node parent;
	private int distaance;
	private String word;
	private ArrayList<NodeConnection> connecttions;

	public Node(String word) {
		this.word = word;
		this.connecttions=new ArrayList<NodeConnection>();
		this.parent=null;
	}
	public String getWord() {
		return word;
	}
	public Node getParent() {
		return this.parent;
	}
	public int getDistance() {
		return this.distaance;
	}
	public ArrayList<NodeConnection> getConnected() {
		return connecttions;
	}

	public void setParent(Node p) {
		this.parent=p;
	}
	public void setDistance(int d) {
		this.distaance=d;
	}
	public void addConnected(Node n, int w) {
		for (int i = 0; i < connecttions.size(); i++) {
			if (connecttions.get(i).getLink().getWord() == n.getWord()) {
				return;
			}
		}
		connecttions.add(new NodeConnection(n,w));
		n.addConnectedNoCheck(this, w);
	}

	private void addConnectedNoCheck(Node n, int w) {// only called in the above methode slightly faster way of building
														// connection the other way
		connecttions.add(new NodeConnection(n,w));
	}



	@Override
	public int compareTo(Object o) {
		Node n= (Node)o;
		
		return this.distaance-n.distaance;
		
	}
	@Override
	public boolean equals(Object o) {
		Node n=(Node)o;
		return this.word.equals(n.word);
	}

	
}
class NodeConnection{
	private Node link;
	private int weight;
	
	public NodeConnection(Node l, int w) {
		this.link=l;
		this.weight=w;
	}
	public Node getLink() {
		return link;
	}
	public int getWeight() {
		return weight;
	}
	
}

