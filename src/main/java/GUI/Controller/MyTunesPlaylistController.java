package GUI.Controller;

import BE.Playlists;
import BE.Song;
import GUI.Model.MyTunesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MyTunesPlaylistController {
    @FXML
    private TextField txtFieldPlaylist;

    private MyTunesModel model;

    // Called by main controller
    public void setModel(MyTunesModel model) {
        this.model = model; //denne metode kommer til interact med songs NÅRR vi har en filepath og ka bruge songs
    }

    @FXML
    private void onClickCancelPlaylist(ActionEvent actionEvent) {
        // TODO: Add a confirmation for closing without saving
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onClickSavePlaylist(ActionEvent actionEvent) {
        try {
            // Get input from text fields
            String name = txtFieldPlaylist.getText().trim();

            // Validate required fields
            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Fields");
                alert.setHeaderText("Please fill in all required fields");
                alert.showAndWait();
                return;
            }

            // Create Song object (ID = 0, DB will generate it)
            int songs = 0;
            double time = 0.0;
            Playlists playlistsToSave = new Playlists(0, name, songs, time);

            // Save song via model (adds to observable list automatically)
            Playlists savedPlaylist = model.createPlaylists(playlistsToSave);

            // Show confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Playlist Added");
            alert.setHeaderText("Playlist successfully added!");
            alert.setContentText(savedPlaylist.getName() + " has been successfully added.");
            alert.showAndWait();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Saving Playlist");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
