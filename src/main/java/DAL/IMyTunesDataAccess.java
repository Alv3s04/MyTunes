package DAL;

// Project imports
import BE.Playlists;
import BE.Songs;

// Java imports
import java.util.List;

/**
 * Basic CRUD operations on the Movie
 */
public interface IMyTunesDataAccess {

    List<Songs> getAllSongs() throws Exception;
    Songs createSongs(Songs newSongs) throws Exception;
    void updateSongs(Songs songs) throws Exception;
    void deleteSongs(Songs songs) throws Exception;

    List<Playlists> getAllPlaylists() throws Exception;
    Playlists createPlaylists(Playlists newPlaylist) throws Exception;
    void updateSongs(Playlists playlists) throws Exception;
    void deleteSongs(Playlists playlists) throws Exception;
}
