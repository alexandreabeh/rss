package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Channel.Day;
import net.mircomacrelli.rss.Cloud.Protocol;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import javax.mail.internet.AddressException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class ChannelTest {

    private URI validLink;
    private Channel channel;
    private Channel smallChannel;
    private String validAddress;
    private DateTime validDate;
    private Cloud cloud;
    private Locale language;
    private Image image;
    private Set<Category> categories;
    private TextInput input;
    private Set<Integer> skipHours;
    private EnumSet<Day> skipDays;
    private List<Item> items;

    @Before
    public void setup() throws URISyntaxException {
        validLink = new URI("http://mircomacrelli.net");
        validAddress = "info@mircomacrelli.net";
        validDate = new DateTime(1380279886610L);
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
        smallChannel = new Channel("titolo", validLink, "descrizione", null, null, null, null, null, null, null, null,
                                   null, null, null, null, null, null, null, null, null);
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
    public void hourCantBeGreaterThan24() {
        new Channel("Mirco Macrelli", validLink, "Descrizione del feed", null, null, null, null, null, null, null, null,
                    null, null, null, null, null, Collections.singleton(25), null, null, null);
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
    public void emptySkipHours() {
        assertTrue(smallChannel.getSkipHours().isEmpty());
    }

    @Test
    public void emptySkipDays() {
        assertTrue(smallChannel.getSkipDays().isEmpty());
    }

    @Test
    public void emptyCategories() {
        assertTrue(smallChannel.getCategories().isEmpty());
    }

    @Test
    public void emptyItems() {
        assertTrue(smallChannel.getItems().isEmpty());
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
    public void editor() {
        assertEquals(validAddress, channel.getEditor());
    }

    @Test
    public void editorAsEmail() throws AddressException {
        assertNotNull(channel.getEditorAsEmailAddress());
    }

    @Test(expected = AddressException.class)
    public void ifEditorIsNotAnEmailLaunchAnException() throws AddressException {
        final Channel chan = new Channel("title", validLink, "desc", null, null, "invalid address", null, null, null,
                                         null, null, null, null, null, null, null, null, null, null, null);
        chan.getEditorAsEmailAddress();
    }

    @Test
    public void webmaster() {
        assertEquals(validAddress, channel.getWebmaster());
    }

    @Test
    public void webmasterAsEmail() throws AddressException {
        assertNotNull(channel.getWebmasterAsEmailAddress());
    }

    @Test(expected = AddressException.class)
    public void ifWebmasterIsNotAnEmailLaunchAnException() throws AddressException {
        final Channel chan = new Channel("title", validLink, "desc", null, null, null, "invalid email address", null,
                                         null, null, null, null, null, null, null, null, null, null, null, null);
        chan.getWebmasterAsEmailAddress();
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

    @Test(expected = UnsupportedOperationException.class)
    public void categoriesAreUnmodifiable() {
        channel.getCategories().clear();
    }

    @Test
    public void inSkipHours24IsReplacedWith0() {
        final Set<Integer> hours = new HashSet<>(1);
        hours.add(24);
        final Channel chan = new Channel("title", validLink, "desc", null, null, null, null, null, null, null, null,
                                         null, null, null, null, null, hours, null, null, null);
        assertTrue(chan.getSkipHours().contains(0));
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Channel.class).suppress(Warning.NULL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        assertEquals(
                "Channel{title='Mirco Macrelli', link='http://mircomacrelli.net', description='Descrizione del feed', language='it_IT', copyright='Copyright (c) 2013 Mirco Macrelli', editor='info@mircomacrelli.net', webmaster='info@mircomacrelli.net', publishDate='Fri, 27 Sep 2013 11:04:46 +0000', buildDate='Fri, 27 Sep 2013 11:04:46 +0000', categories=[Category{location='web'}], generator='Generatore del feed 1.0', documentation='http://mircomacrelli.net', cloud=Cloud{domain='mircomacrelli.net', port=80, path='/subscribe', procedureName='', protocol=HTTP-POST}, timeToLive='60', image=Image{image='http://mircomacrelli.net', alt='logo', link='http://mircomacrelli.net'}, textInput=TextInput{label='Search', description='Search the feed', name='q', scriptUri='http://mircomacrelli.net'}, skipHours=[13, 14], skipDays=[FRIDAY], items=[Item{title='titolo'}]}",
                channel.toString());
    }

    @Test
    public void fromMonday() {
        assertEquals(Day.MONDAY, Day.from("Monday"));
    }

    @Test
    public void fromTuesday() {
        assertEquals(Day.TUESDAY, Day.from("Tuesday"));
    }

    @Test
    public void fromWednesday() {
        assertEquals(Day.WEDNESDAY, Day.from("Wednesday"));
    }

    @Test
    public void fromThursday() {
        assertEquals(Day.THURSDAY, Day.from("Thursday"));
    }

    @Test
    public void fromFriday() {
        assertEquals(Day.FRIDAY, Day.from("Friday"));
    }

    @Test
    public void fromSaturday() {
        assertEquals(Day.SATURDAY, Day.from("Saturday"));
    }

    @Test
    public void fromSunday() {
        assertEquals(Day.SUNDAY, Day.from("Sunday"));
    }
}

