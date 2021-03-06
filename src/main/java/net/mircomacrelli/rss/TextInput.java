package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.net.URI;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.getAllTagsValuesInside;
import static net.mircomacrelli.rss.Utils.parseUri;

/**
 * A text field that could be used for searching the feed or provide feedback
 *
 * @author Mirco Macrelli
 * @version 2.0
 */
public final class TextInput {
    private final String name;
    private final String description;
    private final String label;
    private final URI scriptUri;

    /**
     * Creates a new TextInput
     *
     * @param name the name of the field
     * @param description a description of what this field does
     * @param label the text of the label
     * @param scriptUri the url to the page that will handle the requests
     */
    TextInput(final String name, final String description, final String label, final URI scriptUri) {
        this.label = requireNonNull(label);
        this.description = requireNonNull(description);
        this.name = requireNonNull(name);
        this.scriptUri = requireNonNull(scriptUri);
    }

    /** @return the url to the page that will handle the requests */
    public URI getScriptUri() {
        return scriptUri;
    }

    /** @return the name of the field */
    public String getName() {
        return name;
    }

    /** @return a description of what this field does */
    public String getDescription() {
        return description;
    }

    /** @return the text of the label */
    public String getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        return hash(label, description, name, scriptUri);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof TextInput)) {
            return false;
        }

        final TextInput other = (TextInput)obj;
        return label.equals(other.label) && description.equals(other.description) && name.equals(other.name) &&
               scriptUri.equals(other.scriptUri);
    }

    @Override
    public String toString() {
        return format("TextInput{label='%s', description='%s', name='%s', scriptUri='%s'}", label, description, name,
                      scriptUri);
    }

    static final class Builder extends BuilderBase<TextInput> {
        String name;
        String description;
        String label;
        URI cgiScriptUri;

        @Override
        public void parseElement(final XMLEventReader reader, final StartElement element) throws ParserException {
            final Map<String, String> values = getAllTagsValuesInside(reader, "textInput");

            label = values.get("title");
            description = values.get("description");
            name = values.get("name");
            cgiScriptUri = parseUri(values.get("link"));
        }

        @Override
        public TextInput buildElement() {
            return new TextInput(name, description, label, cgiScriptUri);
        }
    }
}
