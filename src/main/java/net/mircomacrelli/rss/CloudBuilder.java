package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Cloud.Protocol;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

final class CloudBuilder {
    URI domain;
    int port = -1;
    Path path;
    String procedureName;
    Protocol protocol;

    public void setDomain(final String domain) throws URISyntaxException {
        this.domain = new URI(domain);
    }

    public void setPort(final String port) {
        this.port = Integer.parseInt(port);
    }

    public void setPath(final String path) {
        this.path = Paths.get(path);
    }

    public void setProcedureName(final String procedureName) {
        this.procedureName = procedureName;
    }

    public void setProtocol(final String protocol) {
        this.protocol = Protocol.from(protocol);
    }

    public Cloud build() {
        return new Cloud(domain, port, path, procedureName, protocol);
    }
}
