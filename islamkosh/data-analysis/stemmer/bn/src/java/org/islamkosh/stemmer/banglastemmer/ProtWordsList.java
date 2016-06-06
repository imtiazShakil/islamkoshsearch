package org.islamkosh.stemmer.banglastemmer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.islamkosh.configuration.Configuration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by sazid on 1/28/16.
 */
public class ProtWordsList {
	private static final Log LOG = LogFactory.getLog(ProtWordsList.class
			.getName());

	private static HashSet<String> protWords = new HashSet<String>();

	public static void loadProtWords() throws IOException {
		String protWordFilePath = Configuration.getProperty(
				"stemmer.bangla.protected.file", "bengaliprotwords.txt");
		InputStream is = ProtWordsList.class.getClassLoader()
				.getResourceAsStream(protWordFilePath);
		if (is == null) {
			LOG.debug(protWordFilePath + " couldn't be loaded");
			return;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(is,
				StandardCharsets.UTF_8));

		String inp = br.readLine();
		while (inp != null) {
			inp = inp.trim();
			Collections.addAll(protWords, inp.split("[\\s।%,ঃ]+"));
			inp = br.readLine();
		}
		br.close();
		is.close();
		LOG.trace("Protected words list is loaded.");
	}

	public static boolean isProtectedWord(String word) {
		return protWords.contains(word);
	}
}
