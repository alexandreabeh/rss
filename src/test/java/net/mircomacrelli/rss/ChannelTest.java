package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Channel.Day;
import net.mircomacrelli.rss.Cloud.Protocol;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public final class ChannelTest {

    private URL validLink;
    private URL otherLink;
    private Channel channel;
    private InternetAddress validAddress;
    private Date validDate;
    private Cloud cloud;
    private Locale language;
    private Image image;
    private Set<Category> categories;
    private TextInput input;
    private Set<Integer> skipHours;
    private EnumSet<Day> skipDays;
    private List<Item> items;

    @Before
    public void setup() throws MalformedURLException, AddressException, URISyntaxException {
        validLink = new URL("http://mircomacrelli.net");
        validAddress = new InternetAddress("info@mircomacrelli.net");
        otherLink = new URL("http://www.google.com");
        validDate = new Date(1380279886610L);
        categories = new HashSet<>(1);
        categories.add(new Category(null, "web"));
        cloud = new Cloud(new URI("mircomacrelli.net"), 80, Paths.get("/subscribe"), "", Protocol.HTTP_POST);
        image = new Image(validLink, validLink, "logo", null, null, null);
        input = new TextInput("q", "Search the feed", "Search", validLink);
        skipHours = new HashSet<>(2);
        skipHours.add(13);
        skipHours.add(14);
        skipDays = EnumSet.of(Day.FRIDAY);
        language = Locale.forLanguageTag("it-IT");
        items = new ArrayList<>(1);
        items.add(new Item(null, "titolo", null, null, null, null, null, null, null, null));
        channel = new Channel("Mirco Macrelli", validLink, "Descrizione del feed", language,
                              "Copyright (c) 2013 Mirco Macrelli", validAddress, validAddress, validDate, validDate,
                              categories, "Generatore del feed 1.0", validLink, cloud, 60, image, input, skipHours,
                              skipDays, "rating", items);
    }

    @Test(expected = NullPointerException.class)
    public void titleIsRequired() {
        new Channel(null, validLink, "Descrizione del feed", null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void linkIsRequired() {
        new Channel("Mirco Macrelli", null, "Descrizione del feed", null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void descriptionIsRequired() {
        new Channel("Mirco Macrelli", validLink, null, null, null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void timeToLiveCantBeNegative() {
        new Channel("Mirco Macrelli", validLink, "Descrizione del feed", null, null, null, null, null, null, null, null,
                    null, null, -1, null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hourCantBeNegative() {
        new Channel("Mirco Macrelli", validLink, "Descrizione del feed", null, null, null, null, null, null, null, null,
                    null, null, null, null, null, Collections.singleton(-1), null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hourCantBeGreaterThan23() {
        new Channel("Mirco Macrelli", validLink, "Descrizione del feed", null, null, null, null, null, null, null, null,
                    null, null, null, null, null, Collections.singleton(24), null, null, null);
    }

    @Test
    public void items() {
        assertEquals(items, channel.getItems());
    }

    @Test
    public void rating() {
        assertEquals("rating", channel.getRating());
    }

    @Test
    public void skipDays() {
        assertEquals(skipDays, channel.getSkipDays());
    }

    @Test
    public void skipHours() {
        assertEquals(skipHours, channel.getSkipHours());
    }

    @Test
    public void textInput() {
        assertEquals(input, channel.getTextInput());
    }

    @Test
    public void image() {
        assertEquals(image, channel.getImage());
    }

    @Test
    public void timeToLive() {
        assertEquals(Integer.valueOf(60), channel.getTimeToLive());
    }

    @Test
    public void cloud() {
        assertEquals(cloud, channel.getCloud());
    }

    @Test
    public void documentation() {
        assertEquals(validLink, channel.getDocumentation());
    }

    @Test
    public void categories() {
        assertEquals(categories, channel.getCategories());
    }

    @Test
    public void generator() {
        assertEquals("Generatore del feed 1.0", channel.getGenerator());
    }

    @Test
    public void buildDate() {
        assertEquals(validDate, channel.getBuildDate());
    }

    @Test
    public void publishDate() {
        assertEquals(validDate, channel.getPublishDate());
    }

    @Test
    public void title() {
        assertEquals("Mirco Macrelli", channel.getTitle());
    }

    @Test
    public void link() {
        assertSame(validLink, channel.getLink());
    }

    @Test
    public void description() {
        assertEquals("Descrizione del feed", channel.getDescription());
    }

    @Test
    public void language() {
        assertEquals(language, channel.getLanguage());
    }

    @Test
    public void copyright() {
        assertEquals("Copyright (c) 2013 Mirco Macrelli", channel.getCopyright());
    }

    @Test
    public void editorEmail() {
        assertEquals(validAddress, channel.getEditorEmail());
    }

    @Test
    public void webmasterEmail() {
        assertEquals(validAddress, channel.getWebmasterEmail());
    }

    @Test
    public void itemsAreCopiedByCtor() {
        items.clear();
        assertEquals(1, channel.getItems().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void itemsAreUnmodifiable() {
        channel.getItems().clear();
    }

    @Test
    public void skipHoursIsCopiedByCtor() {
        skipHours.clear();
        assertEquals(2, channel.getSkipHours().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void skipHoursIsUnmodifiable() {
        channel.getSkipHours().clear();
    }

    @Test
    public void skipDaysIsCopiedByCtor() {
        skipDays.clear();
        assertEquals(1, channel.getSkipDays().size());
    }

    @Test
    public void skipDaysIsCopiedByGetter() {
        channel.getSkipDays().clear();
        assertEquals(1, channel.getSkipDays().size());
    }

    @Test
    public void editorEmailIsCopiedByCtor() throws UnsupportedEncodingException {
        validAddress.setPersonal("Mirco Macrelli");
        assertNull(channel.getEditorEmail().getPersonal());
    }

    @Test
    public void editorEmailIsCopiedByGetter() throws UnsupportedEncodingException {
        channel.getEditorEmail().setPersonal("Mirco Macrelli");
        assertNull(channel.getEditorEmail().getPersonal());
    }

    @Test
    public void webmasterEmailIsCopiedByCtor() throws UnsupportedEncodingException {
        validAddress.setPersonal("Mirco Macrelli");
        assertNull(channel.getWebmasterEmail().getPersonal());
    }

    @Test
    public void webmasterEmailIsCopiedByGetter() throws UnsupportedEncodingException {
        channel.getWebmasterEmail().setPersonal("Mirco Macrelli");
        assertNull(channel.getWebmasterEmail().getPersonal());
    }

    @Test
    public void publishDateIsCopiedByCtor() {
        validDate.setTime(1);
        assertNotEquals(1, channel.getPublishDate());
    }

    @Test
    public void publishDateIsCopiedByGetter() {
        channel.getPublishDate().setTime(1);
        assertNotEquals(1, channel.getPublishDate());
    }

    @Test
    public void buildDateIsCopiedByCtor() {
        validDate.setTime(1);
        assertNotEquals(1, channel.getBuildDate());
    }

    @Test
    public void buildDateIsCopiedByGetter() {
        channel.getBuildDate().setTime(1);
        assertNotEquals(1, channel.getBuildDate());
    }

    @Test
    public void categoriesAreCopiedByCtor() {
        categories.clear();
        assertEquals(1, channel.getCategories().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void categoriesAreUnmodifiable() {
        channel.getCategories().clear();
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Channel.class).suppress(Warning.NULL_FIELDS)
                      .withPrefabValues(URL.class, validLink, otherLink).verify();
    }

    @Test
    public void testToString() {
        assertEquals(
                "Channel{title='Mirco Macrelli', link='http://mircomacrelli.net', description='Descrizione del feed', language='it_IT', copyright='Copyright (c) 2013 Mirco Macrelli', editorEmail='info@mircomacrelli.net', webmasterEmail='info@mircomacrelli.net', publishDate='Fri, 27 Sep 2013 11:04:46 +0000', buildDate='Fri, 27 Sep 2013 11:04:46 +0000', categories=[Category{location='web'}], generator='Generatore del feed 1.0', documentation='http://mircomacrelli.net', cloud=Cloud{domain='mircomacrelli.net', port=80, path='/subscribe', procedureName='', protocol=HTTP-POST}, timeToLive='60', image=Image{image='http://mircomacrelli.net', alt='logo', link='http://mircomacrelli.net'}, textInput=TextInput{label='Search', description='Search the feed', name='q', scriptUrl='http://mircomacrelli.net'}, skipHours=[13, 14], skipDays=[FRIDAY], items=[Item{title='titolo'}]}",
                channel.toString());
    }
}

