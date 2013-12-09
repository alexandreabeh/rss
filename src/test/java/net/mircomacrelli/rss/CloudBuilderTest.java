package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Cloud.Builder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CloudBuilderTest extends BuilderBaseTestBase<Cloud, Builder> {
    @Test(expected = ParserException.class)
    public void portMustBeAnInteger() throws ParserException {
        parse("<cloud domain=\"mircomacrelli.net\" path=\"/rss\" port=\"something\" protocol=\"http-post\" registerProcedure=\"\" />");
    }

    @Test(expected = ParserException.class)
    public void protocolMustBuValid() throws ParserException {
        parse("<cloud domain=\"mircomacrelli.net\" path=\"/rss\" port=\"80\" protocol=\"random\" registerProcedure=\"\" />");
    }

    @Test
    public void goodCloudTag() throws ParserException {
        final Builder builder = parse(
                "<cloud domain=\"mircomacrelli.net\" path=\"/rss\" port=\"80\" protocol=\"http-post\" registerProcedure=\"\" />");
        assertNotNull(builder.build());
    }

    @Override
    Builder newBuilder() {
        return new Builder();
    }
}
