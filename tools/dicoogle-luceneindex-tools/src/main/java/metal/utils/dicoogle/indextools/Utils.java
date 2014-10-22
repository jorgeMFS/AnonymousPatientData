package metal.utils.dicoogle.indextools;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Utils {

	public static IndexReader openIndexReader(String repPath) throws IOException{
		
		File fRepository = new File(repPath);		        
		
		IndexReader reader = null;
			//opening reader with directory!!
		Directory dir = FSDirectory.open(fRepository);
		reader = IndexReader.open(dir);
			
		return reader;
	}
	
	public static IndexWriter openIndexWriter(String repPath) throws IOException{
		
		File fRep = new File(repPath);
		
		Directory wrDir = FSDirectory.open(fRep);
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		IndexWriter wr = new IndexWriter(wrDir, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);  			
		
		return wr;
	}
	
}
