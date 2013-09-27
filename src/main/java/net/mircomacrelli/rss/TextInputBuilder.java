package net.mircomacrelli.rss;

import java.net.MalformedURLException;
import java.net.URL;

final class TextInputBuilder {
    String name;
    String description;
    String label;
    URL cgiScriptURL;

    public void setName(final String name) {
        this.name = name;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setCgiScriptURL(final String cgiScriptURL) throws MalformedURLException {
        this.cgiScriptURL = new URL(cgiScriptURL);
    }

    public TextInput build() {
        return new TextInput(name, description, label, cgiScriptURL);
    }
}
