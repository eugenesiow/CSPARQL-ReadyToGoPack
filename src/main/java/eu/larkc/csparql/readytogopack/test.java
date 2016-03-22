package eu.larkc.csparql.readytogopack;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.commons.io.FileUtils;

import eu.larkc.csparql.cep.api.RdfStream;
import eu.larkc.csparql.core.engine.CsparqlEngine;
import eu.larkc.csparql.core.engine.CsparqlEngineImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;
import eu.larkc.csparql.readytogopack.streamer.SRBenchStreamer;

public class test {
	public static void main(String[] args) {
		String fileName = "queries/q1.sparql";
        if (args.length > 0) {
        	fileName = args[0];
        }
        File queryFile = new File(fileName);
        String query = null;
        try {
        	query = FileUtils.readFileToString(queryFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		// initializations

		//		String streamURI = "http://myexample.org/stream";
		RdfStream tg = null;

		// Initialize C-SPARQL Engine
		CsparqlEngine engine = new CsparqlEngineImpl();

		engine.initialize(true);
		
		query = "REGISTER QUERY HowManyUsersLikeTheSameObj AS "
				+ "PREFIX ex: <http://myexample.org/> "
				+ "SELECT * "
				+ "FROM STREAM <http://www.cwi.nl/SRBench/observations> [RANGE 5s STEP 1ms] "
				+ "WHERE { ?s ?p ?o "
				+ "} ";
		tg = new SRBenchStreamer("http://www.cwi.nl/SRBench/observations");
		
		// Register an RDF Stream

		engine.registerStream(tg);

		// Start Streaming (this is only needed for the example, normally streams are external
		// C-SPARQL Engine users are supposed to write their own adapters to create RDF streams

		final Thread t = new Thread((Runnable) tg);
		t.start();

		// Register a C-SPARQL query

		CsparqlQueryResultProxy c1 = null;
	
		try {
			c1 = engine.registerQuery(query, false);
			System.out.println("Query: "+ query);
			System.out.println("Query Start Time : " + System.currentTimeMillis());
		} catch (final ParseException ex) {
			ex.printStackTrace();
		}

		// Attach a Result Formatter to the query result proxy

		if (c1 != null) {
			c1.addObserver(new BenchmarkFormatter());
		}

		// leave the system running for a while
		// normally the C-SPARQL Engine should be left running
		// the following code shows how to stop the C-SPARQL Engine gracefully
		try {
			Thread.sleep(200000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
		// clean up (i.e., unregister query and stream)
		engine.unregisterQuery(c1.getId());

		((SRBenchStreamer) tg).pleaseStop();

		engine.unregisterStream(tg.getIRI());

		System.exit(0);
	}
}
