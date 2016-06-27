package org.islamkosh.normalizer;

import java.util.Arrays;

public class Utils {
	/** no instance */
	private Utils() {
	}

	/**
	 * Returns true if the character array starts with the suffix.
	 * 
	 * @param s
	 *            Input Buffer
	 * @param len
	 *            length of input buffer
	 * @param prefix
	 *            Prefix string to test
	 * @return true if <code>s</code> starts with <code>prefix</code>
	 */
	public static boolean startsWith(char s[], int len, String prefix) {
		final int prefixLen = prefix.length();
		if (prefixLen > len)
			return false;
		for (int i = 0; i < prefixLen; i++)
			if (s[i] != prefix.charAt(i))
				return false;
		return true;
	}

	/**
	 * Returns true if the character array ends with the suffix.
	 * 
	 * @param s
	 *            Input Buffer
	 * @param len
	 *            length of input buffer
	 * @param suffix
	 *            Suffix string to test
	 * @return true if <code>s</code> ends with <code>suffix</code>
	 */
	public static boolean endsWith(char s[], int len, String suffix) {
		final int suffixLen = suffix.length();
		if (suffixLen > len)
			return false;
		for (int i = suffixLen - 1; i >= 0; i--)
			if (s[len - (suffixLen - i)] != suffix.charAt(i))
				return false;

		return true;
	}

	/**
	 * Returns true if the character array ends with the suffix.
	 * 
	 * @param s
	 *            Input Buffer
	 * @param len
	 *            length of input buffer
	 * @param suffix
	 *            Suffix string to test
	 * @return true if <code>s</code> ends with <code>suffix</code>
	 */
	public static boolean endsWith(char s[], int len, char suffix[]) {
		final int suffixLen = suffix.length;
		if (suffixLen > len)
			return false;
		for (int i = suffixLen - 1; i >= 0; i--)
			if (s[len - (suffixLen - i)] != suffix[i])
				return false;

		return true;
	}

	/**
	 * Delete a character in-place
	 * 
	 * @param s
	 *            Input Buffer
	 * @param pos
	 *            Position of character to delete
	 * @param len
	 *            length of input buffer
	 * @return length of input buffer after deletion
	 */
	public static int delete(char s[], int pos, int len) {
		assert pos < len;
		if (pos < len - 1) { // don't arraycopy if asked to delete last
								// character
			System.arraycopy(s, pos + 1, s, pos, len - pos - 1);
		}
		return len - 1;
	}

	/**
	 * Delete n characters in-place
	 * 
	 * @param s
	 *            Input Buffer
	 * @param pos
	 *            Position of character to delete
	 * @param len
	 *            Length of input buffer
	 * @param nChars
	 *            number of characters to delete
	 * @return length of input buffer after deletion
	 */
	public static int deleteN(char s[], int pos, int len, int nChars) {
		assert pos + nChars <= len;
		if (pos + nChars < len) { // don't arraycopy if asked to delete the last
									// characters
			System.arraycopy(s, pos + nChars, s, pos, len - pos - nChars);
		}
		return len - nChars;
	}
	
	public static int replace(char s[], char r[], int pos, int sLen, int rLen, int nChars) {
		int len = sLen + rLen - nChars;
		if (len > s.length) throw new ArrayIndexOutOfBoundsException(
				"Array size is "+s.length+" but minimum required size is "+len);
		System.arraycopy(s, 0, s, 0, pos);
		System.arraycopy(r, 0, s, pos, rLen);
		System.arraycopy(s, pos+nChars, s, pos+rLen, sLen-pos-nChars);
		return len;
	}
	
	public static String replace(String string, String replace, int pos, int nChars) {
		int sLen = string.length();
		int rLen = replace.length();
		StringBuilder sb = new StringBuilder(string.length()+replace.length());
		sb.append(string.substring(0, pos));
		sb.append(replace);
		sb.append(string.substring(pos+nChars));
		return sb.toString();
	}
	
	public static void main(String args[]) {
		/** Char Array Test */
		
//		char[] s = "Hello Mazid".toCharArray();
//		char[] r = "Sa".toCharArray();
//		
//		System.out.println(s.length);
//		int len = s.length;
//		s = Arrays.copyOf(s, s.length*2);
//		len = replace(s, r, 6, len, r.length, 1);
////		System.out.println(s.length);
//		System.out.println(new String(s, 0, len));
		
		/** String Test */
		
		String str = "অশ্লীলতা";
		String replace = "";
		
		str = replace(str, replace, 2, 1);
		
		System.out.println(str);
	}
}
