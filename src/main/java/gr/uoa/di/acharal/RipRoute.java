package gr.uoa.di.acharal;

public class RipRoute {
	
	/** Time that must elapse (in ms) before a route is considered expired. */
	public static final int EXPIRY = 120000;
		
	/** Time that must elapse (in ms) before an expired route forgotten. */
	public static final int GARBAGE = 120000;
	
	/** Address of the destination (Let it be string) */
	String dest;
	
	/** Interface */
	String nextHop;

	/** The last time this entry was confirmed. */
	int distance;
	
	/** The last time this entry was confirmed. */
	long time_stamp;
	
	RipRoute(String dest, String nextHop, int distance) { 
		this.dest = dest;
		this.nextHop = nextHop;
		this.distance = distance;
		resetTimeStamp();
	}

	/** Returns how long it has been (in ms) since this route was confirmed. */ 
	public long getAge(){
		return System.currentTimeMillis() - time_stamp;
	}
	
	/** Called when this route is reconfirmed (updates the time stamp to now). */
	public void resetTimeStamp(){
		time_stamp = System.currentTimeMillis();
	}
	
	/** Exipire this route. */
	public void expire(){
		time_stamp = System.currentTimeMillis() - EXPIRY; 
	}
	
	/** Returns if this route has expired. */
	public boolean isExpired(){
		return getAge() > EXPIRY;
	}

	/** Returns if this route is stale and must be garbage collected. */
	public boolean isStale(){
		return getAge() > (EXPIRY + GARBAGE);
	}
}
