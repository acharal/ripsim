package gr.uoa.di.acharal;

public class Router extends Node {

	public RoutingTable routeTable = null;

	Router(String name) {
		super(name);
		routeTable = new RipRoutingTable(this);
		routeTable.advertiseNetwork(name);
		up();
	}
	
	void connectToNetwork() { }

	public void up() { 
		routeTable.initialize();
		super.up();
	}
	
	void addInLink(Link lnk) {
		String nod = lnk.connectedFrom().getAddress();
		super.addInLink(lnk);
		if(isUp())
			routeTable.startListening(nod);
	}
	
	void addOutLink(Link lnk) {
		String nod = lnk.connectedTo().getAddress();
		super.addOutLink(lnk);
//		if(isUp())
//			routeTable.stopListening(nod);
	}
	
}
