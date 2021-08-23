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

package io.spring.demo.entity;

import java.util.Date;

public class CallUsage extends EntityType{

	public static final String CALL_USAGE_TYPE = "callUsageType";

	private double usageInMinutes;

	private Date date;

	private int userId;

	public CallUsage() {
		super(CALL_USAGE_TYPE);
	}

	public CallUsage( double usageInMinutes, Date date, int userId) {
		super(CALL_USAGE_TYPE);
		this.usageInMinutes = usageInMinutes;
		this.date = date;
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getUsageInMinutes() {
		return usageInMinutes;
	}

	public void setUsageInMinutes(double usageInMinutes) {
		this.usageInMinutes = usageInMinutes;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
