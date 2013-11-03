package net.mircomacrelli.rss;

import org.joda.time.format.DateTimeFormatter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

abstract class ModuleBuilder extends BuilderBase<Module> {
    protected ModuleBuilder(final DateTimeFormatter parser) {
        super(parser);
    }

    protected  ModuleBuilder() {
        this(null);
    }
}
