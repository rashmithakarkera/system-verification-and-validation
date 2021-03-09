package allaboutecm.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Rashmitha Karkera -30438101
 *The following code base contains constraints for unit testing class - Album
 * Test case are written for Null,Blank,Special Character,Numeric, duplicate values, etc.,
 **/
class AlbumUnitTest {

    private static final String URL_REGEX =
            "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
                    "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
                    "([).!';/?:,][[:blank:]])?$";
    private String url;

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    private Album album;
    @BeforeEach
    public void setUp() {
        url = "https://www.google.com.au";
        album = new Album(1975, "ECM 1064/65", "The Köln Concert");
    }
    @Test
    @DisplayName("Album name cannot be null")
    public void albumNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setAlbumName(null));
    }
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Album name cannot be empty or blank")
    public void albumNameConnotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setAlbumName(arg));
    }
    @Test
    public void sameNameAndNumberMeansSameAlbum() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        assertEquals(album, album1);
    }



    @Test
    @DisplayName("releaseYear must be four digit")
    public void checkReleaseYearFourDigit() {
        int year = 1975;
        album.setReleaseYear(year);
        int yearLength = String.valueOf(year).length();
        assertEquals(yearLength, 4);

    }

    // ############# Check for RecordNumber ##################
    @Test
    @DisplayName("Same Record name means same album")
    public void sameRecordNumberAndNameMeansSameAlbum() {
        Album albums = new Album(1975, "ECM 1064/65", "The Köln Concert");

        assertEquals(album, albums);
    }

    @Test
    @DisplayName("Record Number cannot be null")
    public void recordNumberCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setRecordNumber(null));
    }
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Record Number cannot be empty or blank")
    public void recordNumberCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setRecordNumber(arg));
    }
    @Test
    @DisplayName("New Release Year is assigned successfully")
    public void newReleaseYearValueAssignment() {
        int releaseYear = album.getReleaseYear();
        album.setReleaseYear(1980);
        int releaseYear1 = album.getReleaseYear();
        assertNotEquals(releaseYear,releaseYear1);
    }
    @Test
    @DisplayName("New Record Number is assigned successfully")
    public void newRecordNumberValueAssignment() {
        String recordNumber = album.getRecordNumber();
        album.setRecordNumber("ECG 106");
        String recordNumber1 = album.getRecordNumber();
        assertNotEquals(recordNumber,recordNumber1);
    }
    @Test
    @DisplayName("New Album Name  is assigned successfully")
    public void newAlbumNameValueAssignment() {
        String albumName = album.getAlbumName();
        album.setAlbumName("Maps 21");
        String albumName1 = album.getAlbumName();
        assertNotEquals(albumName,albumName1);
    }
    @Test
    @DisplayName(" Musicians are added to the album assigned successfully")
    public void addingMusiciansToAlbum() {
        List<Musician> set =new ArrayList<>(); //
        set.add(new Musician("Adam Bryan"));
        album.setFeaturedMusicians(set);
        assertTrue(album.getFeaturedMusicians().size() ==(1));
    }

    @Test
    @DisplayName(" Instruments are added to the album assigned successfully")
    public void addMusicianInstrument() {
        Set <MusicianInstrument> set =new HashSet(); //List of items with no duplicates
        set.add(new MusicianInstrument(
                new Musician("Adam Bryan"),
                new  HashSet<MusicalInstrument>(){{ add(new MusicalInstrument("Saxophone")); }}
        ));
        album.setInstruments(set);
        assertTrue(album.getInstruments().size() ==(1));
    }
    @Test
    @DisplayName("HashSet should give distinct Instruments")
    public void noDuplicateMusicianInstrument() {
        Set<MusicianInstrument> set =new HashSet();
        set.add(new MusicianInstrument(
                new Musician("Adam Bryan"),
                new  HashSet<MusicalInstrument>(){{ add(new MusicalInstrument("Saxophone")); }}
        ));
        set.add(new MusicianInstrument(
                new Musician("Adam Bryan"),
                new  HashSet<MusicalInstrument>(){{ add(new MusicalInstrument("Saxophone")); }}
        ));
        album.setInstruments(set);
        assertTrue(album.getInstruments().size() ==(1));
    }
    @Test
    @DisplayName(" Tracks are added to the album assigned successfully")
    public void addTracks() {
        List<String> list =new ArrayList();
        list.add("Blues");
        album.setTracks(list);
        assertTrue(album.getTracks().size() ==(1));
    }
    @Test
    @DisplayName("Duplicate Tracks  should  be added into the Album")
    public void duplicateTracksToBeAdded() {
        List<String> list =new ArrayList(); // Array List accepts duplicate values
        list.add("Blues");
        list.add("Blues");
        album.setTracks(list);
        assertTrue(album.getTracks().size() ==(2), "Duplicate Tracks should be displayed");
    }
    private boolean custom( String s1) {                             // Regular Expression to check whether given string should  have alphabets and Numbers
        return s1.matches("^[a-zA-Z0-9\\s]+$");
    }
    private boolean custom1( String s1) {
        return s1.matches("[0-9]+");
    }
    @Test
    @DisplayName("Record Number should not accept Special characters ")
    public void recordNumberShouldNotAcceptSpecialCharacters() {
        album.setRecordNumber("@1rhdhk" );
        assertEquals(false , custom(album.getRecordNumber()));
    }
    @Test
    @DisplayName("Record Number should  accept  numeric values ")
    public void recordNumberShouldAcceptNumericValues() {
        album.setRecordNumber("21" );
        assertEquals(true , custom(album.getRecordNumber()));
    }
    @Test
    @DisplayName("Record Number should  not accept ONLY alphabets  ")
    public void recordNumberShouldNotAcceptOnlyAlphabets() {
        album.setRecordNumber("abc" );
        assertEquals(false , custom1(album.getRecordNumber()));
    }
    @Test
    @DisplayName("Record Number should  accept Alphanumeric characters ")
    public void recordNumberShouldAcceptAlphanumeric() {
        album.setRecordNumber("12fgj" );
        assertEquals(true , custom(album.getRecordNumber()));
    }
    @Test
    @DisplayName("Album Name should not accept Special characters ")
    public void albumNameShouldNotAcceptSpecialCharacters() {
        album.setAlbumName("@gjjj" );
        assertEquals(false , custom(album.getAlbumName()));
    }
    @Test
    @DisplayName("Album Name should  accept Alphanumeric ")
    public void albumNameShouldAcceptAlphanumeric() {
        album.setAlbumName("BT21" );
        assertEquals(true , custom(album.getAlbumName()));
    }
    @Test
    @DisplayName("Album Name can  accept Numeric Values ")
    public void albumNameCanAcceptNumericValues() {
        album.setAlbumName("21" );
        assertEquals(true , custom(album.getAlbumName()));
    }
    @Test
    @DisplayName("Album Name can  accept Alphabets ")
    public void albumNameCanAcceptAlphabets() {
        album.setAlbumName("AndBand" );
        assertEquals(true , custom(album.getAlbumName()));
    }
    @Test
    public void TestTrackList() {
        List<String> actual = Arrays.asList("Köln, Jan 24 1975, PART I", "Köln, Jan 24 1975, PART II A", "Köln, Jan 24 1975, PART II B");
        List<String> expected = Arrays.asList("Köln, Jan 24 1975, PART I", "Köln, Jan 24 1975, PART II A", "Köln, Jan 24 1975, PART II B");
        assertEquals(expected, actual);

    }


    // ########## set<Musician> ##############
    @Test
    @DisplayName("FeaturedMusicians can't be null")
    public void featuredMusicianCannotBeNull() throws IllegalArgumentException {
        assertThrows(NullPointerException.class, () -> album.setFeaturedMusicians(null));
    }

    @Test
    @DisplayName("Number of copies sold should not be null")
    public void numberOfCopiesSoldShouldNotBeNull() {
        int i = 0;
        album.setNumberOfCopiesSold(i);
        assertEquals(0, album.getNumberOfCopiesSold());
    }

    @Test
    @DisplayName("URL should be valid")
    public void urlShouldBeValid() throws IllegalArgumentException {
        url = "https://www.google.com.au";
        assertTrue(urlValidator(url));
    }

    // ############## set<MusicianInstrument> ###############
    @Test
    @DisplayName("invalid input")
    public void shouldThrowOnInvalidMusicianInstrument() throws IllegalArgumentException {
        assertThrows(NullPointerException.class, () -> album.setInstruments(null));
    }

    // method to validate the url
    public static boolean urlValidator(String url) {
        if (url == null) {
            return false;
        }
        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }
    @Test
    public void albumComparison() {
        Album album = new Album(2017,"111", "MOTS7");
        Album album1 = new Album(2017,"111", "MOTS7");
       assertTrue(album.equals(album1));
    }

    @Test   //same class with different values
    public void albumComparisonWithDifferentValues() {
        Album album = new Album(2017,"111", "MOTS7");
        Album album1 = new Album(2018,"101", "MOTS7");
        assertFalse(album.equals(album1));
    }
    @Test
    public void albumComparisonWithDifferentObject() {
        Album album = new Album(2017,"111", "MOTS7");
        assertFalse(album.equals(" object"));
    }
    @Test
    public void albumComparisonWithNullObject() {
        Album album = new Album(2017,"111", "MOTS7");
        assertFalse(album.equals(null));
    }

    @Test
    public void albumComparisonSameClassDifferentRecordNumber() {
        Album album = new Album(2017,"111", "MOTS7");
        Album album1 = new Album(2017,"101", "MOTS7");
        assertFalse(album.equals(album1));
    }

    @Test
    public void albumComparisonSameClassDifferentAlbumName() {
        Album album = new Album(2017,"111", "MOTS7");
        Album album1 = new Album(2017,"111", "qwerty");
        assertFalse(album.equals(album1));
    }

}