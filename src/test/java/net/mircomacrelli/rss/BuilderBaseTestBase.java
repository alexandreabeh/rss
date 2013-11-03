package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

abstract class BuilderBaseTestBase<T, B extends BuilderBase<T>> extends XmlTestBase {

    abstract B newBuilder();

    protected B parse(final String xml) throws Exception {
        final XMLEventReader reader = parseString(xml);
        final StartElement element = getElement(reader);
        final B builder = newBuilder();
        builder.parse(reader, element);
        return builder;
    }
}
