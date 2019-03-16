package markus.druid;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import org.json.simple.JSONObject;

public class GUI extends JFrame implements ActionListener{

	JButton load, save, generate, newjson;
	JTextArea textarea;
	JSONObject supervisorjson;
	JLabel status;
	GridBagLayout gridbag;
	JScrollPane scrollPane;
	GridBagConstraints c;
	Integer c_rows=1;
	JPanel head,input,content,container ;
	JScrollPane inputscroll;
	
	public GUI(int width, int height) {
		this.setSize(width, height);
		this.setAutoRequestFocus(true);
		this.setTitle("Druid Supervisor Constructor Tool");
		this.setVisible(true);
		supervisorjson = new JSONObject();
		init();
		
	}
	
	public void init() {
		
		head = new JPanel();
		GridBagLayout headgridbag = new GridBagLayout();
		head.setLayout(headgridbag);
		c = new GridBagConstraints();
		c.gridwidth=1;
		c.gridy=0;
		c.weightx=1.0;
		
		//First Row Buttons
		load = new JButton();
		load.setText("Load");
		load.setActionCommand("load");
		load.addActionListener(this);
		newjson = new JButton();
		newjson.setText("New");
		newjson.setActionCommand("newjson");
		newjson.addActionListener(this);
		save = new JButton();
		save.setText("Save");
		save.setActionCommand("save");
		save.addActionListener(this);
		generate = new JButton();
		generate.setText("Generate");
		generate.setActionCommand("generate");
		generate.addActionListener(this);
		headgridbag.setConstraints(newjson, c);
		head.add(newjson);
		headgridbag.setConstraints(load, c);
		head.add(load);
		//c.gridwidth=GridBagConstraints.REMAINDER;		
		headgridbag.setConstraints(save, c);
		head.add(save);
		headgridbag.setConstraints(generate, c);
		head.add(generate);
		
		
		////////////////////////////////////////////////////////////
		
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		textarea = new JTextArea(30,30);
		textarea.setText("");
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		scrollPane = new JScrollPane(textarea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		status = new JLabel();
		status.setText("status");
		
		
		content.add(scrollPane);
		content.add(status);
		
		/////////////////////////////////////////////////////////////////
		input = new JPanel();
		inputscroll = new JScrollPane(input);
		inputscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		gridbag = new GridBagLayout();
		input.setLayout(gridbag);
		
		
		
		JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT,inputscroll,content);
		splitter.setOneTouchExpandable(true);
		splitter.setResizeWeight(0.5);
		splitter.setContinuousLayout(true);
		splitter.getTopComponent().setMinimumSize(new Dimension(50,50));
					
		
		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		head.setAlignmentX(CENTER_ALIGNMENT);
		splitter.setAlignmentX(CENTER_ALIGNMENT);
		container.add(head);
		container.add(splitter);
		//container.add(content);
		
		this.getContentPane().add(container);
		
		
		pack();
		
	
	}
	
	public void addDim() {
		
		c.gridwidth=3;
		c.gridy=c_rows;
		JLabel dimname = new JLabel();
		dimname.setText("New Dim Name:");
		gridbag.setConstraints(dimname, c);
		input.add(dimname);
		JTextField name = new JTextField(10);
		gridbag.setConstraints(name, c);
		input.add(name);
		JLabel dimtype = new JLabel();
		dimtype.setText("Type:");
		gridbag.setConstraints(dimtype, c);
		input.add(dimtype);
	
		String[] dim_types = {"string", "long", "float", "double", "complex"};
		JComboBox type_ = new JComboBox(dim_types);
		type_.setSelectedIndex(0);
		gridbag.setConstraints(type_, c);
		input.add(type_);
		
		JButton plus = new JButton("+");
		plus.addActionListener(this);
		plus.setActionCommand("plus");
		gridbag.setConstraints(plus, c);
		input.add(plus);
	
		container.revalidate();
		container.repaint();
		c_rows++;
		
	}
	
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case "save":
				save();
				break;
			case "load":
				load();
				break;
			case "generate":
				generate();
				break;
			case "newjson":
				newjson();
				break;
			case "plus":
				addDim();
				break;
			default:
				break;		
		}
		// TODO Auto-generated method stub
		
	}

	private void newjson() {
		// TODO Auto-generated method stub
		String topic="MYTOPIC";
		String server="MYSERVER";
		String datasource="MYDATASOURCE";
		String timestampcol = "TIMESTAMPCOL";
		JSONObject dataschema = new JSONObject();
		dataschema.put("dataSource", datasource);
		
		JSONObject parseSpec = new JSONObject();
		JSONObject timestampSpec = new JSONObject();
		timestampSpec.put("column", timestampcol);
		timestampSpec.put("format", "auto");
		JSONObject dimensionsSpec = new JSONObject();
		
		parseSpec.put("format","json");
		parseSpec.put("timestampSpec",timestampSpec);
		parseSpec.put("dimensionsSpec",dimensionsSpec);
		JSONObject parser= new JSONObject();
		parser.put("type","string");
		parser.put("parseSpec", parseSpec);
		
		//String[] metricsSpec = new String[] {""};
		
		JSONObject granularitySpec = new JSONObject();
		granularitySpec.put("type","uniform");
		granularitySpec.put("segmentGranularity","DAY");
		granularitySpec.put("queryGranularity","NONE");
		granularitySpec.put("rollup", false);
		
		
		dataschema.put("parser", parser);
		dataschema.put("metricsSpec", "[]");
		dataschema.put("granularitySpec", granularitySpec);
		
		
		
		JSONObject tuningConfig = new JSONObject();
		tuningConfig.put("type","kafka");
				
		
		JSONObject ioConfig = new JSONObject();
		ioConfig.put("topic",topic);
		ioConfig.put("replicas",1);
		ioConfig.put("taskDuration","PT30M");
		ioConfig.put("completionTimeout","PT60M");
		JSONObject bootstrapserver = new JSONObject();
		bootstrapserver.put("bootstrap.servers",server);
		ioConfig.put("consumerProperties",bootstrapserver);
		
		
				
		supervisorjson.put("type","kafka");
		supervisorjson.put("dataschema",dataschema);
		supervisorjson.put("tuningConfig",tuningConfig);
		supervisorjson.put("ioConfig",ioConfig);
		
		textarea.setText(supervisorjson.toString());
		addDim();
	}
	
	private void generate() {
		// TODO Auto-generated method stub
		System.out.println("generate");
	}

	private void load() {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("json","json");
		final JFileChooser fc = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		fc.setCurrentDirectory(workingDirectory);
		fc.setFileFilter(filter);
		int returnValue = fc.showOpenDialog(this);
		if(returnValue == 0) {
			File file = fc.getSelectedFile();
			status.setText("Opened:" + file.getName());
			String jsonfile="";
			try {
				FileReader reader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(reader);
				String line;
				while((line = bufferedReader.readLine()) != null) {
					jsonfile += line+"\n";
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			textarea.setText(jsonfile);
		}
		
		this.revalidate();
		
	}

	private void save() {
		System.out.println("save");
		
	}

	
}
