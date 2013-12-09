package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Source.Builder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SourceBuilderTest extends BuilderBaseTestBase<Source, Builder> {

    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Test(expected = ParserException.class)
    public void urlMustBeValid() throws ParserException {
        parse("<source url=\", ,\">fonte</source>");
    }

    @Test
    public void validSource() throws ParserException, BuilderException {
        assertNotNull(parse("<source url=\"http://www.google.it\">Fonte</source>").build());
    }
}
