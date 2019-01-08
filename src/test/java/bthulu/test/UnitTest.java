package bthulu.test;

import bthulu.commons.combine.collection.SetUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnitTest {

	public static void main(String[] args) {

	}

	@Test
	public void testSetUtilCompare() {
		Set<Integer> old = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
		Set<Integer> now = new HashSet<>(Arrays.asList(3, 4, 6, 7));
		List<Integer>[] compare = SetUtil.compare(old, now);
		System.out.println(Arrays.toString(compare));
	}
}
