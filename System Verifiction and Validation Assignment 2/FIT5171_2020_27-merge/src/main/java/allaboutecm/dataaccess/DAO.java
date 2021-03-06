package allaboutecm.dataaccess;

import allaboutecm.model.Album;

import allaboutecm.model.Entity;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.Musician;

import java.net.URL;
import java.util.Collection;
import java.util.List;

public interface DAO {
    <T extends Entity> T load(Class<T> clazz, Long id);

    <T extends Entity> T createOrUpdate(T entity);

    <T extends Entity> Collection<T> loadAll(Class<T> clazz);

    <T extends Entity> void delete(T entity);

    Musician findMusicianByName(String name);
    Album findAlbumByName(String name);
    Album findAlbumByRecordNumber(String recordNumber);
    Album findAlbumByURL(URL albumURL);
    MusicalInstrument findMusicalInstrumentByName(String instrumentName);
    Album findAlbumByReleaseYear(int releaseYear);
    List<Album> findAlbumsByYear(int year);   // a single year can have  multiple albums
    Musician findMusicianByURL(URL url);
}
