package application.fxmlfilescontroller;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import application.javafiles.PopUp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ReturnBook implements Initializable{
	
	@FXML private TextField issueID;
	@FXML private TextField bookId;
	@FXML private Label bookName;
	@FXML private Label price;
	@FXML private Label totalBook;
	@FXML private Label issueDate;
	@FXML private Label lastDate;
	@FXML private Label returnedDate;
	@FXML private Label fine;
	@FXML private Button calculateFine;
	@FXML private Button payFine;
	@FXML private Button submit;
	@FXML private ImageView frontCover;
	@FXML private Label status;
	
	private Image img;
	private LocalDate lastdate;
	private LocalDate returndate;
	private Double bookPrice;
	private String issueId;
	private String member;
	private Double calculatedFine;
	private String payid;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		calculateFine.disableProperty().bind(bookId.textProperty().isEmpty());
		returndate=LocalDate.now();
		submit.setDisable(true);
		payFine.setDisable(true);
		returnedDate.setText(returndate.toString());
		issueID.textProperty().addListener(e-> checkDetails());
	}
	
	private void checkDetails() {
		String issueid=issueID.getText();
		if(issueid!=null && issueid.length()==33) {
			String query="SELECT issue_id FROM `library`.`not_returned` WHERE issue_id=?;";
			PreparedStatement st;
			try {
				st = ProjectController.dbcon.prepareStatement(query);
				st.setString(1, issueid);
				ResultSet rs=st.executeQuery();
				if(rs.next()) {
					issueID.setStyle("-fx-border-color:green;");
					fillDetails();
				}
			} catch (SQLException e) {
				new PopUp().alertMessage("Error", e.getMessage());
				issueID.setStyle("-fx-border-color:red;");
				e.printStackTrace();
			}
		}
		else {
			issueID.setStyle("-fx-border-color:red;");
		}
	}
	private void fillDetails() {
		String query1="SELECT issue_date,last_date,member_id FROM `library`.`not_returned` WHERE book_id=? AND issue_id=?;";
		String query2="SELECT book_name,price,copy FROM `library`.`book` WHERE book_id=?;";
		String query3="SELECT front_cover FROM `library`.`book_cover_images` WHERE book_id=?;";
		try {
			PreparedStatement st=ProjectController.dbcon.prepareStatement(query1);
			st.setString(1, bookId.getText());
			st.setString(2, issueID.getText());
			ResultSet rs=st.executeQuery();
			if(rs.next()) {
				issueId=issueID.getText();
				issueDate.setText(rs.getDate(1).toString());
				lastdate=rs.getDate(2).toLocalDate();
				lastDate.setText(lastdate.toString());
				member=rs.getString(3);
				calculateFine();
				st=ProjectController.dbcon.prepareStatement(query2);
				st.setString(1, bookId.getText());
				rs=st.executeQuery();
				if(rs.next()) {
					bookName.setText(rs.getString(1));
					bookPrice=rs.getDouble(2);
					price.setText(bookPrice.toString());
					totalBook.setText(Integer.toString(rs.getInt(3)));
					st=ProjectController.dbcon.prepareStatement(query3);
					st.setString(1, bookId.getText());
					rs=st.executeQuery();
					if(rs.next()) {
						img=new Image(rs.getBlob(1).getBinaryStream());
						if(img!=null) {
							frontCover.setImage(img);
						}
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void calculateFine() {		
		calculatedFine = 0.0;
		if (returndate.isAfter(lastdate)) {
			calculatedFine = bookPrice * 0.09 * (ChronoUnit.DAYS.between(lastdate, returndate));
			fine.setText(Double.toString(calculatedFine) + " INR");
			payFine.setDisable(false);
		} else {
			fine.setText("No fine.");
			submit.setDisable(false);
		}
	}

	
	@FXML
	public void payFine() {
		PopUp payment=new PopUp();
		payid=member+LocalDateTime.now().toString();
		if(payment.finePay(member,calculatedFine,payid)) {
			String query3="INSERT INTO `library`.`fine` (payment_id,payment,time,member_id) VALUES(?,?,?,?);";
			PreparedStatement st;
			try {
				st = ProjectController.dbcon.prepareStatement(query3);
				st.setString(1, payid);
				st.setDouble(2, calculatedFine);
				st.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
				st.setString(4, member);
				int c=st.executeUpdate();
				System.out.println(c);
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			submit.setDisable(false);
		}
		else {
			payment.alertMessage("Payment Alert", "There is a fine for your account.\n Please clear it first for further processes.");
		}
	}
	
	@FXML 
	public void submit() {
		String query1="UPDATE `library`.`issue` SET return_date=? WHERE issue_id=?;";
		String query2="UPDATE `library`.`book` SET copy=copy+1 WHERE book_id=?;";
		try {
			PreparedStatement st=ProjectController.dbcon.prepareStatement(query1);
			st.setDate(1, Date.valueOf(returndate));
			st.setString(2, issueId);
			int a=st.executeUpdate();
			st=ProjectController.dbcon.prepareStatement(query2);
			st.setString(1, bookId.getText());
			int b=st.executeUpdate();
			System.out.println("a: "+a+" b: "+b);
			status.setText("Book is successfully returned.");
			reset();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void reset() {
		issueID.clear();
		bookId.clear();
		issueDate.setText(null);
		lastDate.setText(null);
		fine.setText(null);
		bookName.setText(null);
		price.setText(null);
		totalBook.setText(null);
		bookName.setText(null);
		frontCover.setImage(null);
	}
}
