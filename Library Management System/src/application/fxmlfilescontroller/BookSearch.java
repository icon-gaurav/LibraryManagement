package application.fxmlfilescontroller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.javafiles.PopUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookSearch implements Initializable{
	
	@FXML private ImageView frontCover;
	@FXML private ComboBox<String> editor;
	@FXML private Button search;
	@FXML private Label bkname;
	@FXML private Label bkAuthor;
	@FXML private Label bkPublisher;
	@FXML private Label bkPubDate;
	@FXML private Label bkCategory;
	@FXML private Label bkPrice;
	@FXML private Label bkAvailable;
	
	private Image img;
	private ObservableList<String> bklist=FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		editor.setEditable(true);
		editor.getEditor().textProperty().addListener(e-> populateList());
		editor.setItems(bklist);
	}
	
	private boolean fillDetails(String id) {
		boolean result=false;
		String query1=" SELECT * FROM `library`.`book` WHERE book_id=?";
		String query2="SELECT front_cover FROM `library`.`book_cover_images` WHERE book_id=?;";
		String query3="SELECT author_name FROM `library`.`book_has_author` WHERE Book_book_id=?;";
		try {
			PreparedStatement st=ProjectController.dbcon.prepareStatement(query1);
			st.setString(1,id);
			ResultSet rs=st.executeQuery();
			if(rs.next()) {
				bkname.setText(rs.getString(2));
				bkPubDate.setText(rs.getDate(3).toString());
				bkPrice.setText(Double.toString(rs.getDouble(4)));
				bkPublisher.setText(rs.getString(5));
				bkAvailable.setText(Integer.toString(rs.getInt(6)));
				bkCategory.setText(rs.getString(7));
				result=true;
			}
			st=ProjectController.dbcon.prepareStatement(query2);
			st.setString(1,id);
			rs=st.executeQuery();
			if(rs.next()) {
				img=new Image(rs.getBlob(1).getBinaryStream());
				if(img!=null) {
					frontCover.setImage(img);
				}
			}
			st=ProjectController.dbcon.prepareStatement(query3);
			st.setString(1,id);
			rs=st.executeQuery();
			while(rs.next()) {
				bkAuthor.setText(bkAuthor.getText()+rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private void populateList() {
		String query="SELECT book_name,book_id FROM `library`.`book` WHERE book_name LIKE ? OR book_id LIKE ? ;";
		try {
			PreparedStatement st=ProjectController.dbcon.prepareStatement(query);
			st.setString(1, "%"+editor.getEditor().getText()+"%");
			st.setString(2, "%"+editor.getEditor().getText()+"%");
			ResultSet rs=st.executeQuery();
			while(rs.next()) {
				String data=rs.getString(1)+" : "+rs.getString(2);
				if(!bklist.contains(data))
					bklist.add(rs.getString(1)+" : "+rs.getString(2));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void search() {
		String id=editor.getSelectionModel().getSelectedItem();
		if(!fillDetails(id.substring(id.indexOf(':')+2))) {
			new PopUp().alertMessage("Error!", "No such Book found!");
		}
		
	}
}
