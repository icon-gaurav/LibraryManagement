package application.fxmlfilescontroller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

public class ForgotPassword implements Initializable{

	@FXML private TextField userId;
	@FXML private PasswordField password;
	@FXML private PasswordField cnfPassword;
	@FXML private ProgressBar pgrbar;
	@FXML private Label recoverPassword;
	@FXML private Button recover;
	@FXML private Button cancel;
	
	private String Query;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		recover.disableProperty().bind(userId.textProperty().isEmpty());
	}
	
	@FXML
	public  void passwordRecovery() {
		if(validateData()) {
		Query="ALTER USER ? @ ? INDENTIFIED BY ?;";
			Connection connection=DatabaseConnection.getDataBaseConnection("root","gaurav Kumar");
			try {
				PreparedStatement st=connection.prepareStatement(Query);
				st.setString(1, userId.getText());
				st.setString(2, "localhost");
				st.setString(3, password.getText());
				int rs=st.executeUpdate();
				if(rs==1)
					recoverPassword.setText("Your password reset is successfull.");
			} catch (SQLException e) {
				recoverPassword.setText(e.getSQLState());
			}
		}
		else {
			recoverPassword.setText("Invalid User is not found!!");
		}
	}


	private boolean validateData() {
		if(password.getText().equals(cnfPassword.getText()))
			return true;
		else
			return false;
	}
	
	@FXML
	public void cancel() {
		Stage stage=(Stage)cancel.getScene().getWindow();
		stage.close();
	}
}
