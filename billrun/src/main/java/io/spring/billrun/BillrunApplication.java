package io.spring.billrun;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableTask
@EnableBatchProcessing
public class BillrunApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillrunApplication.class, args);
	}

	@Bean
	public ItemProcessor<Map<String, Object>, Map<String, Object>> itemProcessor() {
		return new ItemProcessor<Map<String, Object>, Map<String, Object>>() {
			@Override
			public Map<String,Object> process(Map<String, Object> item) throws Exception {
				Map<String, Object> result = new HashMap<>(item);
				double dataBillAmount = Math.round(((double)item.get("usage_in_bytes") * (float)item.get("data_price"))*100)/100.0;
				double callBillAmount = Math.round(((double)item.get("usage_in_minutes") * (float)item.get("call_price"))*100)/100.0;
				result.put("data_bill_amount", dataBillAmount);
				result.put("call_bill_amount", callBillAmount);
				result.put("total_bill_amount", dataBillAmount + callBillAmount);
				return result;
			}
		};
	}
}
