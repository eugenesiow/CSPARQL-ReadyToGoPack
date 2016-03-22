package eu.larkc.csparql.readytogopack.streamer;

import eu.larkc.csparql.cep.api.RdfQuadruple;
import eu.larkc.csparql.cep.api.RdfStream;

public class SRBenchStreamer extends RdfStream implements Runnable {
	
	private boolean keepRunning = false;
	
	public SRBenchStreamer(String iri) {
		super(iri);
	}
	
	public void pleaseStop() {
		keepRunning = false; 
	}

	@Override
	public void run() {
		try { //wait 5s
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		keepRunning = true;
		while (keepRunning) {
			final RdfQuadruple q = new RdfQuadruple("http://myexample.org/1/",
					"http://myexample.org/likes", "http://myexample.org/O", 
					System.currentTimeMillis());
			System.out.println(System.currentTimeMillis());
			this.put(q);
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
