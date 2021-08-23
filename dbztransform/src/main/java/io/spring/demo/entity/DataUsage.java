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

public class DataUsage extends EntityType{

	public static final String DATA_USAGE_TYPE = "dataUsageType";

	private double usageInBytes;

	private Date date;

	private int userId;


	public DataUsage() {
		super(DATA_USAGE_TYPE);
	}

	public DataUsage( double usageInBytes, Date date, int userId) {
		super(DATA_USAGE_TYPE);
		this.usageInBytes = usageInBytes;
		this.date = date;
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getUsageInBytes() {
		return usageInBytes;
	}

	public void setUsageInBytes(double usageInBytes) {
		this.usageInBytes = usageInBytes;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
