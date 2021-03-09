package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.Musician;
import allaboutecm.model.MusicianInstrument;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DONE: perform unit testing on the ECMMiner class, by making use of mocking.
 */
class ECMMinerUnitTest {
    private DAO dao;
    private ECMMiner ecmMiner;
    @BeforeEach
    public void setUp() {
        dao = mock(Neo4jDAO.class);
        ecmMiner = new ECMMiner(dao);
    }
    @Test
    public void shouldReturnTheMusicianWhenThereIsOnlyOne() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album));
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician));
        List<Musician> musicians = ecmMiner.mostProlificMusicians(5, -1, -1);
        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician));
    }

    @Test
    public void shouldReturnMostSoldAlbum() {

        List<Album> albums = Lists.newArrayList();

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setNumberOfCopiesSold(20);
        albums.add(album);

        album = new Album(1976, "ECM 1064/65", "The Köln Concert");
        album.setNumberOfCopiesSold(30);
        albums.add(album);

        album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setNumberOfCopiesSold(40);
        albums.add(album);

        album = new Album(1974, "ECM 1064/65", "The Köln Concert");
        album.setNumberOfCopiesSold(50);
        albums.add(album);

        album = new Album(1973, "ECM 1064/65", "The Köln Concert");
        album.setNumberOfCopiesSold(60);
        albums.add(album);

        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album));

        Album album1 = ecmMiner.mostSoldAlbum(albums);

        assertEquals(1973, album1.getReleaseYear());
    }




    //unit test case to check higest rated album, checking based on rating from 1-5 (1 lowest , 5 highest)

    @Test
    public void  whenDataIsCorrectShouldReturnMusicians() {
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(
                new  MusicianInstrument(
                        new Musician("Keith Jarett"),
                        new HashSet<MusicalInstrument>() {{
                            add( new MusicalInstrument("Saxophone"));
                            add( new MusicalInstrument("Keyboard"));
                            add( new MusicalInstrument("Piano"));
                        }}
                ) ,
                new  MusicianInstrument(
                        new Musician("Kane Kim"),
                        new HashSet<MusicalInstrument>() {{
                            add( new MusicalInstrument("Saxophone"));
                            add( new MusicalInstrument("Keyboard"));
                            add( new MusicalInstrument("Piano"));
                        }}
                )
        ));
        List<Musician> results = ecmMiner.mostTalentedMusicians(2);
        assertEquals(2,results.size());
    }


    @Test
    public void  successfullyIgnoreDuplicateMusicianAndCreateSingleRecord() {
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(
                new  MusicianInstrument(
                        new Musician("Keith Jarett"),
                        new HashSet<MusicalInstrument>() {{
                            add( new MusicalInstrument("Saxophone"));
                            add( new MusicalInstrument("Keyboard"));
                            add( new MusicalInstrument("Piano"));
                        }}
                ) ,
                new  MusicianInstrument(
                        new Musician("Keith Jarett"),
                        new HashSet<MusicalInstrument>() {{
                            add( new MusicalInstrument("Saxophone"));
                            add( new MusicalInstrument("Keyboard"));
                            add( new MusicalInstrument("Piano"));
                            add( new MusicalInstrument("Bass"));
                        }})));
        List<Musician> results = ecmMiner.mostTalentedMusicians(3);
        assertEquals(1,results.size());
    }
    @Test
    public void shouldHighestRatedAlbum() {

        List<Album> albums = Lists.newArrayList();

        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setHighestRating(3);
        albums.add(album);

        album = new Album(1976, "ECM 1064/65", "The Köln Concert");
        album.setHighestRating(4);
        albums.add(album);

        album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setHighestRating(2);
        albums.add(album);

        album = new Album(1974, "ECM 1064/65", "The Köln Concert");
        album.setHighestRating(5);
        albums.add(album);


        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album));

        Album album1 = ecmMiner.higestRatedAlbum(albums);

        assertEquals(1974, album1.getReleaseYear());
    }

    @Test
    public void  successfullyIgnoreDuplicateMusicianAndMusicalInstrumentsAndCreatingSingleRecord() {
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(
                new  MusicianInstrument(
                        new Musician("Keith Jarett"),
                        new HashSet<MusicalInstrument>() {{
                            add( new MusicalInstrument("Saxophone"));
                            add( new MusicalInstrument("Saxophone"));
                            add( new MusicalInstrument("Saxophone"));                        }}
                ) ,
                new  MusicianInstrument(
                        new Musician("Keith Jarett"),
                        new HashSet<MusicalInstrument>() {{
                            add( new MusicalInstrument("Drums"));
                            add( new MusicalInstrument("Keyboard"));
                            add( new MusicalInstrument("Piano"));
                            add( new MusicalInstrument("Bass"));
                        }}
                )));
        List<Musician> results = ecmMiner.mostTalentedMusicians(5);
        assertEquals(0,results.size());
    }
    @Test
    public void shouldReturnMostTalentedMusicians() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album));
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician));

        List<Musician> musicians = ecmMiner.mostTalentedMusicians(5);

        assertTrue(!musicians.contains(musician));
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

        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album,album1,album2,album3,album4));
        List<Integer> albums = ecmMiner.busiestYears(3);
        assertEquals(3,albums.size());
    }

    // checking busiest year recieved is not negative or zero
    @ParameterizedTest
    @ValueSource(ints = {-1,0})
    @DisplayName("Year can not be negative or zero")
    public void busiestYearCanNotBeNegativeOrZero(int k)
    {
        Album album = new Album(1970,"ECM 1000/63","Beatles");
        Album album1 = new Album(1971,"ECM 1071/64","Thousand Suns");
        Album album2 = new Album(1980,"ECM 1064/65","The Köln Concert");

        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album,album1,album2));
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

        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album1));
        List<Album> albumsYear = ecmMiner.mostSimilarAlbums(k,album);
        assertEquals(0,albumsYear.size());
    }

    @Test
    public void shouldReturnMostCommonSocialMusicians() {

        Musician musician = new Musician("test1");

        Album album1 = new Album(1975, "ECM 1064/65", "The Test1 Concert");
        List<Musician> featuredMusicians1 = new ArrayList<Musician>();
        featuredMusicians1.add( new Musician("test1"));
        featuredMusicians1.add( new Musician("test2"));
        album1.setFeaturedMusicians(featuredMusicians1);

        Album album2 = new Album(1975, "ECM 1064/65", "The Test2 Concert");
        List<Musician> featuredMusicians2 =new ArrayList<Musician>();
        featuredMusicians2.add( new Musician("test1"));
        featuredMusicians2.add( new Musician("test3"));
        album2.setFeaturedMusicians(featuredMusicians2);

        Album album3 = new Album(1975, "ECM 1064/65", "The Test3 Concert");
        List<Musician> featuredMusicians3 =new ArrayList<Musician>();
        featuredMusicians3.add( new Musician("test1"));
        featuredMusicians3.add( new Musician("test2"));
        album3.setFeaturedMusicians(featuredMusicians3);

        Album album4 = new Album(1975, "ECM 1064/65", "The Test4 Concert");
        List<Musician> featuredMusicians4 =new ArrayList<Musician>();
        featuredMusicians4.add( new Musician("test1"));
        featuredMusicians4.add( new Musician("test4"));
        album4.setFeaturedMusicians(featuredMusicians4);

        Album album5 = new Album(1975, "ECM 1064/65", "The Test5 Concert");
        List<Musician> featuredMusicians5 =new ArrayList<Musician>();
        featuredMusicians5.add( new Musician("test1"));
        featuredMusicians5.add( new Musician("test5"));
        album5.setFeaturedMusicians(featuredMusicians5);

        List<Album> albums =new ArrayList<Album>();
        albums.add(album1);albums.add(album2);albums.add(album3);albums.add(album4);albums.add(album5);

        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(albums));
        Integer mostSocialMusicians = ecmMiner.mostSocialMusicians();
        assertEquals(5, mostSocialMusicians);
    }
    //methods to populate album and Musician , hence checking the similar albums

    public void addTestDataForSimilarAlbums(List<Album> albumList) {
        try {
            Musician musician_1 = new Musician("Bryan Adams");
            Musician musician_2 = new Musician("Steve Wonder");
            Album album1 = new Album(1978, "ECM 1064/11", "The Köln Concert Updated");
            Album album2 = new Album(1978, "ECM 1064/12", "The Köln Concert Updated");
            Album album3 = new Album(1978, "ECM 1064/13", "The Köln Concert New");
            Album album4 = new Album(1978, "ECM 1064/14", "The Köln Concert New Up");
            List<Musician> musicianList = Lists.newArrayList();
            musicianList.add(musician_1);
            musicianList.add(musician_2);
            List<String> tracks = Lists.newArrayList();
            tracks.add("Track 1");
            tracks.add("Track 2");
            tracks.add("Track 3");
            tracks.add("Track 4");
            List<String> tracksTwo = Lists.newArrayList();
            tracks.add("Track 1");
            tracks.add("Track 2");
            tracks.add("Track 5");
            tracks.add("Track 6");
            album1.setTracks(tracks);
            album3.setTracks(tracksTwo);
            album1.setFeaturedMusicians(musicianList);
            album3.setFeaturedMusicians(musicianList);
            albumList.add(album1);
            albumList.add(album2);
            albumList.add(album4);

        } catch (Exception e) {
            e.printStackTrace();
        }
        when(dao.loadAll(Album.class)).thenReturn(albumList);

    }

   @Test
    @DisplayName("Should return all the albums similar to a given album and equal to k")
    public void shouldReturnAllAlbumsSimilarToGiven() throws Exception {
        List<Album> album_list = Lists.newArrayList();
        addTestDataForSimilarAlbums(album_list);
        Album givenAlbum = new Album(1978, "ECM 1064/59", "The Köln Concert Updated");
        Musician musician_1 = new Musician("Adam Levine");
        Musician musician_2 = new Musician("Keith Jarrett");
        List<Musician> musicianList = Lists.newArrayList();
        musicianList.add(musician_1);
        musicianList.add(musician_2);
        List<String> tracks = Lists.newArrayList();
        tracks.add("Track 1");
        tracks.add("Track 2");
        tracks.add("Track 3");
        tracks.add("Track 4");
        givenAlbum.setTracks(tracks);
        givenAlbum.setFeaturedMusicians(musicianList);
        List<Album> album = ecmMiner.mostSimilarAlbums(2, givenAlbum);
        assertEquals(2, album.size());
    }



}