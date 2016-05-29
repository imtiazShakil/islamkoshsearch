package org.islamkosh.controller;

import static org.junit.Assert.*;

import org.junit.Test;
/**
 * <h2>This class is used for executing the whole project.</h2>
 *  <h3>Warning</h3>
 *  <p>
 *  * Required text files must be present in the classpath<br>
 *  * SystemProperty of Java should be properly initiated ( use vm arguments )<br>
 *  * Solr and mysql instance should be up and Running
 *  </p>
 * @author imtiaz
 * 
 */
public class ControllerTest {

	@Test
	public final void test() {
		Controller.main(null);
	}

	/**
	 * Sample VM arguments
	 * 
	 * -Dmysql.db.url=jdbc:mysql://localhost/hadiths?
	 * -Dmysql.db.user=root
	 * -Dmysql.db.password=shakil123
	 * -Dmysql.track.table=hadiths
	 * -Dsolr.url=http://localhost:8983/solr/islamkoshcore
	 * -Dstopword.file=stopwords.txt
	 * -Dstemmer.bangla.threshold=25
	 * -Dstemmer.bangla.rule.file=stem.rules
	 * -Dstemmer.bangla.protected.file=bengaliprotwords.txt
	 */
}
