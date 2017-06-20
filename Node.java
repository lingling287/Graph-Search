package bfs;

import java.awt.Color;
import java.util.HashSet;

public class Node implements Comparable<Node>{

	String name;
	HashSet<Node> nodeList;
	Node parent;
	
	int distance;
	
	Color color;

	Node (String name) {
		this.name = name;
		nodeList = new HashSet<>();

		parent = null;
		color = Color.WHITE;
		distance = -1;
	}
	
	public synchronized void addNode(Node n) {
		nodeList.add(n);
	}
	
	public String toString() {
		return name;
	}
	
	public boolean equals(Object o) {
		Node that = (Node) o;
		
		if (this.compareTo(that) == 0) {
			return true;
		}
		
		return false;
	}
	
	public int hashCode() {

		return name.hashCode();
	}

	public int compareTo(Node that) {	
		return this.name.compareTo(that.name);
	}
}
