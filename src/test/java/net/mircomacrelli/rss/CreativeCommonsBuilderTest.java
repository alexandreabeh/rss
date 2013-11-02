package net.mircomacrelli.rss;

import net.mircomacrelli.rss.CreativeCommons.Builder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreativeCommonsBuilderTest extends ModuleBuilderTestBase {

    @Before
    public void setup() {
        uri = "http://cyber.law.harvard.edu/rss/creativeCommonsRssModule.html";
        prefix = "cc";
    }

    @Test
    public void twoLicenses() throws Exception {
        final Builder builder = new Builder(null);
        step(builder, "<cc:license>http://www.creativecommons.org/licenses/by-nc/1.0</cc:license>");
        step(builder, "<cc:license>http://www.creativecommons.org/licenses/by/1.0</cc:license>");

        final CreativeCommons cc = (CreativeCommons)builder.build();
        assertEquals(2, cc.getLicenses().size());
    }
}
