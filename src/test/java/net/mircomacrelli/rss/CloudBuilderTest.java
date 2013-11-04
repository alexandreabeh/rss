package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Cloud.Builder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CloudBuilderTest extends BuilderBaseTestBase<Cloud, Builder> {
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

    @Override
    Builder newBuilder() {
        return new Builder();
    }
}