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

package com.example.demo.mutation;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.example.demo.dao.entity.Plan;
import com.example.demo.service.PlanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanMutation implements GraphQLMutationResolver {

	@Autowired
	private PlanService planService;

	public Plan createPlan(final int planId, final String planName, final double dataPrice, final double callPrice) {
		return this.planService.createPlan(planId, planName, dataPrice, callPrice);
	}
}
