/*
 * Copyright 2017 the original author or authors.
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

package com.example.demo.query;

import java.util.List;
import java.util.Optional;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.demo.dao.entity.Plan;
import com.example.demo.dao.entity.User;
import com.example.demo.service.PlanService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Queries implements GraphQLQueryResolver {

	@Autowired
	private UserService userService;

	@Autowired
	private PlanService planService;

	public List<User> getUsers(final int count) {
		return this.userService.getAllUsers(count);
	}

	public Optional<User> getUser(final int id) {
		return this.userService.getUser(id);
	}

	public List<Plan> getPlans(final int count) {
		return this.planService.getAllPlans(count);
	}

	public Optional<Plan> getPlan(final int planId) {
		return this.planService.getPlan(planId);
	}
}
