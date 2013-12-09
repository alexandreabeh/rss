package net.mircomacrelli.rss;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static net.mircomacrelli.rss.Utils.getAttributesValues;
import static net.mircomacrelli.rss.Utils.parseUri;

/**
 * Information about the Cloud that can be used to subscribe to the push notifications for this feed
 *
 * @author Mirco Macrelli
 * @version 2.0
 */
public final class Cloud {
    private final URI domain;
    private final int port;
    private final Path path;
    private final String procedureName;
    private final Protocol protocol;

    /**
     * Create a new Cloud
     *
     * @param domain the domain that will receive the requests
     * @param port the port number
     * @param path the path of the script
     * @param procedureName the name of the procedure that will be called
     * @param protocol the protocol used for the request
     */
    Cloud(final URI domain, final int port, final Path path, final String procedureName, final Protocol protocol) {
        portInvariant(port);
        procedureNameInvariant(procedureName, protocol);

        this.domain = requireNonNull(domain);
        this.port = port;
        this.path = requireNonNull(path);
        this.procedureName = requireNonNull(procedureName);
        this.protocol = requireNonNull(protocol);
    }

    private static void procedureNameInvariant(final String procedureName, final Protocol protocol) {
        if ((protocol == Protocol.XML_RPC) && procedureName.isEmpty()) {
            throw new IllegalArgumentException("procedureName cant be empty if protocol il xml-rpc");
        }
    }

    private static void portInvariant(final int port) {
        if ((port < 0) || (port > 65535)) {
            throw new IllegalArgumentException(format("port must be between 0 and 65535. was %d", port));
        }
    }

    /** @return the domain that will receive the requests */
    public URI getDomain() {
        return domain;
    }

    /** @return the port number */
    public int getPort() {
        return port;
    }

    /** @return the path of the script */
    public Path getPath() {
        return path;
    }

    /** @return the name of the procedure that will be called */
    public String getProcedureName() {
        return procedureName;
    }

    /** @return the protocol used for the request */
    public Protocol getProtocol() {
        return protocol;
    }

    @Override
    public int hashCode() {
        return hash(domain, port, path, procedureName, protocol);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Cloud)) {
            return false;
        }

        final Cloud other = (Cloud)obj;
        return (port == other.port) && (protocol == other.protocol) && domain.equals(other.domain) &&
               path.equals(other.path) && procedureName.equals(other.procedureName);
    }

    @Override
    public String toString() {
        return format("Cloud{domain='%s', port=%d, path='%s', procedureName='%s', protocol=%s}", domain, port, path,
                      procedureName, protocol);
    }

    /** Protocol used to subscribe to the notifications */
    public enum Protocol {
        /** Normal HTTP POST requests */
        HTTP_POST("HTTP-POST"),
        /** XML-RPC requests */
        XML_RPC("XML-RPC"),
        /** SOAP 1.1 */
        SOAP("SOAP 1.1");
        private final String name;

        Protocol(final String name) {
            this.name = name;
        }

        /**
         * Get the Protocol from the normal name
         *
         * @param value the name of the protocol
         * @return the protocol name
         */
        public static Protocol from(final String value) {
            return valueOf(value.toUpperCase(Locale.ENGLISH).replace('-', '_'));
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static final class Builder extends BuilderBase<Cloud> {
        URI domain;
        Integer port;
        Path path;
        String procedureName;
        Protocol protocol;

        @Override
        public void parseElement(final XMLEventReader reader, final StartElement element) throws ParserException {
            final Map<String, String> attributes = getAttributesValues(element);

            try {
                domain = parseUri(attributes.get("domain"));
            } catch (final URISyntaxException cause) {
                throw new ParserException(cause);
            }
            path = Paths.get(attributes.get("path"));
            procedureName = attributes.get("registerProcedure");
            port = Integer.parseInt(attributes.get("port"));
            protocol = Protocol.from(attributes.get("protocol"));
        }

        @Override
        public Cloud realBuild() {
            return new Cloud(domain, port, path, procedureName, protocol);
        }
    }
}
