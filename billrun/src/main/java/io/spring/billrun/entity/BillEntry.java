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

package io.spring.billrun.entity;

public class BillEntry {
	private int userId;

	private String firstName;

	private String lastName;

	private int planId;

	private String planName;

	private double callPrice;

	private double dataPrice;

	private int usageInMinutes;

	private int usageInBytes;

	private double dataBillAmount;

	private double callBillAmount;

	private double totalBillAmount;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public double getCallPrice() {
		return callPrice;
	}

	public void setCallPrice(double callPrice) {
		this.callPrice = callPrice;
	}

	public double getDataPrice() {
		return dataPrice;
	}

	public void setDataPrice(double dataPrice) {
		this.dataPrice = dataPrice;
	}

	public int getUsageInMinutes() {
		return usageInMinutes;
	}

	public void setUsageInMinutes(int usageInMinutes) {
		this.usageInMinutes = usageInMinutes;
	}

	public int getUsageInBytes() {
		return usageInBytes;
	}

	public void setUsageInBytes(int usageInBytes) {
		this.usageInBytes = usageInBytes;
	}

	public double getDataBillAmount() {
		return dataBillAmount;
	}

	public void setDataBillAmount(double dataBillAmount) {
		this.dataBillAmount = dataBillAmount;
	}

	public double getCallBillAmount() {
		return callBillAmount;
	}

	public void setCallBillAmount(double callBillAmount) {
		this.callBillAmount = callBillAmount;
	}

	public double getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}
}
