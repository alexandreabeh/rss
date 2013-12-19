package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

abstract class BuilderTestBase<T, B extends BuilderBase<T>> extends XmlTestBase {

    abstract B newBuilder();

    B parse(final String xml) throws ParserException {
        final XMLEventReader reader = parseString(xml);
        final StartElement element = getElement(reader);
        final B builder = newBuilder();
        builder.parse(reader, element);
        return builder;
    }
}
