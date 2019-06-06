package bthulu.test;

import bthulu.commons.combine.collection.ListUtil;
import bthulu.commons.combine.io.FileUtil;
import bthulu.commons.combine.reflect.BeanUtil;
import bthulu.commons.combine.text.Jackson;
import bthulu.commons.combine.text.StringUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UnitTest {

	public static void main(String[] args) {
		String fileExtension = FileUtil.getFileExtension("abc.txt");
		System.out.println(fileExtension);
	}

	public static class Apple {
		private String name;
		private String address;

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
	}

	@Test
	public void testAsserts() {
		List<Apple> old = new ArrayList<>();
		old.add(new Apple("lisi", "old"));
		old.add(new Apple("wangwu", "old"));
		old.add(new Apple("lisan", "old"));
		List<Apple> now = new ArrayList<>();
		now.add(new Apple("zhangsan", "now"));
		now.add(new Apple("wangwu", "now"));
		now.add(new Apple("lisi", "now"));
		ListUtil.CompareResult<Apple> compare = ListUtil.compare(old, now, Apple::getName);
		System.out.println(Jackson.toJSONString(compare));
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
