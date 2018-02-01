package application.fxmlfilescontroller;

import java.net.URL;
import java.util.ResourceBundle;

import application.javafiles.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
public class UserLogin implements Initializable {
	
	@FXML private TextField username;
	@FXML private PasswordField userpassword;
	@FXML private Label userId;
	@FXML private Label userPass;
	@FXML private ProgressBar progressBar;
	@FXML private Label result;
	@FXML private Button login;
	@FXML private Button forgotpassword;
	
	private DatabaseConnection dbcon;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		progressBar.setVisible(false);
	}
	@FXML
	public void userLogin() {
		dbcon=new DatabaseConnection(username.getText(),userpassword.getText());
		progressBar.setVisible(true);
		progressBar.progressProperty().bind(dbcon.progressProperty());
		result.textProperty().bind(dbcon.messageProperty());
		new Thread(dbcon).start();
		if(dbcon.isDone()) {
			Stage stage=(Stage)login.getScene().getWindow();
			stage.close();
		}
		if(dbcon.isCancelled())
			result.setText("Cancelled!");
	}
}
