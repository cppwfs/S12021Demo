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

import com.example.demo.dao.entity.User;
import com.example.demo.dao.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	private final UserRepository userRepository ;

	public UserService(final UserRepository userRepository) {
		this.userRepository = userRepository ;
	}

	@Transactional
	public User createUser(final int id, final String firstName, final String lastName, final int planId) {
		final User user = new User(id, firstName, lastName, planId);
		return this.userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public List<User> getAllUsers(final int count) {
		return this.userRepository.findAll().stream().limit(count).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Optional<User> getUser(final int id) {
		return this.userRepository.findById(id);
	}
}
