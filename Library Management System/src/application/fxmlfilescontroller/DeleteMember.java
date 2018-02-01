package application.fxmlfilescontroller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class DeleteMember implements Initializable{
	
	@FXML private TextField memberId;
	@FXML private Label memberName;
	@FXML private Label email;
	@FXML private Label mobile;
	@FXML private ImageView profile;
	@FXML private Button delete;
	@FXML private Button cancel;
	@FXML private Label status;
	@FXML private ProgressBar pgrbar;
	
	private Connection dbc=application.fxmlfilescontroller.ProjectController.dbcon;
	private String query="";
	private String memberid="";
	private ResultSet result;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		delete.setDisable(true);
		memberId.textProperty().addListener(e-> checkMember());
		pgrbar.setVisible(false);
	}
	
	private void checkMember() {
		memberid=memberId.getText();
		if(memberid.length()==10) {
			String query="SELECT member_id,fname,mname,lname,email,mobileno,profilePicture FROM library.member WHERE member_id=?;";
			try {
				PreparedStatement st=dbc.prepareStatement(query);
				st.setString(1, memberid);
				result=st.executeQuery();
				if(result.next()) {
					fillEntries();
					delete.setDisable(false);
					memberId.setStyle("-fx-border-color:green;");
					st.close();
				}
				else {
					memberId.setStyle("-fx-border-color:red;");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			memberId.setStyle("-fx-border-color:red;");
		}
		
	}
	
	private void fillEntries() {
		try {
			memberName.setText(result.getString(2));
			if(result.getString(3)!=null) {
				memberName.setText(memberName.getText()+" "+result.getString(3));
			}
			memberName.setText(memberName.getText()+" "+result.getString(4));
			email.setText(result.getString(5));
			mobile.setText(result.getString(6));
			Image img=new Image(result.getBlob(7).getBinaryStream());
			if(img!=null)
				profile.setImage(img);
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void delete() {
		query="DELETE FROM `library`.`member` WHERE member_id = ?;";
		try {
			PreparedStatement st=dbc.prepareStatement(query);
			st.setString(1, memberid);
			int response= st.executeUpdate();
			if(response!=1)
				throw new SQLException("updation error");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void cancel() {
		Stage stage=(Stage)cancel.getScene().getWindow();
		stage.close();
	}
	
}
