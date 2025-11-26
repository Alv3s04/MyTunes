package DAL.db;

// Project imports
import BE.Playlists;
import BE.Song;
import DAL.IMyTunesDataAccess;
// Java imports
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MyTunesDAO_DB implements IMyTunesDataAccess {
    private DBConnector databaseConnector = new DBConnector();

    public MyTunesDAO_DB() throws IOException {
    }

    @Override
    public List<Song> getAllSongs() throws Exception {
        List<Song> allSongs = new ArrayList<>();
        String sql = "SELECT * FROM songs";

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                //Map DB row to Song object
                String title = rs.getString("Title");
                String artist = rs.getString("Artist");
                String category = rs.getString("Category");
                double time = rs.getDouble("Time");

                Song song = new Song(title, artist, category, time);
                allSongs.add(song);
            }
        }
        return allSongs;
    }


    @Override
    public Song createSongs(Song newSong) throws Exception {
return null;
    }

    @Override
    public void updateSongs(Song song) throws Exception {

    }

    @Override
    public void deleteSongs(Song song) throws Exception {

    }

    @Override
    public List<Playlists> getAllPlaylists() throws Exception {
        // return List.of();
        throw new UnsupportedOperationException();
    }

    @Override
    public Playlists createPlaylists(Playlists newPlaylist) throws Exception {
        return null;
    }

    @Override
    public void updateSongs(Playlists playlists) throws Exception {

    }

    @Override
    public void deleteSongs(Playlists playlists) throws Exception {

    }
}