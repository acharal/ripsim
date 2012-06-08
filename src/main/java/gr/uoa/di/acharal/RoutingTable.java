package gr.uoa.di.acharal;

class RoutingException extends Exception { static final long serialVersionUID = 50000; }
class NoRouteException extends RoutingException { static final long serialVersionUID = 50000; }

public abstract class  RoutingTable {
	
	/**  The node that this RoutingTable is for */
	Node parent_node;

	RoutingTable(Node node) { parent_node = node; }
	
	/**  Abstract class that must implement the lookup
	 *   on the routing table.
	 *   @param dest destination id
	 *   @return interface id */
	public abstract String lookup(String destination) throws NoRouteException; 

	public abstract void initialize();
	
	public abstract void advertiseNetwork(String netname);
	
	public abstract void print();
	
	public abstract void stopListening(String iface);

	public abstract void startListening(String iface); 
	
	public abstract String[] toStringArray();

	public abstract void clear();
}
