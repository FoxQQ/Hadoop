package markus.kafka.producers;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;
public class JSONTester {
public static void main(String[] args) throws InterruptedException {
	System.out.println(args[0]);
	System.out.println("Started");
	  JSONObject compressor = new JSONObject();

	String sn = Integer.toString( (int)((Math.random()*10000000)+1000000));
	
    compressor.put("serialnumber", sn);
   
    double oil=100.0,
    		coolant=100.0;
    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    DateTime dt; 
    while (true) {
    	compressor.put("rpm", new Integer((int)(Math.random()*20)+990));
	    compressor.put("evaporationtemp", new Integer((int)(Math.random()*2)+80));
	    compressor.put("saggastemp", new Integer((int)(Math.random()*2)+80));
	    compressor.put("compressedgastemp", new Integer((int)(Math.random()*2)+80));
	    compressor.put("oiltemp", new Integer((int)(Math.random()*2)+80));
	    
	    compressor.put("curret", new Double(Math.round(Math.random()+5.3) * 1d));
	    compressor.put("voltage", new Double(220.0));
	    compressor.put("coolant", oil);
	    compressor.put("oil", coolant);
	    dt = DateTime.now();
	    
	    
	    compressor.put("timestamp", fmt.print(dt));
	    if(oil<=1) {
	    	oil=100.0;
	    }
	    if(coolant<=1) {
	    	coolant=100.0;
	    }
	    oil = oil - Math.random()*0.3;
	    coolant = coolant - Math.random();
	    System.out.println(compressor.toString());
	    Thread.sleep(5000);
	    
    	
    }
    
    
	
}
}
