package ru.mipt.java2017.sm11;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

public class XmlBasedUi {

    Stack<Container> containerStack = new Stack<>();
    Container rootElement = null;
    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    SAXParser saxParser = saxParserFactory.newSAXParser();

    public XmlBasedUi() throws ParserConfigurationException, SAXException, IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("ui.xml");
        saxParser.parse(inputStream, xmlParseHandler);
    }

    XmlParseHandler xmlParseHandler = new XmlParseHandler();

    class XmlParseHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            Container currentElement = null;
            String xmlElementName = qName;
            Class clazz = null;
            try {
                // xmlElementName: JFrame, ... etc.
                String fullClassName =
                        "javax.swing." + xmlElementName;
                clazz = Class.forName(fullClassName);
                Object instance = clazz.newInstance();
                currentElement = (Container) instance;

                for (int i=0; i<attributes.getLength(); ++i) {
                    String attrName = attributes.getQName(i);
                    String attrValue = attributes.getValue(i);
                    if (attrName.equalsIgnoreCase("width")) {
                        // in case there is no method setWidth
                        int w = Integer.parseInt(attrValue);
                        currentElement.setSize(w, currentElement.getHeight());
                    }
                    else if (attrName.equalsIgnoreCase("height")) {
                        int h = Integer.parseInt(attrValue);
                        currentElement.setSize(currentElement.getWidth(), h);
                    }
                    else {
                        String setterMethodName = "set" +
                                attrName.substring(0, 1).toUpperCase() +
                                attrName.substring(1);
                        // Method signature: name + types...
                        Method setterMethod = clazz.getMethod(setterMethodName, String.class);
                        // First is an object
                        setterMethod.invoke(currentElement, attrValue);
                    }
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
            containerStack.push(currentElement);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            Container current = containerStack.pop();
            if (containerStack.isEmpty()) {
                rootElement = current;
            }
            else {
                Container parent = containerStack.peek();
                parent.add(current);
            }
        }
    }

    public void showRoot() {
        rootElement.setVisible(true);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            try {
                new XmlBasedUi().showRoot();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
