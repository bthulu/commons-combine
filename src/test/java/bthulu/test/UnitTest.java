package bthulu.test;

import bthulu.commons.combine.io.FileUtil;
import bthulu.commons.combine.reflect.BeanUtil;
import bthulu.commons.combine.text.StringUtil;
import org.junit.Test;

import java.util.Map;

public class UnitTest {

	public static void main(String[] args) {
		String fileExtension = FileUtil.getFileExtension("abc.txt");
		System.out.println(fileExtension);
	}

	public static class Apple {
		private String name;
		private String address;
		private boolean good;

		public Apple(String name, String address) {
			this.name = name;
			this.address = address;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public boolean isGood() {
			return good;
		}

		public void setGood(boolean good) {
			this.good = good;
		}
	}

	@Test
	public void testAsserts() {
		Apple apple = new Apple("lisi", "old");
		Map<String, ?> stringMap = BeanUtil.toMap(apple);
		System.out.println(stringMap);
	}

	@Test
	public void testSetUtilCompare() {
		for (int i = 0; i < 5; i++) {
			Float s = StringUtil.cherryPickFloat("B|0.232|-|-|", null, i);
			System.out.println(s);
		}

	}

	@Test
	public void testDefaultValue() {
		User user = new User();
		BeanUtil.writeDefaultValue(user);
		System.out.println(user);
	}
}
