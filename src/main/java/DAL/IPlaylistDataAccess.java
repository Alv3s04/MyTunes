package DAL;

import BE.Playlists;

import java.util.List;

public interface IPlaylistDataAccess {

    List<Playlists> getAllPlaylists() throws Exception;

    Playlists createPlaylists(Playlists newPlaylist) throws Exception;

    void updatePlaylists(Playlists playlists) throws Exception;

    void deletePlaylists(Playlists playlists) throws Exception;
}
