package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

public final class Syndication implements Module {
    static final class Builder implements ModuleBuilder {

        @Override
        public void parse(final XMLEventReader reader, final StartElement element) throws Exception {
        }

        @Override
        public Module build() {
            return null;
        }
    }
}
