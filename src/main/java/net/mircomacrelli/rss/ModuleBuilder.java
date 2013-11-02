package net.mircomacrelli.rss;

import org.joda.time.format.DateTimeFormatter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

abstract class ModuleBuilder extends BuilderBase {
    protected ModuleBuilder(final DateTimeFormatter parser) {
        super(parser);
    }

    abstract void parse(XMLEventReader reader, final StartElement element) throws Exception;

    abstract Module build();
}
