package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Source.Builder;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertNotNull;

public class SourceBuilderTest extends BuilderBaseTestBase<Source, Builder> {

    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Test(expected = MalformedURLException.class)
    public void urlMustBeValid() throws Exception {
        parse("<source url=\"invalid\">fonte</source>");
    }

    @Test
    public void validSource() throws Exception {
        assertNotNull(parse("<source url=\"http://www.google.it\">Fonte</source>").build());
    }
}
