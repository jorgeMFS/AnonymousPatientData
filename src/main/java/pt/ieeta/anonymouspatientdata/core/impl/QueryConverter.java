/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  Conversor is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Conversor is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ieeta.anonymouspatientdata.core.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import pt.ieeta.anonymouspatientdata.core.util.RuntimeIOException;

/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */
public class QueryConverter {

	private AnonDatabase lucy;
	/**
	 * Constructor
	 */
	public QueryConverter(AnonDatabase lucy) {
		this.lucy=lucy;
	}

	/**
	 * @return 
	 * @throws IOException 
	 * 
	 */
	public Query transformQuery(Query q) throws IOException {

		if(q.getClass().isAssignableFrom(TermQuery.class))
		{
			TermQuery tq=(TermQuery) q;

			String fn=tq.getTerm().field();

			if(fn.equals("PatientName")){
				String value=tq.getTerm().text();
				List<String> getvariousmapIdbyPatientName =this.lucy.getvariousmapIdbyPatientName(value);

				if (getvariousmapIdbyPatientName.isEmpty()) return q;
                BooleanClause bClause= new BooleanClause(q, Occur.SHOULD);
                Builder bq = new BooleanQuery.Builder();
                        bq.add(bClause);

				for(String getmapIdbyPatientName:getvariousmapIdbyPatientName ){
                    TermQuery tq2=new TermQuery(new Term(fn, getmapIdbyPatientName));
                    BooleanClause a= new BooleanClause(tq2, Occur.SHOULD);
                    bq.add(new BooleanClause(tq2, Occur.SHOULD));
                }

                BooleanQuery bQ= bq.build();
				return bQ;
			}
			if(fn.equals("PatientID")){
				String value=tq.getTerm().text();
				Optional<String> getmapIdbyPatientId=this.lucy.getmapIdbyPatientId(value);
				if (!getmapIdbyPatientId.isPresent()) return q;
				TermQuery tq2=new TermQuery(new Term(fn,  getmapIdbyPatientId.get()));
				BooleanClause bClause= new BooleanClause(q, Occur.SHOULD);
				BooleanClause bClause2= new BooleanClause(tq2, Occur.SHOULD);
				BooleanQuery bq = new BooleanQuery.Builder().
						add(bClause).add(bClause2)
						.build();

				return (Query) bq;
			}
			if(fn.equals("AccessionNumber")){
				String value=tq.getTerm().text();
				Optional<String>getmapAccessionNumber= this.lucy.getmapAccessionNumber(value);
				if (!getmapAccessionNumber.isPresent()) return q;
				TermQuery tq2=new TermQuery(new Term(fn, getmapAccessionNumber.get()));
				BooleanClause bClause= new BooleanClause(q, Occur.SHOULD);
				BooleanClause bClause2= new BooleanClause(tq2, Occur.SHOULD);
				BooleanQuery bq = new BooleanQuery.Builder()
						.add(bClause).add(bClause2)
					    .build();

				return (Query) bq;
			}
			if(fn.equals("other")){

				String value=tq.getTerm().text();
				Optional<String> getmapIdbyPatientId=this.lucy.getmapIdbyPatientId(value);
                List<String> getvariousmapIdbyPatientName =this.lucy.getvariousmapIdbyPatientName(value);
				Optional<String>getmapAccessionNumber= this.lucy.getmapAccessionNumber(value);
				BooleanClause bClause= new BooleanClause(q, Occur.SHOULD);
				Builder bq = new BooleanQuery.Builder()
						.add(bClause);

				if (getmapIdbyPatientId.isPresent()){
					String pIdValue=getmapIdbyPatientId.get();				
					TermQuery tq2=new TermQuery(new Term("PatientID",pIdValue));
					BooleanClause bClause2= new BooleanClause(tq2, Occur.SHOULD);
					bq.add(bClause2);
				}

				if (!getvariousmapIdbyPatientName.isEmpty()){
                    for(String getmapIdbyPatientName:getvariousmapIdbyPatientName ){
                        TermQuery tq3=new TermQuery(new Term("PatientName",getmapIdbyPatientName));
                        bq.add( new BooleanClause(tq3, Occur.SHOULD));
                    }
				}

				if (getmapAccessionNumber.isPresent()){
					String mapAccessionNumberValue=getmapAccessionNumber.get();
					TermQuery tq4=new TermQuery(new Term("AccessionNumber",mapAccessionNumberValue));
					BooleanClause bClause4= new BooleanClause(tq4, Occur.SHOULD);
					bq.add(bClause4);
				}
				BooleanQuery bq1 = bq.build();
				return bq1;
			}

			return q;
		}


		if(q.getClass().isAssignableFrom(BooleanQuery.class))
		{
			BooleanQuery bq= (BooleanQuery) q;
			List<BooleanClause> bClauseList =bq.clauses();

			Builder bld = new BooleanQuery.Builder();
			try {
				bClauseList.stream()
				.map(cl -> {

					try {
						return new BooleanClause(transformQuery(cl.getQuery()),cl.getOccur());
					} catch (IOException e) {
						throw new RuntimeIOException(e);
					}
				})
				.forEachOrdered(cl -> bld.add(cl));
			} catch (RuntimeIOException e)
			{
				throw e.getCause();
			}
			return bld.build();

		}
		if(q.getClass().isAssignableFrom(PhraseQuery.class))
		{
			PhraseQuery pq= (PhraseQuery) q;
			Term[] pqTerms= pq.getTerms();
			Builder bq = new BooleanQuery.Builder();
			BooleanClause bClause1= new BooleanClause(pq, Occur.SHOULD);
			bq.add(bClause1);


			for (Term t:pqTerms){

				String value=t.text();

				if (t.field().equals("PatientID")){
					Optional<String> getmapIdbyPatientId=this.lucy.getmapIdbyPatientId(value);
					String pIdValue=getmapIdbyPatientId.get();
					if (getmapIdbyPatientId.isPresent()){
						Term term= new Term("PatientID",pIdValue);
						TermQuery tQ=new TermQuery(term);
						BooleanClause bClause= new BooleanClause(tQ, Occur.SHOULD);
						bq.add(bClause);
					}
				}

				if (t.field().equals("PatientName")){
                    List<String> getvariousmapIdbyPatientName =this.lucy.getvariousmapIdbyPatientName(value);
					if (!getvariousmapIdbyPatientName.isEmpty()){
                        for(String getmapIdbyPatientName:getvariousmapIdbyPatientName ) {
                            TermQuery tQ = new TermQuery(new Term("PatientName", getmapIdbyPatientName));
                            bq.add(new BooleanClause(tQ, Occur.SHOULD));
                        }
                    }
				}

				if(t.field().equals("AccessionNumber")){
					Optional<String>getmapAccessionNumber= this.lucy.getmapAccessionNumber(value);
					
					if (getmapAccessionNumber.isPresent()){
						String mapAccessionNumberValue=getmapAccessionNumber.get();
						Term term= new Term("AccessionNumber",mapAccessionNumberValue);
						TermQuery tQ =new TermQuery(term);
						BooleanClause bClause= new BooleanClause(tQ, Occur.SHOULD);
						bq.add(bClause);
					}
				}
				if(t.field().equals("other") ){
					Optional<String> getmapIdbyPatientId=this.lucy.getmapIdbyPatientId(value);
                    List<String> getvariousmapIdbyPatientName =this.lucy.getvariousmapIdbyPatientName(value);
					Optional<String>getmapAccessionNumber= this.lucy.getmapAccessionNumber(value);

					
					if (getmapIdbyPatientId.isPresent()){
						String pIdValue=getmapIdbyPatientId.get();
						Term term= new Term("PatientID",pIdValue);
						TermQuery tQ=new TermQuery(term);
						BooleanClause bClause= new BooleanClause(tQ, Occur.SHOULD);
						bq.add(bClause);
					}
                    if (!getvariousmapIdbyPatientName.isEmpty()){
                        for(String getmapIdbyPatientName:getvariousmapIdbyPatientName ){
                            TermQuery tQ =new TermQuery(new Term("PatientName",getmapIdbyPatientName));
                            bq.add(new BooleanClause(tQ, Occur.SHOULD));
                        }
                    }

					if (getmapAccessionNumber.isPresent()){
						String mapAccessionNumberValue=getmapAccessionNumber.get();
						TermQuery tQ =new TermQuery(new Term("AccessionNumber",mapAccessionNumberValue));
						bq.add(new BooleanClause(tQ, Occur.SHOULD));
					}
				}
			}
			BooleanQuery bq1 = bq.build();
			return bq1;
		}
		return q;
	}

	@Override
	public String toString() {
		return "QueryConverter [lucy=" + lucy + "]";
	}
	
	
	
}
