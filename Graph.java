package bfs;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Graph extends Thread{

	File file;
	HashSet<Node> movieList;
	HashSet<Node> actorList;
	BlockingQueue<String> bq;
	CountDownLatch cdl;
	
	Node start;

	public Graph(File file) {
		this.file = file;
		movieList = new HashSet<>();
		actorList =  new HashSet<>();
		bq = new ArrayBlockingQueue<>(20);
		cdl = new CountDownLatch(1);
	}

	public void readFile() {

		System.out.println("Starting File Read");

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String s = br.readLine();

			
			
			int timeElapse  = 0;
			
			while (s != null) {
				timeElapse++;
				
				if (timeElapse %800 == 0) {
					//System.out.print(".");
				}
				//System.out.println(Thread.currentThread().getName() + " Reading String");
				bq.put(s);
				s = br.readLine();
			}

			br.close();
			
			bq.put("EXIT");
			bq.put("EXIT");
			bq.put("EXIT");
			bq.put("EXIT");

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\nEnd File Read\n");

	}

	
	public void run() {
		
		new Worker(this).start();
		
		readFile();
	}

	//performs a graph search using BFS starting at actor s given
	public boolean bfs(String s) {


		start = new Node(s);
		
		if (!actorList.contains(start)) {
			System.out.println("Actor does not exist in graph\n");
			return false;
		}

		for (Node n : actorList) {
			if (n.equals(start)) {
				start = n;
				break;
			}
		}

		Queue<Node> q = new LinkedList<Node>(); // Queue for algorithm
		q.add(start); // add to queue

		start.distance = 0;

		start.color = Color.GRAY; // set color to grey
		start.parent = null; // update cell parent

		while (!q.isEmpty()) { // while not empty
			Node u = q.poll(); // get a cell from the queue

			HashSet<Node> list = u.nodeList; // gets list of connected cells

			for (Node c : list) { // for each neighbor, update color, timer, and
									// parent
				if (c.color == Color.WHITE) {
					c.color = Color.GRAY;
					c.distance = u.distance + 1;
					c.parent = u;

					q.add(c);
				}
			}
			u.color = Color.BLACK; // no more neighbors to be found
		}
		
		printFrequency();
		
		return true;
	}

	private void printFrequency() {

		int max = 0; // set max to zero

		for (Node n : actorList) { // for each node, if distance is greater than max
			if (n.distance / 2 > max) {
				max = n.distance / 2; // set new max to the farthest found
			}
		}

		int[] freqency = new int[max + 2]; // initialize array to max + 2 for infinity nodes

		for (int i = 0; i < freqency.length; i++) {
			freqency[i] = 0; // initialize each element to zero
		}

		for (Node n : actorList) {
			if (n.distance == -1) { // if distance is infinity, update last element
				freqency[max + 1]++;
			}

			else {
				freqency[n.distance / 2]++; // else, update corresponding distance element
			}
		}

		
		// prints frequency
		System.out.println(start + " Frequency:");
		System.out.println("------------------------");

		for (int i = 0; i < freqency.length; i++) {

			if (i == freqency.length - 1) {
				System.out.println("infinity:   " + freqency[i]);
			}

			else {
				System.out.println(i + "          " + freqency[i]);
			}
		}

	}

	public void printRelation(String e) {

		
		Node end = new Node(e);

		if (!actorList.contains(end)) {
			System.out.println("Actor does not exist in graph");
			return;
		}
		

		for (Node n : actorList) {
			if (n.equals(end)) {
				end = n;
			}
		}
		
		
		System.out.println(end + " has a " + start + " number of " + end.distance/2);
		
		if (end.distance/2 != 0) {
			while (end != start) {
				System.out.println(end + " is in " + end.parent + " with " + end.parent.parent);
				end = end.parent.parent;
			}
		}
		
	}
	

	public static void main(String[] args) {

		
		if (args.length == 0) {
			args = new String[1];
			args[0] = "src/bfs/cast.06.txt";
		}
		
		Graph graph = new Graph(new File(args[0]));
		graph.start();
		
		try {
			graph.cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Scanner in = new Scanner(System.in);
	
		
		while (true) {
			System.out.print("\nEnter First Actor Name: ");
			String actor1 = in.nextLine();
			
			if (graph.bfs(actor1)) {
				break;
			}
			
			
		}
		
		
		while (true) {
			System.out.print("\nEnter Second Actor Name, press e to exit: ");
			String actor2 = in.nextLine();	
			
			
			if (actor2.equals("e") || actor2.equals("E")) {
				break;
			}
			
			
			graph.printRelation(actor2);
		}
		
		
		System.out.println("\nEnd of Program");
		in.close();
	}
}