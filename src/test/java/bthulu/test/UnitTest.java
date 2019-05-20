package bthulu.test;

import bthulu.commons.combine.exception.Asserts;
import bthulu.commons.combine.io.FileUtil;
import bthulu.commons.combine.reflect.BeanUtil;
import bthulu.commons.combine.text.StringUtil;
import org.junit.Test;

public class UnitTest {

	public static void main(String[] args) {
		String fileExtension = FileUtil.getFileExtension("abc.txt");
		System.out.println(fileExtension);
	}

	@Test
	public void testAsserts() {
		Asserts.isTrue("lll".length() > 1, () -> {
			System.out.println("===========");
			return "eeeeee";
		});
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
