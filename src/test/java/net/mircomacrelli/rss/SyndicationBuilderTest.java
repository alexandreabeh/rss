package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Syndication.Builder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SyndicationBuilderTest extends ModuleBuilderTestBase {

    @Before
    public void setup() {
        uri = "http://purl.org/rss/1.0/modules/syndication/";
        prefix = "sy";
    }

    @Test(expected = IllegalStateException.class)
    public void periodCantBeRepeated() throws Exception {
        final Builder builder = new Builder(null);
        step(builder, "<sy:updatePeriod>weekly</sy:updatePeriod>");
        step(builder, "<sy:updatePeriod>yearly</sy:updatePeriod>");
    }

    @Test(expected = IllegalStateException.class)
    public void frequencyCantBeRepeated() throws Exception {
        final Builder builder = new Builder(null);
        step(builder, "<sy:updateFrequency>2</sy:updateFrequency>");
        step(builder, "<sy:updateFrequency>8</sy:updateFrequency>");
    }

    @Test(expected = IllegalStateException.class)
    public void baseCantBeRepeated() throws Exception {
        final Builder builder = new Builder(null);
        step(builder, "<sy:updateBase>2013-12-21T12:21:00+0000</sy:updateBase>");
        step(builder, "<sy:updateBase>2013-12-21T21:12:00+0000</sy:updateBase>");
    }

    @Test
    public void build() throws Exception {
        final Builder builder = new Builder(null);
        step(builder, "<sy:updatePeriod>weekly</sy:updatePeriod>");
        step(builder, "<sy:updateFrequency>2</sy:updateFrequency>");
        step(builder, "<sy:updateBase>2013-12-21T12:21:00+0000</sy:updateBase>");
        assertNotNull(builder.build());
    }

    @Test
    public void unknownTagsAreIgnored() throws Exception {
        final Builder builder = new Builder(null);
        step(builder, "<sy:unknownTag>12</sy:unknownTag>");
    }
}
