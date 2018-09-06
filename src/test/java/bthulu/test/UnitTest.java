package bthulu.test;

import bthulu.commons.combine.constant.ConstDatePattern;
import bthulu.commons.combine.text.JSON;
import bthulu.commons.combine.time.DateFormatter;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UnitTest {

	public static void main(String[] args) {

	}

	@Test
	public void testJSON() {
		User user = new User();
		user.setId(22);
		user.setName("lisi");
		user.setCtime(DateFormatter.parseLocalDateTime(ConstDatePattern.DEFAULT,
				DateFormatter.format(ConstDatePattern.DEFAULT, LocalDateTime.now())));
		String s = JSON.writeValueAsString(user);
		System.out.println(s);
		User user1 = JSON.parseObject(s, User.class);
		assertEquals(user, user1);
		Map<String, Object> map = new HashMap<>();
		map.put("id", user.getId());
		map.put("name", user.getName());
		map.put("ctime", user.getCtime());
		map.put("hh", null);
		map.put("ke", "");
		map.put("ignored", "ig");
		User user2 = JSON.parseObject(JSON.writeValueAsString(map), User.class);
		assertEquals(user, user2);
		// todo + toJsonString();

	}
}
