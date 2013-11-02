package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.mail.internet.AddressException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public final class ItemTest {
    private URL validLink;
    private String author;
    private Set<Category> categories;
    private UniqueId uniqueId;
    private Source source;
    private DateTime publishDate;
    private List<Enclosure> enclosures;
    private URL otherLink;
    private Item item;

    @Before
    public void setup() throws MalformedURLException, MimeTypeParseException {
        validLink = new URL("http://mircomacrelli.net");
        otherLink = new URL("http://www.google.com");
        author = "info@mircomacrelli.net";
        categories = new HashSet<>(1);
        categories.add(new Category(null, "web"));
        source = new Source("Mirco Macrelli", validLink);
        enclosures = new ArrayList<>(2);
        enclosures.add(new Enclosure(validLink, 10, new MimeType("audio/mp3")));
        enclosures.add(new Enclosure(otherLink, 1024, new MimeType("audio/mp3")));
        uniqueId = new UniqueId("id12345", false);
        publishDate = new DateTime(1380279886610L);
        item = new Item(validLink, "Titolo post", "parla di questo e quello", author, publishDate, categories, source,
                        validLink, enclosures, uniqueId);
    }

    @Test(expected = IllegalStateException.class)
    public void titleAndDescriptionCantBeNullAtTheSameTime() {
        new Item(null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    public void anItemWithOnlyTitleIsValid() {
        assertNotNull(new Item(null, "Titolo post", null, null, null, null, null, null, null, null).getTitle());
    }

    @Test
    public void anItemWithOnlyDescriptionIsValid() {
        assertNotNull(new Item(null, null, "parla di questo e quello", null, null, null, null, null, null, null)
                              .getDescription());
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

    @Test
    public void categoriesAreCopiedByCtor() {
        categories.clear();
        assertEquals(1, item.getCategories().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void categoriesAreUnmodifiable() {
        item.getCategories().clear();
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Item.class).withPrefabValues(URL.class, validLink, otherLink).verify();
    }

    @Test
    public void enclosuresAreCopiedByCtor() {
        enclosures.clear();
        assertNotEquals(enclosures, item.getEnclosures());
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
