package SentenceExtractionTest;

import SentenceExtraction.JWIStemmer;

public class JWIStemmerTest {

	public static void main(String[] args) throws Exception {
		
		JWIStemmer jwiStemmer = new JWIStemmer();
		
		//getting
		System.out.println(jwiStemmer.FindAllStems("getting"));
		System.out.println(jwiStemmer.FindFirstStem("getting"));
		//fishes
		System.out.println(jwiStemmer.FindAllStems("fishes"));
		System.out.println(jwiStemmer.FindFirstStem("fishes"));
		//a word without meaning
		System.out.println(jwiStemmer.FindAllStems("eardhasegyaerha"));
		System.out.println(jwiStemmer.FindFirstStem("eardhasegyaerha"));
	}
}
