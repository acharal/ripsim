package gr.uoa.di.acharal;

import java.util.*;

public class TestRip {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Router r[] = new Router[5];

		r[1] = new Router("A");
		r[2] = new Router("B");
		r[3] = new Router("C");
		r[4] = new Router("D");
		
		BiLink l1 = new BiLink(r[1], r[2]);
		BiLink l2 = new BiLink(r[2], r[3]);
		BiLink l3 = new BiLink(r[3], r[4]);
		BiLink l4 = new BiLink(r[4], r[2]);

		for(int i = 1; i <= 4; ++i)
			r[i].up();

	}

}
