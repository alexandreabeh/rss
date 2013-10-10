package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Cloud.Protocol;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class CloudTest {
    private URI validDomain;
    private Path validPath;
    private Cloud cloud;

    @Before
    public void setup() throws URISyntaxException {
        validDomain = new URI("mircomacrelli.net");
        validPath = Paths.get("/subscribe");
        cloud = new Cloud(validDomain, 80, validPath, "subscribe.feed", Protocol.XML_RPC);
    }

    @Test(expected = NullPointerException.class)
    public void domainIsRequired() {
        new Cloud(null, 80, validPath, "subscribe.feed", Protocol.XML_RPC);
    }

    @Test(expected = NullPointerException.class)
    public void pathIsRequired() {
        new Cloud(validDomain, 80, null, "subscribe.feed", Protocol.XML_RPC);
    }

    @Test(expected = IllegalArgumentException.class)
    public void portCantBeNegative() {
        new Cloud(validDomain, -1, validPath, "subscribe.feed", Protocol.XML_RPC);
    }

    @Test(expected = IllegalArgumentException.class)
    public void portCantBeGreaterThan65535() {
        new Cloud(validDomain, 65536, validPath, "subscribe.feed", Protocol.XML_RPC);
    }

    @Test(expected = NullPointerException.class)
    public void procedureNameIsRequired() {
        new Cloud(validDomain, 80, validPath, null, Protocol.XML_RPC);
    }

    @Test(expected = IllegalArgumentException.class)
    public void procedureNameCantBeEmptyIfProtocolIsXMLRPC() {
        new Cloud(validDomain, 80, validPath, "", Protocol.XML_RPC);
    }

    @Test
    public void procedureNameCanBeEmptyForHTTPPOST() {
        assertTrue(new Cloud(validDomain, 80, validPath, "", Protocol.HTTP_POST).getProcedureName().isEmpty());
    }

    @Test
    public void procedureNameCanBEmptyForSOAP() {
        assertTrue(new Cloud(validDomain, 80, validPath, "", Protocol.SOAP).getProcedureName().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void protocolIsRequired() {
        new Cloud(validDomain, 80, validPath, "subscribe.feed", null);
    }

    @Test
    public void domain() {
        assertSame(validDomain, cloud.getDomain());
    }

    @Test
    public void port() {
        assertEquals(80, cloud.getPort());
    }

    @Test
    public void procedureName() {
        assertEquals("subscribe.feed", cloud.getProcedureName());
    }

    @Test
    public void protocol() {
        assertEquals(Protocol.XML_RPC, cloud.getProtocol());
    }

    @Test
    public void path() {
        assertSame(validPath, cloud.getPath());
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Cloud.class).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        assertEquals(
                "Cloud{domain='mircomacrelli.net', port=80, path='/subscribe', procedureName='subscribe.feed', protocol=XML-RPC}",
                cloud.toString());
    }

    @Test
    public void fromHttpPost() {
        assertEquals(Protocol.HTTP_POST, Protocol.from("http-post"));
    }

    @Test
    public void fromXmlRpc() {
        assertEquals(Protocol.XML_RPC, Protocol.from("xml-rpc"));
    }

    @Test
    public void fromSoap() {
        assertEquals(Protocol.SOAP, Protocol.from("soap"));
    }
}
