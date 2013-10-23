package net.mircomacrelli.rss;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.StringReader;

public abstract class ModuleBuilderTestBase {
    private static XMLInputFactory factory;

    @BeforeClass
    public static void setupFactory() {
        factory = XMLInputFactory.newFactory();
        factory.setProperty("javax.xml.stream.supportDTD", false);
    }

    protected String uri;

    protected String prefix;

    private String decorate(final String xml) {
        if ((uri != null) && (prefix != null)) {
            return String.format("<rss xmlns:%s=\"%s\">%s</rss>", prefix, uri, xml);
        }
        return xml;
    }

    private XMLEventReader parseString(final String xml) throws XMLStreamException {
        return factory.createXMLEventReader(new StringReader(decorate(xml)));
    }

    private static StartElement getElement(final XMLEventReader reader) throws XMLStreamException {
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

    public void step(final ModuleBuilder builder, final String xml) throws Exception {
        final XMLEventReader reader = parseString(xml);
        final StartElement element = getElement(reader);
        builder.parse(reader, element);
    }
}
