package gr.uoa.di.acharal;

import java.util.*;

public class RipRoutingTable extends RoutingTable
                             implements Runnable {

	/** How often (in ms) to send out advertisments. */
	public static final int NORMAL_UPDATE_PERIOD=30000;
	public static final int NORMAL_UPDATE_DELAY=30000;
	
	/** How long to wait (in ms) before sending out a triggered update. */
	public static final int TRIGGERED_UPDATE_DELAY=2000;

	/** infinity/destination unreachable for RIP */
	public static final int INFINITY=16;
	
	/** route list */
	private List routes;
	private List networks;

	private Timer timer = null;
	private Map listeners;

	RipRoutingTable(Node node) {
		super(node);
		routes = new LinkedList();
		networks = new LinkedList();
	}
	
	public void initialize() {
		timer = new Timer();
		timer.schedule(new RipUpdate(), NORMAL_UPDATE_DELAY, NORMAL_UPDATE_PERIOD);

		Thread ripd = new Thread(this);
		ripd.setDaemon(true);
		ripd.start();
	}

	public void run() {
		Collection ifaces = parent_node.getOutNeighboorNames();
		Iterator it = ifaces.iterator();
		
		/* we may sent a Request message to our neighboors if 
		 * we want to get routing info immediately */
		listeners = new HashMap();
		while (it.hasNext()) {
			String iface = (String) it.next();
			startListening(iface);
		}
		
	}
	
	public void stopListening(String iface) { 
		Thread thr = (Thread)listeners.get(iface);
		if(thr != null) {
			thr.interrupt();
			thr.stop();
			listeners.remove(iface);
		}
	}

	public void startListening(String iface) { 
		Thread thr = (Thread)listeners.get(iface);
		if(thr == null) {
			thr = new RipListener(iface);
			thr.start();
			listeners.put(iface, thr);
		}
	}	
	
	synchronized void processMessage(RipMessage mesg) {
		int size = mesg.entries.length;
		
		for (int i = 0; i < size; ++i) {
			if (mesg.entries[i] == null) 
				continue;
			
			String dest = mesg.entries[i].dest;
			boolean ournet = false;
			int distance = mesg.entries[i].distance + 1;
			
			if (distance > INFINITY)
				distance = INFINITY;
			

			Iterator it = networks.iterator();
			while (it.hasNext()) {
				if (dest.equals((String)it.next())) {
					ournet = true;
					break;
				}
			}
			
			if (ournet)
				continue;
			
			RipRoute rr = find(dest);
			
			if (rr == null && distance != INFINITY) {
				/* create new route */
				routes.add(new RipRoute(dest, mesg.src, distance));
			} else if (rr == null && distance == INFINITY){
				/* nothing to do */
			} else { 
				/* update existing route */
				if (rr.nextHop.equals(mesg.src)) {
					/* route from the same router so we update the distance */
					rr.distance = distance;

					if (distance != INFINITY)
						rr.resetTimeStamp();
					else
						rr.expire();
				} else {
					/* we update the route if distance is Shorter */
					if (distance < rr.distance) {
						rr.nextHop = mesg.src;
						System.err.println("Changing route to "+rr.dest+" of metric "+rr.distance+", go via "+mesg.src+" with metric "+distance);
						rr.distance = distance;
						rr.resetTimeStamp();
					}
				}
			}
		}
	}
	
	
	synchronized RipMessage prepareMessage(String to) {
		cleanupRoutingTable();
		int netsize = networks.size();
		int routesize = routes.size();
		RipMessage mesg = new RipMessage(netsize + routesize);

		Iterator it = networks.iterator();
		int i = 0;
		
		while (it.hasNext()) {
			mesg.setEntry(i, (String)it.next(), 0);
			++i;
		}

		it = routes.iterator();
		
		while (it.hasNext()) {
			String dest;
			int distance;

			RipRoute rr = (RipRoute) it.next();
			
			/* if route is expired don't send it */
			if(rr.isExpired())
				continue;
			
			dest = rr.dest;
			distance = rr.distance;
			if (rr.nextHop.equals(to)) { 
				/** Split horizon with poisoned reverse.
				 *  Set distances to infinity if the routes come from the
				 *  node we prepare the message for */
				distance = INFINITY;
			}

			mesg.setEntry(i,dest,distance);
			++i;
		}
		mesg.src = parent_node.getAddress();
	
		return mesg;
	}
	
	protected RipRoute find(String dest) {
		RipRoute rr;
		Iterator i;

		i = routes.iterator();
		while (i.hasNext()) {
			rr = (RipRoute) i.next();
			if (rr.dest.equals(dest))
				return rr;
		}
		
		return null;
	}
	
	protected void cleanupRoutingTable() { 
		Iterator it = routes.iterator();
		
		while(it.hasNext()) {
			RipRoute rr = (RipRoute) it.next();
			
			if (rr.isExpired())
				rr.distance = INFINITY;
			else if (rr.isStale())
				it.remove();
		}
	}
	
	public synchronized String lookup(String dest) throws NoRouteException {
		RipRoute rr = find(dest);
		if (rr == null || rr.isExpired())
			throw new NoRouteException();
		else
			return rr.nextHop;
	}
	

	public void advertiseNetwork(String netname) { 
		networks.add(netname);
	}
	
	public synchronized void print() {
		System.out.println("Routing Table on "+parent_node);
		Iterator it = networks.iterator();
		while (it.hasNext()) {
			System.out.println(""+(String)it.next()+", directly connected");
		}

		it = routes.iterator();
		
		while (it.hasNext()) {
			RipRoute rr = (RipRoute) it.next();
			System.out.println(""+rr.dest+", via "+rr.nextHop+", metric "+rr.distance);
		}
	}
	
	public synchronized String[] toStringArray() {
		
		String table[] = new String[networks.size() + routes.size()];
		Iterator it = networks.iterator();
		int i = 0;
		while (it.hasNext()) {
			table[i++] = ""+(String)it.next()+", directly connected";
		}

		it = routes.iterator();
		
		while (it.hasNext()) {
			RipRoute rr = (RipRoute) it.next();
			table[i] = ""+rr.dest+", via "+rr.nextHop+", metric "+rr.distance;
			if (rr.isExpired())
				table[i] += "(expired)";
			else if (rr.isStale()) { 
				table[i] += "(staled)";
			}
			i++;
		}
		
		return table;
	}
	
	
	public synchronized void clear() { 
		routes.clear();
	}
	
	class RipUpdate extends TimerTask { 

		public void run() {
			//print();
			Collection neighboors = parent_node.getOutNeighboorNames();
			Iterator it = neighboors.iterator();
			while(it.hasNext()) {
				String toNode = (String) it.next();
				RipMessage rm = prepareMessage(toNode);
				parent_node.transmitPacket(toNode, rm);	
			}
			
		}
	}
	
	class RipListener extends Thread {
		
		String iface;
		Node node;
		
		RipListener(String iface) {
			this.iface = iface;
		}
		
		public void run() {
			RipMessage rm = null;

			while (true) {
			
				try {
					rm = (RipMessage) parent_node.readPacket(iface);
					processMessage(rm);
				}
				catch (ClassCastException e) {
					/* unexpected behaviour */
				}
			}
		}
		
	}

}
