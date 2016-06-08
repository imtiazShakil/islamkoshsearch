package org.islamkosh.languages;

/**
 * 
 * @author sazid
 * @version 2.0
 */
public class LanguageCheck {

	public static final int OTHERWORD = 0;
	public static final int BANGLAWORD = 1;
	public static final int BURMEESEWORD = 2;
	public static final int NEPALIWORD = 3;
	public static final int BASICLATINWORD = 4;
	public static final int LANGUAGECOUNT = 5; // Including other language

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static int checkWords(String str){


		if (str.isEmpty()) return 0;

		boolean lang[] = new boolean[LANGUAGECOUNT];
		
		for(int i=0; i<str.length(); i++){
			int unicodeDecimal = (int)str.charAt(i);
			// Unicode range for Latin character
			if(  unicodeDecimal >=0 && unicodeDecimal<=127 ) {
				lang[BASICLATINWORD] = true;
			// Unicode range for Nepali (Devanagari) character
			} else if( unicodeDecimal >=2305 && unicodeDecimal<=2416 ) {
				lang[NEPALIWORD] = true;
			// Unicode range for Bengali character
			} else if( unicodeDecimal >=2432 && unicodeDecimal<=2559 ) {
				lang[BANGLAWORD] = true;
			// Unicode range for Burmeese character
			} else if( unicodeDecimal >=4096 && unicodeDecimal<=4255 ) {
				lang[BURMEESEWORD] = true;
			} else {
				lang[OTHERWORD] = true;
			}
		}
		
		for (int i=0; i<LANGUAGECOUNT; i++) {
			if (lang[i] == true) return i;
		}
		return OTHERWORD;
	}

}
