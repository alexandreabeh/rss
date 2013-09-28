package net.mircomacrelli.rss;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public final class ItemTest {
    private URL validLink;

    private InternetAddress authorEmail;

    private Set<Category> categories;

    private UniqueId uniqueId;

    private Source source;

    private Date publishDate;

    private Enclosure enclosure;

    private URL otherLink;

    private Item item;

    @Before
    public void setup() throws MalformedURLException, AddressException, MimeTypeParseException {
        validLink = new URL("http://mircomacrelli.net");
        otherLink = new URL("http://www.google.com");
        authorEmail = new InternetAddress("info@mircomacrelli.net");
        categories = new HashSet<>();
        categories.add(new Category(null, "web"));
        source = new Source("Mirco Macrelli", validLink);
        enclosure = new Enclosure(validLink, 10, new MimeType("audio/mp3"));
        uniqueId = new UniqueId("id12345", false);
        publishDate = new Date(1380279886610L);
        item = new Item(validLink, "Titolo post", "parla di questo e quello", authorEmail, publishDate, categories,
                        source, validLink, enclosure, uniqueId);
    }

    @Test(expected = IllegalStateException.class)
    public void titleAndDescriptionCantBeNullAtTheSameTime() {
        new Item(null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    public void anItemWithOnlyTitleIsValid() {
        new Item(null, "Titolo post", null, null, null, null, null, null, null, null);
    }

    @Test
    public void anItemWithOnlyDescriptionIsValid() {
        new Item(null, null, "parla di questo e quello", null, null, null, null, null, null, null);
    }

    @Test
    public void link() {
        assertSame(validLink, item.getLink());
    }

    @Test
    public void authorEmail() {
        assertEquals(authorEmail, item.getAuthorEmail());
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
    public void enclosure() {
        assertEquals(enclosure, item.getEnclosure());
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
    public void authorEmailIsCopiedByCtor() throws UnsupportedEncodingException {
        authorEmail.setPersonal("Altro nome");
        assertNull(item.getAuthorEmail().getPersonal());
    }

    @Test
    public void authorEmailIsCopiedByGetter() throws UnsupportedEncodingException {
        item.getAuthorEmail().setPersonal("Mirco Macrelli");
        assertNull(item.getAuthorEmail().getPersonal());
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
    public void publishDateIsCopiedByCtor() {
        publishDate.setTime(1);
        assertNotEquals(1, item.getPublishDate().getTime());
    }

    @Test
    public void publishDateIsCopiedByGetter() {
        item.getPublishDate().setTime(1);
        assertNotEquals(1, item.getPublishDate().getTime());
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Item.class).withPrefabValues(URL.class, validLink, otherLink).verify();
    }

    @Test
    public void testToString() {
        assertEquals(
                "Item{title='Titolo post', link='http://mircomacrelli.net', description='parla di questo e quello', authorEmail='info@mircomacrelli.net', commentsLink='http://mircomacrelli.net', uniqueId=UniqueId{id='id12345', isLink=false}, publishDate='Fri, 27 Sep 2013 11:04:46 +0000', categories=[Category{location='web'}], source=Source{name='Mirco Macrelli', link='http://mircomacrelli.net'}, enclosure=Enclosure{link='http://mircomacrelli.net', length=10, type=audio/mp3}}",
                item.toString());
    }
}
