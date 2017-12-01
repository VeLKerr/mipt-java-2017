package ru.mipt.java2017.sm11;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Stack;

public class XmlUiUsage {

    private final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    private final SAXParser saxParser = saxParserFactory.newSAXParser();
    private final XmlEventHanlder xmlEventHanlder = new XmlEventHanlder();

    private Container rootElement = null;

    class XmlEventHanlder extends DefaultHandler {

        Stack<Container> stack = new Stack<>();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            Container currentElement = null;
            try {
                // WRong!!!
//                Class clazz = Class.forName(qName);
                Class clazz = Class.forName("javax.swing."+qName);
                Object instance = clazz.newInstance();
                currentElement = (Container) instance;

                for (int i=0; i<attributes.getLength(); ++i) {
                    String attrName = attributes.getQName(i);
                    String attrValue = attributes.getValue(i);
                    // title -> getTitle
                    String setterMethodName = "set" +
                            attrName.substring(0,1).toUpperCase() +
                            attrName.substring(1);
                    // method: name + arguments
                    Method setter = clazz.getMethod(setterMethodName, String.class);
                    setter.invoke(instance, attrValue);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            stack.push(currentElement);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            Container current = stack.pop();
            if (stack.isEmpty()) {
                rootElement = current;
            }
            else {
                stack.peek().add(current);
            }
        }
    }

    public XmlUiUsage() throws ParserConfigurationException, SAXException, IOException {

        URL uiUrl = this.getClass().getResource("ui.xml");
        System.out.println(uiUrl);
        InputStream inputStream = uiUrl.openStream();
        saxParser.parse(inputStream, xmlEventHanlder);
        rootElement.setSize(400, 300);
        rootElement.setVisible(true);
    }


    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {
        new XmlUiUsage();
    }

}
