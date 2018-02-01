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
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class NewMember implements Initializable{
	
	
	@FXML private TextField memberid;
	@FXML private TextField fname;
	@FXML private TextField mname;
	@FXML private TextField lname;
	@FXML private ChoiceBox<String> gender;
	@FXML private DatePicker dob;
	@FXML private TextArea address;
	@FXML private TextField email;
	@FXML private TextField mobileno;
	@FXML private ChoiceBox<String> college;
	@FXML private ChoiceBox<String> branch;
	@FXML private ImageView profilePicture;
	@FXML private Button upload;
	@FXML private Label status;
	@FXML private Button back;
	@FXML private Button Submit;
	@FXML private Button erase;
	
	private ObservableList<String> genderList=FXCollections.observableArrayList("Male","Female","Others");
	private ObservableList<String> collegeList=FXCollections.observableArrayList("USICT","DTU","USMS","USTS");
	private ObservableList<String> branchList=FXCollections.observableArrayList("B.Tech(CSE)","BCA","B.Ed","B.Tech(Mechanical)");

	private String Query="";
	private Image profile;
	private File file;
	private String memberId;
	private String emailId;
	private LocalDate dateofbirth;
	private Long mobile;
	private String add;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		gender.setValue("Male");
		gender.setItems(genderList);
		college.setValue("USICT");
		college.setItems(collegeList);
		branch.setValue("B.Ed");
		branch.setItems(branchList);
		Query="insert into library.member (member_id,fname,mname,lname,gender,dateofbirth,mobileno,college,branch,address,profilePicture) values(?,?,?,?,?,?,?,?,?,?,?);";
	}
	private boolean validateDetails() {
		boolean result=true;
		String query="SELECT member_id FROM `library`.`member` WHERE member_id=?;";
		memberId=memberid.getText();
		if(fname.getText().isEmpty()) {
			fname.setStyle("-fx-border-color:red");
			result=false;
		}
		else
			fname.setStyle("-fx-border-color:green");
		mname.setStyle("-fx-border-color:green");		
		if(lname.getText().isEmpty()) {
			lname.setStyle("-fx-border-color:red");
			result=false;
		}
		else
			lname.setStyle("-fx-border-color:green");
		if(memberId.length()<=10) {
			try {
				PreparedStatement st=ProjectController.dbcon.prepareStatement(query);
				st.setString(1, memberId);
				ResultSet rs=st.executeQuery();
				if(rs.next()) {
					result=false;
					memberid.setStyle("-fx-border-color:red");
				}
				else {
					memberid.setStyle("-fx-border-color:green");
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				result=false;
				memberid.setStyle("-fx-border-color:red");
			}
		}
		else {
			result=false;
			memberid.setStyle("-fx-border-color:red");
		}
		
		try {
			mobile=Long.parseLong(mobileno.getText());
			mobileno.setStyle("-fx-border-color:green;");
		}catch(NumberFormatException e) {
			mobileno.setStyle("-fx-border-color:red;");
			result=false;
		}
		if((dateofbirth=dob.getValue())==null) {
			dob.setStyle("-fx-border-color:red;");
			result=false;
		}
		emailId=email.getText();
		if(emailId.isEmpty()) {
			email.setStyle("-fx-border-color:red;");
			result=false;
		}
		else {
			String pattern="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Pattern p=Pattern.compile(pattern);
			Matcher match=p.matcher(emailId);
			if(match.matches()) {
				email.setStyle("-fx-border-color:green;");
				result=true;
			}
			else {
				email.setStyle("-fx-border-color:red;");
				result=false;
			}
		}
		add=address.getText();
		if(add.isEmpty()) {
			address.setStyle("-fx-border-color:red;");
			result=false;
		}
		else {
			String pattern="[^$#@!*~`{}]";
			Pattern p=Pattern.compile(pattern);
			Matcher match=p.matcher(add);
			if(!match.matches()) {
				address.setStyle("-fx-border-color:green;");
				result=true;
			}
			else {
				address.setStyle("-fx-border-color:red;");
				result=false;
			}
		}
		return result;		
	}
	@FXML
	void submit() {
		if(validateDetails()) {
			try {
				PreparedStatement st=ProjectController.dbcon.prepareStatement(Query);
				st.setString(1, memberid.getText());
				st.setString(2, fname.getText());
				st.setString(3, mname.getText());
				st.setString(4, lname.getText());
				st.setString(5, gender.getValue());
				st.setDate(6, Date.valueOf(dateofbirth));
				st.setLong(7, mobile);
				st.setString(8,college.getValue());
				st.setString(9, branch.getValue());
				st.setString(10, add);
				InputStream input=new FileInputStream(file);
				st.setBinaryStream(11, input);
				int a=st.executeUpdate();
				if(a==1)
					status.setText(" Member Added Successfull!!");
				else
					throw new SQLException();
			}
			catch(SQLException | FileNotFoundException e) {
				e.printStackTrace();
				status.setText("Oops! Something wentwrong!!");
			}
		}
	}
	
	@FXML
	public void uploadPicture() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter filtergif=new FileChooser.ExtensionFilter("GIF files", "*.gif");
		FileChooser.ExtensionFilter filterjpg=new FileChooser.ExtensionFilter("JPG files", "*.jpg");
		FileChooser.ExtensionFilter filterpng=new FileChooser.ExtensionFilter("PNG files", "*.png");
		fileChooser.getExtensionFilters().addAll(filterjpg,filtergif,filterpng);
		file=fileChooser.showOpenDialog(null);
		try {
			BufferedImage img=ImageIO.read(file);
			profile=SwingFXUtils.toFXImage(img,null);
			profilePicture.setImage(profile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
