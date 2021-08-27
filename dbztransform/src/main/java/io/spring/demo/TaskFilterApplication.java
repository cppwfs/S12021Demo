/*
 * Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.spring.demo;

import java.util.Date;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.demo.entity.CallUsage;
import io.spring.demo.entity.DataUsage;
import io.spring.demo.entity.Plan;
import io.spring.demo.entity.User;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

@SpringBootApplication
public class TaskFilterApplication {


	public static void main(String[] args) {
		SpringApplication.run(TaskFilterApplication.class, args);
	}

	@Bean
	public Function<Message<String>, Message<String>> transform() {
		return (messagePayload) -> {
			String payload = messagePayload.getPayload();
			JsonNode jsonNode = getJsonNode(payload);
			String result;
			String isUsage = "true";
			String tableName = jsonNode.get("source").get("table").asText();
			if (tableName == null || jsonNode.get("after") == null) {
				result = translateOthers(jsonNode);
				isUsage = "false";
			}
			else {
				if (tableName.equals("plans")) {
					result = translatePlan(jsonNode);
				}
				else if (tableName.equals("users")) {
					result = translateUser(jsonNode);
				}
				else if (tableName.equals("call_usage")) {
					result = translateCallUsage(jsonNode);
				}
				else if (tableName.equals("data_usage")) {
					result = translateDataUsage(jsonNode);
				}
				else {
					result = translateOthers(jsonNode);
				}
			}
			return MessageBuilder.withPayload(result).setHeader("usage", isUsage).build();
		};
	}

	public String translateUser(JsonNode jsonNode) {
		String result;
		ObjectMapper objectMapper = new ObjectMapper();

		User user = new User(jsonNode.get("after").get("id").asInt(-1),
				jsonNode.get("after").get("last_name").asText(),
				jsonNode.get("after").get("first_name").asText(),
				jsonNode.get("after").get("plan_id").asInt(-1));
		try {
			result = objectMapper.writeValueAsString(user);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
			result = "";
		}
		return result;
	}

	public String translatePlan(JsonNode jsonNode) {
		String result;
		ObjectMapper objectMapper = new ObjectMapper();
		Plan plan = new Plan(jsonNode.get("after").get("plan_id").asInt(-1),
				jsonNode.get("after").get("plan_name").asText(),
				jsonNode.get("after").get("data_price").asDouble(),
				jsonNode.get("after").get("data_price").asDouble());
		try {
			result = objectMapper.writeValueAsString(plan);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
			result = "";
		}
		return result;
	}

	public String translateCallUsage(JsonNode jsonNode) {
		String result;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			CallUsage usage = new CallUsage(jsonNode.get("after").get("usage_in_minutes").asDouble(),
					new Date(jsonNode.get("after").get("date").asLong()),
					jsonNode.get("after").get("user_id").asInt(-1));
			result = objectMapper.writeValueAsString(usage);
		}
		catch (Exception e) {
			e.printStackTrace();
			result = "";
		}
		return result;
	}

	public String translateDataUsage(JsonNode jsonNode) {
		String result;
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			DataUsage usage = new DataUsage(jsonNode.get("after").get("usage_in_bytes").asDouble(),
					new Date(jsonNode.get("after").get("date").asLong()),
					jsonNode.get("after").get("user_id").asInt(-1));
			result = objectMapper.writeValueAsString(usage);
		}
		catch (Exception e) {
			e.printStackTrace();
			result = "";
		}
		return result;
	}

	public String translateOthers(JsonNode jsonNode) {
		return "";
	}
	public JsonNode getJsonNode(String payload) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode;
		try {
			jsonNode = objectMapper.readTree(payload);
		}
		catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
		return jsonNode;
	}
}
