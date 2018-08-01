package com.jtj.cloud.feignclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@EnableFeignClients
@SpringCloudApplication
public class FeignClientApplication {

	@Resource
	private BaseClient baseClient;

	public static void main(String[] args) {
		SpringApplication.run(FeignClientApplication.class, args);
	}

	@GetMapping("/")
	public String index(){
		return "Feign Client Started !!";
	}

	@GetMapping("/base")
	public String getBaseClientData(){
		return baseClient.getBaseClientData();
	}

	@GetMapping("/base/curl")
	public Map<String,String> getBaseClientCurl(){
		Map<String,String> result = new HashMap<>();

		result.put("ID 1",baseClient.getUser(1L));
		Map<String,String> query = new HashMap<>();

		query.put("name","Jone Taki");
		result.put("Query Jone",baseClient.getUser(query));

		result.put("Post",baseClient.postUser(User.of("Jone Tiki",20,1)));
		result.put("Put",baseClient.postUser(User.of("Jone Kolo",30,1)));

		try {
			baseClient.deleteUser();
			result.put("Delete","success");
		} catch (RuntimeException e) {
			result.put("Delete","fail: " + e.getMessage());
		}

		return result;
	}

}
