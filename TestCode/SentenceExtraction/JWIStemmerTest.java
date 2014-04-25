package SentenceExtraction;

import SentenceExtraction.JWIStemmer;

public class JWIStemmerTest {

	public static void main(String[] args) throws Exception {
		
		JWIStemmer jwiStemmer = new JWIStemmer();
		
		System.out.println(jwiStemmer.getAllStems("getting"));
		System.out.println(jwiStemmer.getFirstStem("getting"));
		System.out.println(jwiStemmer.getAllStems("includes"));
		System.out.println(jwiStemmer.getFirstStem("fishes"));
		System.out.println(jwiStemmer.getAllStems("eardhasegyaerha"));
		System.out.println(jwiStemmer.getFirstStem("eardhasegyaerha"));
	}
}
