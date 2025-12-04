package GUI.Controller;

import BE.Song;
import BLL.MyTunesManager;
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

    private MyTunesModel model;
    private Song editingSong = null;
    private boolean editMode = false;
    private MyTunesMainController mainController;

    @FXML
    public void initialize() {
        comboBoxGenre.getItems().addAll(
                "Pop",
                "Rock",
                "Hip-Hop / Rap",
                "R&B / Soul",
                "Electronic / Dance",
                "Jazz",
                "Classical",
                "Metal",
                "Country",
                "Reggae",
                "Folk",
                "Indie",
                "Blues",
                "Latin",
                "K-Pop",
                "Soundtrack",
                "Alternative",
                "House",
                "Techno",
                "Drum & Bass"
        );
        comboBoxGenre.setOnAction(e -> {
            String selected = comboBoxGenre.getSelectionModel().getSelectedItem();
            comboBoxGenre.setPromptText(selected);
            comboBoxGenre.setPromptText(selected);
        });
    }

    // Called by main controller
    public void setModel(MyTunesModel model) {
        this.model = model; //denne metode kommer til interact med songs NÅRR vi har en filepath og ka bruge songs
    }

    // Called when editing an existing song
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

    @FXML
    private void onClickMP3FileChooser(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose MP3 File"); //laver et window til fil vælger

        File initialDir = new File("data/MP3_Files");
        if (initialDir.exists() && initialDir.isDirectory()) {
            fileChooser.setInitialDirectory(initialDir);
        } //den viser dig direkte til MP3 mappen

        File file = fileChooser.showOpenDialog(txtFieldMP3.getScene().getWindow());// Vis dialogen og return den valgte fil eller null

        if (file != null && file.getName().toLowerCase().endsWith(".mp3")) {
            Path dataFolder = Paths.get("data").toAbsolutePath();

            Path selectedPath = file.toPath().toAbsolutePath();

            Path relativePath = dataFolder.relativize(selectedPath);

            txtFieldMP3.setText(relativePath.toString());
        }  else if (file != null) {
            // User picked a wrong file via some trick
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid File");
            alert.setHeaderText("Only MP3 files are allowed");
            alert.setContentText("Please choose a file ending with .mp3");
            alert.showAndWait();
        }
    }

    @FXML
    private void onClickCancelSong(ActionEvent actionEvent) {
        // TODO: Add a confirmation for closing without saving
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onClickSaveSong(ActionEvent actionEvent) {
        try {
            // Get input from text fields
            String title = txtFieldSongTitle.getText().trim();
            String artist = txtFieldArtistName.getText().trim();
            String category = comboBoxGenre.getValue(); // getValue() gives selected item
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

            // Update existing song
            if (editMode && editingSong != null) {
                editingSong.setTitle(title);
                editingSong.setArtist(artist);
                editingSong.setCategory(category);
                editingSong.setTime(time);
                editingSong.setFilePath(filePath);

                model.updateSongs(editingSong); // you need this method in your model

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Song Updated");
                alert.setHeaderText("Song successfully updated!");
                alert.setContentText(editingSong.getTitle() + " by " + editingSong.getArtist() + " has been updated.");
                alert.showAndWait();

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }
            // Create song
            else {
                Song newSong = new Song(0, title, artist, category, time, filePath);// Create Song object (ID = 0, DB will generate it)
                newSong.setFilePath(filePath);
                model.createSongs(newSong);// Save song via model (adds to observable list automatically)
                // Show confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Song Added");
                alert.setHeaderText("Song successfully added!");
                alert.setContentText(newSong.getTitle() + " by " + newSong.getArtist() + " has been successfully added.");
                alert.showAndWait();

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Time must be a number");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Saving Song");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
