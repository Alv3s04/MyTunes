package DAL;

import BE.Playlists;
import BE.Song;

import java.util.List;

/**
 * Basic CRUD operations on the Playlists without U (update)
 */
public interface ISongsOnPlaylistDataAccess {
    void addSongToPlaylist(int playlistId, int songId) throws Exception;
    List<Song> getSongsOnPlaylist(Playlists playlists) throws Exception;
    void deleteSongOnPlaylist(int playlistId, int songId) throws Exception;
}
