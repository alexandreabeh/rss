package net.mircomacrelli.rss;

import org.joda.time.format.DateTimeFormatter;

abstract class ModuleBuilder extends BuilderBase<Module> {
    protected ModuleBuilder(final DateTimeFormatter parser) {
        super(parser);
    }
}
