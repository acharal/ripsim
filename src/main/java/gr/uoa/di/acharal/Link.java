package gr.uoa.di.acharal;

import java.util.*;

public class Link {

	private boolean enabled = true;
	private List buff;
	
	Node from, to;

	Link(Node from_node, Node to_node) {
		from = from_node;
		to = to_node;
		buff = new LinkedList();
		from.addOutLink(this);
		to.addInLink(this);
	}
	
	synchronized void send(Packet p) {
		if (!isEnabled()) {
			/* drop the packet. link is not enabled */
			return;
		}
		buff.add(p);
		if (buff.size() == 1)
			this.notify();
	}
	
	synchronized Packet receive() {

		while (buff.size() == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				
			}
		}

		return (Packet) buff.remove(0);
	}
	
	Node connectedTo() { return to; }
	
	Node connectedFrom() { return from; }
	
	void enable() { enabled = true; }

	void disable() { enabled = false; }
	
	boolean isEnabled() { return enabled; }

}
