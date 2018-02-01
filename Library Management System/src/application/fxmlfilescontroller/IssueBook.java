package application.fxmlfilescontroller;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import application.javafiles.IssueTable;
import application.javafiles.PopUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IssueBook implements Initializable{
	
	@FXML private TextField memberId;
	@FXML private TextField bookId;
	@FXML private DatePicker issueDate;
	@FXML private DatePicker lastDate;
	@FXML private Label bookName;
	@FXML private Label publisher;
	@FXML private Label author;
	@FXML private Label price;
	@FXML private Label totalBook;
	@FXML private ImageView frontCover;
	@FXML private Button issue;
	@FXML private Button cancel;
	@FXML private TableView<IssueTable> issueTable;
	@FXML private TableColumn<IssueTable,String> bkid;
	@FXML private TableColumn<IssueTable,String> bkname;
	@FXML private TableColumn<IssueTable,String> memid;
	@FXML private TableColumn<IssueTable,String> memname;
	@FXML private TableColumn<IssueTable,String> issid;
	@FXML private TableColumn<IssueTable,LocalDate> issdate;
	@FXML private TableColumn<IssueTable,LocalDate> lastdate;
	
	private String memberName="";
	private ObservableList<IssueTable> data=FXCollections.observableArrayList();
	
	private void checkMember() {
		String memberid=memberId.getText();
		boolean result=false;
		String query="SELECT member_id,fname,mname,lname FROM `library`.`member` WHERE member_id=?;";
		if(memberid.length()<=10) {
			try {
				PreparedStatement st=ProjectController.dbcon.prepareStatement(query);
				st.setString(1, memberid);
				ResultSet rs=st.executeQuery();
				if(rs.next()) {
					memberName=rs.getString(2);
					if(!rs.getString(3).isEmpty()) {
						memberName+=" "+rs.getString(3);
					}
					memberName+=" "+rs.getString(4);
					result=true;
					memberId.setStyle("-fx-border-color:green");
				}
				else {
					memberId.setStyle("-fx-border-color:red");
				}
				
			} catch (SQLException e) {
				new PopUp().alertMessage("Error!", e.getMessage());
				e.printStackTrace();
				memberId.setStyle("-fx-border-color:red");
			}
		}
		else {
			result=false;
			memberId.setStyle("-fx-border-color:red");
		}
		if(result==true) {
			bookId.setDisable(false);
		}
	}
	
	private void checkBook() {
		String bookid=bookId.getText();
		boolean result=false;
		String query="SELECT book_id,copy FROM `library`.`book` WHERE book_id=?;";
		if(bookid.length()==10) {
			try {
				PreparedStatement st=ProjectController.dbcon.prepareStatement(query);
				st.setString(1,bookid);
				ResultSet rs=st.executeQuery();
				if(rs.next() && (rs.getInt(2)>=1)) {
					result=true;
					bookId.setStyle("-fx-border-color:green");
				}
				else {
					result=false;
					bookId.setStyle("-fx-border-color:red");
				}
			} catch (SQLException e1) {
				new PopUp().alertMessage("Error", e1.getMessage());
				e1.printStackTrace();
				result=false;
				bookId.setStyle("-fx-border-color:red");
			}
		}
		else {
			result=false;
			bookId.setStyle("-fx-border-color:red");
		}
		if(result==true) {
			fillBookDetails();
			issue.setDisable(false);
		}
	}

	private void clearFields() {
		bookName.setText("");
		author.setText("");
		publisher.setText("");
		price.setText("");
		totalBook.setText("");
		frontCover.setImage(null);
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		issueDate.setValue(LocalDate.now());
		lastDate.setValue(LocalDate.now().plusDays(14));
		issueDate.setDisable(true);
		lastDate.setDisable(true);
		bookId.setDisable(true);
		bookId.textProperty().addListener(e-> checkBook());
		memberId.textProperty().addListener(e-> checkMember());
		issue.setDisable(true);
		bkid.setCellValueFactory(new PropertyValueFactory<IssueTable,String>("bookId"));
		bkname.setCellValueFactory(new PropertyValueFactory<IssueTable,String>("bookName"));
		memid.setCellValueFactory(new PropertyValueFactory<IssueTable,String>("memberId"));
		memname.setCellValueFactory(new PropertyValueFactory<IssueTable,String>("memberName"));
		issid.setCellValueFactory(new PropertyValueFactory<IssueTable,String>("issueId"));
		issdate.setCellValueFactory(new PropertyValueFactory<IssueTable,LocalDate>("issueDate"));
		lastdate.setCellValueFactory(new PropertyValueFactory<IssueTable,LocalDate>("lastDate"));
		
	}
	
	
	@FXML
	public void issue() {
		String query="INSERT INTO library.issue (issue_id,book_id,issue_date,last_date,member_id) VALUES (?,?,?,?,?);";
		String query2="\"UPDATE `library`.`book` SET copy=copy-1 WHERE book_id=?;";
		String issueid=memberId.getText().substring(0, 5)+bookId.getText().substring(0, 5)+LocalDateTime.now().toString();
		try {
			PreparedStatement st=ProjectController.dbcon.prepareStatement(query);
			st.setString(1, issueid);
			st.setString(2, bookId.getText());
			st.setDate(3, Date.valueOf(LocalDate.now()));
			st.setDate(4, Date.valueOf(LocalDate.now().plusDays(14)));
			st.setString(5, memberId.getText());
			if(st.executeUpdate()==1) {
				st=ProjectController.dbcon.prepareStatement(query2);
				st.setString(1, bookId.getText());
				st.executeUpdate();
				data.add(new IssueTable(memberId.getText(),memberName,bookId.getText(),bookName.getText(),issueid,issueDate.getValue(),lastDate.getValue()));
				issueTable.setItems(data);
				clearFields();
			}
			else
				throw new SQLException("affected row is not one..");
		} catch (SQLException e) {
			new PopUp().alertMessage("Error", e.getMessage());
			e.printStackTrace();
			e.getMessage();
		}
		bookId.clear();
	}
	
	private void fillBookDetails() {
		String query="SELECT book_name,publisher_pub_name,price,copy,front_cover ";
		query+="FROM book left join book_cover_images on book.book_id = book_cover_images.book_id ";
		query+=" WHERE book.book_id=?;";
		String query2="SELECT author_name FROM book_has_author WHERE Book_book_id=?;";
		try {
			PreparedStatement st=ProjectController.dbcon.prepareStatement(query);
			PreparedStatement st2=ProjectController.dbcon.prepareStatement(query2);
			st.setString(1, bookId.getText());
			st2.setString(1, bookId.getText());
			ResultSet rs=st.executeQuery();
			ResultSet rs2=st2.executeQuery();
			if(rs.next()) {
				bookName.setText(rs.getString(1));
				publisher.setText(rs.getString(2));
				price.setText(Double.toString(rs.getDouble(3)));
				totalBook.setText(Integer.toString(rs.getInt(4)));
				while(rs2.next()) {
					author.setText(author.getText()+rs2.getString(1)+", ");
				}
				if(!rs.wasNull()) {
					frontCover.setImage(new Image(rs.getBlob(5).getBinaryStream()));
				}

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@FXML
	public void cancel() {
		bookId.clear();
		clearFields();
	}

}
