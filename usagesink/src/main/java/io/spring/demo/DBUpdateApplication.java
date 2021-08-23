package io.spring.demo;

import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.demo.entity.CallUsage;
import io.spring.demo.entity.DataUsage;
import io.spring.demo.entity.Plan;
import io.spring.demo.entity.User;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DBUpdateApplication {

	@Autowired
	private DataSource dataSource;


	public static void main(String[] args) {
		SpringApplication.run(DBUpdateApplication.class, args);
	}

	@Bean
	public Consumer<String> process() {
		return  (payload) -> {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = null;
			if(payload == null || payload.equals("")) {
				return;
			}
			try {
				jsonNode = objectMapper.readTree(payload);
			}
			catch (JsonProcessingException e) {
				System.out.println("REJECTING ==> " + payload);
				return;
				//throw new IllegalStateException(e);
			}
			String result = "";
			String type = jsonNode.get("type").asText();
			if(type.equals(Plan.PLAN_TYPE)) {
				savePlan(payload);
			}
			if(type.equals(User.USER_TYPE)) {
				saveUser(payload);
			}
			if(type.equals(CallUsage.CALL_USAGE_TYPE)) {
				saveCallUsage(payload);
			}
			if(type.equals(DataUsage.DATA_USAGE_TYPE)) {
				saveDataUsage(payload);
			}
		};
	}

	private void savePlan(String payload) {
		System.out.println(">>>>>>>>> Plan  => " + payload);
		ObjectMapper objectMapper = new ObjectMapper();
		Plan plan = null;
		try {
			plan = objectMapper.readValue(payload, Plan.class);
		}
		catch (JsonProcessingException e) {
			System.out.println("FAILED**********" + e.getMessage());
			e.printStackTrace();
			return;
		}
		JdbcTemplate template = new JdbcTemplate(this.dataSource);
		template.update("INSERT INTO plans (plan_id, plan_name, data_price, " +
				"call_price) VALUES(?, ?, ?, ?)", plan.getPlanId(),
				plan.getPlanName(), plan.getDataPrice(), plan.getCallPrice() );
	}

	private void saveUser(String payload) {
		ObjectMapper objectMapper = new ObjectMapper();
		User user = null;
		try {
			user = objectMapper.readValue(payload, User.class);
		}
		catch (JsonProcessingException e) {
			System.out.println("FAILED**********" + e.getMessage());
			e.printStackTrace();
			return;
		}
		JdbcTemplate template = new JdbcTemplate(this.dataSource);
		template.update("INSERT INTO users (id, plan_id,first_name, last_name) VALUES(?, ?, ?, ?)", user.getId(),
				user.getPlanId(), user.getFirstName(), user.getLastName() );
	}

	private void saveCallUsage(String payload) {
		ObjectMapper objectMapper = new ObjectMapper();
		CallUsage callUsage = null;
		try {
			callUsage = objectMapper.readValue(payload, CallUsage.class);
		}
		catch (JsonProcessingException e) {
			System.out.println("FAILED**********" + e.getMessage());
			e.printStackTrace();
			return;
		}
		JdbcTemplate template = new JdbcTemplate(this.dataSource);
		template.update("INSERT INTO call_usage (user_id, usage_in_minutes, date) VALUES(?, ?, ?);", callUsage.getUserId(),
				callUsage.getUsageInMinutes(), callUsage.getDate() );
	}

	private void saveDataUsage(String payload) {
		ObjectMapper objectMapper = new ObjectMapper();
		DataUsage dataUsage = null;
		try {
			dataUsage = objectMapper.readValue(payload, DataUsage.class);
		}
		catch (JsonProcessingException e) {
			System.out.println("FAILED**********" + e.getMessage());
			e.printStackTrace();
			return;
		}
		JdbcTemplate template = new JdbcTemplate(this.dataSource);
		template.update("INSERT INTO data_usage (user_id, usage_in_bytes, date) VALUES(?, ?, ?);", dataUsage.getUserId(),
				dataUsage.getUsageInBytes(), dataUsage.getDate() );
	}
}
