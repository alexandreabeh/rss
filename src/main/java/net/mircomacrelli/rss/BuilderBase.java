package net.mircomacrelli.rss;

import org.joda.time.format.DateTimeFormatter;

abstract class BuilderBase {
    protected final DateTimeFormatter parser;

    protected BuilderBase(final DateTimeFormatter parser) {
        this.parser = parser;
    }
}
