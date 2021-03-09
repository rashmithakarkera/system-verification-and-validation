package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.Musician;
import allaboutecm.model.MusicianInstrument;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;


/**
 * DONE: implement and test the methods in this class.
 * Note that you can extend the Neo4jDAO class to make implementing this class easier.
 */
public class ECMMiner {
    private static Logger logger = LoggerFactory.getLogger(ECMMiner.class);

    private final DAO dao;

    public ECMMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * Returns the most prolific musician in terms of number of albums released.
     *
     * @Param k the number of musicians to be returned.
     * @Param startYear, endYear between the two years [startYear, endYear].
     * When startYear/endYear is negative, that means startYear/endYear is ignored.
     */
    public List<Musician> mostProlificMusicians(int k, int startYear, int endYear) {
        Collection<Musician> musicians = dao.loadAll(Musician.class);
        Map<String, Musician> nameMap = Maps.newHashMap();
        for (Musician m : musicians) {
            nameMap.put(m.getName(), m);
        }

        ListMultimap<String, Album> multimap = MultimapBuilder.treeKeys().arrayListValues().build();
        ListMultimap<Integer, Musician> countMap = MultimapBuilder.treeKeys().arrayListValues().build();

        for (Musician musician : musicians) {
            Set<Album> albums = musician.getAlbums();
            for (Album album : albums) {
                boolean toInclude =
                        !((startYear > 0 && album.getReleaseYear() < startYear) ||
                                (endYear > 0 && album.getReleaseYear() > endYear));

                if (toInclude) {
                    multimap.put(musician.getName(), album);
                }
            }
        }

        Map<String, Collection<Album>> albumMultimap = multimap.asMap();
        for (String name : albumMultimap.keySet()) {
            Collection<Album> albums = albumMultimap.get(name);
            int size = albums.size();
            countMap.put(size, nameMap.get(name));
        }

        List<Musician> result = Lists.newArrayList();
        List<Integer> sortedKeys = Lists.newArrayList(countMap.keySet());
        sortedKeys.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys) {
            List<Musician> list = countMap.get(count);
            if (list.size() >= k) {
                break;
            }
            if (result.size() + list.size() >= k) {
                int newAddition = k - result.size();
                for (int i = 0; i < newAddition; i++) {
                    result.add(list.get(i));
                }
            } else {
                result.addAll(list);
            }
        }
        return result;
    }

    /**
     * Most talented musicians by the number of different musical instruments they play
     *
     * @Param k the number of musicians to be returned.
     */
    public List<Musician> mostTalentedMusicians(int k) {
        Collection<MusicianInstrument> allMusicianInstruments = dao.loadAll(MusicianInstrument.class);

        List<Musician> talentedMusicians = new ArrayList<>();   //creating bucket to return
        Map<Musician, Set<MusicalInstrument>> distinctMusicianInstruments = Maps.newHashMap(); //map is for extraction and saving distinct

        /**
         *  Forming distinct Musicians  List having distinct musical instruments
         *  */
        for (MusicianInstrument mi : allMusicianInstruments) {

            if(!distinctMusicianInstruments.containsKey(mi.getMusician()))
            {
                distinctMusicianInstruments.put(mi.getMusician(), mi.getMusicalInstruments());  //  here we are forming distinct musicians list
            }
            else {
                Set <MusicalInstrument> oldInstruments = distinctMusicianInstruments.get(mi.getMusician());
                Set <MusicalInstrument> newInstruments = mi.getMusicalInstruments();
                Set <MusicalInstrument> mergedInstruments = new HashSet<MusicalInstrument>()
                {
                    {
                        addAll(oldInstruments) ;
                        addAll(newInstruments);
                    }
                };

                distinctMusicianInstruments.put(mi.getMusician(),mergedInstruments);    // if musicians plays more instruments and if there are duplicates records playing different instruments . merginng instruments for the same musicians .
            }
        }

        for(Musician m : distinctMusicianInstruments.keySet())
        {
            if(distinctMusicianInstruments.get(m).size() > k)
            {

                talentedMusicians.add(m);
            }
        }
        return talentedMusicians;
    }


    /**
     * Musicians that collaborate the most widely, by the number of other musicians they work with on albums.
     *
     * @Param k the number of musicians to be returned.
     */
    public List<Musician> mostSocialMusicians(int k) {
        return Lists.newArrayList();
    }
        public Integer mostSocialMusicians() {
        Collection<Album> album = dao.loadAll(Album.class);
        Map<String, Integer> nameMap = Maps.newHashMap();
        for (Album m : album) {
            nameMap.put(m.getAlbumName(), 1);
        }
        ListMultimap<String, Album> multimap = MultimapBuilder.treeKeys().arrayListValues().build();
        ListMultimap<Integer, Musician> countMap = MultimapBuilder.treeKeys().arrayListValues().build();
        List<String> name = new ArrayList< >();
        TreeMap<String, Integer> tmap = new TreeMap< >();
        for (Album featuredMusicians : album) {
            List<Musician> musician = featuredMusicians.getFeaturedMusicians();
            for (Musician musician1 : musician) {
                name.add(musician1.getName());
                multimap.put(musician1.getName(), featuredMusicians);
            }
        }
        Map<String, Collection<Album>> albumMultimap = multimap.asMap();
        for (String t : name) {
            Integer c = tmap.get(t);
            tmap.put(t, (c == null) ? 1 : c + 1);
        }
        int max = tmap.values().stream().max(Integer::compare).get();
        for (String name1 : albumMultimap.keySet()) {
            Collection<Album> albums = albumMultimap.get(name1);
        }
        return max;
    }

    /**
     * Busiest year in terms of number of albums released.
     *
     * @Param k the number of years to be returned.
     */



    public List<Integer> busiestYears(int k) {
        if (k > 0)
        {
            Collection<Album> albums = dao.loadAll(Album.class);
            ListMultimap<Integer, Album> albumMap = MultimapBuilder.treeKeys().arrayListValues().build();
            ListMultimap<Integer, Integer> countMap = MultimapBuilder.treeKeys().arrayListValues().build();

            for (Album album : albums)
            {
                int year = album.getReleaseYear();
                albumMap.put(year, album);
            }

            for (Integer i : albumMap.keySet())
            {
                int totalAlbum = albumMap.get(i).size();
                countMap.put(totalAlbum, i);
            }

            List<Integer> albumYear = Lists.newArrayList();
            List<Integer> sortedAlbumYear = Lists.newArrayList(countMap.keySet());
            sortedAlbumYear.sort(Ordering.natural().reverse());

            for (Integer count : sortedAlbumYear)
            {
                List<Integer> data = countMap.get(count);
                if (albumYear.size() + data.size() >= k)
                {
                    int flag = k - albumYear.size();
                    for (int i = 0; i < flag; i++)
                    {
                        albumYear.add(data.get(i));
                    }
                }
                else
                    {
                    albumYear.addAll(data);
                }
            }

            return albumYear;

        }
        else
            {
            List<Integer> albumYear = new ArrayList<>();
            return albumYear;
        }
    }


    /**
     * Most similar albums to a give album. The similarity can be defined in a variety of ways.
     * For example, it can be defined over the musicians in albums, the similarity between names
     * of the albums & tracks, etc.
     *
     * @Param k the number of albums to be returned.
     * @Param album
     */

    // Similarity in albums based on musicians
    public List<Album> mostSimilarAlbums(int k, Album album)
    {
        // If album smaller than or equal to zero return list
        if (k <= 0)
        {
            return Lists.newArrayList();
        }

        Collection<Album> albums = dao.loadAll(Album.class);
        List<Album> list = new ArrayList<>(albums);
        List<Musician> musician = album.getFeaturedMusicians();
        ListMultimap<Double, Album> map = MultimapBuilder.treeKeys().arrayListValues().build();

        for (Album album1 : list)
        {
            double count = 0;

            for (Musician musicians : musician)
            {
                if (album1.getFeaturedMusicians().contains(musicians))
                {
                    count++;
                }
            }
            map.put(count / musician.size(), album1);
        }

        List<Album> albumList = Lists.newArrayList();
        List<Double> sortedKeys = Lists.newArrayList(map.keySet());
        sortedKeys.sort(Ordering.natural().reverse());

        for (Double doSort : sortedKeys)
        {
            List<Album> album2 = map.get(doSort);

            if (albumList.size() + album2.size() >= k)
            {
                int newAddition = k - albumList.size();

                for (int i = 0; i < newAddition; i++)
                {
                    albumList.add(album2.get(i));
                }
            }
            else
                {
                    albumList.addAll(album2);
            }
        }
        return albumList;
    }

    /**
     * Most Played albums. This method derives an interesting facts about the albums
     * that has been played or viewed most number of times. This can help the users to know
     * the most trending or the most popular albums from the list provided.
     */

    public String mostPlayedAlbums() {
        Collection<Album> album = dao.loadAll(Album.class);
        ListMultimap<String, Album> multimap = MultimapBuilder.treeKeys().arrayListValues().build();
        ListMultimap<Integer, Musician> countMap = MultimapBuilder.treeKeys().arrayListValues().build();
        List<String> name = new ArrayList< >();
        for (Album album1 : album) {
            name.add(album1.getAlbumName());
        }
        System.out.println(name);
        Collections.sort(name);
        String mostCommon = null;
        String last = null;
        int mostCount = 0;
        int lastCount = 0;
        for (String x : name) {
            if (x.equals(last)) {
                lastCount++;
            } else if (lastCount > mostCount) {
                mostCount = lastCount;
                mostCommon = last;
            }
            last = x;
        }
        System.out.println(mostCommon);
        return mostCommon;
    }
    /**
     * Returns the most sold album
     *
     *
     * @Param albums
     */
    public Album mostSoldAlbum(List<Album> albums){
        Album album = new Album();

        int highestCopiesSold = 0;

        for (Album album1 : albums){
            if(album1.getNumberOfCopiesSold() > highestCopiesSold){
                highestCopiesSold = album1.getNumberOfCopiesSold();
                album = album1;
            }
        }

        return album;
    }


    /** highest rated album
     *
     * @param albums
     * @return
     */

    public Album higestRatedAlbum(List<Album> albums){
        Album al = new Album();
        int higestRating = 0;
        for(Album a: albums){
            if(a.getHighestRating()>higestRating){
                higestRating = a.getHighestRating();
                al = a;
            }
        }
        return al;
    }

}
