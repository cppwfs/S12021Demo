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

package io.spring.s1bestcust.config;

import io.spring.s1bestcust.entity.BillEntry;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
@EnableTask
@EnableConfigurationProperties
public class BestCustomerConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;


	@Bean
	public BestCustomerProperties properties() {
		return new BestCustomerProperties();
	}

	@Bean
	public Job readCSVFilesJob(BestCustomerProperties properties) {
		return jobBuilderFactory
				.get("generateBestCustRep")
				.incrementer(new RunIdIncrementer())
				.start(step1(properties))
				.build();
	}

	@Bean
	public Step step1(BestCustomerProperties properties) {
		return stepBuilderFactory.get("step1").<BillEntry, BillEntry>chunk(5)
				.reader(reader(properties))
				.processor(billEntryProcessor())
				.writer(writer(properties))
				.build();
	}

	@Bean
	public FlatFileItemReader<BillEntry> reader(BestCustomerProperties properties)
	{
		//Create reader instance
		FlatFileItemReader<BillEntry> reader = new FlatFileItemReader<BillEntry>();

		//Set input file location
		reader.setResource(new FileSystemResource(properties.getInputFile()));

		//Set number of lines to skips. Use it if file has header rows.
		//reader.setLinesToSkip(1);

		reader.setName("billentryreader");
		reader.setEncoding("UTF-16");

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(",");
		tokenizer.setNames(new String[]{ "id", "firstName", "lastName", "planId", "planName", "callPrice", "dataPrice", "usageInMinutes", "usageInBytes", "dataBillAmount", "callBillAmount", "totalBillAmount" });

		DefaultLineMapper lineMapper = new DefaultLineMapper<BillEntry>();
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(new BeanWrapperFieldSetMapper<BillEntry>() {
					{
						setTargetType(BillEntry.class);
					}
				});
		reader.setLineMapper(lineMapper);
		return reader;
	}

	@Bean
	public ItemProcessor<BillEntry, BillEntry> billEntryProcessor(){
		return new ItemProcessor<BillEntry, BillEntry>() {
			@Override
			public BillEntry process(BillEntry item) throws Exception {
				System.out.println(item);
				if (item.getTotalBillAmount() < 40d) {
					item = null;
				}
				return item;
			}
		};
	}

	@Bean
	public FlatFileItemWriter<BillEntry> writer(BestCustomerProperties properties)
	{
		FlatFileItemWriter<BillEntry> writer = new FlatFileItemWriterBuilder<BillEntry>()
				.resource(new FileSystemResource(properties.getOutputFile()))
				.name("billentrywriter")
				.encoding("UTF-16")
				.saveState(false)
				.shouldDeleteIfEmpty(true)
				.append(true)
				.forceSync(true)
				.shouldDeleteIfEmpty(true)
				.transactional(false)
				.delimited().delimiter(",").names("id", "firstName", "lastName", "planId", "totalBillAmount")
				.build();
				return writer;
	}
}
