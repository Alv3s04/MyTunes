package DAL;

import BE.Playlists;
import BE.Song;

import java.util.List;
/**
 * Basic CRUD operations on the Playlists
 */
public interface IPlaylistDataAccess {
    Playlists createPlaylists(Playlists newPlaylist) throws Exception;
    List<Playlists> getAllPlaylists() throws Exception;
    void updatePlaylists(Playlists playlists) throws Exception;
    void deletePlaylists(Playlists playlists) throws Exception;

    List<Song> getSongsOnPlaylist(Playlists playlists) throws Exception;

}