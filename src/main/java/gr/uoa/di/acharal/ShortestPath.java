package gr.uoa.di.acharal;

import java.util.*;

public class ShortestPath {

	class Route { 
		String dest;
		String nextHop;
		int distance;
	}
	
	List routes;
	Node parent_node;
	
	ShortestPath(Node node) {
		//super(node);
		parent_node = node;
	}
	
	public void initialize() { 
		Map paths = shortestPath(parent_node);
		createTable(paths);
	}
	
	static private Map shortestPath(Node startNode) { 
		
		List pendingNodes = new LinkedList();
		Set visited = new HashSet();
		Map parent = new HashMap();
		
		pendingNodes.add(startNode);
		
		while (pendingNodes.size() > 0) {
			Node nextNode = (Node) pendingNodes.remove(0);
			visited.add(nextNode);
			Collection neighboors = nextNode.getOutNeighboorNodes();
			Iterator it = neighboors.iterator();
			
			while (it.hasNext()) {
				Node neighboor = (Node)it.next();
				if (!visited.contains(neighboor)) {
					pendingNodes.add(neighboor);
					parent.put(neighboor,nextNode);
				}
			}
		}
		return parent;
	}

	private void createTable(Map parents) { 
		
	}
	
	public String lookup(String dest) { return null; }
	
}
