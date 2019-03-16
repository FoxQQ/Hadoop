package markus.kafka.producers;

public class CreateProducers {
	public static void main(String[] args) {
		CompressorProducerGUI p1 = new CompressorProducerGUI(400,400,"master.myhdp.io","6667","compressor_data");
	}
}
