package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.mail.internet.AddressException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class ItemTest {
    private URI validLink;
    private String author;
    private Set<Category> categories;
    private UniqueId uniqueId;
    private Source source;
    private DateTime publishDate;
    private List<Enclosure> enclosures;
    private Item item;
    private Item smallItem;

    @Before
    public void setup() throws MimeTypeParseException, URISyntaxException {
        validLink = new URI("http://mircomacrelli.net");
        author = "info@mircomacrelli.net";
        categories = new HashSet<>(1);
        categories.add(new Category(null, "web"));
        source = new Source("Mirco Macrelli", validLink);
        enclosures = new ArrayList<>(2);
        enclosures.add(new Enclosure(validLink, 10, new MimeType("audio/mp3")));
        final URI otherLink = new URI("http://www.google.com");
        enclosures.add(new Enclosure(otherLink, 1024, new MimeType("audio/mp3")));
        uniqueId = new UniqueId("id12345", false);
        publishDate = new DateTime(1380279886610L);
        item = new Item(validLink, "Titolo post", "parla di questo e quello", author, publishDate, categories, source,
                        validLink, enclosures, uniqueId);
        smallItem = new Item(null, "title", "description", null, null, null, null, null, null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void titleAndDescriptionCantBeNullAtTheSameTime() {
        new Item(null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    public void anItemWithOnlyTitleIsValid() {
        assertNotNull(item.getTitle());
    }

    @Test
    public void anItemWithOnlyDescriptionIsValid() {
        assertNotNull(item.getDescription());
    }

    @Test
    public void link() {
        assertSame(validLink, item.getLink());
    }

    @Test
    public void author() {
        assertEquals(author, item.getAuthor());
    }

    @Test
    public void authorEmail() throws AddressException {
        assertNotNull(item.getAuthorAsEmailAddress());
    }

    @Test(expected = AddressException.class)
    public void ifAuthorIsNotAnEmailThrowAnException() throws AddressException {
        final Item itm = new Item(null, "title", null, "invalid address", null, null, null, null, null, null);
        itm.getAuthorAsEmailAddress();
    }

    @Test
    public void categories() {
        assertEquals(categories, item.getCategories());
    }

    @Test
    public void noCategories() {
        assertTrue(smallItem.getCategories().isEmpty());
    }

    @Test
    public void noEnclosures() {
        assertTrue(smallItem.getEnclosures().isEmpty());
    }

    @Test
    public void uniqueId() {
        assertEquals(uniqueId, item.getUniqueId());
    }

    @Test
    public void source() {
        assertEquals(source, item.getSource());
    }

    @Test
    public void publishDate() {
        assertEquals(publishDate, item.getPublishDate());
    }

    @Test
    public void commentsLink() {
        assertSame(validLink, item.getCommentsLink());
    }

    @Test
    public void enclosures() {
        assertEquals(enclosures, item.getEnclosures());
    }

    @Test
    public void title() {
        assertEquals("Titolo post", item.getTitle());
    }

    @Test
    public void description() {
        assertEquals("parla di questo e quello", item.getDescription());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void categoriesAreUnmodifiable() {
        item.getCategories().clear();
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Item.class).verify();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void enclosuresAreUnmodifiable() {
        item.getEnclosures().clear();
    }

    @Test
    public void testToString() {
        assertEquals(
                "Item{title='Titolo post', link='http://mircomacrelli.net', description='parla di questo e quello', author='info@mircomacrelli.net', commentsLink='http://mircomacrelli.net', uniqueId=UniqueId{id='id12345', isLink=false}, publishDate='Fri, 27 Sep 2013 11:04:46 +0000', categories=[Category{location='web'}], source=Source{name='Mirco Macrelli', link='http://mircomacrelli.net'}, enclosures=[Enclosure{link='http://mircomacrelli.net', length=10, type=audio/mp3}, Enclosure{link='http://www.google.com', length=1024, type=audio/mp3}]}",
                item.toString());
    }
}
