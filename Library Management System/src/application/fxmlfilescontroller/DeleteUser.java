package application.fxmlfilescontroller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DeleteUser implements Initializable{
	
	@FXML private TextField username;
	@FXML private Label status;
	@FXML private Button delete;
	@FXML private Button cancel;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		delete.disableProperty().bind(username.textProperty().isEmpty());
	}
	
	@FXML
	public void deleteUser() {
		String query="DROP USER IF EXISTS ?@? ;";
		try {
			PreparedStatement st=ProjectController.dbcon.prepareStatement(query);
			st.setString(1, username.getText());
			st.setString(2, "localhost");
			st.executeUpdate();
			status.setText("User Deleted Successfully");
		} catch (SQLException e) {
			e.printStackTrace();
			status.setText(e.getMessage());
		}
	}
	
	@FXML
	public void cancel() {
		Stage stage=(Stage)cancel.getScene().getWindow();
		stage.close();
	}
		
}
