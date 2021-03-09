package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.Musician;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * DONE: perform integration testing of both ECMMiner and the DAO classes together.
 */
class ECMMinerIntegrationTest {
    private static final String TEST_DB = "target/test-data/test-db.neo4j";

    private static DAO dao;

    private static Session session;
    private static SessionFactory sessionFactory;
    private static ECMMiner ecmMiner;
    private DAO daoE;
    @BeforeEach
    public void setUpDAO() {

        daoE = mock(Neo4jDAO.class);
        ecmMiner = new ECMMiner(dao);
    }

    @BeforeAll
    public static void setUp() {
        Configuration configuration = new Configuration.Builder().build();
        sessionFactory = new SessionFactory(configuration, Musician.class.getPackage().getName());
        session = sessionFactory.openSession();
        dao = new Neo4jDAO(session);
        ecmMiner = new ECMMiner(dao);
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
    public void  createOrUpdateAlbumUrl() throws MalformedURLException {
        Album album = new Album(2020,"112","MOTS8");
        album.setAlbumURL(new URL("https://www.keithjarrett.org/"));
        dao.createOrUpdate(album);
        Album getAlbum = dao.findAlbumByURL(new URL("https://www.keithjarrett.org/"));
        assertEquals(album,getAlbum);
    }

    @Test
    @DisplayName("Check if we are getting multiple years for busiest")
    public void checkForMultipleBusiestYear()
    {
        Album album = new Album(1970,"ECM 1000/63","Beatles");
        Album album1 = new Album(1971,"ECM 1071/64","Thousand Suns");
        Album album2 = new Album(1980,"ECM 1064/65","The Köln Concert");
        Album album3 = new Album(1980,"ECM 1064/65","The Köln Concert");
        Album album4 = new Album(1980,"ECM 1064/65","The Köln Concert");

        dao.createOrUpdate(album);
        dao.createOrUpdate(album1);
        dao.createOrUpdate(album2);
        dao.createOrUpdate(album3);
        dao.createOrUpdate(album4);

        List<Integer> albums = ecmMiner.busiestYears(5);
        assertEquals(3,albums.size());
    }


    @ParameterizedTest
    @ValueSource(ints = {-1,0})
    @DisplayName("Year can not be negative or zero")
    public void busiestYearCanNotBeNegativeOrZero(int k)
    {
        Album album = new Album(1970,"ECM 1000/63","Beatles");
        Album album1 = new Album(1971,"ECM 1071/64","Thousand Suns");
        Album album2 = new Album(1980,"ECM 1064/65","The Köln Concert");

        dao.createOrUpdate(album);
        dao.createOrUpdate(album1);
        dao.createOrUpdate(album2);

        List<Integer> albums = ecmMiner.busiestYears(k);
        assertEquals(0,albums.size());
    }

    // checking for invalid argument (negative or zero) entered for year
    @ParameterizedTest
    @ValueSource(ints = {-10,-1,0})
    @DisplayName("Checking if invalid argument is entered")
    public void yearForMostSimilarAlbumAndMusicianCanNotBeNegativeOrEmpty(int k)
    {
        Musician musician = new Musician("Norman Chapman");
        Album album = new Album(1970,"ECM 1000/63","Beatles");
        Musician musician1 = new Musician("Mike Shinoda");
        Album album1 = new Album(1971,"ECM 1071/64","Thousand Suns");
        Musician musician2 = new Musician("Keith Jarrett");
        Album album2 = new Album(1980,"ECM 1064/65","The Köln Concert");

        album.setFeaturedMusicians(Lists.newArrayList(musician,musician1));
        album1.setFeaturedMusicians(Lists.newArrayList(musician,musician1,musician2));

        dao.createOrUpdate(album1);
        dao.createOrUpdate(album2);

        List<Album> albumsYear = ecmMiner.mostSimilarAlbums(k,album);
        assertEquals(0,albumsYear.size());
    }


    @Test
    @DisplayName("Checking for most similar albums Name")
    public void checkingForAlbumsWithSimilarAlbumName()
    {
        Album album1 = new Album(1970,"ECM 1000/63","Beatles");
        Album album2 = new Album(1971,"ECM 1071/64","Thousand Suns");
        Album album3 = new Album(1980,"ECM 1064/65","Beatles");
        dao.createOrUpdate(album1);
        dao.createOrUpdate(album2);
        dao.createOrUpdate(album3);
        String commonAlbum = ecmMiner.mostPlayedAlbums();
        assertTrue(album1.getAlbumName().matches(commonAlbum) ||
                album2.getAlbumName().matches(commonAlbum)||
                album3.getAlbumName().matches(commonAlbum));
    }
    @Test
    public void shouldSearchAlbumByRecordNumberAndReturnBusiestYears() throws MalformedURLException {

        List<Album> albums = Lists.newArrayList();

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        albums.add(album);

        album = new Album(1976, "ECM 1064/67", "The Köln Concert1");
        dao.createOrUpdate(album);
        albums.add(album);

        album = new Album(1977, "ECM 1064/66", "The Köln Concert2");
        dao.createOrUpdate(album);
        albums.add(album);

        album = new Album(1978, "ECM 1064/61", "The Köln Concert3");
        dao.createOrUpdate(album);
        albums.add(album);

        album = new Album(1979, "ECM 1064/62", "The Köln Concert4");
        dao.createOrUpdate(album);
        albums.add(album);

        album = new Album(1971, "ECM 1064/63", "The Köln Concert5");
        dao.createOrUpdate(album);
        albums.add(album);

        album = new Album(1972, "ECM 1064/69", "The Köln Concert6");
        dao.createOrUpdate(album);
        albums.add(album);

        Album loadedAlbum = dao.findAlbumByRecordNumber("ECM 1064/69");
        assertEquals(album, loadedAlbum);

    }


}