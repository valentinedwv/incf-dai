package edu.ucsd.crbs.incf.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.parsers.DOMParser;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XMLUtil class - To work with XML formatted data
 * 
 * @author amemon
 * @version 5.0
 * 
 * Date Who Description 09-JAN-2006 AM Initial Version
 * 
 */
public class XMLUtil {

	// ----------------------------Member
	// Variables-------------------------------

	static Element outputElement;

	// ----------------------------Public Methods-------------------------------

	/**
	 * Get root element from XML String
	 * 
	 * @param arg
	 *            XML String
	 * @return Root Element
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Element getDocumentElementFromString(String arg)
			throws ParserConfigurationException, SAXException, IOException {

		Element element = null;

		try {
			DocumentBuilderFactory dbf;
			DocumentBuilder db;
			Document document;
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			document = db.parse(new InputSource(new StringReader(arg)));
			element = document.getDocumentElement();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return element;

	}

	public org.jdom.Document getJDOMXMLDocumentFromString(String xmlString) {

		org.jdom.Document document = null;

		try {

			SAXBuilder builder = new SAXBuilder(
					"org.apache.xerces.parsers.SAXParser", false);

			document = builder.build(new InputSource(new ByteArrayInputStream(
					xmlString.getBytes())));

		} catch (Exception e) {

			e.printStackTrace();

		}

		return document;

	}

	/**
	 * Get Document Object for XML String
	 * 
	 * @param arg
	 *            XML String
	 * @return Document Object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document getDocumentFromString(String arg)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document document;
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
		document = db.parse(new InputSource(new StringReader(arg)));
		return document;

	}

	/**
	 * Get string value for a tag
	 * 
	 * @param xmlElement
	 *            Root Element of subtree in which required tag is to be
	 *            searched
	 * @param key
	 *            Required tage name
	 * @return String value
	 */
	public static String getStringValueForXMLTag(Element xmlElement, String key) {
		NodeList nl = xmlElement.getElementsByTagName(key);
		String output = "nothing";
		if (nl.getLength() > 0) {
			Node node = nl.item(0).getFirstChild();
			if (node != null) {
				output = node.getNodeValue().trim();
				return output;
			}
		}
		return output;
	}

	/**
	 * Get string value for a tag
	 * 
	 * @param xmlElement
	 *            Root Element of subtree in which required tag is to be
	 *            searched
	 * @param key
	 *            Required tage name
	 * @return String value
	 */
	public static String getStringValueForXMLTagFromAnywhere(
			Element xmlElement, String key) {

		// Start - testing
		String value = xmlElement.getAttribute(key);
		System.out.println("Value is - " + value);
		// End - testing

		NodeList nl = xmlElement.getElementsByTagName(key);
		String output = "nothing";
		if (nl.getLength() > 0) {
			Node node = nl.item(0).getFirstChild();
			if (node != null) {
				output = node.getNodeValue().trim();
				return output;
			}
		}
		return output;
	}

	/*
	 * private static Node findElement(Node node, String key, String value) { if
	 * (!node.hasChildNodes()) { return null; } NodeList list =
	 * node.getChildNodes(); for (int i = 0; i < list.getLength(); i++) { Node
	 * subNode = list.item(i); if (node.hasChildNodes()) { findElement(subNode,
	 * key, value); } if (subNode.hasAttributes()) { if ( ( (Element)
	 * subNode).getAttribute(key).equals(value)) { outputElement = (Element)
	 * subNode; break; } } } return outputElement; }
	 */
	/**
	 * Get root element from XML File
	 * 
	 * @param path
	 *            Absolute path for XML File
	 * @return Root element for XML document
	 */
	public static Element getDocumentElementFromFile(String path)
			throws SAXException, IOException {
		DOMParser parser = new DOMParser();
		parser.parse(path);
		Document document = parser.getDocument();
		Element outputElement = document.getDocumentElement();
		return outputElement;

	}

	/**
	 * Get document object from XML file
	 * 
	 * @param path
	 *            Absolute path for XML document
	 * @return Document object for XML document
	 */

	public static Document getDocumentFromFile(String path)
			throws SAXException, IOException {
		DOMParser parser = new DOMParser();
		parser.parse(path);
		Document document = parser.getDocument();
		return document;

	}

	/**
	 * Retrieves string values for all like named tags from a collection of
	 * tags.
	 * 
	 * @param inputString
	 *            XML String
	 * @param tagName
	 *            Tag Name to be found
	 * @return Array of String values for given tag name
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static String[] getLayers(String inputString, String tagName)
			throws ParserConfigurationException, SAXException, IOException {
		Element layers = getDocumentElementFromString(inputString);
		NodeList layerElements = layers.getElementsByTagName(tagName);
		String[] layersList = new String[layerElements.getLength()];
		for (int i = 0; i < layersList.length; i++) {
			Element aLayerElement = (Element) layerElements.item(i);
			layersList[i] = aLayerElement.getFirstChild().getNodeValue().trim();
		}
		return layersList;
	}

	public static Element[] searchElementsByAttributeValue(String xml,
			String tagName, String attributeName, String attributeValue)
			throws ParserConfigurationException, SAXException, IOException {

		Element root = getDocumentElementFromString(xml);
		NodeList rootChildren = root.getElementsByTagName(tagName);
		Vector requiredElementsVec = new Vector();
		for (int i = 0; i < rootChildren.getLength(); i++) {
			Element tempElement = (Element) rootChildren.item(i);
			String tempAttributeValue = tempElement.getAttribute(attributeName);
			if (tempAttributeValue.equals(attributeValue)) {
				requiredElementsVec.add(tempElement);
			}
		}
		Element[] requiredElementsArr = new Element[requiredElementsVec.size()];
		requiredElementsArr = (Element[]) requiredElementsVec
				.toArray(requiredElementsArr);
		return requiredElementsArr;
	}

	// Retrieves the xml tag with its attributes from anywhere in the file
	public static Hashtable searchElementsByAttributeValueTest(
			String xmlFilePath, Hashtable imagesScaleFactorCombination)
			throws ParserConfigurationException, SAXException, IOException {

		String tagName = "LAYER";
		String attributeName1 = "name";
		String attributeName2 = "maxscale";

		DOMParser parser = new DOMParser();
		parser.parse(xmlFilePath);
		Document document = parser.getDocument();
		Element root = document.getDocumentElement();
		NodeList rootChildren = root.getElementsByTagName(tagName);

		/*
		 * Set set = new HashSet(); Set sortedSet = null;
		 * 
		 * set.add(imageName_zoomLevelValue);// This combined value is for the
		 * current image
		 * 
		 * for (int i = 0; i < rootChildren.getLength(); i++) {
		 * 
		 * Element tempElement = (Element) rootChildren.item(i); String
		 * tempAttributeValue1 = tempElement.getAttribute(attributeName1);
		 * String tempAttributeValue2 =
		 * tempElement.getAttribute(attributeName2);
		 * 
		 * System.out.println("tempAttributeValue1 - " + i + " - " +
		 * tempAttributeValue1); System.out.println("tempAttributeValue2 - " + i + " - " +
		 * tempAttributeValue2);
		 * 
		 * set.add(tempAttributeValue1+"|,|"+tempAttributeValue2);
		 *  }
		 * 
		 * //sortedSet = new TreeSet(set);
		 * 
		 * System.out.println("Sorted - " + set);
		 */

		// Data insert code
		for (int i = 0; i < rootChildren.getLength(); i++) {

			Element tempElement = (Element) rootChildren.item(i);
			String imageName = tempElement.getAttribute(attributeName1);
			String scaleFactor = tempElement.getAttribute(attributeName2)
					.replaceAll("1:", "").trim();

			imagesScaleFactorCombination.put(scaleFactor, imageName);
			System.out
					.println("tempAttributeValue1 - " + i + " - " + imageName);
			System.out.println("tempAttributeValue2 - " + i + " - "
					+ scaleFactor);

		}

		return imagesScaleFactorCombination;

	}

	public static String makeXMLSafe(String xml) {

		xml = xml.replaceAll("&", "&amp;");
		xml = xml.replaceAll("'", "&apos;");
		xml = xml.replaceAll(">", "&gt;");
		xml = xml.replaceAll("<", "&lt;");
		xml = xml.replaceAll("\"", "&quot;");
		return xml;
	}

}