package DAL;

import BE.Playlists;
import java.util.List;
/**
 * Basic CRUD operations on the Playlists
 */
public interface IPlaylistDataAccess {
    Playlists createPlaylists(Playlists newPlaylist) throws Exception;
    List<Playlists> getAllPlaylists() throws Exception;
    void updatePlaylists(Playlists playlists) throws Exception;
    void deletePlaylists(Playlists playlists) throws Exception;
}
