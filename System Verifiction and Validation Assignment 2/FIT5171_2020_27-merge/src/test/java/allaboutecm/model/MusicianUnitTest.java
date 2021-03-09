package allaboutecm.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Rebecca Christi - 30824516
 * The following code base contains constraints for unit testing class - Musician
 * Unit test is written to show the behavior and logic of the component under test.
 * Various behaviors such as catching exceptions on Null,Blank,Special Character,Numeric etc..
 * methods such as "setName" and "setMusicianUrl" has been extended in the main class to derive better unit tests.
 */

public class MusicianUnitTest {
    private Musician musician;
    private Album album;

    private static final String URL_REGEX =
            "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
                    "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
                    "([).!';/?:,][[:blank:]])?$";
    private String url;

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    @BeforeEach
    public void setUp() throws MalformedURLException {
        URL url = new URL("https://www.google.co.in/?gfe_rd=cr&ei=ptYqWK26I4fT8gfth6CACg#q=geeks+for+geeks+java");
        musician = new Musician("Becky");
        album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        musician.setMusicianUrl(url);
    }

    @ParameterizedTest
    @ValueSource(strings = {"345", "test123", "$#@$#"})
    @DisplayName("Musician Name cannot be Numbers or AlphaNumeric or Special Characters")
    public void MusicianNameCannotBeNumbersOrAlphaNumericOrSpecialChar(String arg) {
        String pattern = "^[a-z\\s]+$";
        if (Pattern.matches(pattern, arg)) {
            throw new IllegalArgumentException(String.format("The string %s does not match the pattern %s", arg, pattern));
        }
    }


    @Test
    @DisplayName("Album name cannot be null")
    public void AlbumNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setAlbumName(null));
    }


    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Album name cannot be empty or blank")
    public void AlbumNameCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setAlbumName(arg));
    }


    @ParameterizedTest
    @ValueSource(strings = {"$#@$#"})
    @DisplayName("Album name cannot be a Special Characters")
    public void AlbumNameCannotBeSpecialChar(String arg) {
        String pattern = "[a-z^0-9\\s]+$";
        if (Pattern.matches(pattern, arg)) {
            throw new IllegalArgumentException(String.format("The string %s does not match the pattern %s", arg, pattern));
        }
    }


    @Test
    @DisplayName("Record Number cannot be null")
    public void RecordNumberCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setRecordNumber(null));
    }


    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Record Number cannot be empty or blank")
    public void RecordNumberCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setRecordNumber(arg));
    }


    @ParameterizedTest
    @ValueSource(strings = {"$#@$#"})
    @DisplayName("Record Number cannot be a Special Characters")
    public void RecordNumberCannotBeSpecialChar(String arg) {
        String pattern = "[a-z^0-9\\s]+$";
        if (Pattern.matches(pattern, arg)) {
            throw new IllegalArgumentException(String.format("The string %s does not match the pattern %s", arg, pattern));
        }
    }


    @ParameterizedTest
    @ValueSource(ints = {-1000})
    @DisplayName("Release Year cannot be negative")
    public void ReleaseYearCannotBeNegative(int arg) {
        if (arg > 0) {
            throw new IllegalArgumentException(String.format("The string %s does not match the pattern %s", arg));
        }
    }


    @Test
    public void SameNameAndNumberMeansSameAlbum() {
        Album b1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        assertEquals(album, b1);
    }

    @Test
    @DisplayName("Musician URL Cannot be null")
    public void MusicianUrlCannotBeNull() {
        NullPointerException except = assertThrows(NullPointerException.class, () -> musician.setMusicianUrl(null));
        assertEquals("The validated object is null", except.getMessage());

    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Musician URL cannot be empty or blank")
    public void MusicianUrlCannotBeEmptyOrBlank(String arg) {
        NullPointerException except = assertThrows(NullPointerException.class, () -> musician.setMusicianUrl(null));
        assertEquals("The validated object is null", except.getMessage());
    }
    @Test
    @DisplayName("Musician Comparison with different object")
    public void musicianComparisonWithDifferentObject() {
        Musician musician = new Musician("Keith");
        assertFalse(musician.equals(" object"));
    }

    @Test
    @DisplayName("Musician Comparison with null object")
    public void musicianComparisonWithNullObject() {
        Musician musician = new Musician("Keith");
        assertFalse(musician.equals(null));
    }

    @Test
    @DisplayName("Musician Comparison")
    public void musicianComparison() {
        Musician musician = new Musician("Keith");
        Musician musician1 = new Musician("Keith");
        assertTrue(musician.equals(musician1));
    }

    @Test
    @DisplayName("Musician Comparison with different Name")
    public void musicianComparisonWithDifferentNames() {
        Musician musician = new Musician("Keith");
        Musician musician1 = new Musician("Raj");
        assertFalse(musician.equals(musician1));
    }

    public static boolean urlValidator(String url) {
        if (url == null) {
            return false;
        }
        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }
   @Test
    @DisplayName("URL should be valid")
    public void urlShouldBeValid() throws IllegalArgumentException {
        url = "https://www.google.com.au";
        assertTrue(urlValidator(url));
    }

    @Test
    public void musicianComparisonSameClassDifferentAlbumName() {
        Musician musician = new Musician("Becky");
        Set<Album> albums =  new  HashSet<Album>(){{ add(new Album(2017,"111", "MOTS7")); }};
        musician.setAlbums(albums);
        Musician  musician1 = new Musician("Becky");
        Set<Album> albums1 =  new  HashSet<Album>(){{ add(new Album(2017,"111", "BAM")); }};
        musician.setAlbums(albums1);
        assertFalse(musician.equals(musician1));
    }
}
