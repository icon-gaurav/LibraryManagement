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

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NewBook implements Initializable{
	
	private String bookId;
	private String bookName;
	private String author;
	private String publisher;
	private LocalDate pub_date;
	private Double price;
	private String category;
	private Integer totalBooks;
	private Image frontCover;
	private File file;
	
		
	@FXML private TextField BookId;
	@FXML private TextField BookName;
	@FXML private TextField Author;
	@FXML private TextField authorCountry;
	@FXML private DatePicker authorDob;
	@FXML private TextField Publisher;
	@FXML private TextArea publisherAddress;
	@FXML private DatePicker Pub_Date;
	@FXML private TextField Price;
	@FXML private TextField Category;
	@FXML private TextField TotalBooks;
	@FXML private ImageView FrontCoverView;
	@FXML private Button submit;
	@FXML private Button validate;
	@FXML private Button close;
	@FXML private Label status;

	@Override
 	public void initialize(URL arg0, ResourceBundle arg1) {
		submit.setDisable(true);
		BookId.textProperty().addListener(e-> checkBookid());
		validate.setDisable(true);
		Author.textProperty().addListener(e-> checkAuthor());
		Publisher.textProperty().addListener(e-> checkPublisher());
		authorCountry.setDisable(true);
		authorDob.setDisable(true);
		publisherAddress.setDisable(true);
	}
	private void checkBookid() {
		bookId=BookId.getText();
		boolean result=true;
		String query="SELECT book_id FROM `library`.`book` WHERE book_id=?;";
		if(bookId.length()==10) {
			try {
				PreparedStatement st=ProjectController.dbcon.prepareStatement(query);
				st.setString(1,bookId);
				ResultSet rs=st.executeQuery();
				if(rs.next()) {
					result=false;
					BookId.setStyle("-fx-border-color:red");
				}
				else {
					BookId.setStyle("-fx-border-color:green");
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				result=false;
				BookId.setStyle("-fx-border-color:red");
			}
		}
		else {
			result=false;
			BookId.setStyle("-fx-border-color:red");
		}
		if(result==false) {
			bookId=null;
		}
		else {
			validate.setDisable(false);
		}
	}
	private void checkAuthor() {
		author=Author.getText();
		String query="SELECT name FROM `library`.`author` WHERE name=?;";
		if((!author.isEmpty()) && author.length()<=30) {
			try {
				PreparedStatement st=ProjectController.dbcon.prepareStatement(query);
				st.setString(1,author);
				ResultSet rs=st.executeQuery();
				if(rs.next()) {
					author=null;
					authorCountry.setDisable(true);
					authorDob.setDisable(true);
					Author.setStyle("-fx-border-color:green");
				}
				else {
					authorCountry.setDisable(false);
					authorDob.setDisable(false);
					Author.setStyle("-fx-border-color:green");
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				author=null;
				Author.setStyle("-fx-border-color:red");
			}
		}
		else {
			author=null;
			Author.setStyle("-fx-border-color:red");
		}
	}
	private void checkPublisher() {
		publisher=Publisher.getText();
		String query="SELECT pub_name FROM `library`.`publisher` WHERE pub_name=?;";
		if((!publisher.isEmpty()) && publisher.length()<=30) {
			try {
				PreparedStatement st=ProjectController.dbcon.prepareStatement(query);
				st.setString(1,publisher);
				ResultSet rs=st.executeQuery();
				if(rs.next()) {
					publisher=null;
					publisherAddress.setDisable(true);
					Publisher.setStyle("-fx-border-color:green");
				}
				else {
					publisherAddress.setDisable(false);
					Publisher.setStyle("-fx-border-color:green");
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				publisher=null;
				Publisher.setStyle("-fx-border-color:red");
			}
		}
		else {
			publisher=null;
			Publisher.setStyle("-fx-border-color:red");
		}
	}
	private boolean check() {
		boolean result=true;		
		bookName=BookName.getText();
		if(bookName!=null) {
			BookName.setStyle("-fx-border-color:green");
		}
		else {
			result=false;
			BookName.setStyle("-fx-border-color:red");
		}
		if((pub_date=Pub_Date.getValue())==null) {
			result=false;
			Pub_Date.setStyle("-fx-border-color:red;");
		}
		else {
			Pub_Date.setStyle("-fx-border-color:green;");
		}
		try {
			price=Double.parseDouble(Price.getText());
			Price.setStyle("-fx-border-color:green");
		}
		catch(Exception e) {
			result=false;
			Price.setStyle("-fx-border-color: red");
		}
		category=Category.getText();
		Category.setStyle("-fx-border-color:green");
		try {
			totalBooks=Integer.parseInt(TotalBooks.getText());
			TotalBooks.setStyle("-fx-border-color:green");
		}
		catch(Exception e) {
			result=false;
			TotalBooks.setStyle("-fx-border-color: red");
		}
		
		return result;
	}
	
	@FXML
	public void validate() {
		if(check()) {
			submit.setDisable(false);
		}
		else {
			status.setStyle("-fx-text-fill:red");
			status.setText("Pay attention to red marked entries!");
		}
		
	}
	
	@FXML
	public void submit() {
		if(check()) {
			String query1="INSERT INTO `library`.`book` (book_id, book_name,published_date,price,publisher_pub_name,copy,category) VALUES (?,?,?,?,?,?,?);";
			String query2="INSERT INTO `library`.`book_has_author`(`Book_book_id`,`author_name`) VALUES(?,?);";
			String query3="INSERT INTO `library`.`book_cover_images` (`book_id`,`front_cover`) VALUES (?,?);";
			String query4="INSERT INTO `library`.`publisher`(pub_name,address) VALUES(?,?);";
			try {
				PreparedStatement st;
				if(author!=null) {
					String query="INSERT INTO `library`.`author`(name,country,birthdate,totalbook) VALUES (?,?,?,?);";
					st=ProjectController.dbcon.prepareStatement(query);
					st.setString(1, author);
					st.setString(2, authorCountry.getText());
					if(authorDob.getValue()!=null)
						st.setDate(3, Date.valueOf(authorDob.getValue()));
					else
						st.setDate(3, null);
					st.setInt(4, 1);
					st.executeUpdate();
				}
				else {
					String query="UPDATE `library`.`author` SET totalbook=totalbook+1 WHERE name=?";
					st=ProjectController.dbcon.prepareStatement(query);
					st.setString(1, author);
					st.executeUpdate();
				}
				st=ProjectController.dbcon.prepareStatement(query1);
				st.setString(1, bookId);
				st.setString(2,bookName);
				st.setString(3, pub_date.toString());
				st.setDouble(4, price);
				st.setString(5, publisher);
				st.setInt(6, totalBooks);
				st.setString(7,category);
				st.executeUpdate();
				st=ProjectController.dbcon.prepareStatement(query2);
				st.setString(1, bookId);
				st.setString(2, author);
				st.executeUpdate();
				st=ProjectController.dbcon.prepareStatement(query3);
				st.setString(1, bookId);
				st.setBinaryStream(2,(InputStream)new FileInputStream(file));
				st.executeUpdate();
				if(publisher!=null) {
					st=ProjectController.dbcon.prepareStatement(query4);
					st.setString(1, publisher);
					st.setString(2, publisherAddress.getText());
					st.executeUpdate();
				}
				status.setStyle("-fx-text-fill:green");
				status.setText("Book added Successfully.");
			} catch (SQLException | FileNotFoundException e) {
				status.setStyle("-fx-text-fill:red");
				status.setText(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void close() {
		reset();
		Stage stage=(Stage)close.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	public void uploadPicture() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter filtergif=new FileChooser.ExtensionFilter("GIF files", "*.gif");
		FileChooser.ExtensionFilter filterjpg=new FileChooser.ExtensionFilter("JPG files", "*.jpg");
		FileChooser.ExtensionFilter filterpng=new FileChooser.ExtensionFilter("PNG files", "*.png");
		fileChooser.getExtensionFilters().addAll(filterjpg,filterpng,filtergif);
		file=fileChooser.showOpenDialog(null);
		try {
			BufferedImage img=ImageIO.read(file);
			frontCover=SwingFXUtils.toFXImage(img,null);
			FrontCoverView.setImage(frontCover);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void reset() {
		BookId.clear();
		BookId.setStyle("-fx-border-color:none");
		BookName.clear();
		BookName.setStyle("-fx-border-color:none");
		Author.clear();
		Author.setStyle("-fx-border-color:none");
		Publisher.clear();
		Publisher.setStyle("-fx-border-color:none");
		Category.clear();
		Category.setStyle("-fx-border-color:none");
		Pub_Date.setStyle("-fx-border-color:none");
		Price.clear();
		Price.setStyle("-fx-border-color:none");
		TotalBooks.clear();
		TotalBooks.setStyle("-fx-border-color:none");
		FrontCoverView.setImage(null);
	}

}
