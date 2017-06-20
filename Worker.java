package bfs;

public class Worker extends Thread {

	Graph graph;

	public Worker(Graph graph) {
		this.graph = graph;
	}

	public void createGraph() {
		try {
			
			while(true) {
			
				String s = graph.bq.take();
	
				
				if (s.equals("EXIT")) {
					break;
				}
				
				boolean first = false;
	
				Node movie = null;
	
				int start = 0;
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == '/') {
						Node n = new Node(s.substring(start, i));
						if (!first) {
							movie = n;
							first = true;
							graph.movieList.add(n);
							System.out.println(Thread.currentThread().getName() + " : " + movie);
						}
	
						else {
							addActor(n, movie);
						}
						start = i + 1;
					}
				}
				
				Node n = new Node(s.substring(start));
				addActor(n, movie);
			}
			
			graph.cdl.countDown();
	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// if actor already exist in graph, if not add new actor
	private synchronized void addActor(Node n, Node movie) {
		if (graph.actorList.contains(n)) {
			for (Node actor : graph.actorList) {
				if (actor.equals(n)) {
					actor.addNode(movie);
					movie.addNode(actor);
					break;
				}
			}
		}

		else {
			graph.actorList.add(n);
			movie.addNode(n);
			n.addNode(movie);
		}
	}
	
	public void run() {
		createGraph();
	}
}
