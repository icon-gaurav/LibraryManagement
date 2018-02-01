package application.fxmlfilescontroller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MemberSearch implements Initializable{
	
	@FXML private ComboBox<String> mname;
	@FXML private TextField mid;
	@FXML private Button srchId;
	@FXML private Button srchName;
	@FXML private Label name;
	@FXML private Label dob;
	@FXML private Label address;
	@FXML private Label gender;
	@FXML private Label joinDate;
	@FXML private ImageView profile;
	
	private ObservableList<String> mlist=FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		srchId.disableProperty().bind(mid.textProperty().isEmpty());
		srchName.disableProperty().bind(mname.getEditor().textProperty().isEmpty());
		mname.getEditor().textProperty().addListener(e-> {populateList();});
		mname.setItems(mlist);
		mname.setEditable(true);
	}
	
	private boolean fillDetails(String id) {
		boolean result=false;
		String query1=" SELECT fname,mname,lname,gender,dateofbirth,address,joining_date,profilePicture FROM `library`.`member` WHERE member_id=?";
		try {
			PreparedStatement st=ProjectController.dbcon.prepareStatement(query1);
			st.setString(1,id);
			ResultSet rs=st.executeQuery();
			if(rs.next()) {
				name.setText(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));
				gender.setText(rs.getString(4));
				dob.setText(rs.getDate(5).toString());
				address.setText(rs.getString(6));				
				joinDate.setText(rs.getString(7));
				Image img=new Image(rs.getBlob(8).getBinaryStream());
				if(img!=null) {
					profile.setImage(img);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private void populateList() {
		String query="SELECT fname,member_id FROM `library`.`member` WHERE fname LIKE ?;";
		try {
			PreparedStatement st=ProjectController.dbcon.prepareStatement(query);
			st.setString(1, "%"+mname.getEditor().getText()+"%");
			ResultSet rs=st.executeQuery();
			mlist.clear();
			while(rs.next()) {
				mlist.add(rs.getString(1)+" : "+rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void srchByName() {
		if(fillDetails(mname.getValue().substring(mname.getValue().indexOf(':')+1,mname.getValue().length()))) {
			
		}
		else {
			
		}
		
	}
	
	@FXML
	public void srchById() {
		if(fillDetails(mid.getText())) {
			
		}
		else {
			
		}
	}
}
