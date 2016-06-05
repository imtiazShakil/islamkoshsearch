package org.islamkosh.configuration;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class ConfigurationTest {

	@Test
	public final void testLoadProperties_() {
		InputStream stream = new ByteArrayInputStream(
				testXml.getBytes(StandardCharsets.UTF_8));
		try {
			Properties properties = Configuration.loadProperties_(stream);
			Enumeration en = properties.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				System.out.println(key + "<-->" + properties.getProperty(key));
			}
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	private String testXml = "<?xml version=\"1.0\"?>"
			+ "<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>"
			+ "<configuration> " 
			+ "<property>"
			+ "<name>islamkosh.test.case</name>" + "<value>First Test</value> "
			+ "</property>"
			+ "</configuration>";
}
