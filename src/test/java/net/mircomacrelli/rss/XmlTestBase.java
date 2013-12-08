package net.mircomacrelli.rss;

import org.junit.BeforeClass;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.StringReader;

public abstract class XmlTestBase {
    private static XMLInputFactory factory;

    @BeforeClass
    public static void setupFactory() {
        factory = XMLInputFactory.newFactory();
        factory.setProperty("javax.xml.stream.supportDTD", false);
    }

    static XMLEventReader parseString(final String xml) {
        XMLEventReader reader = null;
        try {
            reader = factory.createXMLEventReader(new StringReader(xml));
            reader.nextEvent();
            return reader;
        } catch (XMLStreamException e) {
            throw new AssertionError("error while parsing the string for the test", e);
        }
    }

    static StartElement getElement(final XMLEventReader reader) {
        while (reader.hasNext()) {
            final XMLEvent event;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                throw new AssertionError("the test was expecting an element", e);
            }
            if (event.isStartElement()) {
                final StartElement element = event.asStartElement();
                if (!element.getName().getLocalPart().equals("rss")) {
                    return element;
                }
            }
        }
        return null;
    }
}
