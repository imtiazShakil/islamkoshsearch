package org.islamkosh.normalizer;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.StringTokenizer;

import org.junit.Test;

public class BengaliNormalizerTest {

	static String testCase = "রেখেছেন শির্কসহ সব রকমের পাপাচার অশ্লীলতা থেকে মুক্ত";
	// File will be loaded from the classpath. Make sure it is accessible from the classpath.
	static String ruleFileName = "bengali-normalizer-rules.txt";
	
//	public static void main(String args[]) {
//		BengaliNormalizer bNorm = new BengaliNormalizer(BengaliNormalizerTest.class.getClassLoader().getResourceAsStream(
//				ruleFileName));
//		
//		StringTokenizer tok = new StringTokenizer(testCase, " ");
//		while (tok.hasMoreTokens()) {
//			System.out.println(tok.countTokens());
//			String str = tok.nextToken();
//			str = bNorm.normalize(str);
//			System.out.print(str+" ");
//		}
//		System.out.println();
//	}
	
	@Test
	public void testBengaliNormalizer() {
		BengaliNormalizer bNorm = new BengaliNormalizer(BengaliNormalizerTest.class.getClassLoader().getResourceAsStream(
				ruleFileName));
	}

	@Test
	public void testNormalizeCharArrayInt() {
		BengaliNormalizer bNorm = new BengaliNormalizer(BengaliNormalizerTest.class.getClassLoader().getResourceAsStream(
				ruleFileName));
		
		StringTokenizer tok = new StringTokenizer(testCase, " ");
		char arr[];
		while (tok.hasMoreTokens()) {
//			System.out.println(strArr[i]);
			String str = tok.nextToken();
			int len = str.length();
			arr = Arrays.copyOf(str.toCharArray(), len*2);
			len = bNorm.normalize(arr, len);
			System.out.print(new String(arr, 0, len)+" ");
		}
		System.out.println();
	}

	@Test
	public void testNormalizeString() {
		BengaliNormalizer bNorm = new BengaliNormalizer(BengaliNormalizerTest.class.getClassLoader().getResourceAsStream(
				ruleFileName));
		
		StringTokenizer tok = new StringTokenizer(testCase, " ");
		System.out.println(testCase);
//		System.out.println(tok.countTokens());
		while (tok.hasMoreTokens()) {
//			System.out.println(tok.countTokens());
			String str = tok.nextToken();
			str = bNorm.normalize(str);
			System.out.print(str+" ");
//			System.out.println(tok.hasMoreTokens());
		}
		System.out.println();
	}

}
