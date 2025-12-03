package DAL;

// Project imports
import BE.Playlists;
import BE.Song;

// Java imports
import java.util.List;

/**
 * Basic CRUD operations on the Movie
 */
public interface IMyTunesDataAccess {

    List<Song> getAllSongs() throws Exception;
    Song createSongs(Song newSong) throws Exception;
    void updateSongs(Song song) throws Exception;
    void deleteSongs(Song song) throws Exception;
}
