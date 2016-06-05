package org.islamkosh.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Configuration {

	public static final String CONFIGURATION_LABEL = "configuration";
	public static final String PROPERTY_LABEL = "property";
	public static final String NAME_LABEL = "name";
	public static final String VALUE_LABEL = "value";
	public static final String DESCRIPTION_LABEL = "description";

	public static Properties loadProperties(File file)
			throws ParserConfigurationException, SAXException, IOException {
		InputStream inputStream = new FileInputStream(file);
		return loadProperties_(inputStream);
	}

	public static Properties loadPropertiesFromClasspath(String name)
			throws ParserConfigurationException, SAXException, IOException {
		return loadProperties_(Configuration.class.getClassLoader()
				.getResourceAsStream(name));
	}

	public static Properties loadProperties_(InputStream inputStream)
			throws ParserConfigurationException, SAXException, IOException {
		if (inputStream == null)
			throw new IOException("Required inputStream is null");

		Properties properties = new Properties();

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		DefaultHandler handler = new DefaultHandler() {
			boolean configuration = false;
			boolean property = false;
			boolean name = false;
			boolean value = false;
			String name_text, value_text;

			public void startElement(String uri, String localName,
					String qName, Attributes attributes) throws SAXException {

				switch (qName.toLowerCase()) {
				case CONFIGURATION_LABEL:
					configuration = true;
					break;
				case PROPERTY_LABEL:
					property = true;
					break;
				case NAME_LABEL:
					name = true;
					break;
				case VALUE_LABEL:
					value = true;
					break;
				}

			}

			public void endElement(String uri, String localName, String qName)
					throws SAXException {
				switch (qName.toLowerCase()) {
				case CONFIGURATION_LABEL:
					configuration = false;
					break;
				case PROPERTY_LABEL:
					property = false;
					if (name_text == null || value_text == null)
						throw new SAXException(
								"configuration xml is not formatted properly. "
										+ "Inconsistent name and value tags after closing property tag");

					properties.setProperty(name_text, value_text);
					name_text = null;
					value_text = null;
					break;
				case NAME_LABEL:
					name = false;
					break;
				case VALUE_LABEL:
					value = false;
					break;
				}
			}

			public void characters(char ch[], int start, int length)
					throws SAXException {
				if (configuration == false)
					return;

				if (property == true && name == true) {
					name_text = new String(ch, start, length);
				}
				if (property == true && value == true) {
					value_text = new String(ch, start, length);
				}

			}
		};
		saxParser.parse(inputStream, handler);
		inputStream.close();
		return properties;
	}

}
