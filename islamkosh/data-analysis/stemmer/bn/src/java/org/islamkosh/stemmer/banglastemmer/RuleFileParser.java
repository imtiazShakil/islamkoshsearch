package org.islamkosh.stemmer.banglastemmer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.islamkosh.configuration.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * @author Rafi Kamal
 * @author Sazid Based on the research and implementation of Rafi Kamal
 */
public class RuleFileParser {
	private static final Log LOG = LogFactory.getLog(RuleFileParser.class
			.getName());

	private ArrayList<String> lines;
	private ArrayList<ArrayList<String>> pass;

	private static final String CURLY_OPEN = "{";
	private static final String CURLY_CLOSE = "}";

	private TreeSet<Character> st;
	private HashMap<String, String> replaceRule;
	private HashMap<String, Integer> mValue; // the length of the pattern (cv)*
												// where c=consonant v=vowel

	public RuleFileParser() throws IOException {
		String ruleFilePath = Configuration.getProperty("stemmer.bangla.rule.file",
				"stem.rules");
		InputStream is = RuleFileParser.class.getClassLoader()
				.getResourceAsStream(ruleFilePath);
		if (is == null) {
			throw new IOException(ruleFilePath +" file couldn't be loaded");
		}
		replaceRule = new HashMap<String, String>();
		mValue = new HashMap<String, Integer>();

		dependantCharSetInstallation();
		// loadVowelConsonant();
		// escapeOfRuleInstallation();
		lines = new ArrayList<String>();
		pass = new ArrayList<ArrayList<String>>();

		LOG.trace("Starting to Collect rules...");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				StandardCharsets.UTF_8));
		String line = null;
		while ((line = reader.readLine()) != null) {
			line = whiteSpaceTrim(line);
			line = commentTrim(line);
			if (line.equals(""))
				continue;
			int m = extractValueOfM(line);
			line = line.replaceAll("[+].*", "");
			// System.out.println(line);
			String replace = extractReplaceRule(line);
			line = line.replaceAll("->.*", "");
			if (m != 0) {
				mValue.put(line, m);
			}
			if (!replace.equals("")) {
				replaceRule.put(line, replace);
			}
			lines.add(line);
		}
		reader.close();
		is.close();
		
		int cnt = 0;
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).equals(CURLY_OPEN)) {
				pass.add(new ArrayList<String>());
				i++;
				while (i < lines.size() && !lines.get(i).equals(CURLY_CLOSE)) {
					pass.get(cnt).add(lines.get(i));
					i++;
				}
				cnt++;
			}
		}

	}

	// private void loadVowelConsonant() {
	// /**
	// * Installing Vowels
	// **/
	// vow.add('অ');vow.add('আ');vow.add('ই');vow.add('ঈ');vow.add('উ');
	// vow.add('ঊ');vow.add('ঋ');vow.add('এ');vow.add('ঐ');vow.add('ও');
	// vow.add('ঔ');vow.add('া');vow.add('ি');vow.add('ী');vow.add('ু');
	// vow.add('ূ');vow.add('ৃ');vow.add('ে');vow.add('ৈ');vow.add('ো');vow.add('ৌ');
	// /**
	// * Installing Consonants
	// **/
	// cons.add('অ');
	//
	// }

	private String whiteSpaceTrim(String str) {
		return str.replaceAll("[\t' ']+", "");
	}

	private String commentTrim(String str) {
		return str.replaceAll("#.*", "");
	}

	private int extractValueOfM(String str) {
		int m = 0;
		// গুলি $0
		// System.out.println(str);
		if (str.matches(".*[+].*")) {
			String[] l = str.split("[+]");
			m = Integer.parseInt(l[1]);
		}
		// System.out.println("m = "+m);
		return m;
	}

	private String extractReplaceRule(String str) {
		if (str.matches(".*->.*")) {
			String[] l = str.split("->");
			return l[1];
		}
		return "";
	}

	protected String stemOfWord(String word) {
		int i, j, mRule = 0;
		int m = calculateM(word);

		for (i = 0; i < pass.size(); i++) {
			for (j = 0; j < pass.get(i).size(); j++) {
				String replacePrefix = pass.get(i).get(j);
				// System.out.println("replacePrefix-->"+replacePrefix+",word-->"+word);
				String matcher = ".*" + replacePrefix + "$";
				// System.out.println("matcher-->"+matcher);
				if (word.matches(matcher)) {
					int indx = word.length() - replacePrefix.length();
					if (mValue.containsKey(replacePrefix)) {
						mRule = mValue.get(replacePrefix);
					} else {
						mRule = 0;
					}
					// m is lower than needed so don't apply the rule
					if (m < mRule)
						continue;
					if (replaceRule.containsKey(replacePrefix)) {
						String replaceSuffix = replaceRule.get(replacePrefix);
						StringBuilder builder = new StringBuilder(word);
						int k, l;
						for (k = indx, l = 0; k < indx + replaceSuffix.length(); k++, l++) {
							if (replaceSuffix.charAt(l) != '.') {
								builder.setCharAt(k, replaceSuffix.charAt(l));
							}
						}
						word = builder.substring(0, k);
					} else if (/* escape.contains(pass.get(i).get(j)) || */check(word
							.substring(0, indx))) {
						word = word.substring(0, indx);
					}

					break;
				}
			}
		}

		return word;
	}

	private int calculateM(String word) {

		return word.length();
	}

	private void dependantCharSetInstallation() {
		st = new TreeSet<Character>();
		st.add('া');
		st.add('ি');
		st.add('ী');
		st.add('ু');
		st.add('ূ');
		st.add('ৃ');
		st.add('ে');
		st.add('ৈ');
		st.add('ো');
		st.add('ৌ');
	}

	// private void escapeOfRuleInstallation()
	// {
	// escape=new TreeSet<String>();
	// escape.add("চ্ছি");
	// escape.add("চ্ছিল");
	// escape.add("চ্ছে");
	// escape.add("চ্ছিস");
	// escape.add("চ্ছিলেন");
	// escape.add("টি");
	// escape.add("টা");
	// escape.add("েরটা");
	// escape.add("গুলো");
	// }

	private boolean check(String word) {
		int i;
		int wordLength = 0;

		for (i = 0; i < word.length(); i++) {
			if (st.contains(word.charAt(i)))
				continue;
			wordLength++;
		}

		return wordLength >= 1;
	}

}