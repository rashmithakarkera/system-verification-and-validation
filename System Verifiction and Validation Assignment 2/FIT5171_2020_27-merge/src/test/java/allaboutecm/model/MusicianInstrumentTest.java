package allaboutecm.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Neel Rahul Shridharani - 29882206
 * The following code base contains constraints for unit testing class - MusicianInstrument
 */

class MusicianInstrumentTest {
    private MusicianInstrument musicianInstrument;
    private MusicalInstrument musicalInstrument;
    private Musician musician;
    public MusicianInstrumentTest() {
    }

    @BeforeEach
    public void setUp() {
        Set<MusicalInstrument> musicalInstruments = new HashSet<MusicalInstrument>(){{ add(new MusicalInstrument("Piano"));}};
        musician = new Musician ("Keith Jarrett");
        musicianInstrument = new MusicianInstrument(musician,musicalInstruments);
    }

    // To check if musician name is empty
    @Test
    @DisplayName("Musician name cannot be null")
    public void musicianCannotBeNull() {
        NullPointerException except = assertThrows(NullPointerException.class, () -> musicianInstrument.setMusician(null));
        assertEquals("The validated object is null",except.getMessage());
    }

    // To check if instrument name is empty -- changes
    @Test
    @DisplayName("Musician Instrument name cannot be null")
    public void musicalInstrumentNameCannotBeNull() {
        Set<MusicianInstrument> musicianInstruments = new HashSet<MusicianInstrument>();
        NullPointerException except = assertThrows(NullPointerException.class, () -> musicianInstrument.setMusicalInstruments(null));
        assertEquals("The validated collection is empty",except.getMessage());
    }

    @Test
    @DisplayName("musician name should not be same")
    public void newMusicianNameSet() {
        Musician musicianName = musicianInstrument.getMusician();
        Musician musician = new Musician("xyz");
        musicianInstrument.setMusician(musician);
        Musician musicianName1 = musicianInstrument.getMusician();
        assertNotEquals(musicianName,musicianName1);
    }

    @Test
    @DisplayName("Instrument not be same")
    public void newMusicalInstrumentNameSet() {
        Set  <MusicalInstrument> instrumentName = musicianInstrument.getMusicalInstruments();
        Set <MusicalInstrument> addingPianoAgain =  new  HashSet<MusicalInstrument>(){{ add(new MusicalInstrument("Piano")); }};
        musicianInstrument.setMusicalInstruments(addingPianoAgain);
        assertEquals(1, musicianInstrument.getMusicalInstruments().size());
    }

    @Test
    public void musicianInstrumentComparisonWithDifferentObject() {
        Musician musicianobj = new Musician("Keith");
        Set <MusicalInstrument> musicalInstruments = new HashSet<MusicalInstrument>() {{
            add( new MusicalInstrument("Saxophone"));
        }};
        MusicianInstrument musicianInstrument = new MusicianInstrument(musicianobj,musicalInstruments);
        assertFalse(musicianInstrument.equals("object"));
    }

    @Test
    public void musicianInstrumentComparisonWithNullObject() {
        Musician musicianobj = new Musician("Keith");
        Set <MusicalInstrument> musicalInstruments = new HashSet<MusicalInstrument>() {{
            add( new MusicalInstrument("Saxophone"));
        }};
        MusicianInstrument musicianInstrument = new MusicianInstrument(musicianobj,musicalInstruments);
        assertFalse(musicianInstrument.equals(null));
    }

    @Test
    public void musicianInstrumentComparisonWithSameObject() {
        Musician musicianobj = new Musician("Keith");
        Set <MusicalInstrument> musicalInstruments = new HashSet<MusicalInstrument>() {{
            add( new MusicalInstrument("Saxophone"));
        }};
        MusicianInstrument musicianInstrument = new MusicianInstrument(musicianobj,musicalInstruments);
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musicianobj,musicalInstruments);
        assertTrue(musicianInstrument.equals(musicianInstrument1));
    }

    @Test
    public void musicianInstrumentComparisonWithDifferentMusician() {
        Musician musicianobj = new Musician("Keith");
        Set <MusicalInstrument> musicalInstruments = new HashSet<MusicalInstrument>() {{
            add( new MusicalInstrument("Saxophone"));
        }};
        MusicianInstrument musicianInstrument = new MusicianInstrument(musicianobj,musicalInstruments);
        Musician musicianobj1 = new Musician("Raj");
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musicianobj1,musicalInstruments);
        assertFalse(musicianInstrument.equals(musicianInstrument1));
    }

    @Test
    public void musicianInstrumentComparisonWithDifferentMusicalInstrument() {
        Musician musicianobj = new Musician("Keith");
        Set <MusicalInstrument> musicalInstruments = new HashSet<MusicalInstrument>() {{
            add( new MusicalInstrument("Saxophone"));
        }};
        MusicianInstrument musicianInstrument = new MusicianInstrument(musicianobj,musicalInstruments);
        Set <MusicalInstrument> musicalInstruments1 = new HashSet<MusicalInstrument>() {{
            add( new MusicalInstrument("Piano"));
        }};
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musicianobj,musicalInstruments1);
        assertFalse(musicianInstrument.equals(musicianInstrument1));
    }
}