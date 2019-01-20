package bthulu.test;

import bthulu.commons.combine.text.StringUtil;
import org.junit.Test;

public class UnitTest {

	public static void main(String[] args) {

	}

	@Test
	public void testSetUtilCompare() {
		for (int i = 0; i < 5; i++) {
			Float s = StringUtil.cherryPickFloat("B|0.232|-|-|", null, i);
			System.out.println(s);
		}

	}
}
