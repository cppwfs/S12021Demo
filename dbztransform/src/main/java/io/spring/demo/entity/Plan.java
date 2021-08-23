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

package io.spring.demo.entity;

public class Plan extends EntityType{

	public static String PLAN_TYPE = "plan";
	private int planId;
	private String planName;
	private double dataPrice;
	private double callPrice;

	public Plan(int planId, String planName, double dataPrice, double callPrice) {
		super(PLAN_TYPE);
		this.planId = planId;
		this.planName = planName;
		this.dataPrice = dataPrice;
		this.callPrice = callPrice;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public double getDataPrice() {
		return dataPrice;
	}

	public void setDataPrice(double dataPrice) {
		this.dataPrice = dataPrice;
	}

	public double getCallPrice() {
		return callPrice;
	}

	public void setCallPrice(double callPrice) {
		this.callPrice = callPrice;
	}
}
