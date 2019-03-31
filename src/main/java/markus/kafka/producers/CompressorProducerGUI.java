package markus.kafka.producers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;

public class CompressorProducerGUI extends JFrame implements ActionListener{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		String server = "master.myhdp.io",
				port = "6667",
				topic = "compressor_data";
		
		String sn = Integer.toString( (int)((Math.random()*10000000)+1000000));
		 
		int rpm=990,
			evaporationtemp=100,
					saggastemp=80,
					compressedgastemp=90,
					oiltemp=70;
		double current=5.3,
					voltage=220.0,
					oil=100.0,
					coolant=100.0,
					oil_multiplier=2.0,
					coolant_multiplier=1.0;

		JTextField tf_rpm,tf_evaporationtemp, tf_saggastemp, tf_compressedgastemp, tf_oiltemp, tf_current, tf_voltage, tf_oil_multiplier, tf_coolant_multiplier;
		
		public CompressorProducerGUI(int width, int height, String server, String port, String topic) {
			this.server=server;
			this.port=port;
			this.topic=topic;
			this.setSize(width, height);
			this.setAutoRequestFocus(true);
			this.setTitle(this.sn);
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			System.out.println(this.server + ":" + this.port +" - " +this.topic);
			init();
			transmit();
		}
		
		public void init() {
			GridBagLayout gridbag=new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			this.getContentPane().setLayout(gridbag);
			int rows=0;
			
			c.weightx=1;
			c.weighty=1;
			JLabel label_sn = new JLabel("Serialnumber: " + this.sn);
			gridbag.setConstraints(label_sn, c);
			this.getContentPane().add(label_sn);
			
			rows++;
			c.gridy=rows;
			JLabel label_rpm = new JLabel("rpm");
			gridbag.setConstraints(label_rpm, c);
			this.getContentPane().add(label_rpm);
			tf_rpm = new JTextField(Integer.toString(this.rpm), 10);
			gridbag.setConstraints(tf_rpm, c);
			this.getContentPane().add(tf_rpm);
			
			rows++;
			c.gridy=rows;
			JLabel label_saggastemp= new JLabel("saggastemp");
			gridbag.setConstraints(label_saggastemp, c);
			this.getContentPane().add(label_saggastemp);
			tf_saggastemp = new JTextField(Integer.toString(this.saggastemp), 10);
			gridbag.setConstraints(tf_saggastemp, c);
			this.getContentPane().add(tf_saggastemp);
			
			
			
			rows++;
			c.gridy=rows;
			JLabel label_evaporationtemp= new JLabel("evaporationtemp");
			gridbag.setConstraints(label_evaporationtemp, c);
			this.getContentPane().add(label_evaporationtemp);
			tf_evaporationtemp = new JTextField(Integer.toString(this.evaporationtemp), 10);
			gridbag.setConstraints(tf_evaporationtemp, c);
			this.getContentPane().add(tf_evaporationtemp);

			rows++;
			c.gridy=rows;
			JLabel label_compressedgastemp= new JLabel("compressedgastemp");
			gridbag.setConstraints(label_compressedgastemp, c);
			this.getContentPane().add(label_compressedgastemp);
			tf_compressedgastemp = new JTextField(Integer.toString(this.compressedgastemp), 10);
			gridbag.setConstraints(tf_compressedgastemp, c);
			this.getContentPane().add(tf_compressedgastemp);
			
			rows++;
			c.gridy=rows;
			JLabel label_oiltemp= new JLabel("oiltemp");
			gridbag.setConstraints(label_oiltemp, c);
			this.getContentPane().add(label_oiltemp);
			tf_oiltemp = new JTextField(Integer.toString(this.oiltemp), 10);
			gridbag.setConstraints(tf_oiltemp, c);
			this.getContentPane().add(tf_oiltemp);
			
			rows++;
			c.gridy=rows;
			JLabel label_oil_multiplier= new JLabel("oil_multiplier");
			gridbag.setConstraints(label_oil_multiplier, c);
			this.getContentPane().add(label_oil_multiplier);
			tf_oil_multiplier = new JTextField(Double.toString(this.oil_multiplier), 10);
			gridbag.setConstraints(tf_oil_multiplier, c);
			this.getContentPane().add(tf_oil_multiplier);
			
			rows++;
			c.gridy=rows;
			JLabel label_coolant_multiplier= new JLabel("coolant_multiplier");
			gridbag.setConstraints(label_coolant_multiplier, c);
			this.getContentPane().add(label_coolant_multiplier);
			tf_coolant_multiplier = new JTextField(Double.toString(this.coolant_multiplier), 10);
			gridbag.setConstraints(tf_coolant_multiplier, c);
			this.getContentPane().add(tf_coolant_multiplier);

			
			rows++;
			c.gridy=rows;
			JButton submit = new JButton("Submit");
			submit.addActionListener(this);
			submit.setActionCommand("submit");
			gridbag.setConstraints(submit, c);
			this.getContentPane().add(submit);
		
			
			this.revalidate();
		}
		
		public void transmit() {
			Properties props = new Properties();
		      props.put("bootstrap.servers", this.server+":"+this.port);
		      props.put("acks", "all");
		      props.put("retries", 0);
		      props.put("batch.size", 16384);
		      props.put("linger.ms", 1);
		      props.put("buffer.memory", 33554432);
		      props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
		      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		    
		      JSONObject compressor = new JSONObject();
		     		  		
		      compressor.put("serialnumber", sn);
		     
		      Producer<String, String> producer = new KafkaProducer<String, String>(props);
		      int i=0;
		      DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		      DateTime dt; 
		      while (true) {
		    	dt = DateTime.now();
		  	    compressor.put("timestamp", fmt.print(dt));
		      	compressor.put("rpm", new Integer((int)(Math.random()*500)+this.rpm));
		  	    compressor.put("evaporationtemp", new Integer((int)(Math.random()*2)+this.evaporationtemp));
		  	    compressor.put("saggastemp", new Integer((int)(Math.random()*2)+this.saggastemp));
		  	    compressor.put("compressedgastemp", new Integer((int)(Math.random()*2)+this.compressedgastemp));
		  	    compressor.put("oiltemp", new Integer((int)(Math.random()*2)+this.oiltemp));
		  	    
		  	    compressor.put("curret", new Double(Math.round(Math.random()+this.current) * 1d));
		  	    compressor.put("voltage", new Double(this.voltage));
		  	    compressor.put("coolant", this.oil);
		  	    compressor.put("oil", this.coolant);
		  	    if(oil<=1) {
		  	    	oil=100.0;
		  	    }
		  	    if(coolant<=1) {
		  	    	coolant=100.0;
		  	    }
		  	    oil = Math.round(oil - Math.random()*oil_multiplier);
		  	    coolant = Math.round(coolant - Math.random()*coolant_multiplier);
		  	    System.out.println(coolant);
		  	    
		  	    try{
			  	    producer.send(new ProducerRecord<String, String>(this.topic,Integer.toString(i), compressor.toString()));
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
		
		 		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getActionCommand()=="submit") {
				submit();
			}
		}

	
		 private void submit() {
					// TODO Auto-generated method stub
			 try {
					this.rpm=Integer.parseInt(this.tf_rpm.getText()); 
					this.evaporationtemp=Integer.parseInt(this.tf_evaporationtemp.getText()); 
					this.saggastemp=Integer.parseInt(this.tf_saggastemp.getText()); 
					this.compressedgastemp=Integer.parseInt(this.tf_compressedgastemp.getText()); 
					this.oiltemp=Integer.parseInt(this.tf_oiltemp.getText()); 
					this.oil_multiplier=Double.parseDouble(this.tf_oil_multiplier.getText()); 
					this.coolant_multiplier=Double.parseDouble(this.tf_coolant_multiplier.getText()); 
					
			 }				
			catch (Exception e){
					System.out.println(e);
			}
		}

}
