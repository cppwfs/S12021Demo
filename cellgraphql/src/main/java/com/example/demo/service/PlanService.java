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

package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dao.entity.Plan;
import com.example.demo.dao.entity.User;
import com.example.demo.dao.repository.PlanRepository;
import com.example.demo.dao.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlanService {
	private final PlanRepository planRepository;

	public PlanService(final PlanRepository planRepository) {
		this.planRepository = planRepository ;
	}

	@Transactional
	public Plan createPlan(final int planId, final String planName, final double dataPrice, final double callPrice) {
		final Plan plan = new Plan(planId, planName, dataPrice, callPrice);
		return this.planRepository.save(plan);
	}

	@Transactional(readOnly = true)
	public List<Plan> getAllPlans(final int count) {
		return this.planRepository.findAll().stream().limit(count).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Optional<Plan> getPlan(final int planId) {
		return this.planRepository.findById(planId);
	}
}
