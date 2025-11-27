package DAL.db;

// Project imports
import BE.Playlists;
import BE.Song;
import DAL.IMyTunesDataAccess;
// Java imports
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyTunesDAO_DB implements IMyTunesDataAccess {
    private DBConnector databaseConnector = new DBConnector();

    public MyTunesDAO_DB() throws IOException {
    }

    // Songs
    // create
    @Override
    public Song createSongs(Song newSong) throws Exception {
        String sql = "INSERT INTO dbo.Song (Title, Artist, Category, Time) VALUES (?, ?, ?, ?);";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, newSong.getTitle()); // This is placeholders, 1: Title 2: Artist 3: category 4: time
            stmt.setString(2, newSong.getArtist());
            stmt.setString(3, newSong.getCategory());
            stmt.setDouble(4, newSong.getTime());

            stmt.executeUpdate(); // sends the command to the database
        }
        return newSong;
    }

    // read
    @Override
    public List<Song> getAllSongs() throws Exception {

        ArrayList<Song> allSongs = new ArrayList<>();

        // try-with-resources
        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM dbo.Song;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Song object
                int id = rs.getInt("Song_ID");
                String title = rs.getString("Title");
                String artist = rs.getString("Artist");
                String category = rs.getString("Category");
                double time = rs.getDouble("Time");

                Song song = new Song(id, title, artist, category, time);
                allSongs.add(song);
            }
            return allSongs;
        }

        catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get songs from database", ex);
        }
    }

    @Override
    public void updateSongs(Song song) throws Exception {
        String sql = "UPDATE dbo.Song SET Title = ?, Artist = ?, Category = ?, Time = ? WHERE Song_ID = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, song.getTitle());
            stmt.setString(2, song.getArtist());
            stmt.setString(3, song.getCategory());
            stmt.setDouble(4, song.getTime());
            stmt.setInt(5, song.getId()); //her identificere vi hvilken row der ska opdateres.

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteSongs(Song song) throws Exception {
        String sql = "DELETE FROM dbo.Song WHERE Song_ID = ?;";

        try (Connection conn = databaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1,song.getId());
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new Exception("Could not get songs from database.",ex);
        }
    }

    // Playlist
    @Override
    public List<Playlists> getAllPlaylists() throws Exception {

        ArrayList<Playlists> allPlaylists = new ArrayList<>();

        // try-with-resources
        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM dbo.Playlist;";
            ResultSet rs = stmt.executeQuery(sql);

            // Loop through rows from the database result set
            while (rs.next()) {
                //Map DB row to Song object
                int id = rs.getInt("Playlist_ID");
                String name = rs.getString("Name");
                int songs = rs.getInt("Songs");
                double time = rs.getDouble("Time");
                Playlists playlist = new Playlists(id, name, songs, time);
                allPlaylists.add(playlist);
            }
            return allPlaylists;
        }

        catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database", ex);
        }
    }

    @Override
    public Playlists createPlaylists(Playlists newPlaylist) throws Exception {
        return null;
    }

    @Override
    public void updatePlaylists(Playlists playlists) throws Exception {

    }

    @Override
    public void deletePlaylists(Playlists playlists) throws Exception {

    }
}