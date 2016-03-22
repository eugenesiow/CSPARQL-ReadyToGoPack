package eu.larkc.csparql.readytogopack;

import java.util.Observable;

import eu.larkc.csparql.common.RDFTable;
import eu.larkc.csparql.common.RDFTuple;
import eu.larkc.csparql.core.ResultFormatter;

public class BenchmarkFormatter extends ResultFormatter {

	@Override
	public void update(Observable o, Object arg) {
		
		RDFTable q = (RDFTable) arg;
		
		System.out.println();
		System.out.println("-------"+ q.size() + " results at SystemTime=["+System.currentTimeMillis()+"]--------");
		for (final RDFTuple t : q) {
			System.out.println(t.toString());
		}
		System.out.println();
		
	}

}
