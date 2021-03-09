package allaboutecm.dataaccess.neo4j;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.Musician;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DONE: add test cases to adequately test the Neo4jDAO class.
 */
class Neo4jDAOUnitTest {
    private static final String TEST_DB = "target/test-data/test-db.neo4j";

    private static DAO dao;
    private static Session session;
    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void setUp() {
        // See @https://neo4j.com/docs/ogm-manual/current/reference/ for more information.

        // To use an impermanent embedded data store which will be deleted on shutdown of the JVM,
        // you just omit the URI attribute.

        // Impermanent embedded store
        Configuration configuration = new Configuration.Builder().build();

        // Disk-based embedded store
        // Configuration configuration = new Configuration.Builder().uri(new File(TEST_DB).toURI().toString()).build();

        // HTTP data store, need to install the Neo4j desktop app and create & run a database first.
//        Configuration configuration = new Configuration.Builder().uri("http://neo4j:password@localhost:7474").build();

        sessionFactory = new SessionFactory(configuration, Musician.class.getPackage().getName());
        session = sessionFactory.openSession();

        dao = new Neo4jDAO(session);
    }

    @AfterEach
    public void tearDownEach() {
        session.purgeDatabase();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        session.purgeDatabase();
        session.clear();
        sessionFactory.close();
        File testDir = new File(TEST_DB);
        if (testDir.exists()) {
        }
    }

    @Test
    public void daoIsNotEmpty() {
        assertNotNull(dao);
    }

    @Test
    public void successfulCreationAndLoadingOfMusician() throws MalformedURLException {
        assertEquals(0, dao.loadAll(Musician.class).size());

        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        dao.createOrUpdate(musician);
        Musician loadedMusician = dao.load(Musician.class, musician.getId());

        assertNotNull(loadedMusician.getId());
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getMusicianUrl(), loadedMusician.getMusicianUrl());

        assertEquals(1, dao.loadAll(Musician.class).size());
    }

    @Test
    public void successfulCreationOfMusicianAndAlbum() throws MalformedURLException {
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        musician.setAlbums(Sets.newHashSet(album));

        dao.createOrUpdate(album);
        dao.createOrUpdate(musician);

        Collection<Musician> musicians = dao.loadAll(Musician.class);
        assertEquals(1, musicians.size());
        Musician loadedMusician = musicians.iterator().next();
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getMusicianUrl(), loadedMusician.getMusicianUrl());
        assertEquals(musician.getAlbums(), loadedMusician.getAlbums());
    }

    //findAlbumByName   and return album.

    @Test
    public void successfulCreationAndLoadingOfAlbum() {

        assertEquals(0, dao.loadAll(Album.class).size());


        Album album = new Album(2020,"112","MOTS8");

        dao.createOrUpdate(album);
        Album loadedAlbum = dao.load(Album.class, album.getId());

        assertNotNull(loadedAlbum.getId());
        assertEquals(album, loadedAlbum);
        assertEquals(1, dao.loadAll(Album.class).size());

    }

    @Test
    public void successfullyGetAlbumByYear() {
        assertEquals(0, dao.loadAll(Album.class).size());
        Album album = new Album(2020,"112","MOTS8");

        dao.createOrUpdate(album);
        Collection<Album> albumByYear = dao.findAlbumsByYear(2020);

        assertEquals(1,albumByYear.size());
    }
    //whenYearIsPassedAsNullOrEmptyShouldReturnNull

    @Test
    public void whenMatchedAlbumDoesntExistShouldReturnEmptyList() {
        assertEquals(0, dao.loadAll(Album.class).size());
        Album album = new Album(2020,"112","MOTS8");

        dao.createOrUpdate(album);
        Collection<Album> albumByYear = dao.findAlbumsByYear(2021);

        assertEquals(0,albumByYear.size());
    }

    @Test
    public void whenYearIsNegativeOrZeroShouldThrowException() {
        //If the given year is zero or lesser than Zero. from the code we are throwing illegal argument exception as it is invalid year
        assertThrows(IllegalArgumentException.class, () -> dao.findAlbumsByYear(-2020) );
    }

    @Test
    public void returnNullWhenAlbumNameIsEmpty() {
        Album foundAlbum = dao.findAlbumByName("");
        assertNull(foundAlbum);
    }

    @Test
    public void returnNullWhenAlbumNameIsNull() {
        Album foundAlbum = dao.findAlbumByName(null);
        assertNull(foundAlbum);
    }

    @Test
    public void successfulCreationOfAlbumInDBAndFindAlbumByName() {
        assertEquals(0, dao.loadAll(Album.class).size());
        Album album = new Album(2020,"112","MOTS8");
        dao.createOrUpdate(album);
        Album foundAlbum = dao.findAlbumByName("MOTS8");
        assertEquals(album, foundAlbum);
    }

    @Test
    public void successfulCreationOfAlbumInDBAndFindAlbumByNameIrrespectiveOfCase() {
        assertEquals(0, dao.loadAll(Album.class).size());
        Album album = new Album(2020,"112","RASH");
        dao.createOrUpdate(album);
        Album foundAlbum = dao.findAlbumByName("rash");
        assertEquals(album, foundAlbum);
    }

    @Test
    public void successfulCreationOfAlbumInDBAndFindAlbumByNameForEmptyValues() {
        assertEquals(0, dao.loadAll(Album.class).size());
        Album album = new Album(2020,"112","MOTS8");
        dao.createOrUpdate(album);
        Album foundAlbum = dao.findAlbumByName("");
        assertNull(foundAlbum);
    }

    @Test
    public void successfulCreationOfAlbumInDBAndFindAlbumByNameForNullValues() {
        assertEquals(0, dao.loadAll(Album.class).size());
        Album album = new Album(2020, "112", "MOTS8");
        dao.createOrUpdate(album);
        Album foundAlbum = dao.findAlbumByName(null);
        assertNull(foundAlbum);
    }

    // Testing  findMusicalInstrumentByName instrument method by adding a record and searching for musical instrument
    @Test
    public void createOrUpdateInstrumentNameAndCheckIfInstrumentAdded() {
        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());
        MusicalInstrument musicalInstrument = new MusicalInstrument();

        musicalInstrument.setName("Piano");

        dao.createOrUpdate(musicalInstrument);
        MusicalInstrument instrumentName = dao.findMusicalInstrumentByName("Piano");

        assertEquals(musicalInstrument,instrumentName);
    }

    // Testing  findAlbumByRecordNumber to check if record exists or new record can be created
    @Test
    public void createOrUpdateRecordNumberAndCheckIfRecordExists() {
        assertEquals(0, dao.loadAll(Album.class).size());

        Album album = new Album(2020,"112","MOTS8");

        dao.createOrUpdate(album);
        Album albumByRecordNumber = dao.findAlbumByRecordNumber("112");

        assertEquals(album,albumByRecordNumber);
    }



    // To add or update album url
    @Test
    public void  createOrUpdateAlbumUrl() throws MalformedURLException{
        Album album = new Album(2020,"112","MOTS8");
        album.setAlbumURL(new URL("https://www.keithjarrett.org/"));
        dao.createOrUpdate(album);
        Album getAlbum = dao.findAlbumByURL(new URL("https://www.keithjarrett.org/"));
        assertEquals(album,getAlbum);
    }

    @Test
    public void  returnNullWhenAlbumURLNotFound() throws MalformedURLException {
        Album album = new Album(2020, "112", "MOTS8");
        album.setAlbumURL(new URL("https://www.keithjarrett.org/"));
        dao.createOrUpdate(album);
        Album getAlbum = dao.findAlbumByURL(new URL("https://www.jk.org/"));
        assertNull(getAlbum);
    }


        @Test
    public void  createOrUpdateMusicianByName() throws MalformedURLException{
        Musician musician = new Musician("becky");
        musician.setMusicianUrl(new URL("https://www.boxigo.in/"));
        dao.createOrUpdate(musician);
        Musician getMusician = dao.findMusicianByName("becky");
        assertEquals(musician,getMusician);
    }

    @Test
    public void  returnNullMusicianNameNotFound() throws MalformedURLException{
        Musician musician = new Musician("ironman");
        musician.setMusicianUrl(new URL("https://www.boxigo.in/"));
        dao.createOrUpdate(musician);
        Musician getMusician = dao.findMusicianByName("becky");
        assertNull(getMusician);
    }

    @Test
    public void  createOrUpdateMusicianByAlbum() throws MalformedURLException{
        Musician musician = new Musician("becky");
        musician.setMusicianUrl(new URL("https://www.becky.org/"));

        Album album = new Album(1992, "t 101", "The becky");
        musician.setAlbums(Sets.newHashSet(album));

        dao.createOrUpdate(album);
        dao.createOrUpdate(musician);

        Album albumByRecordNumber = dao.findAlbumByRecordNumber("The becky");
        Collection<Musician> musicians1 = dao.loadAll(Musician.class);
        assertEquals(1, musicians1.size());
        Musician loadedMusician = musicians1.iterator().next();
        assertEquals(musician.getAlbums(), loadedMusician.getAlbums());
        if (musician.getAlbums() == loadedMusician.getAlbums()) {
            assertEquals(musician.getName(), loadedMusician.getName());
        }
    }
    @Test

    public void successfulSearchOfAlbumByReleaseYear() throws MalformedURLException {

        int releaseYear = 1975;

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");

        dao.createOrUpdate(album);


        Album loadedAlbum = dao.findAlbumByReleaseYear(releaseYear);

        assertEquals(loadedAlbum, album);

    }


    @Test

    public void successfulSearchOfAlbumByRecordNumber() throws MalformedURLException {

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");

        dao.createOrUpdate(album);

        Album loadedAlbum = dao.findAlbumByRecordNumber("ECM 1064/65");

        assertEquals(loadedAlbum, album);

    }

    @Test

    public void successfulSearchOfMusicianByName() throws MalformedURLException {

        Musician musician = new Musician("Keith Jarrett");

        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        dao.createOrUpdate(musician);

        Musician loadedMusician = dao.findMusicianByName("Keith Jarrett");

        assertEquals(loadedMusician, musician);
    }

    @Test
    public void successfulSearchOfMusicianByURL() throws MalformedURLException {

        Musician musician = new Musician("Keith Jarrett");

        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        dao.createOrUpdate(musician);

        Musician loadedMusician = dao.findMusicianByURL(new URL("https://www.keithjarrett123.org/"));

        assertNull(loadedMusician);

    }

    @Test
    public void returnNullWhenMusicianUrlNotFound()  throws MalformedURLException {

        Musician musician = new Musician("Keith Jarrett");

        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        dao.createOrUpdate(musician);

        Musician loadedMusician = dao.findMusicianByURL(new URL("https://www.keithjarrett123.org/"));

        assertNull(loadedMusician);

    }



    @Test

    public void successfulSearchOfAlbumByName() throws MalformedURLException {

        String albumName = "The Köln Concert";

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");

        dao.createOrUpdate(album);

        Album loadedAlbum = dao.findAlbumByName(albumName);

        assertEquals(loadedAlbum, album);

    }


    @Test
    public void createOrUpdateMusicianByRecordNumber() throws MalformedURLException {
        Musician musician = new Musician("becky");
        musician.setMusicianUrl(new URL("https://www.becky.org/"));

        Album album = new Album(1992, "t 101", "The becky");
        musician.setAlbums(Sets.newHashSet(album));

        dao.createOrUpdate(album);
        dao.createOrUpdate(musician);

        Album albumByRecordNumber = dao.findAlbumByRecordNumber("t 101");
        System.out.println(albumByRecordNumber.getRecordNumber());
        assertEquals(album,albumByRecordNumber);
        System.out.println(musician.getAlbums());
        Collection<Album> albumtest = musician.getAlbums();
        for (Album alb : albumtest)
            assertEquals(alb.getRecordNumber(),albumByRecordNumber.getRecordNumber());
    }


    @Test
    public void shouldUpdateAlbumAttributeSuccessfully() throws MalformedURLException {
        Album album = new Album(2014, "Test123", "Be the Best");
        album.setAlbumURL(new URL("https://www.ecmrecords.com/artists/1435342332445/keith-jarrettsss"));

        Album checkAlbum = dao.createOrUpdate(album);
        assertNotNull(checkAlbum.getId());
        assertEquals(album, checkAlbum);
        URL newURL = new URL("https://www.ecmrecords.com/artists/1435045745/keith-jarrett");
        album.setAlbumURL(newURL);
        dao.createOrUpdate(album);

        checkAlbum = dao.load(Album.class, album.getId());
        assertEquals(newURL, checkAlbum.getAlbumURL());
    }

    @Test
    public void shouldNotSaveTwoSameAlbums() {
        Album album1 = new Album(2014, "Test123", "Be the Best");
        Album album2 = new Album(2014, "Test123", "Be the Best");

        assertEquals(album1, album2);
        dao.createOrUpdate(album1);
        Collection<Album> albums = dao.loadAll(Album.class);
        assertEquals(1, albums.size());
        dao.createOrUpdate(album2);
        albums = dao.loadAll(Album.class);
        assertEquals(1, albums.size());
    }

    @Test
    public void shouldNotSaveTwoSameMusicalInstruments() {
        MusicalInstrument musicalinstrument1 = new MusicalInstrument("Violin");
        MusicalInstrument musicalinstrument2 = new MusicalInstrument( "Violin");

        assertEquals(musicalinstrument1, musicalinstrument2);
        dao.createOrUpdate(musicalinstrument1);
        Collection<MusicalInstrument> instruments = dao.loadAll(MusicalInstrument.class);
        assertEquals(1, instruments.size());
        dao.createOrUpdate(musicalinstrument2);
        instruments = dao.loadAll(MusicalInstrument.class);
        assertEquals(1, instruments.size());
    }



    @Test
    public void shouldDeleteAlbum() {
        Album album = new Album(2018, "Testing123", "Monash Madness");
        dao.createOrUpdate(album);

        assertNotNull(album.getId());
        assertFalse(dao.loadAll(Album.class).isEmpty());

        dao.delete(album);
    }

    @Test
    public void shouldDeleteMusician() {
        Musician musician = new Musician("Keith Jarrett");
        dao.createOrUpdate(musician);

        assertNotNull(musician.getId());
        assertFalse(dao.loadAll(Musician.class).isEmpty());

        dao.delete(musician);
    }
}