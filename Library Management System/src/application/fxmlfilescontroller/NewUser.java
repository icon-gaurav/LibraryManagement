package application.fxmlfilescontroller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class NewUser implements Initializable {

	public Connection dbcon;

	@FXML private TextField username;
	@FXML private PasswordField password;
	@FXML private PasswordField cnfPassword;
	@FXML private ChoiceBox<String> userType;
	@FXML private Button addUser;
	@FXML private Button cancel;
	@FXML private Label status;
	
	private ObservableList<String> usertype=FXCollections.observableArrayList();
	
	private boolean checkVariables() {
		if(password.getText().equals(cnfPassword.getText()))
			return true;
		else
			return false;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		usertype.addAll("Librarian","Administrator");
		userType.setValue("Librarian");
		userType.setItems(usertype);
		cnfPassword.disableProperty().bind(password.textProperty().isEmpty());
		addUser.disableProperty().bind(username.textProperty().isEmpty().or(cnfPassword.textProperty().isEmpty()));
	}

	@FXML
	public void addUser() {
		if(checkVariables()) {
			int cntrl=1;
			String query[]=new String[6];
			query[0]="CREATE USER ?@? IDENTIFIED BY ?;";
			if(userType.getValue().equals("Librarian")) {
				query[1]="GRANT SELECT, UPDATE ON library.book TO ?@?; ";
				query[2]="GRANT SELECT, UPDATE ON library.member TO ?@?;";
				query[3]="GRANT SELECT, INSERT ON library.issue TO ?@?;";
				query[4]="GRANT SELECT, UPDATE ON library.book_cover_images TO ?@?;";
				query[5]="GRANT SELECT ON library.not_returned TO ?@?;";
				cntrl=6;
			}
			else {
				query[1]="GRANT ALL ON library.* TO ? @ ?;";
				cntrl=2;
			}
			try {
				
				PreparedStatement st=ProjectController.dbcon.prepareStatement(query[0]);
				st.setString(1, username.getText());
				st.setString(2, "localhost");
				st.setString(3, password.getText());
				int result=st.executeUpdate();
				if(result==0) {
					int i=1;
					while(i<cntrl) {
						st=ProjectController.dbcon.prepareStatement(query[i]);
						st.setString(1, username.getText());
						st.setString(2, "localhost");
						st.executeUpdate();
					}										
					status.setText("user added successfully.");
				}
				else
					throw new SQLException("create user statement error!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			cnfPassword.setStyle("-fx-border-color:red;");
			status.setStyle("-fx-color:red;");
			status.setText("Confirm Password didn't match with Password Field!");
		}
	}
	
	@FXML
	public void cancel() {
		username.clear();
		password.clear();
		cnfPassword.clear();
	}

}
