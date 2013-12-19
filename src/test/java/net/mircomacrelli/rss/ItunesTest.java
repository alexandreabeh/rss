package net.mircomacrelli.rss;

import net.mircomacrelli.rss.Itunes.Explicit;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static net.mircomacrelli.rss.Itunes.Builder.DURATION;
import static org.junit.Assert.assertEquals;

public class ItunesTest {

    private Itunes itunes;

    private URI image;
    private URI newFeedUrl;

    private InternetAddress ownerEmail;

    private List<Itunes.Category> categories;

    @Before
    public void setup() throws URISyntaxException, AddressException {
        image = new URI("http://www.sito.com/immagine.png");
        newFeedUrl = new URI("http://www.sito.com/new/feed.xml");
        ownerEmail = new InternetAddress("owner@email.com");
        categories = new ArrayList<>(1);
        categories.add(new Itunes.Category("Categoria", null));
        itunes = new Itunes("Autore", false, image, false, "Sommario", "sottotitolo", newFeedUrl, 1, false, "Owner", ownerEmail,
                            Explicit.NO, DURATION.parsePeriod("2:54"), categories);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Itunes.class)
                      .withPrefabValues(Period.class, DURATION.parsePeriod("2:54"), DURATION.parsePeriod("9:11"))
                      .verify();
    }

    @Test
    public void testToString() {
        assertEquals("iTunes{author='Autore', block=false, image='http://www.sito.com/immagine.png', cc=false, summary='Sommario', subtitle='sottotitolo', newFeedUrl='http://www.sito.com/new/feed.xml', order=1, complete=false, owner.name='Owner', owner.email='owner@email.com', explicit=NO, duration='PT2H54M', categories=[Category{name='Categoria'}]}", itunes.toString());
    }
}
