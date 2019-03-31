package markus.kafka.producers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

public class CompressorProducerSplunkGUI extends JFrame implements ActionListener{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		String server = "localhost",
				port = "8088",
				sourcetype = "httpevent";
				
		
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
		
		public CompressorProducerSplunkGUI(int width, int height, String server, String port, String sourcetype) throws IOException {
			this.server=server;
			this.port=port;
			this.sourcetype=sourcetype;
			this.setSize(width, height);
			this.setAutoRequestFocus(true);
			this.setTitle(this.sn);
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			System.out.println(this.server + ":" + this.port);
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
		
		public void transmit() throws IOException {

		      System.out.println("starting to transmit");
		      //JSONObject compressor = new JSONObject();
		      
		      
		      int i=0;
		      DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd_HH:mm:ss");
		      DateTime dt; 
		      while (true) {
		    	dt = DateTime.now();
		    	String compressor = "";
			    compressor = "serialnumber="+sn;
		  	    compressor+=" timestamp="+fmt.print(dt);
		      	compressor+=" rpm="+new Integer((int)(Math.random()*500)+this.rpm);
		      	
		  	    compressor+=" evaporationtemp="+new Integer((int)(Math.random()*2)+this.evaporationtemp);
		  	    compressor+=" saggastemp="+new Integer((int)(Math.random()*2)+this.saggastemp);
		  	    compressor+=" compressedgastemp="+new Integer((int)(Math.random()*2)+this.compressedgastemp);
		  	    compressor+=" oiltemp="+new Integer((int)(Math.random()*2)+this.oiltemp);
		  	    
		  	    compressor+=" curret="+new Double(Math.round(Math.random()+this.current) * 1d);
		  	    compressor+=" voltage="+new Double(this.voltage);
		  	    compressor+=" coolant="+this.oil;
		  	    compressor+=" oil="+this.coolant;
		  	    
		  	    if(oil<=1) {
		  	    	oil=100.0;
		  	    }
		  	    if(coolant<=1) {
		  	    	coolant=100.0;
		  	    }
		  	    oil = Math.round(oil - Math.random()*oil_multiplier);
		  	    coolant = Math.round(coolant - Math.random()*coolant_multiplier);
		  	    
			  	  String postData = "{\"event\":\""+compressor+"\"}";
	     	      System.out.println(postData);
			      URL url = new URL("http://localhost:8088/services/collector/event");
			      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			      connection.setRequestMethod("POST");
			      connection.setRequestProperty("Authorization", "Splunk aa29f5ee-1461-4abd-8832-3547028575ad");
			      connection.setDoOutput(true);
			      connection.setDoInput(true);
			      connection.setRequestProperty("Content-Length", "" +Integer.toString(postData.getBytes().length));
			      DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			      wr.writeBytes (postData);
			      wr.flush ();
			      wr.close ();
			   
			      int status = connection.getResponseCode();
			      System.out.println(status);
			      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			      String inputline;
			      StringBuffer content = new StringBuffer();
			      while((inputline=in.readLine())!=null) {
			    	  System.out.println(inputline);
			    	  content.append(inputline);
			      }
			      in.close();
			      connection.disconnect();
		   
		  	    
			  	    try{
			  	    	i++;
			  	    	System.out.println("Iteration" + Integer.toString(i));
			  	    	Thread.sleep(500);
			  	    }
			  	    catch(InterruptedException ex){
			  	    	System.out.println(ex);
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
