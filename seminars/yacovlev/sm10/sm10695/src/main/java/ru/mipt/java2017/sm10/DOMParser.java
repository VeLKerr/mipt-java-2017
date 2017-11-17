package ru.mipt.java2017.sm10;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

public class DOMParser {

    private final DocumentBuilder documentBuilder =
            DocumentBuilderFactory.newInstance().newDocumentBuilder();

    public DOMParser() throws ParserConfigurationException {
    }

    List<ParsedEntry> parseDocument(String xmlText) throws IOException, SAXException {
        InputSource source = new InputSource(new StringReader(xmlText));

        // CRASH!!!! Html is not XML
        Document document = documentBuilder.parse(source);

        Element rootElement = document.getDocumentElement();
        NodeList articles = rootElement.getElementsByTagName("article");
        List<ParsedEntry> result = new LinkedList<ParsedEntry>();
        for (int i=0; i<articles.getLength(); ++i) {
            Node articleNode = articles.item(i);
            Element articleElement = (Element) articleNode;

            NodeList articleChilds = articleElement.getChildNodes();
            // then traverse to child elements H2 and DIV

        }
        return result;
    }

    public static void main(String args[]) throws ParserConfigurationException, IOException, SAXException {
        String contents = new WebPageLoader()
                .loadPageAsString("https://habrahabr.ru");
        List<ParsedEntry> entries = new DOMParser().parseDocument(contents);
        entries.forEach(entry -> System.out.println(entry));
    }

}
