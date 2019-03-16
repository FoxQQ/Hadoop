package markus.kafka.producers;

import java.util.Properties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;

public class simpleProducer {
	public static void main(String[] args) throws Exception{
		String server = "master.myhdp.io",
				port = "6667",
				topic = "test";
		
		
		 Properties props = new Properties();
	      props.put("bootstrap.servers", server+":"+port);
	      props.put("acks", "all");
	      props.put("retries", 0);
	      props.put("batch.size", 16384);
	      props.put("linger.ms", 1);
	      props.put("buffer.memory", 33554432);
	      props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
	      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	    
	      Producer<String, String> producer = new KafkaProducer<String, String>(props);
	      
	      for (int i=0;i<100;i++) {
	    	  producer.send(new ProducerRecord<String, String>(topic,Integer.toString(i),Integer.toString(i) )); 
	    	  try {
	    		  Thread.sleep(1000);
	    	  }
	    	  catch(InterruptedException ex) {

	    		  producer.close();
	    	  }
	    	  
	      }
		  
			 
		  producer.close();
		  
  
	      }
	  	    	  	    
	      
	     
	}

	 
