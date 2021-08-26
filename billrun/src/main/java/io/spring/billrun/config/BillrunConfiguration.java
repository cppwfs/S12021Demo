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

package io.spring.billrun.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.spring.billrun.entity.BillEntry;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;

@EnableBatchProcessing
@EnableTask
@Configuration
public class BillrunConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Bean
	public BillrunProperties properties() {
		return new BillrunProperties();
	}

	@Bean
	public Job billRunJob(BillrunProperties properties, Step step) {
		return jobBuilderFactory
				.get("billRunJob")
				.incrementer(new RunIdIncrementer())
				.start(step)
				.incrementer(new RunIdIncrementer())
				.build();
	}

	@Bean
	public Step step(BillrunProperties properties, ItemReader itemReader,
			ItemProcessor itemProcessor,
			ItemWriter itemWriter) {
		return stepBuilderFactory.get("step1").<BillEntry, BillEntry>chunk(5)
				.reader(itemReader)
				.processor(itemProcessor)
				.writer(itemWriter)
				.build();
	}

	@Bean
	public ItemReader<BillEntry> reader(BillrunProperties properties) {
		return new JdbcCursorItemReaderBuilder<BillEntry>()
				.dataSource(this.dataSource)
				.name("creditReader")
				.sql("select id, first_name, last_name, planid, plan_name, call_price , data_price, sum(usage_in_minutes) as usage_in_minutes, sum(usage_in_bytes) as usage_in_bytes from (select id, first_name, last_name, plans.plan_id as planid , plan_name, call_price ,data_price, usage_in_minutes, 0 as usage_in_bytes, call_usage.date from users, plans, call_usage where users.plan_id = plans.plan_id and users.id = call_usage.user_id UNION select id, first_name, last_name, plans.plan_id as planid, plan_name, call_price, data_price ,0 as usage_in_minutes, usage_in_bytes as use_amount, data_usage.date from users, plans, data_usage where users.plan_id = plans.plan_id and users.id = data_usage.user_id) d group by id, first_name, last_name, planid, plan_name, call_price, data_price")
				.rowMapper(new BillMapper())
				.build();
	}

	@Bean
	public ItemProcessor<BillEntry, BillEntry> itemProcessor() {
		return new ItemProcessor<BillEntry, BillEntry>() {
			@Override
			public BillEntry process(BillEntry item) throws Exception {
				item.setDataBillAmount(Math.round(item.getDataPrice() * item.getUsageInBytes() * 100) / 100);
				item.setCallBillAmount(Math.round(item.getCallPrice() * item.getUsageInMinutes() * 100) / 100);
				item.setTotalBillAmount(item.getDataBillAmount() + item.getCallBillAmount());
				return item;
			}
		};
	}

	@Bean
	public ItemWriter<BillEntry> writer(BillrunProperties properties) {
		return new FlatFileItemWriterBuilder<BillEntry>()
				.resource(new FileSystemResource(properties.getOutputFile()))
				.name("billentrywriter")
				.encoding("UTF-16")
				.saveState(false)
				.shouldDeleteIfEmpty(true)
				.append(true)
				.forceSync(true)
				.shouldDeleteIfEmpty(true)
				.transactional(false)
				.delimited().delimiter(",").names("userId", "firstName", "lastName", "planId", "totalBillAmount")
				.build();
	}

	public class BillMapper implements RowMapper<BillEntry> {
		@Override
		public BillEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
			BillEntry billEntry = new BillEntry();
			billEntry.setUserId(rs.getInt("id"));
			billEntry.setLastName(rs.getString("last_name"));
			billEntry.setFirstName(rs.getString("first_name"));
			billEntry.setPlanId(rs.getInt("planid"));
			billEntry.setCallPrice(rs.getDouble("call_price"));
			billEntry.setDataPrice(rs.getDouble("data_price"));
			billEntry.setUsageInBytes(rs.getInt("usage_in_bytes"));
			billEntry.setUsageInMinutes(rs.getInt("usage_in_minutes"));
			return billEntry;
		}
	}
}
