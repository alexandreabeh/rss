package net.mircomacrelli.rss;

import net.mircomacrelli.rss.ExtensibleElementBuilderTest.MockElement.MockBuilder;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.Set;

import static net.mircomacrelli.rss.Utils.allowedModules;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ExtensibleElementBuilderTest {
    @Test
    public void unknownModulesAreIgnored() throws Exception {
        final String xml = "<rss xmlns:unk=\"http://mircomacrelli.net/unknown-module\">" +
                           "<unk:tag>value</unk:tag>" +
                           "</rss>";

        assertTrue(parse(xml, "tag").build().getModules().isEmpty());
    }

    private static MockBuilder parse(final String xml, final String name) throws Exception {
        final XMLEventReader reader = getReader(xml);
        final StartElement element = getFirstElementOf(reader, name);
        final MockBuilder builder = new MockBuilder(null);
        builder.passToModuleParser(reader, element);
        return builder;
    }

    private static StartElement getFirstElementOf(final XMLEventReader reader, final String name) throws
                                                                                                  XMLStreamException {
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

    private static XMLEventReader getReader(final String xml) throws XMLStreamException {
        final XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty("javax.xml.stream.supportDTD", false);
        return factory.createXMLEventReader(new StringReader(xml));
    }

    @Test(expected = IllegalStateException.class)
    public void throwExceptionWhenModuleIsNotAllowed() throws Exception {
        final String xml = "<rss xmlns:sy=\"http://purl.org/rss/1.0/modules/syndication/\">" +
                           "<sy:period>hourly</sy:period>" +
                           "</rss>";
        parse(xml, "period");
    }

    @Test
    public void moduleIsBuiltCorrectly() throws Exception {
        final String xml = "<rss xmlns:cc=\"http://cyber.law.harvard.edu/rss/creativeCommonsRssModule.html\">" +
                           "<cc:license>http://www.google.it</cc:license>" +
                           "</rss>";
        assertNotNull(parse(xml, "license").build().getModule(CreativeCommons.class));
    }

    static final class MockElement extends ExtensibleElement {
        static final class MockBuilder extends ExtensibleElementBuilder<MockElement> {
            MockBuilder(final DateTimeFormatter parser) {
                super(parser);
            }

            @Override
            MockElement buildElement() {
                return new MockElement();
            }

            @Override
            void handleTag(final XMLEventReader reader, final StartElement element) throws Exception {
            }

            @Override
            Set<Class<? extends Module>> getAllowedModules() {
                return allowedModules(CreativeCommons.class);
            }
        }
    }
}
