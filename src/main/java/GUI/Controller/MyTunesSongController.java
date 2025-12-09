package GUI.Controller;

import BE.Song;
import GUI.Model.MyTunesModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyTunesSongController {
    @FXML
    private ComboBox<String> comboBoxGenre;
    @FXML
    private TextField txtFieldSongTitle;
    @FXML
    private TextField txtFieldTime;
    @FXML
    private TextField txtFieldMP3;
    @FXML
    private TextField txtFieldArtistName;

    private MyTunesModel model; // Reference to model layer for DB + logic
    private Song editingSong = null; // Song being edited (null if creating new)
    private boolean editMode = false; // Boolean for whether the controller is in edit mode (false being not in edit mode)

    /**
     * Initializes the controller after FXML is loaded.
     * Populates the genre ComboBox with predefined options.
     */
    @FXML
    public void initialize() {
        comboBoxGenre.getItems().addAll(
                "Pop", "Rock", "Hip-Hop /Rap", "R&B / Soul", "Electronic / Dance",
                "Jazz", "Classical", "Metal", "Country", "Reggae", "Folk", "Indie", "Blues",
                "Latin", "K-Pop", "Soundtrack", "Alternative", "House", "Techno", "Drum & Bass"
        );
        comboBoxGenre.setOnAction(e -> {
            String selected = comboBoxGenre.getSelectionModel().getSelectedItem();
            comboBoxGenre.setPromptText(selected); // Update prompt text with selected genre
        });
    }

    /**
     * Injects the model into the controller.
     * Called by main controller to access songs and data layer.
     */
    public void setModel(MyTunesModel model) {
        this.model = model;
    }

    /**
     * Prepares the controller for editing an existing song.
     * Fills all input fields with the song's current data.
     */
    public void setEditingSong(Song song) {
        if (song != null) {
            this.editingSong = song;
            this.editMode = true;

            txtFieldSongTitle.setText(song.getTitle());
            txtFieldArtistName.setText(song.getArtist());
            comboBoxGenre.setValue(song.getCategory());
            txtFieldTime.setText(String.valueOf(song.getTime()));
            txtFieldMP3.setText(song.getFilePath());
        }
    }

    /**
     * Opens a file chooser to select an MP3 file.
     * Only allows files ending with .mp3 and stores relative path in txtFieldMP3.
     */
    @FXML
    private void onClickMP3FileChooser(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose MP3 File");

        File initialDir = new File("data/MP3_Files");
        if (initialDir.exists() && initialDir.isDirectory()) {
            fileChooser.setInitialDirectory(initialDir); // Start in MP3 folder
        }

        File file = fileChooser.showOpenDialog(txtFieldMP3.getScene().getWindow()); // Open dialog

        if (file != null && file.getName().toLowerCase().endsWith(".mp3")) {
            Path dataFolder = Paths.get("data").toAbsolutePath();
            Path selectedPath = file.toPath().toAbsolutePath();
            Path relativePath = dataFolder.relativize(selectedPath);

            txtFieldMP3.setText(relativePath.toString()); // Store relative path
        } else if (file != null) {
            // Invalid file selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid File");
            alert.setHeaderText("Only MP3 files are allowed");
            alert.setContentText("Please choose a file ending with .mp3");
            alert.showAndWait();
        }
    }

    /**
     * Closes the window without saving the song.
     * TODO: Add a confirmation for closing without saving.
     */
    @FXML
    private void onClickCancelSong(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Saves a new song or updates an existing one.
     * Validates input, updates the model, shows confirmation alerts,
     * and closes the window when finished.
     */
    @FXML
    private void onClickSaveSong(ActionEvent actionEvent) {
        try {
            // Get input values
            String title = txtFieldSongTitle.getText().trim();
            String artist = txtFieldArtistName.getText().trim();
            String category = comboBoxGenre.getValue();
            double time = Double.parseDouble(txtFieldTime.getText().trim());
            String filePath = txtFieldMP3.getText().trim();

            // Validate required fields
            if (title.isEmpty() || artist.isEmpty() || category == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Fields");
                alert.setHeaderText("Please fill in all required fields");
                alert.showAndWait();
                return;
            }

            // Update existing song if we are in edit mode
            if (editMode && editingSong != null) {
                // Update song object with new title, artist, category, time and filepath
                editingSong.setTitle(title);
                editingSong.setArtist(artist);
                editingSong.setCategory(category);
                editingSong.setTime(time);
                editingSong.setFilePath(filePath);

                model.updateSongs(editingSong); // Persist update via model

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Song Updated");
                alert.setHeaderText("Song successfully updated!");
                alert.setContentText(editingSong.getTitle() + " by " + editingSong.getArtist() + " has been updated.");
                alert.showAndWait();

                // Close the window
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }
            // Create new song
            else {
                // Create new song object (ID = 0 since DB will assign ID automatically)
                Song newSong = new Song(0, title, artist, category, time, filePath);
                model.createSongs(newSong); // Save song through model

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Song Added");
                alert.setHeaderText("Song successfully added!");
                alert.setContentText(newSong.getTitle() + " by " + newSong.getArtist() + " has been successfully added.");
                alert.showAndWait();

                // Close the window
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }
        }
        // Handle unexpected errors and display an error alert
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Time must be a number");
            alert.showAndWait();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Saving Song");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}