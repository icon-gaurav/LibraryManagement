package application.fxmlfilescontroller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import application.Main;
import application.javafiles.DatabaseConnection;
import application.javafiles.PopUp;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ProjectController implements Initializable{
	
	public static Connection dbcon=null;
	private Parent subRoot;
	private PopUp popup=new PopUp();
	             
	@FXML private TextField userId;
	@FXML private PasswordField password;
	@FXML private Button login;
	@FXML private Button forgotPassword;
	@FXML private Label status;
	@FXML private Label date_time;
	@FXML private BorderPane workingWindow;
	@FXML private MenuItem newMember;
	@FXML private MenuItem newUser;
	@FXML private MenuItem newBook;
	@FXML private MenuItem issueBook;
	@FXML private MenuItem save;
	@FXML private MenuItem print;
	@FXML private MenuItem close;
	@FXML private MenuItem updateMember;
	@FXML private MenuItem updateUser;
	@FXML private MenuItem updateBook;
	@FXML private MenuItem cut;
	@FXML private MenuItem copy;
	@FXML private MenuItem paste;
	@FXML private MenuItem deleteMember;
	@FXML private MenuItem deleteUser;
	@FXML private MenuItem deleteBook;
	@FXML private MenuItem welcome;
	@FXML private MenuItem helpContent;
	@FXML private MenuItem search;
	@FXML private MenuItem about;
	@FXML private MenuItem theme;
	@FXML private MenuItem textColor;
	@FXML private MenuItem checkForUpdate;
	@FXML private Button memberDetails;
	@FXML private Button bookDetails;
	@FXML private Button bookSearch;
	@FXML private Button memberSearch;
	@FXML private Button bookIssue;
	@FXML private Button bookReturn;
	@FXML private AnchorPane window;
	@FXML private Button logout;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		Main.mainWindow.setOnCloseRequest(e-> {e.consume();close();});
		setClock();
		login.disableProperty().bind(userId.textProperty().isEmpty().or(password.textProperty().isEmpty()));
		logout.setDisable(true);
	}
	
	private void setClock() {		// setting  digital clock to application
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> { 
	        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        Date date = new Date();
	        date_time.setText(dateFormat.format(date));
	    }),new KeyFrame(Duration.seconds(1)) );
	    clock.setCycleCount(Animation.INDEFINITE);
	    clock.play();
	}
		
	@FXML
	public void login() {			// login user with valid userid and password
		if(!checkConnectionAvailablity()) {
			dbcon=DatabaseConnection.getDataBaseConnection(userId.getText(), password.getText());
			if(dbcon!=null) {
				status.setStyle("-fx-text-fill:green");
				status.setText("Successfully Login....");
				logout.setDisable(false);
				userId.clear();
				password.clear();
			}
			else {
				popup.alertMessage("Login Alert", "Either username or password is invalid!");
			}
		}
		else {
			popup.alertMessage("Login Alert", "Other User currently logged in.");
		}
	}
	
	@FXML 
	public void logout() {			// logout current logged in user
		if(checkConnectionAvailablity()) {
			try {
				dbcon.close();
				status.setText("Logout successfully.");
			}
			catch (SQLException e) {
				status.setStyle("-fx-text-fill: red;");
				status.setText(e.getMessage());
			}
		}
		else {
			popup.alertMessage("Logout Alert", "No User logged in!");
		}
	}
	
	public boolean checkConnectionAvailablity() {			// return true when a connection is connected to database otherwise false

			if(dbcon!=null) {
				try {
					if(dbcon.isClosed())
						return false;
					else
						return true;
				}catch(SQLException e) {
					status.setStyle("-fx-text-fill:red:");
					status.setText(e.getMessage());
					return true;
				}				
			}
			else
				return false;
	}
	
	@FXML
	public void forgotPassword() {		// recover password with valid userid
		if(checkConnectionAvailablity())
			popup.resetPassword();
		else {
			status.setStyle("-fx-text-fill:red");
			status.setText("Connection not available!");
		}
	}
	
	@FXML
	public void newMember() {		// add new member
		if(checkConnectionAvailablity()) {
			popup.newMember();
		}
		else
			popup.alertMessage("Login Alert", "Connection not available.");
	}
	
	@FXML
	public void newUser() {			// add new user
		if(checkConnectionAvailablity()) {
			popup.newUserAccount();
		}
		else
			popup.alertMessage("Login Alert", "Other User currently logged in.");
	}
	
	@FXML
	public void newBook() {			// add new book details
		if(checkConnectionAvailablity()) {
			popup.newBook();
		}
		else
			popup.alertMessage("Login Alert", "Other User currently logged in.");
	}
	
	@FXML
	public void updateMember() {	// update member details
		if(checkConnectionAvailablity())
			popup.updateMember();
		else
			popup.alertMessage("Login Alert", "Other User currently logged in.");
	}
	
	@FXML
	public void updateUser() {
		
	}
	
	@FXML
	public void updateBook() {
		
	}
	
	@FXML
	public void deleteMember() {		// delete member
		if(checkConnectionAvailablity())
			popup.deleteMember();
		else
			popup.alertMessage("Login Alert", "Other User currently logged in.");
	}
	
	@FXML
	public void deleteUser() {			// delete user
		if(checkConnectionAvailablity()) {
			popup.deleteUser();
		}
		else
			popup.alertMessage("Login Alert", "Other User currently logged in.");
	}
	
	@FXML
	public void deleteBook() {		// delete book
		if(checkConnectionAvailablity())
			popup.deleteBook();
		else
			popup.alertMessage("Login Alert", "Other User currently logged in.");
	}
	
	@FXML
	public void allMemberDetails() {		// all member details in tabular form
		if(checkConnectionAvailablity()) {
			try {
				subRoot=FXMLLoader.load(getClass().getResource("../fxmlfiles/AllMemberDetails.fxml"));
				subRoot.minHeight(workingWindow.getHeight());
				workingWindow.setCenter(subRoot);
			} catch (IOException e) {
				status.setTextFill(Color.RED);
				status.setText(e.getMessage());
			}
		}
		else 
			popup.alertMessage("Login Alert", "No user logged in.");
	}
	
	@FXML
	public void issueBook() {		// issue book 
		if(checkConnectionAvailablity()) {
			try {
				subRoot=FXMLLoader.load(getClass().getResource("../fxmlfiles/IssueBook.fxml"));
				workingWindow.setCenter(subRoot);
			} catch (IOException e) {
				status.setTextFill(Color.RED);
				status.setText(e.getMessage());
			}
		}
		else 
			popup.alertMessage("Login Alert", "No user logged in.");
	}
	
	@FXML public void bookDetails() {		// book details in tabular form
		if(checkConnectionAvailablity()) {
			try {
				subRoot=FXMLLoader.load(getClass().getResource("../fxmlfiles/BookDetails.fxml"));
				subRoot.minHeight(workingWindow.getHeight());
				workingWindow.setCenter(subRoot);
			} catch (IOException e) {
				status.setTextFill(Color.RED);
				status.setText(e.getMessage());
				e.printStackTrace();
			}
		}
		else
			popup.alertMessage("Login Alert", "No User currently logged in.");
	}
	
	@FXML
	public void returnBook() {		// book returning
		if(checkConnectionAvailablity()) {
			try {
				subRoot=FXMLLoader.load(getClass().getResource("../fxmlfiles/ReturnBook.fxml"));
				subRoot.minHeight(workingWindow.getHeight());
				workingWindow.setCenter(subRoot);
			} catch (IOException e) {
				status.setTextFill(Color.RED);
				status.setText(e.getMessage());
			}
		}
		else 
			popup.alertMessage("Login Alert", "No user logged in.");
	}
	
	@FXML
	public void memberSearch() {		// search member details
		if(checkConnectionAvailablity()) {
			popup.search("../fxmlfiles/MemberSearch.fxml");
		}
		else 
			popup.alertMessage("Login Alert", "No user logged in.");
	}
	
	@FXML
	public void bookSearch() {		// search book details
		if(checkConnectionAvailablity()) {
			popup.loadFXML("Book Search", "BookSearch.fxml", "book-128.png");
		}
		else 
			popup.alertMessage("Login Alert", "No user logged in.");
	}
	
	@FXML
	public void welcome() {
		
	}
	
	@FXML
	public void settings() {
			popup.loadFXML("Settings", "Settings.fxml", null);
	}
	
	@FXML
	public void helpContent() {
		
	}
	
	@FXML
	public void search() {
		
	}
	
	@FXML
	public void about() {
		
	}
	
	@FXML
	public void copy() {
		
	}
	
	@FXML
	public void paste() {
		
	}
	
	@FXML
	public void cut() {
		
	}
	
	@FXML
	public void save() {
		
	}
	
	@FXML
	public void print() {
		
	}
	
	@FXML
	public void close() {
		if(!checkConnectionAvailablity()) {
			if(popup.exitRequest()) 
				Main.mainWindow.close();
		}
		else
			popup.alertMessage("Logout Alert"," User is logged in.");
	}

}
