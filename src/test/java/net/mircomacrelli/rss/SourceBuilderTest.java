package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Source.Builder;
import org.junit.Test;

import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;

public class SourceBuilderTest extends BuilderBaseTestBase<Source, Builder> {

    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Test(expected = URISyntaxException.class)
    public void urlMustBeValid() throws Exception {
        parse("<source url=\", ,\">fonte</source>");
    }

    @Test
    public void validSource() throws Exception {
        assertNotNull(parse("<source url=\"http://www.google.it\">Fonte</source>").build());
    }
}
