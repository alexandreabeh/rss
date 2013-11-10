package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.getText;
import static net.mircomacrelli.rss.Utils.parseURL;

/**
 * Implementation of the Creative Commons module
 *
 * @author Mirco Macrelli
 * @version 2.0
 */
public final class CreativeCommons implements Module {
    private final List<URL> licenses;

    CreativeCommons(final List<URL> licenses) {
        this.licenses = requireNonNull(licenses);
    }

    /** @return the list with the URL of the licenses */
    public List<URL> getLicenses() {
        return unmodifiableList(licenses);
    }

    @Override
    public int hashCode() {
        return hash(licenses);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CreativeCommons)) {
            return false;
        }

        final CreativeCommons other = (CreativeCommons)obj;
        return licenses.equals(other.licenses);
    }

    @Override
    public String toString() {
        return format("CreativeCommons{licenses=%s}", licenses);
    }

    static final class Builder extends ModuleBuilder {
        List<URL> licenses;

        public Builder() {
            super(null);
        }

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
