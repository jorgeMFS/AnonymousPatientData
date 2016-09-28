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

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import pt.ieeta.anonymouspatientdata.core.util.RuntimeIOException;

import org.apache.lucene.search.BooleanClause.Occur;

/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */
public class Conversor {

	private PersistantDataLucene Lucy=new PersistantDataLucene();
	/**
	 * Constructor
	 */
	public Conversor() {
		
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
				TermQuery tq2=new TermQuery(new Term(fn, this.Lucy.getmapIdbyPatientName(value)));
				BooleanClause bClause= new BooleanClause(q, Occur.SHOULD);
				BooleanClause bClause2= new BooleanClause(tq2, Occur.SHOULD);
				BooleanQuery bq = new BooleanQuery.Builder()
						.add(bClause).add(bClause2)
						.setMinimumNumberShouldMatch(1)
						.build();

				return bq;
			}
			if(fn.equals("PatientId")){
				String value=tq.getTerm().text();
				TermQuery tq2=new TermQuery(new Term(fn,  this.Lucy.getmapIdbyPatientId(value)));
				BooleanClause bClause= new BooleanClause(q, Occur.SHOULD);
				BooleanClause bClause2= new BooleanClause(tq2, Occur.SHOULD);
				BooleanQuery bq = new BooleanQuery.Builder().
						add(bClause).add(bClause2)
						.setMinimumNumberShouldMatch(1)
						.build();

				return (Query) bq;
			}
			if(fn.equals("AccessionNumber")){
				String value=tq.getTerm().text();
				TermQuery tq2=new TermQuery(new Term(fn, this.Lucy.getmapAccessionNumber(value)));
				BooleanClause bClause= new BooleanClause(q, Occur.SHOULD);
				BooleanClause bClause2= new BooleanClause(tq2, Occur.SHOULD);
				BooleanQuery bq = new BooleanQuery.Builder()
						.add(bClause).add(bClause2)
						.setMinimumNumberShouldMatch(1)
						.build();

				return (Query) bq;
			}
			if(fn.equals("other")){
				String value=tq.getTerm().text();
				String pIdValue=this.Lucy.getmapIdbyPatientId(value);
				String PatientNameValue=this.Lucy.getmapIdbyPatientName(value);
				String mapAccessionNumberValue=this.Lucy.getmapAccessionNumber(value);
				TermQuery tq2=new TermQuery(new Term("PatientId",pIdValue));
				TermQuery tq3=new TermQuery(new Term("PatientName",PatientNameValue));
				TermQuery tq4=new TermQuery(new Term("AccessionNumber",mapAccessionNumberValue));
				BooleanClause bClause= new BooleanClause(q, Occur.SHOULD);
				Builder bq = new BooleanQuery.Builder()
						.add(bClause);

				if (pIdValue!= null){
					BooleanClause bClause2= new BooleanClause(tq2, Occur.SHOULD);
					bq.add(bClause2);
				}

				if (PatientNameValue!= null){
					BooleanClause bClause3= new BooleanClause(tq3, Occur.SHOULD);
					bq.add(bClause3);
				}

				if (mapAccessionNumberValue!= null){
					BooleanClause bClause4= new BooleanClause(tq4, Occur.SHOULD);
					bq.add(bClause4);
				}
				BooleanQuery bq1 = bq.setMinimumNumberShouldMatch(1)
						.build();
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

			for (Term t:pqTerms){
				String value=t.text();
				String pIdValue=this.Lucy.getmapIdbyPatientId(value);
				String PatientNameValue=this.Lucy.getmapIdbyPatientName(value);
				String mapAccessionNumberValue=this.Lucy.getmapAccessionNumber(value);

				if (pIdValue!= null){
					Term term= new Term("PatientId",pIdValue);
					TermQuery tQ=new TermQuery(term);
					BooleanClause bClause= new BooleanClause(tQ, Occur.SHOULD);
					bq.add(bClause);
				}

				if (PatientNameValue!= null){
					Term term= new Term("AccessionNumber",PatientNameValue);
					TermQuery tQ =new TermQuery(term);
					BooleanClause bClause= new BooleanClause(tQ, Occur.SHOULD);
					bq.add(bClause);
				}


				if (mapAccessionNumberValue!= null){
					Term term= new Term("AccessionNumber",mapAccessionNumberValue);
					TermQuery tQ =new TermQuery(term);
					BooleanClause bClause= new BooleanClause(tQ, Occur.SHOULD);
					bq.add(bClause);
				}

			}
			BooleanQuery bq1 = bq.setMinimumNumberShouldMatch(1)
					.build();
			return bq1;
		}
		return q;
	}


































}
