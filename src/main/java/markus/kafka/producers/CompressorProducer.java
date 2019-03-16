package markus.kafka.producers;

import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;

public class CompressorProducer {
	public static void main(String[] args) throws Exception{
		String server = "master.myhdp.io",
				port = "6667",
				topic = "compressor_data";
		
		if(args.length>0) {
			topic = args[0];
		}
		
		 Properties props = new Properties();
	      props.put("bootstrap.servers", server+":"+port);
	      props.put("acks", "all");
	      props.put("retries", 0);
	      props.put("batch.size", 16384);
	      props.put("linger.ms", 1);
	      props.put("buffer.memory", 33554432);
	      props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
	      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	    
	      JSONObject compressor = new JSONObject();
	      String sn = Integer.toString( (int)((Math.random()*10000000)+1000000));
	  		
	      compressor.put("serialnumber", sn);
	     
	      double oil=100.0,
	      		coolant=100.0;
	      Producer<String, String> producer = new KafkaProducer<String, String>(props);
	      int i=0;
	      DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	      DateTime dt; 
	      while (true) {
	    	dt = DateTime.now();
	  	    compressor.put("timestamp", fmt.print(dt));
	      	compressor.put("rpm", new Integer((int)(Math.random()*500)+990));
	  	    compressor.put("evaporationtemp", new Integer((int)(Math.random()*2)+100));
	  	    compressor.put("saggastemp", new Integer((int)(Math.random()*2)+80));
	  	    compressor.put("compressedgastemp", new Integer((int)(Math.random()*2)+90));
	  	    compressor.put("oiltemp", new Integer((int)(Math.random()*2)+120));
	  	    
	  	    compressor.put("curret", new Double(Math.round(Math.random()+5.3) * 1d));
	  	    compressor.put("voltage", new Double(220.0));
	  	    compressor.put("coolant", oil);
	  	    compressor.put("oil", coolant);
	  	    if(oil<=1) {
	  	    	oil=100.0;
	  	    }
	  	    if(coolant<=1) {
	  	    	coolant=100.0;
	  	    }
	  	    oil = oil - Math.random()*2;
	  	    coolant = coolant - Math.random();
	  	    System.out.println(compressor);
	  	    
	  	    try{
		  	    producer.send(new ProducerRecord<String, String>(topic,Integer.toString(i), compressor.toString()));
		  	    i++;
	  	    	System.out.println("Iteration" + Integer.toString(i));
	  	    	Thread.sleep(5000);
	  	    }
	  	    catch(InterruptedException ex){
	  	    	System.out.println(ex);

	  	    	producer.close();
	  	    }
	  	    	
	  	    
	      	
	      }
	     
	}

	 
	
}
