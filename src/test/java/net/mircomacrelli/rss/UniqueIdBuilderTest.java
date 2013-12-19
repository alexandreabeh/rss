package net.mircomacrelli.rss;

import net.mircomacrelli.rss.UniqueId.Builder;
import org.junit.Test;

import java.net.URISyntaxException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UniqueIdBuilderTest extends BuilderTestBase<UniqueId, Builder> {

    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Test
    public void ifNotSpecifiedIsALink() throws ParserException {
        final Builder builder = parse("<guid>http://www.google.it</guid>");
        assertTrue(builder.build().isLink());
    }

    @Test
    public void isALinkAndIsSpecified() throws ParserException {
        final Builder builder = parse("<guid isPermaLink=\"true\">http://www.google.it</guid>");
        assertTrue(builder.build().isLink());
    }

    @Test
    public void isNotALink() throws ParserException {
        final Builder builder = parse("<guid isPermaLink=\"false\">NEWS_12345</guid>");
        assertFalse(builder.build().isLink());
    }

    @Test(expected = URISyntaxException.class)
    public void ifIsALinkButIsMalformedThenThrowsAnException() throws URISyntaxException, ParserException {
        final Builder builder = parse("<guid>12345 NEWS,qualcosa</guid>");
        builder.build().asURI();
    }
}
