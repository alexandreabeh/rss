package net.mircomacrelli.rss;

import net.mircomacrelli.rss.ExtensibleElementBuilderTest.MockElement.MockBuilder;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.StringReader;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ExtensibleElementBuilderTest {
    static final class MockElement extends ExtensibleElement {
        static final class MockBuilder extends ExtensibleElementBuilder {
            public MockElement build() {
                return extend(new MockElement());
            }

            @Override
            Set<Class<? extends Module>> getAllowedModules() {
                return allowedModules(CreativeCommons.class);
            }
        }
    }

    private static XMLEventReader getReader(final String xml) throws XMLStreamException {
        final XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty("javax.xml.stream.supportDTD", false);
        return factory.createXMLEventReader(new StringReader(xml));
    }

    private static StartElement getFirstElementOf(final XMLEventReader reader, final String name) throws XMLStreamException {
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                final StartElement element = event.asStartElement();
                if (element.getName().getLocalPart().equals(name)) {
                    return element;
                }
            }
        }
        return null;
    }

    private static MockBuilder parse(final String xml, final String name) throws Exception {
        final XMLEventReader reader = getReader(xml);
        final StartElement element = getFirstElementOf(reader, name);
        final MockBuilder builder = new MockBuilder();
        builder.passToModuleParser(reader, element);
        return builder;
    }

    @Test
    public void unknownModulesAreIgnored() throws Exception {
        final String xml = "<rss xmlns:unk=\"http://mircomacrelli.net/unknown-module\">"+
                           "<unk:tag>value</unk:tag>"+
                           "</rss>";

        assertTrue(parse(xml, "tag").build().getModules().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void throwExceptionWhenModuleIsNotAllowed() throws Exception {
        final String xml = "<rss xmlns:sy=\"http://purl.org/rss/1.0/modules/syndication/\">"+
                           "<sy:period>hourly</sy:period>"+
                           "</rss>";
        parse(xml, "period");
    }

    @Test
    public void moduleIsBuiltCorrectly() throws Exception {
        final String xml = "<rss xmlns:cc=\"http://cyber.law.harvard.edu/rss/creativeCommonsRssModule.html\">"+
                           "<cc:license>http://www.google.it</cc:license>"+
                           "</rss>";
        assertNotNull(parse(xml, "license").build().getModule(CreativeCommons.class));
    }
}
