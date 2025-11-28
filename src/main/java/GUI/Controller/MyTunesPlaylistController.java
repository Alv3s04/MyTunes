package GUI.Controller;

import GUI.Model.MyTunesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    }
}
