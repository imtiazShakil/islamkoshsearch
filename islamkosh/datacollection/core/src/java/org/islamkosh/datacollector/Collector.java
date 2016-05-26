package org.islamkosh.datacollector;

import java.util.ArrayList;

import org.islamkosh.metadata.Metadata;

public interface Collector {	
	public ArrayList<Metadata> collectData();
}
