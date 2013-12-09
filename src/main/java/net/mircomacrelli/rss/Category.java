package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import java.util.Objects;

import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.getAttributesValues;
import static net.mircomacrelli.rss.Utils.getText;

/**
 * One category of an Item
 *
 * @author Mirco Macrelli
 * @version 2.0
 */
public final class Category {

    private final String domain;
    private final String location;

    /**
     * Creates a new Category
     *
     * @param domain domain that identifies the category taxonomy. can be null
     * @param location the location/name of the category
     */
    Category(final String domain, final String location) {
        this.domain = domain;
        this.location = requireNonNull(location);
    }

    /** @return return the domain of the category. can be null */
    public String getDomain() {
        return domain;
    }

    /** @return the name/location of the category */
    public String getLocation() {
        return location;
    }

    @Override
    public int hashCode() {
        return hash(domain, location);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Category)) {
            return false;
        }

        final Category other = (Category)obj;
        return location.equals(other.location) && Objects.equals(domain, other.domain);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(120);
        sb.append("Category{");
        if (hasDomain()) {
            sb.append("domain='").append(domain).append("', ");
        }
        sb.append("location='").append(location).append("'}");
        return sb.toString();
    }

    /** @return true if the category has a domain set */
    public boolean hasDomain() {
        return domain != null;
    }

    static final class Builder extends BuilderBase<Category> {
        String domain;
        String location;

        @Override
        public void parseElement(final XMLEventReader reader, final StartElement element) throws ParserException {
            domain = getAttributesValues(element).get("domain");
            location = getText(reader);
        }

        @Override
        public Category buildElement() {
            return new Category(domain, location);
        }
    }
}
