package gr.uoa.di.acharal;

public class RipMessage extends Packet {
	
	
	class RipEntry {
		String dest;
		int distance;
	}
	
	String src;
	
	RipEntry[] entries;

	RipMessage(int size) {
		entries = new RipEntry[size];
	}
	
	void setEntry(int i, String d, int dist) {
		entries[i] = new RipEntry();
		entries[i].dest = d;
		entries[i].distance = dist;
	}
	
}
