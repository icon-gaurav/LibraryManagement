package application.javafiles;

import java.io.IOException;

import application.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*  
 *	class for handling all types of popup window 
 */

public class PopUp {	
	
	private BooleanProperty response=new SimpleBooleanProperty();
	public Stage popupwindow=new Stage();
	private Image img;
	
	public void loadFXML(String title,String fileName,String imgName) {
		/* 
		 * load fxml files with title, filename and path of image as input 
		 */
		popupwindow.setTitle(title);
		Pane pane=new Pane();
		try {			
			pane = FXMLLoader.load(getClass().getResource("../fxmlfiles/"+fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene=new Scene(pane);
		popupwindow.setScene(scene);
		if(imgName!=null) {
			img=new Image(getClass().getResourceAsStream("../images/icons/"+imgName));
			popupwindow.getIcons().clear();
			popupwindow.getIcons().add(img);			
		}
		popupwindow.setResizable(true);
		popupwindow.showAndWait();
	}
	public PopUp() {
		popupwindow.initOwner(Main.mainWindow);				// set owner of this popup window as mainwindow
		popupwindow.initModality(Modality.WINDOW_MODAL);	// set modality of this window
	}
	 
	public boolean finePay(String member, Double fine,String payid) {
		popupwindow.setTitle("Payment");
		VBox vbox=new VBox();
		Button ok=new Button("OK");
		ok.setOnAction(e-> popupwindow.close());
		Label PayId=new Label("Bill No. : "+payid);
		Label memberid=new Label("Member ID : "+member);
		Label amount=new Label("Amount : "+Double.toString(fine));
		Label status=new Label("Status : Successfull.");
		status.setStyle("-fx-color:green");
		HBox buttonBar=new HBox();
		buttonBar.setPadding(new Insets(5,5,5,5));
		buttonBar.setAlignment(Pos.CENTER_RIGHT);
		buttonBar.setSpacing(10);
		buttonBar.getChildren().addAll(ok);
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(5,5,5,5));
		vbox.getChildren().addAll(PayId,memberid,amount,status,buttonBar);
		Scene scene=new Scene(vbox);
		popupwindow.setScene(scene);
		popupwindow.setResizable(true);
		popupwindow.showAndWait();
		return true;
	}
	
	public void search(String fxmlPath) {
		popupwindow.setTitle("New Book");
		Pane pane=new Pane();
		try {			
			pane = FXMLLoader.load(getClass().getResource(fxmlPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene=new Scene(pane);
		popupwindow.setScene(scene);
		popupwindow.setResizable(true);
		img=new Image(getClass().getResourceAsStream("../images/icons/book-128.png"));
		popupwindow.getIcons().clear();
		popupwindow.getIcons().add(img);
		popupwindow.show();
	}
	public void alertMessage(String title,String message) {		// to alert popup for wrong action
		popupwindow.setTitle(title);
		VBox vbox=new VBox();
		Button ok=new Button("OK");
		ok.setOnAction(e-> popupwindow.close());
		Label msg=new Label();
		msg.setText(message);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10,10,10,10));
		vbox.setSpacing(10);
		vbox.getChildren().addAll(msg,ok);
		Scene scene=new Scene(vbox);
		popupwindow.setScene(scene);
		popupwindow.setResizable(false);
		img=new Image(getClass().getResourceAsStream("../images/icons/warning.png"));
		popupwindow.getIcons().clear();
		popupwindow.getIcons().add(img);
		popupwindow.showAndWait();
	}
	
/*	public boolean handleLogin(String title,Connection dbcon) {
		popupwindow.setTitle(title);
		Pane pane=new Pane();
		try {
			pane=FXMLLoader.load(getClass().getResource("PopUp.fxml"));
		} catch (IOException e) {
			result=false;
			e.printStackTrace();
		}
		Scene scene=new Scene(pane);
		popupwindow.setScene(scene);
		popupwindow.showAndWait();
		return result;
	}
*/	
	public void login() {
		loadFXML("Log In","UserLogin.fxml",null);
//		popupwindow.setTitle("Log In");
//		Pane pane=new Pane();
//		try {			
//			pane = FXMLLoader.load(getClass().getResource("../fxmlfiles/UserLogin.fxml"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Scene scene=new Scene(pane);
//		popupwindow.setScene(scene);
//		popupwindow.setResizable(true);
//		popupwindow.showAndWait();
	}
	public void newBook() {
		popupwindow.setTitle("New Book");
		Pane pane=new Pane();
		try {			
			pane = FXMLLoader.load(getClass().getResource("../fxmlfiles/NewBook.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene=new Scene(pane);
		popupwindow.setScene(scene);
		popupwindow.setResizable(true);
		img=new Image(getClass().getResourceAsStream("../images/icons/book-128.png"));
		popupwindow.getIcons().clear();
		popupwindow.getIcons().add(img);
		popupwindow.showAndWait();
	}
	
	public void newMember() {
		popupwindow.setTitle("New Member");
		Pane pane=new Pane();
		try {			
			pane = FXMLLoader.load(getClass().getResource("../fxmlfiles/NewMember.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene=new Scene(pane);
		popupwindow.setScene(scene);
		popupwindow.setResizable(true);
		img=new Image(getClass().getResourceAsStream("../images/icons/newmember.png"));
		popupwindow.getIcons().clear();
		popupwindow.getIcons().add(img);
		popupwindow.showAndWait();
	}
	public void resetPassword() {		// reset password popup
		popupwindow.setTitle("Reset Password");
		Pane pane=new Pane();
		try {			
			pane = FXMLLoader.load(getClass().getResource("../fxmlfiles/forgotpassword.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene=new Scene(pane);
		popupwindow.setScene(scene);
		popupwindow.setResizable(false);
		popupwindow.showAndWait();
	}
	
	public boolean exitRequest() {			// exit confirmation dialog
		Button yes=new Button();
		Button no=new Button();
		Stage exitAlert=new Stage();
		exitAlert.setTitle("Exit Window");
		yes=new Button("Yes");
		no=new Button("No");
		yes.setOnAction(e->{response.setValue(true); exitAlert.close();});
		no.setOnAction(e->{response.setValue(false); exitAlert.close();});
		AnchorPane pane=new AnchorPane();
		Label label=new Label("Do You Want to Close the Window?(yes/no)");
		VBox vbox=new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(5,5,5,5));
		vbox.setSpacing(10);
		HBox hbox=new HBox();
		hbox.setAlignment(Pos.CENTER_RIGHT);
		hbox.setPadding(new Insets(10,10,10,10));
		hbox.setSpacing(10);
		hbox.getChildren().addAll(yes,no);
		vbox.getChildren().addAll(label,hbox);
		pane.getChildren().add(vbox);
		pane.setMaxSize(200, 300);
		Scene scene =new Scene(pane);
		exitAlert.setScene(scene);
		exitAlert.setResizable(false);
		exitAlert.initOwner(Main.mainWindow);
		exitAlert.initModality(Modality.APPLICATION_MODAL);
		exitAlert.showAndWait();
		return response.get();
				
	}

	public void deleteMember() {			// loading of delete member window as popup
		popupwindow.setTitle("Delete Member");
		Pane pane=new Pane();
		try {
			pane= FXMLLoader.load(getClass().getResource("../fxmlfiles/DeleteMember.fxml"));
		} catch (IOException e) {
			
		}
		Scene scene = new Scene(pane);
		popupwindow.setScene(scene);
		popupwindow.setResizable(false);
		popupwindow.showAndWait();
	}

	public void deleteBook() {				// loading of delete book window as popup
		popupwindow.setTitle("Delete Book");
		Pane pane=new Pane();
		try {
			pane= FXMLLoader.load(getClass().getResource("../fxmlfiles/DeleteBook.fxml"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		Scene scene = new Scene(pane);
		popupwindow.setScene(scene);
		popupwindow.setResizable(false);		
		popupwindow.showAndWait();
	}

	public void updateMember() {			// loading of update member window as popup
		popupwindow.setTitle("Update Member");
		Pane pane=new Pane();
		try {
			pane= FXMLLoader.load(getClass().getResource("../fxmlfiles/UpdateMember.fxml"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		Scene scene = new Scene(pane);
		popupwindow.setScene(scene);		
		popupwindow.showAndWait();
		
	}

	public void newUserAccount() {		// add new user account
		popupwindow.setTitle("New User");
		Pane pane=new Pane();
		try {
			pane= FXMLLoader.load(getClass().getResource("../fxmlfiles/NewUser.fxml"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		Scene scene = new Scene(pane);
		popupwindow.setScene(scene);
		popupwindow.setResizable(false);
		img=new Image(getClass().getResourceAsStream("../images/icons/book-128.png"));
		popupwindow.getIcons().clear();
		popupwindow.getIcons().add(img);
		popupwindow.showAndWait();
	}
	
	public void deleteUser() {
		popupwindow.setTitle("Delete User");
		Pane pane=new Pane();
		try {
			pane= FXMLLoader.load(getClass().getResource("../fxmlfiles/DeleteUser.fxml"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		Scene scene = new Scene(pane);
		popupwindow.setScene(scene);
		popupwindow.setResizable(false);
		popupwindow.showAndWait();
	}
	
}
