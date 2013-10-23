package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

interface ModuleBuilder {
    void parse(XMLEventReader reader, final StartElement element) throws Exception;
    Module build();
}
