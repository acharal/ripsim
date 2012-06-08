package gr.uoa.di.acharal;

public class BiLink {

	Link lnk1, lnk2;
	
	BiLink(Node a, Node b) {
		lnk1 = new Link(a,b);
		lnk2 = new Link(b,a);
	}
	
	Link getLink1() { return lnk1; }
	Link getLink2() { return lnk2; }
	
	void enable() {
		lnk1.enable();
		lnk2.enable();
	}
	
	void disable() { 
		lnk1.disable();
		lnk2.disable();
	}
	
	boolean isEnabled() {
		return lnk1.isEnabled() && lnk2.isEnabled();
	}
}
