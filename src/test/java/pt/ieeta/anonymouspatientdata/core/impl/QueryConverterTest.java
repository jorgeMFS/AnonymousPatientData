/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  QueryConverterTest is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  QueryConverterTest is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ieeta.anonymouspatientdata.core.impl;

import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
/**
 * @author Jorge Miguel Ferreira da Silva
 *
 */
public class QueryConverterTest {

	Map<String,PatientData> patientDataMap;
	Map<String, String> studyDataMap;
	PatientData Pd;

	@Mock
	AnonDatabase Anon;

	@Rule 
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before 
	public void create()throws IOException{
		Anon =Mockito.mock(AnonDatabase.class);
		
		when(Anon.getmapAccessionNumber(anyString())).thenReturn("321");
		when(Anon.getAccessionNumberByAccessionMapNumber(anyString())).thenReturn(studyDataMap.get("2"));		
		when(Anon.getPatientIdByPatientMapId(anyString())).thenReturn(patientDataMap.get("123").getPatientId());	
		when(Anon.getPatientNameByPatientMapId(anyString())).thenReturn(patientDataMap.get("123").getPatientName());		
		when(Anon.getAccessionNumberByAccessionMapNumber(anyString())).thenReturn(studyDataMap.get(anyString()));
		when(Anon.getmapIdbyPatientId(patientDataMap.get("123").getPatientId())).thenReturn(patientDataMap.get("123").getMapId());
		when(Anon.getmapIdbyPatientName(anyString())).thenReturn("123");
	}

	@Before
	public void setUp() throws Exception {
		patientDataMap= new HashMap<String, PatientData>();
		studyDataMap=new HashMap<String, String>() ;


		Integer id=1;
		String patientId = id.toString();
		Integer aN=1*2;
		String accessionNumber = aN.toString();
		String patientName ="a" +  patientId;
		String patientMapId = "123";
		String accessionMapNumber="321";
		PatientData Pd=new PatientData(patientName, patientId,patientMapId);
		patientDataMap.put(Pd.getMapId(), Pd);
		studyDataMap.put(accessionMapNumber,accessionNumber);
	}

	@Test
	public void test() {

		//TermQuery PatientName
		TermQuery tQ =new TermQuery(new Term("PatientName","a1"));
		//--TEST PatientName
		TermQuery tQ1 =new TermQuery(new Term("PatientName","123"));

		//TermQuery PatientId
		TermQuery tQa =new TermQuery(new Term("PatientId","1"));
		TermQuery tQa1 =new TermQuery(new Term("PatientId","123"));
		//--TEST1 PatientId
		BooleanQuery.Builder builder= new Builder();
		BooleanClause clause= new BooleanClause(tQa, Occur.SHOULD);
		BooleanQuery boolQ= builder.add(clause).setMinimumNumberShouldMatch(1).build();
		BooleanClause clause2=new BooleanClause(tQa1, Occur.SHOULD);
		BooleanQuery boolQ2= builder.add(clause2).setMinimumNumberShouldMatch(1).build();

		//Boolean Query PatientName
		BooleanQuery.Builder b= new Builder();
		BooleanClause bClause= new BooleanClause(tQ, Occur.SHOULD);
		BooleanQuery bQ= b.add(bClause).setMinimumNumberShouldMatch(1).build();		
		//--TEST1 PatientName
		BooleanQuery.Builder b2= new Builder();
		BooleanQuery.Builder b3= new Builder();
		BooleanClause bClause2=new BooleanClause(tQ1, Occur.SHOULD);
		BooleanQuery bQ2= b2.add(bClause).add(bClause2).setMinimumNumberShouldMatch(1).build();

		//--TEST1 PatientID
		BooleanClause clause3=new BooleanClause(boolQ2, Occur.SHOULD);
		BooleanQuery.Builder builder2= new Builder();
		BooleanQuery boolQ3=builder2.add(clause3).build();


		//--TEST2 PatientName
		BooleanClause bClause3=new BooleanClause(bQ2, Occur.SHOULD);
		BooleanQuery bQ3=b3.add(bClause3).build();

		//Phrase Query PatientName
		String T="a1";
		PhraseQuery pQ =new PhraseQuery("PatientName",T);
		String T2="1";
		PhraseQuery phraseQ =new PhraseQuery("PatientId",T2);

		//QueryConverter
		QueryConverter qC = new QueryConverter(Anon);

		//--TEST PatientName
		BooleanQuery.Builder b4= new Builder();
		BooleanClause bClause4=new BooleanClause(pQ, Occur.SHOULD);
		BooleanQuery bQ4= b4.add(bClause4).add(bClause2).setMinimumNumberShouldMatch(1).build();

		//--TEST PatientId
		BooleanQuery.Builder builder3=new Builder();
		BooleanClause clause4=new BooleanClause(phraseQ, Occur.SHOULD);
		BooleanQuery boolQ4=builder3.add(clause4).build();
		BooleanQuery.Builder builder4=new Builder();
		BooleanClause clause5=new BooleanClause(phraseQ, Occur.SHOULD);
		BooleanQuery bQ5= builder4.add(clause5).add(clause2).setMinimumNumberShouldMatch(1).build();
		BooleanClause cla=new BooleanClause(bQ5, Occur.SHOULD);

		BooleanQuery.Builder builder5=new Builder();
		BooleanQuery booleanQuery= builder5.add(cla).build();
		try {
			Query q = qC.transformQuery(tQ);
			Query qa = qC.transformQuery(tQa);
			Query q2 = qC.transformQuery(bQ);
			Query qa2 = qC.transformQuery(boolQ);
			Query q3 = qC.transformQuery(pQ);
			Query qa3 = qC.transformQuery(boolQ4);
			//--TEST PatientName
			Assert.assertEquals(bQ2,q);
			Assert.assertEquals(bQ3,q2);
			Assert.assertEquals(bQ4,q3);
			//--TEST PatientId
			Assert.assertEquals(boolQ2,qa);
			Assert.assertEquals(boolQ3,qa2);
			Assert.assertEquals(booleanQuery,qa3);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	@Test
	public void test2() {
		//TermQuery AccessionNumber
		TermQuery tQ =new TermQuery(new Term("AccessionNumber","2"));
		TermQuery tQa =new TermQuery(new Term("AccessionNumber","321"));
		BooleanQuery.Builder builder= new Builder();
		BooleanClause clause= new BooleanClause(tQ, Occur.SHOULD);
		BooleanClause clause2= new BooleanClause(tQa, Occur.SHOULD);
		BooleanQuery boolQ= builder.add(clause).add(clause2).setMinimumNumberShouldMatch(1).build();

		//QueryConverter
		QueryConverter qC = new QueryConverter(Anon);

		//BooleanQuery
		BooleanQuery.Builder b= new Builder();
		BooleanQuery bQ =b.add(clause).setMinimumNumberShouldMatch(1).build();
		BooleanQuery.Builder b2= new Builder();
		BooleanClause clauseBQtest=new BooleanClause(boolQ, Occur.SHOULD);
		BooleanQuery bQtester=b2.add(clauseBQtest).build();
		
		//PhraseQuery
		String T2="2";
		PhraseQuery phraseQ =new PhraseQuery("AccessionNumber",T2);
		BooleanQuery.Builder b3= new Builder();
		BooleanClause clausePQtest=new BooleanClause(phraseQ, Occur.SHOULD);
		BooleanQuery pQtester=b3.add(clausePQtest).add(clause2).setMinimumNumberShouldMatch(1).build();
		
		
		try {
			Query q = qC.transformQuery(tQ);
			Query q2 = qC.transformQuery(bQ);
			Query q3 = qC.transformQuery(phraseQ);
			
			
			Assert.assertEquals(boolQ,q);
			Assert.assertEquals(bQtester,q2);
			Assert.assertEquals(pQtester,q3);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test3() {
		//TermQuery Others
		TermQuery tQ =new TermQuery(new Term("other","1 a1"));
		TermQuery tQa =new TermQuery(new Term("PatientId","123"));
		TermQuery tQb =new TermQuery(new Term("PatientName","123"));
		TermQuery tQc =new TermQuery(new Term("AccessionNumber","321"));
		BooleanQuery.Builder builder= new Builder();
		BooleanClause clause= new BooleanClause(tQ, Occur.SHOULD);
		BooleanClause clause2= new BooleanClause(tQa, Occur.SHOULD);
		BooleanClause clause3= new BooleanClause(tQb, Occur.SHOULD);
		BooleanClause clause4= new BooleanClause(tQc, Occur.SHOULD);
		BooleanQuery boolQ= builder.add(clause).add(clause2).add(clause3).add(clause4).setMinimumNumberShouldMatch(1).build();

		//QueryConverter.
		QueryConverter qC = new QueryConverter(Anon);

		//BooleanQuery
		BooleanQuery.Builder b= new Builder();
		BooleanQuery bQ =b.add(clause).setMinimumNumberShouldMatch(1).build();
		BooleanQuery.Builder b2= new Builder();
		BooleanClause clauseBQtest=new BooleanClause(boolQ, Occur.SHOULD);
		BooleanQuery bQtester=b2.add(clauseBQtest).build();
		
		//PhraseQuery
		String T2="1 a1";
		PhraseQuery phraseQ =new PhraseQuery("other",T2);
		BooleanQuery.Builder b3= new Builder();
		BooleanClause clausePQtest=new BooleanClause(phraseQ, Occur.SHOULD);
		BooleanQuery pQtester=b3.add(clausePQtest).add(clause2).add(clause3).add(clause4).setMinimumNumberShouldMatch(1).build();
		
		
		try {
			Query q = qC.transformQuery(tQ);
			Query q2 = qC.transformQuery(bQ);
			Query q3 = qC.transformQuery(phraseQ);
			
			
			Assert.assertEquals(boolQ,q);
			Assert.assertEquals(bQtester,q2);
			Assert.assertEquals(pQtester,q3);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
