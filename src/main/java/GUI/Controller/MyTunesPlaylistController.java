package GUI.Controller;

import BE.Playlists;
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

    private MyTunesModel model; // Reference to model layer for DB + logic
    private Playlists editingPlaylist = null; // Playlist being edited (null if creating new)
    private boolean editMode = false; // Boolean for whether the controller is in edit mode (false being not in edit mode)

    /**
     * Injects the model into the controller.
     * Called by the main controller to give access to data and logic.
     */
    public void setModel(MyTunesModel model) {
        this.model = model;
    }

    /**
     * Prepares the controller for editing an existing playlist.
     * Loads the playlist's current data into the UI.
     */
    public void setEditingPlaylist(Playlists playlists) {
        if (playlists != null) {
            this.editingPlaylist = playlists; // Store playlist to edit
            this.editMode = true; // Enable edit mode
            txtFieldPlaylist.setText(playlists.getName()); // Pre-fill input with existing name
        }
    }

    /**
     * Closes the window.
     * Triggered when the user clicks the Cancel button.
     */
    @FXML
    private void onClickCancelPlaylist(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Saves a new playlist or updates an existing one depending on edit mode.
     * Validates input, updates the model, shows confirmation alerts, and closes the window when finished.
     */
    @FXML
    private void onClickSavePlaylist(ActionEvent actionEvent) {
        try {
            // Read and trim playlist name input
            String name = txtFieldPlaylist.getText().trim();

            // Validate that required fields are filled out
            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Fields");
                alert.setHeaderText("Please fill in all required fields");
                alert.showAndWait();
                return;
            }

            // Update existing playlist if we are in edit mode
            if (editMode && editingPlaylist != null) {
                editingPlaylist.setName(name); // Update playlist object with new name
                model.updatePlaylists(editingPlaylist); // Persist update via model

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Playlist Updated");
                alert.setHeaderText("Playlist successfully updated!");
                alert.setContentText(editingPlaylist.getName() + " has been updated.");
                alert.showAndWait();

                // Close the window
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();

            }
            // Create new playlist
            else {
                // Create mode: initializing values for a new playlist
                int songs = 0; // Default: no songs yet
                double time = 0.0; // Default: no time yet

                // Create new playlist object (ID = 0 since DB will assign ID automatically)
                Playlists newPlaylist = new Playlists(0, name, songs, time);
                model.createPlaylists(newPlaylist); // Save playlist through model

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Playlist Added");
                alert.setHeaderText("Playlist successfully added!");
                alert.setContentText(newPlaylist.getName() + " has been successfully added.");
                alert.showAndWait();

                // Close the window
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }
        }
        // Handle unexpected errors and display an error alert
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Saving Playlist");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}