package net.mircomacrelli.rss;

import net.mircomacrelli.rss.UniqueId.Builder;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import java.net.MalformedURLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UniqueIdBuilderTest extends BuilderBaseTestBase<UniqueId, Builder> {

    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Test
    public void ifNotSpecifiedIsALink() throws Exception {
        final Builder builder = parse("<guid>http://www.google.it</guid>");
        assertTrue(builder.build().isLink());
    }

    @Test
    public void isALinkAndIsSpecified() throws Exception {
        final Builder builder = parse("<guid isPermaLink=\"true\">http://www.google.it</guid>");
        assertTrue(builder.build().isLink());
    }

    @Test
    public void isNotALink() throws Exception {
        final Builder builder = parse("<guid isPermaLink=\"false\">NEWS_12345</guid>");
        assertFalse(builder.build().isLink());
    }

    @Test(expected = MalformedURLException.class)
    public void ifIsALinkButIsMalformedThenThrowsAnException() throws Exception {
        final Builder builder = parse("<guid>NEWS_12345</guid>");
        builder.build();
    }
}