package net.mircomacrelli.rss;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.canBeWrittenOnlyOnce;

/**
 * A text field that could be used for searching the feed or provide feedback
 *
 * @author Mirco Macrelli
 * @version 1.0
 */
public final class TextInput {
    private final String name;
    private final String description;
    private final String label;
    private final URL scriptUrl;

    /**
     * Creates a new TextInput
     *
     * @param name the name of the field
     * @param description a description of what this field does
     * @param label the text of the label
     * @param scriptUrl the url to the page that will handle the requests
     */
    TextInput(final String name, final String description, final String label, final URL scriptUrl) {
        requireNonNull(name);
        requireNonNull(description);
        requireNonNull(label);
        requireNonNull(scriptUrl);

        this.label = label;
        this.description = description;
        this.name = name;
        this.scriptUrl = scriptUrl;
    }

    /** @return the url to the page that will handle the requests */
    public URL getScriptUrl() {
        return scriptUrl;
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
        return hash(label, description, name, scriptUrl);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TextInput)) {
            return false;
        }

        final TextInput other = (TextInput)obj;
        return label.equals(other.label) && description.equals(other.description) && name.equals(other.name) &&
               scriptUrl.toString().equals(other.scriptUrl.toString());
    }

    @Override
    public String toString() {
        return format("TextInput{label='%s', description='%s', name='%s', scriptUrl='%s'}", label, description, name,
                      scriptUrl);
    }

    static final class Builder {
        String name;
        String description;
        String label;
        URL cgiScriptURL;

        public void setName(final String val) {
            canBeWrittenOnlyOnce(name);
            name = val;
        }

        public void setDescription(final String val) {
            canBeWrittenOnlyOnce(description);
            description = val;
        }

        public void setLabel(final String val) {
            canBeWrittenOnlyOnce(label);
            label = val;
        }

        public void setCgiScriptURL(final String val) throws MalformedURLException {
            canBeWrittenOnlyOnce(cgiScriptURL);
            cgiScriptURL = new URL(val);
        }

        public TextInput build() {
            return new TextInput(name, description, label, cgiScriptURL);
        }
    }
}
