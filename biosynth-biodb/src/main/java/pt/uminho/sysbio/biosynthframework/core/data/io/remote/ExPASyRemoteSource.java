package pt.uminho.sysbio.biosynthframework.core.data.io.remote;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import pt.uminho.sysbio.biosynthframework.GenericEnzyme;
import pt.uminho.sysbio.biosynthframework.GenericMetabolite;
import pt.uminho.sysbio.biosynthframework.GenericReaction;
import pt.uminho.sysbio.biosynthframework.GenericReactionPair;
import pt.uminho.sysbio.biosynthframework.core.data.io.IRemoteSource;
import pt.uminho.sysbio.biosynthframework.core.data.io.http.HttpRequest;
import pt.uminho.sysbio.biosynthframework.core.data.io.parser.swissprot.ExPASyEnzymeFlatFileParser;

@Deprecated
public class ExPASyRemoteSource implements IRemoteSource{

	public static boolean VERBOSE = false;
	
	@Deprecated
	public TreeSet<String> getCompoundReactions(String cpdId) {
		return null;
	}

	@Override
	public GenericReaction getReactionInformation(String rxnId) {
		return null;
	}
	@Override
	public GenericMetabolite getMetaboliteInformation(String cpdId) {
		return null;
	}
	@Override
	public GenericEnzyme getEnzymeInformation(String ecId) {
		if (VERBOSE) System.out.println("ExPASyRemoteSource::getEnzymeInformation - " + ecId);
		
		String flatfile = null;
		try {
			flatfile = HttpRequest.get("http://enzyme.expasy.org/EC/" + ecId + ".txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if ( flatfile == null) return null;
		ExPASyEnzymeFlatFileParser parser = new ExPASyEnzymeFlatFileParser(flatfile);
		
		GenericEnzyme ecn = null;
		ecn = new GenericEnzyme(parser.getId());
		ecn.setName( parser.getName());
		
//		List<String> entryList = new ArrayList<String> ( parser.getGeneEntrys());
//	    Query query = UniProtQueryBuilder.buildIDListQuery( entryList);
//	    UniProtQueryService uniProtQueryService = UniProtJAPI.factory.getUniProtQueryService();
//	    EntryIterator<UniProtEntry> entries = uniProtQueryService.getEntryIterator(query);
//		
//	    for (UniProtEntry entry : entries) {
//	    	ecn.addOrganims(entry.getNcbiTaxonomyIds().get(0).getValue() , entry.getUniProtId().toString());
//	    }
		return ecn;
	}
	@Override
	public GenericReactionPair getPairInformation(String rprId) {
		return null;
	}

	@Override
	public Set<String> getAllReactionIds() {
		Set<String> empty = new HashSet<String> ();
		return empty;
	}
	@Override
	public Set<String> getAllMetabolitesIds() {
		Set<String> empty = new HashSet<String> ();
		return empty;
	}
	@Override
	public Set<String> getAllEnzymeIds() {
		// TODO WORK HERE !
		return null;
	}
	@Override
	public Set<String> getAllReactionPairIds() {
		Set<String> empty = new HashSet<String> ();
		return empty;
	}

	@Override
	public void initialize() {

	}

}
