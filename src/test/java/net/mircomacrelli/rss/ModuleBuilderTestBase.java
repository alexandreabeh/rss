package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import static java.lang.String.format;

public abstract class ModuleBuilderTestBase extends XmlTestBase {
    String uri;
    String prefix;

    void step(final ModuleBuilder builder, final String xml) throws ParserException {
        final XMLEventReader reader = parseString(decorate(xml));
        final StartElement element = getElement(reader);
        builder.parse(reader, element);
    }

    private String decorate(final String xml) {
        if ((uri != null) && (prefix != null)) {
            return format("<rss xmlns:%s=\"%s\">%s</rss>", prefix, uri, xml);
        }
        return xml;
    }
}
