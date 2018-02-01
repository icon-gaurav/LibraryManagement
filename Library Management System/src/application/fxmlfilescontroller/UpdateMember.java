package application.fxmlfilescontroller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UpdateMember implements Initializable {
	
	@FXML private TextField memberId;
	@FXML private TextField fname;
	@FXML private TextField mname;
	@FXML private TextField lname;
	@FXML private ChoiceBox<String> gender;
	@FXML private DatePicker dob;
	@FXML private TextField email;
	@FXML private TextField mobile;
	@FXML private TextField college;
	@FXML private TextField branch;
	@FXML private TextArea address;
	@FXML private ImageView profile;
	@FXML private Button search;
	@FXML private Button update;
	@FXML private Button cancel;
	@FXML private Label status;
	@FXML private Button upload;
	
	private File file;
	private Image image;
	private InputStream inputstream;
	private PreparedStatement st;
	private ObservableList<String> genderList=FXCollections.observableArrayList("Male","Female","Others");
	private String query="SELECT fname,mname,lname,gender,dateofbirth,email,mobileno,college,branch,address,profilePicture FROM library.member WHERE member_id=?;";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		update.setDisable(true);
		upload.setDisable(true);
		gender.setItems(genderList);
		search.disableProperty().bind(memberId.textProperty().isEmpty());
		
	}
	
	@FXML
	public void fillEntries() {
		try {
			st=ProjectController.dbcon.prepareStatement(query);
			st.setString(1, memberId.getText());
			ResultSet rs=st.executeQuery();
			if(rs.next()) {
				fname.setText(rs.getString(1));
				mname.setText(rs.getString(2));
				lname.setText(rs.getString(3));
				gender.setValue(rs.getString(4));
				dob.setValue(rs.getDate(5).toLocalDate());
				email.setText(rs.getString(6));
				mobile.setText(rs.getString(7));
				college.setText(rs.getString(8));
				branch.setText(rs.getString(9));
				address.setText(rs.getString(10));
				inputstream=rs.getBlob(11).getBinaryStream();
				image=new Image(inputstream);
				if(image!=null)
					profile.setImage(image);
				update.setDisable(false);
				upload.setDisable(false);
			}
			else {
				status.setStyle("-fx-color: red");
				status.setText("No Member found with this Member Id!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	@FXML 
	public void uploadProfile() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter filtergif=new FileChooser.ExtensionFilter("GIF files", "*.gif");
		FileChooser.ExtensionFilter filterjpg=new FileChooser.ExtensionFilter("JPG files", "*.jpg");
		FileChooser.ExtensionFilter filterpng=new FileChooser.ExtensionFilter("PNG files", "*.png");
		fileChooser.getExtensionFilters().addAll(filterjpg,filtergif,filterpng);
		file=fileChooser.showOpenDialog(null);
		try {
			BufferedImage img=ImageIO.read(file);
			image=SwingFXUtils.toFXImage(img,null);
			profile.setImage(image);
		}
		catch (IOException e) {
		e.printStackTrace();
		}
	}
	
	@FXML
	public void update() {
		query="UPDATE library.member SET fname=?, mname=?, lname=?, gender=?, dateofbirth=?, email=?, mobileno=?, college=?, branch=?, address=?, profilePicture=? WHERE member.member_id=?;";
		try {
			st=ProjectController.dbcon.prepareStatement(query);
			st.setString(1, fname.getText());
			st.setString(2, mname.getText());
			st.setString(3, lname.getText());
			st.setString(4, gender.getValue());
			st.setDate(5, Date.valueOf(dob.getValue()));
			st.setString(6, email.getText());
			st.setLong(7, Long.parseLong(mobile.getText()));
			st.setString(8, college.getText());
			st.setString(9, branch.getText());
			st.setString(10, address.getText());
			if(file!=null)
				st.setBinaryStream(11, (InputStream)new FileInputStream(file));
			else
				st.setBinaryStream(11,inputstream);
			st.setString(12, memberId.getText());
			int response=st.executeUpdate();
			if(response==1) {
				status.setStyle("-fx-color: green");
				status.setText("Member Updation Successfully...");
			}
			else
				throw new SQLException("updation erro!");
		} catch (SQLException | FileNotFoundException e) {
			status.setStyle("-fx-color: red");
			status.setText(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@FXML
	public void cancel() {
		Stage stage=(Stage) cancel.getScene().getWindow();
		stage.close();
	}
}
