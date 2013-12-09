package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Category.Builder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CategoryBuilderTest extends BuilderBaseTestBase<Category, Builder> {
    @Override
    Builder newBuilder() {
        return new Builder();
    }

    @Test
    public void validCategory() throws ParserException, BuilderException {
        assertNotNull(parse("<category domain=\"dmoz.org\">news/italian</category>").build());
    }
}
