package gr.uoa.di.acharal;

import java.util.*;

public class Node {

	/** unique id of the node (address) */
	private String id;

	Map inLinks;
	Map outLinks;
	
	boolean up = false;
	
	Node(String id) { 
		this.id = id;
		inLinks = new HashMap();
		outLinks = new HashMap();
	}
	
	void addInLink(Link lnk) {
		String nod = lnk.connectedFrom().getAddress();
		inLinks.put(nod,lnk);
	}
	
	void addOutLink(Link lnk) {
		String nod = lnk.connectedTo().getAddress();
		outLinks.put(nod,lnk);
	}
	
	void delInLink(Link lnk) {
		String nod = lnk.connectedFrom().getAddress();
		inLinks.remove(nod);
	}
	
	void delOutLink(Link lnk) {
		String nod = lnk.connectedTo().getAddress();
		outLinks.remove(nod);
	}
	
	void transmitPacket(String lnk_id, Packet p) {
		Link lnk = (Link) outLinks.get(lnk_id);
		lnk.send(p);
	}
	
	void broadcastPacket(Packet p) {
		Collection links = outLinks.values();
		Iterator it = links.iterator();
		while (it.hasNext()) {
			Link lnk = (Link) it.next();
			lnk.send(p);
		}
		
	}
	
	Packet readPacket(String lnk_id) { 
		Link lnk = (Link) inLinks.get(lnk_id);
		Packet p = (Packet) lnk.receive();
		return p;
	}
	
	public Collection getOutNeighboorNames() { return outLinks.keySet(); }
	
	public Collection getOutNeighboorNodes() {
		Collection neighboors = new LinkedList();
		Collection lnks = outLinks.values();
		Iterator it = lnks.iterator();
		
		while(it.hasNext()) {
			Node n = ((Link) it.next()).connectedTo();
			neighboors.add(n);
		}
		
		return neighboors;
	}

	
	public String getAddress() { return id; }

	public String toString() { return id; } 

	public void up() { up = true; }
	public boolean isUp() { return up; }
}
