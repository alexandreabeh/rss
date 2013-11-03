package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Cloud.Builder;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import static org.junit.Assert.assertNotNull;

public class CloudBuilderTest extends XmlTestBase {

    private static Builder parse(final String xml) throws Exception {
        final XMLEventReader reader = parseString(xml);
        final StartElement element = getElement(reader);
        final Builder builder = new Builder();
        builder.parse(reader, element);
        return builder;
    }

    @Test(expected = NumberFormatException.class)
    public void portMustBeAnInteger() throws Exception {
        parse("<cloud domain=\"mircomacrelli.net\" path=\"/rss\" port=\"somthing\" protocol=\"http-post\" registerProcedure=\"\" />");
    }

    @Test(expected = IllegalArgumentException.class)
    public void protocolMustBuValid() throws Exception {
        parse("<cloud domain=\"mircomacrelli.net\" path=\"/rss\" port=\"80\" protocol=\"random\" registerProcedure=\"\" />");
    }


    @Test
    public void goodCloudTag() throws Exception {
        final Builder builder = parse("<cloud domain=\"mircomacrelli.net\" path=\"/rss\" port=\"80\" protocol=\"http-post\" registerProcedure=\"\" />");
        assertNotNull(builder.build());
    }
}
