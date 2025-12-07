package DAL;

import BE.Song;
import java.util.List;
/**
 * Basic CRUD operations on the Songs
 */
public interface IMyTunesDataAccess {
    Song createSongs(Song newSong) throws Exception;
    List<Song> getAllSongs() throws Exception;
    void updateSongs(Song song) throws Exception;
    void deleteSongs(Song song) throws Exception;
}
