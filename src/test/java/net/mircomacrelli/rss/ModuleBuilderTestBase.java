package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

public abstract class ModuleBuilderTestBase extends XmlTestBase {
    protected String uri;
    protected String prefix;

    public void step(final ModuleBuilder builder, final String xml) throws Exception {
        final XMLEventReader reader = parseString(decorate(xml));
        final StartElement element = getElement(reader);
        builder.parse(reader, element);
    }

    private String decorate(final String xml) {
        if ((uri != null) && (prefix != null)) {
            return String.format("<rss xmlns:%s=\"%s\">%s</rss>", prefix, uri, xml);
        }
        return xml;
    }
}
