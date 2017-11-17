package ru.mipt.java2017.sm10;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.List;

public class XmlBuilder {

    private final DocumentBuilder documentBuilder =
            DocumentBuilderFactory.newInstance().newDocumentBuilder();

    public XmlBuilder() throws ParserConfigurationException {
    }

    public Document toXmlDocument(List<ParsedEntry> entries) {
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("habrahabr");
        entries.forEach(entry -> {
            Element article = document.createElement("article");
            article.setAttribute("title", entry.getTitle());
            article.setAttribute("link", entry.getLink());
            Text innerText = document.createTextNode(entry.getAnnotationText());
            article.appendChild(innerText);
            root.appendChild(article);
        });
        document.appendChild(root);
        return document;
    }

    public static void main(String args[]) throws ParserConfigurationException, TransformerException {
        String contents = new WebPageLoader().loadPageAsString("https://habrahabr.ru");
        List<ParsedEntry> entries = new JSoupParser().parseHtml(contents);
        Document document = new XmlBuilder().toXmlDocument(entries);

        Transformer xmlToTextTransformer = TransformerFactory.newInstance().newTransformer();
        xmlToTextTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter stringWriter = new StringWriter();
        xmlToTextTransformer.transform(
                new DOMSource(document),
                new StreamResult(stringWriter)
        );
        System.out.println(stringWriter.getBuffer().toString());
    }

}
