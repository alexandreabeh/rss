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

    protected static XMLEventReader parseString(final String xml) throws XMLStreamException {
        XMLEventReader reader = factory.createXMLEventReader(new StringReader(xml));
        reader.nextEvent();
        return reader;
    }

    protected static StartElement getElement(final XMLEventReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
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
