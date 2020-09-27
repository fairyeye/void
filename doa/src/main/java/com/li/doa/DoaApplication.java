package com.li.doa;

import com.alibaba.fastjson.JSON;
import com.li.doa.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class DoaApplication {

	@Resource
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(DoaApplication.class, args);
	}

	@GetMapping("/test")
	public List<User> test() {
		List<User> result = new ArrayList<>();
		List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from user");
		maps.forEach(stringObjectMap -> {
			User user = JSON.parseObject(JSON.toJSONString(stringObjectMap), User.class);
			System.out.println(user);
			result.add(user);
		});
		return result;
	}
}
