package application.fxmlfilescontroller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class DeleteBook implements Initializable{
	
	@FXML private TextField bookId;
	@FXML private Label bookName;
	@FXML private Label author;
	@FXML private Label publisher;
	@FXML private Label pubDate;
	@FXML private Label totalBook;
	@FXML private ImageView frontCover;
	@FXML private Button search;
	@FXML private Button delete;
	@FXML private Button close;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		search.disableProperty().bind(bookId.textProperty().isEmpty());
		delete.disableProperty().bind(bookName.textProperty().isEmpty());
	}
	
	@FXML
	public void search() {
		String query="SELECT book_name,author,publisher_pub_name,published_date,copy,front_cover FROM library.book left join library.book_cover_images ON book.book_id=book_cover_images.book_id WHERE book.book_id=?;";
		try {
			PreparedStatement st= ProjectController.dbcon.prepareStatement(query);
			st.setString(1, bookId.getText());
			ResultSet rs=st.executeQuery();
			if(rs.next()) {
				bookName.setText(rs.getString(1));
				author.setText("Author : "+rs.getString(2));
				publisher.setText("Publisher : "+rs.getString(3));
				pubDate.setText("Published Date : "+rs.getDate(4).toString());
				totalBook.setText("Total No. of Books : "+rs.getInt(5));
				frontCover.setImage(new Image(rs.getBlob(6).getBinaryStream()));
				delete.setDisable(false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	@FXML 
	public void delete() {
		String query1="DELETE FROM `library`.`book_cover_images` WHERE book_cover_images.book_id=?;";
		String query2="DELETE FROM `library`.`Book_has_author` WHERE Book_book_id=?;";
		String query3=" DELETE FROM `library`.`book` WHERE book.book_id=?;";
		try {
			PreparedStatement st=ProjectController.dbcon.prepareStatement(query1);
			st.setString(1, bookId.getText());
			int a=st.executeUpdate();
			st=ProjectController.dbcon.prepareStatement(query2);
			st.setString(1, bookId.getText());
			int b=st.executeUpdate();
			st=ProjectController.dbcon.prepareStatement(query3);
			st.setString(1, bookId.getText());
			int c=st.executeUpdate();
			System.out.println("a : "+a+" b : "+b+" c : "+c);
			bookId.clear();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void close() {
		Stage stage=(Stage)close.getScene().getWindow();
		stage.close();
	}

}
