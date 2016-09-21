package index;

import anonymizer.Anonymizer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 *
 * @author Carlos
 */
public class IndexHandler
{
    private Directory originalIndex;
    private Directory destinationIndex;
    private Analyzer analyzer;

    public IndexHandler(File originalIndexFile, File destinationIndex)
    {
        try
        {
            this.analyzer = new StandardAnalyzer(Version.LUCENE_30);
            this.originalIndex = FSDirectory.open(originalIndexFile);
            if (!destinationIndex.exists())
            {
                destinationIndex.mkdir();
            }
            this.destinationIndex = FSDirectory.open(destinationIndex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(IndexHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void indexDocuments(List<Document> docs)
    {
        System.out.println("Sent to index: " + docs.size());
        IndexWriter writer = null;
        try
        {
            if (!IndexReader.indexExists(destinationIndex))
            {
                writer = new IndexWriter(this.destinationIndex, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
            } else
            {
                writer = new IndexWriter(this.destinationIndex, analyzer, false, IndexWriter.MaxFieldLength.UNLIMITED);
            }
            for (Document doc : docs)
            {
                writer.addDocument(doc);
            }
            writer.commit();
        }
        catch (LockObtainFailedException ex)
        {
            Logger.getLogger(IndexHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(IndexHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(IndexHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void anonymizeIndex(Anonymizer anonymizer, int sleepingTime) throws IOException
    {
        IndexReader indexReader = IndexReader.open(this.originalIndex);
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < indexReader.maxDoc(); i++)
        {
            if (!indexReader.isDeleted(i))
            {
                documents.add(anonymizer.anonymize(indexReader.document(i)));
            }
            if (i % 1000 == 999)
            {
                this.indexDocuments(documents);
                documents.clear();
            }
            if (sleepingTime > 0)
            {
                try
                {
                    Thread.sleep(sleepingTime);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(IndexHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        indexReader.close();
        this.indexDocuments(documents);
    }

    public void testFinalIndex() throws IOException
    {
        IndexReader indexReaderFinal = IndexReader.open(this.destinationIndex);
        System.out.println("Number of documents copied : " + indexReaderFinal.numDocs());
        IndexReader indexReaderOriginal = IndexReader.open(this.originalIndex);
        System.out.println("Number of documents original : " + indexReaderOriginal.numDocs());
        this.printIndex();
    }
    
    private void printIndex() throws IOException
    {
        IndexReader indexReader = IndexReader.open(this.destinationIndex);
        for(int i = 0; i< indexReader.maxDoc(); i++)
        {
            System.out.println("**************************** Document " + i+"***************************");
            Document d = indexReader.document(i);
            List<Fieldable> fields = d.getFields();
            
            for(Fieldable f : fields)
            {
                System.out.printf("%s : %s\n", f.name(), f.stringValue());
            }
            
        }
    }

    /*   public void getTerms() throws IOException
     {
     IndexReader indexReader = IndexReader.open(originalIndex);
     TermEnum termEnum = indexReader.terms();
     while (termEnum.next())
     {
     Term term = termEnum.term();
     System.out.println(term.text());
     }
     indexReader.close();
     }

     public void getFields() throws IOException
     {
     IndexReader indexReader = IndexReader.open(this.originalIndex);
     Collection<String> fields = indexReader.getFieldNames(IndexReader.FieldOption.ALL);
     for (String st : fields)
     {
     System.out.println(st);
     }
     indexReader.close();
     }

     public void getFirst() throws IOException, ParseException
     {
     IndexReader indexReader = IndexReader.open(this.originalIndex);
     System.out.println(indexReader.maxDoc());
     // indexReader.document(1);
     for(int i = 0; i< hits.length; i++)
     {
     Document hitDoc = searcher.doc(hits[i].doc);
            
     }
     }

     public void cloneIndex() throws IOException
     {
     IndexReader indexReader = IndexReader.open(this.originalIndex);
     List<Document> documents = new ArrayList<>();
     for (int i = 0; i < indexReader.maxDoc(); i++)
     {
     if (!indexReader.isDeleted(i))
     {
     documents.add(indexReader.document(i));
     }
     if (i % 1000 == 999)
     {
     this.indexDocuments(documents);
     documents.clear();
     }
     }
     indexReader.close();
     this.indexDocuments(documents);
     }*/
    /*public void search() throws IOException
     {
     IndexSearcher searcher = new IndexSearcher(originalIndex);
     //QueryParser parser = new QueryParser(Version.LUCENE_30, "fileName", this.analyzer);
     TermEnum termEnum = this.destinationIndex.terms();
     while (termEnum.next())
     {
     Term term = termEnum.term();
     System.out.println(term.text());
     }
     termEnum.close();
     indexReader.close();
     }*/
}
