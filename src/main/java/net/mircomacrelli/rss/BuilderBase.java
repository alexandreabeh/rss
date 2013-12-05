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

    public abstract void parse(final XMLEventReader reader, StartElement element) throws Exception;

    public abstract T build() throws Exception;
}
