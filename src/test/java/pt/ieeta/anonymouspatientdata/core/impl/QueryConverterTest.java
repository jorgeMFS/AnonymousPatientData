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
import java.util.*;

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
		//Patient1
		List <String> array = new ArrayList <>();
		final Optional<String> ID=Optional.ofNullable("1");
		final Optional<String> NAME=Optional.ofNullable("a1");
		final Optional<String> MAPID= Optional.ofNullable("123");
		final Optional<String> ACCESSNUMB=Optional.ofNullable("19");
		final Optional<String> ACCESSMAPID=Optional.ofNullable("321");
		array.add(MAPID.get());
		//Patient2
		final Optional<String> ID2=Optional.ofNullable("2");
		final Optional<String> NAME2=Optional.ofNullable("a2");
		final Optional<String> MAPID2= Optional.ofNullable("1234");
		final Optional<String> ACCESSNUMB2=Optional.ofNullable("29");
		final Optional<String> ACCESSMAPID2=Optional.ofNullable("4321");

		//Leite^Antonia
		List <String> antonia_array = new ArrayList <>();
		final Optional<String> NAMEa=Optional.ofNullable("Leite^Antónia");
		final Optional<String> MAPIDa= Optional.ofNullable("789");
		antonia_array.add(MAPIDa.get());

		//Leite^Alice
		List <String> alice_array = new ArrayList <>();
		final Optional<String> NAMEal=Optional.ofNullable("Leite^Alice");
		final Optional<String> MAPIDal= Optional.ofNullable("456");
		alice_array.add(MAPIDal.get());


		//Leite
		final Optional<String> NAMEL=Optional.ofNullable("Leite");
		List <String> Leitearray = new ArrayList <>();
		Leitearray.add(MAPIDal.get());
		Leitearray.add(MAPIDa.get());



		//Mock Leite^Antonia
		when(Anon.getPatientNameByPatientMapId(MAPIDa.get()))
				.thenReturn(NAMEa);
		when(Anon.getvariousmapIdbyPatientName(NAMEa.get()))
				.thenReturn(antonia_array);
		when(Anon.getmapIdbyPatientId(ID.get()))
				.thenReturn(MAPIDa);


		//Mock Leite^Alice
		when(Anon.getPatientNameByPatientMapId(MAPIDal.get()))
				.thenReturn(NAMEal);
		when(Anon.getvariousmapIdbyPatientName(NAMEal.get()))
				.thenReturn(alice_array);
		when(Anon.getmapIdbyPatientId(ID.get()))
				.thenReturn(MAPIDal);

		when(Anon.getvariousmapIdbyPatientName(NAMEL.get())).thenReturn(Leitearray);






		//Mock Patient1
		when(Anon.getmapAccessionNumber(ACCESSNUMB.get()))
		.thenReturn(ACCESSMAPID);
		when(Anon.getAccessionNumberByAccessionMapNumber(ACCESSMAPID.get()))
		.thenReturn(ACCESSNUMB);		
		when(Anon.getPatientIdByPatientMapId(MAPID.get()))
		.thenReturn(ID);	
		when(Anon.getPatientNameByPatientMapId(MAPID.get()))
		.thenReturn(NAME);		
		when(Anon.getmapIdbyPatientId(ID.get()))
		.thenReturn(MAPID);
		when(Anon.getvariousmapIdbyPatientName(NAME.get()))
				.thenReturn(array);


		//Mock Patient2
		when(Anon.getmapAccessionNumber(ACCESSNUMB2.get()))
		.thenReturn(Optional.empty());
		when(Anon.getAccessionNumberByAccessionMapNumber(ACCESSMAPID2.get()))
		.thenReturn(Optional.empty());		
		when(Anon.getPatientIdByPatientMapId(MAPID2.get()))
		.thenReturn(Optional.empty());	
		when(Anon.getPatientNameByPatientMapId(MAPID2.get()))
		.thenReturn(Optional.empty());		
		when(Anon.getmapIdbyPatientId(ID2.get()))
		.thenReturn(Optional.empty());
		when(Anon.getvariousmapIdbyPatientName(NAME2.get()))
				.thenReturn(Collections.emptyList());



	}

	@Before
	public void setUp() throws Exception {


	}

	@Test
	public void test() {

		//TermQuery PatientName
		TermQuery tQ = new TermQuery(new Term("PatientName","a1"));
		//--TEST PatientName
		TermQuery tQ1 = new TermQuery(new Term("PatientName","123"));

		//TermQuery PatientId
		TermQuery tQa = new TermQuery(new Term("PatientID","1"));
		TermQuery tQa1 = new TermQuery(new Term("PatientID","123"));
		//--TEST1 PatientId
		BooleanQuery.Builder builder= new Builder();
		BooleanClause clause= new BooleanClause(tQa, Occur.SHOULD);
		BooleanQuery boolQ= builder.add(clause).build();
		BooleanClause clause2=new BooleanClause(tQa1, Occur.SHOULD);
        BooleanQuery.Builder builders= new Builder();
		BooleanQuery boolQ2= builders.add(clause2).build();

		//Boolean Query PatientName
		BooleanQuery.Builder b= new Builder();
		BooleanClause bClause = new BooleanClause(tQ, Occur.SHOULD);
		BooleanQuery bQ = b.add(bClause).build();
		//--TEST1 PatientName
		BooleanQuery.Builder b2 = new Builder();
		BooleanQuery.Builder b3 = new Builder();
		BooleanClause bClause2 = new BooleanClause(tQ1, Occur.SHOULD);
		BooleanQuery bQ2 = b2.add(bClause2).build();

		//--TEST1 PatientID
		BooleanClause clause3 = new BooleanClause(boolQ2, Occur.SHOULD);
		BooleanQuery.Builder builder2 = new Builder();
		BooleanQuery boolQ3 = builder2.add(clause3).build();


		//--TEST2 PatientName
		BooleanClause bClause3 = new BooleanClause(bQ2, Occur.SHOULD);
		BooleanQuery bQ3 = b3.add(bClause3).build();

		//Phrase Query PatientName
		String T="a1";
		PhraseQuery pQ =new PhraseQuery("PatientName",T);
		String T2="1";
		PhraseQuery phraseQ =new PhraseQuery("PatientID",T2);

		//QueryConverter
		QueryConverter qC = new QueryConverter(Anon);

		//--TEST PatientName
		BooleanQuery.Builder b4 = new Builder();
		BooleanClause bClause4 = new BooleanClause(pQ, Occur.SHOULD);
		BooleanQuery bQ4 = b4.add(bClause2).build();

		//--TEST PatientId
		BooleanQuery.Builder builder3 = new Builder();
		BooleanClause clause4 = new BooleanClause(phraseQ, Occur.SHOULD);
		BooleanQuery boolQ4 = builder3.add(clause4).build();
		BooleanQuery.Builder builder4 = new Builder();
		BooleanClause clause5 = new BooleanClause(phraseQ, Occur.SHOULD);
		BooleanQuery bQ5 = builder4.add(clause5).add(clause2).build();

		try {
			Query q = qC.transformQuery(tQ);
			Query qa = qC.transformQuery(tQa);
			Query q2 = qC.transformQuery(bQ);
			Query qa2 = qC.transformQuery(boolQ);
			Query q3 = qC.transformQuery(pQ);

			//--TEST PatientName
			Assert.assertEquals(bQ2,q);
			Assert.assertEquals(bQ3,q2);
			Assert.assertEquals(bQ4,q3);
			//--TEST PatientId
			Assert.assertEquals(boolQ2,qa);
			Assert.assertEquals(boolQ3,qa2);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	@Test
	public void test2() {
		//TermQuery AccessionNumber
		TermQuery tQ =new TermQuery(new Term("AccessionNumber","19"));
		TermQuery tQa =new TermQuery(new Term("AccessionNumber","321"));
		BooleanQuery.Builder builder= new Builder();
		BooleanClause clause= new BooleanClause(tQ, Occur.SHOULD);
		BooleanClause a= new BooleanClause(tQa, Occur.SHOULD);

		BooleanQuery boolQ= builder.add(a).build();

		//QueryConverter
		QueryConverter qC = new QueryConverter(Anon);

		//BooleanQuery
		BooleanQuery.Builder b= new Builder();
		BooleanQuery bQ =b.add(clause).build();
		BooleanQuery.Builder b2= new Builder();
		BooleanClause clauseBQtest=new BooleanClause(boolQ, Occur.SHOULD);
		BooleanQuery bQtester=b2.add(clauseBQtest).build();

		//PhraseQuery
		String T2="19";
		PhraseQuery phraseQ =new PhraseQuery("AccessionNumber",T2);
		BooleanQuery.Builder b3= new Builder();
		BooleanClause clausePQtest=new BooleanClause(phraseQ, Occur.SHOULD);
		BooleanQuery pQtester=b3.add(a).build();


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
		TermQuery tQ =new TermQuery(new Term("other","a1"));
		TermQuery tQb =new TermQuery(new Term("PatientName","123"));
		BooleanQuery.Builder builder= new Builder();
		BooleanClause clause= new BooleanClause(tQ, Occur.SHOULD);
		BooleanClause clause3= new BooleanClause(tQb, Occur.SHOULD);
		BooleanQuery boolQ= builder.add(clause3).build();

		//QueryConverter.
		QueryConverter qC = new QueryConverter(Anon);

		//BooleanQuery
		BooleanQuery.Builder b= new Builder();
		BooleanQuery bQ =b.add(clause).build();
		BooleanQuery.Builder b2= new Builder();
		BooleanClause clauseBQtest=new BooleanClause(boolQ, Occur.SHOULD);
		BooleanQuery bQtester=b2.add(clauseBQtest).build();

		//PhraseQuery
		String T2="a1";
		PhraseQuery phraseQ =new PhraseQuery("other",T2);
		BooleanQuery.Builder b3= new Builder();
		BooleanClause clausePQtest=new BooleanClause(phraseQ, Occur.SHOULD);
		BooleanQuery pQtester=b3.add(clause3).build();


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
	public void test4() {
		//TermQuery AccessionNumber
		TermQuery tQ =new TermQuery(new Term("AccessionNumber","29"));
		BooleanQuery.Builder builder= new Builder();
		BooleanClause clause= new BooleanClause(tQ, Occur.SHOULD);;
		BooleanQuery boolQ= builder.add(clause).build();

		//QueryConverter
		QueryConverter qC = new QueryConverter(Anon);

		//BooleanQuery
		BooleanQuery.Builder b= new Builder();
		BooleanQuery bQ =b.add(clause).build();
		
		
		//PhraseQuery
		String T2="29";
		PhraseQuery phraseQ =new PhraseQuery("AccessionNumber",T2);
		BooleanQuery.Builder b3= new Builder();
		BooleanClause clausePQtest=new BooleanClause(phraseQ, Occur.SHOULD);
		BooleanQuery pQtester=b3.build();


		try {
			Query q = qC.transformQuery(tQ);
			Query q2 = qC.transformQuery(bQ);
			Query q3 = qC.transformQuery(phraseQ);


			Assert.assertEquals(tQ,q);
			Assert.assertEquals(boolQ,q2);
			Assert.assertEquals(pQtester,q3);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	@Test
	public void test5() {
		TermQuery tQ =new TermQuery(new Term("PatientName","Leite"));
		TermQuery tQan =new TermQuery(new Term("PatientName","Leite^Antónia"));
		TermQuery tQ1 =new TermQuery(new Term("PatientName","456"));
		TermQuery tQ2 =new TermQuery(new Term("PatientName","Leite^Alice"));
		TermQuery tQal =new TermQuery(new Term("PatientName","789"));
		BooleanQuery.Builder builder= new Builder();
		BooleanClause clause= new BooleanClause(tQ, Occur.SHOULD);
		BooleanClause clause1= new BooleanClause(tQ1, Occur.SHOULD);
		BooleanClause clause2= new BooleanClause(tQal, Occur.SHOULD);
		BooleanQuery  boolQ = builder.add(clause1).add(clause2).build();
        BooleanQuery.Builder b4 = new Builder();
        BooleanQuery bQ1 = b4.add(new BooleanClause(boolQ, Occur.SHOULD)).build();
        //QueryConverter
		QueryConverter qC = new QueryConverter(Anon);


		//BooleanQuery
		BooleanQuery.Builder b= new Builder();
		BooleanQuery bQ =b.add(clause).build();


		try {
			Query q = qC.transformQuery(tQ);
			Query q2 = qC.transformQuery(bQ);
			Assert.assertEquals(bQ1,q2);
			Assert.assertEquals(boolQ,q);



		} catch (IOException e) {
			e.printStackTrace();
		}


	}



}
































