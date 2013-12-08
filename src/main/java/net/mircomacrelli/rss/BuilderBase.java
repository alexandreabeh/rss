package net.mircomacrelli.rss;

import org.joda.time.format.DateTimeFormatter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

abstract class BuilderBase<T> {
    final DateTimeFormatter parser;

    BuilderBase(final DateTimeFormatter parser) {
        this.parser = parser;
    }

    BuilderBase() {
        this(null);
    }

    abstract void parseElement(final XMLEventReader reader, StartElement element) throws Exception;

    public final void parse(final XMLEventReader reader, final StartElement element) throws ParserException {
        try {
            parseElement(reader, element);
        } catch (final Exception cause) {
            throw new ParserException(cause);
        }
    }

    public abstract T build() throws Exception;
}
