package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.copyList;
import static net.mircomacrelli.rss.Utils.getText;
import static net.mircomacrelli.rss.Utils.parseURL;

public final class CreativeCommons implements Module {
    private final List<URL> licenses;

    CreativeCommons(final List<URL> licenses) {
        requireNonNull(licenses);
        this.licenses = copyList(licenses);
    }

    public List<URL> getLicenses() {
        return licenses;
    }

    static final class Builder implements ModuleBuilder {
        List<URL> licenses;

        @Override
        public void parse(final XMLEventReader reader, final StartElement element) throws MalformedURLException,
                                                                                          XMLStreamException {
            if (licenses == null) {
                licenses = new ArrayList<>(1);
            }

            licenses.add(parseURL(getText(reader)));
        }

        @Override
        public Module build() {
            return new CreativeCommons(licenses);
        }
    }
}
