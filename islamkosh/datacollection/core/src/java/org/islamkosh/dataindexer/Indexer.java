package org.islamkosh.dataindexer;

import java.util.ArrayList;

import org.islamkosh.metadata.Metadata;

public interface Indexer {	
	public boolean indexData(ArrayList<Metadata> metadataCollection);
}
