package application.fxmlfilescontroller;
import application.javafiles.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MemberDetails implements Initializable{
	
	@FXML private TableView<Member> memberdetails;
	@FXML private TableColumn<Member,String> memberId;
	@FXML private TableColumn<Member,String> memberName;
	@FXML private TableColumn<Member,LocalDate> dob;
	@FXML private TableColumn<Member,String> gender;
	@FXML private TableColumn<Member,String> address;
	@FXML private TableColumn<Member,Timestamp> joinDate;
	@FXML private TableColumn<Member,Integer> bookIssued;
	
	private ResultSet rs;
	private Connection dbcon=ProjectController.dbcon;
	private ObservableList<Member> data=FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		memberId.setCellValueFactory(new PropertyValueFactory<Member,String>("Id"));
		memberName.setCellValueFactory(new PropertyValueFactory<Member,String>("Name"));
		dob.setCellValueFactory(new PropertyValueFactory<Member,LocalDate>("Dob"));
		gender.setCellValueFactory(new PropertyValueFactory<Member,String>("Gender"));
		address.setCellValueFactory(new PropertyValueFactory<Member,String>("Address"));
		joinDate.setCellValueFactory(new PropertyValueFactory<Member,Timestamp>("Joiningdate"));
		bookIssued.setCellValueFactory(new PropertyValueFactory<Member,Integer>("Issuedbook"));
		populateTable();
	}
	
	private void populateTable() {
		String query="SELECT member_id,fname,mname,lname,dateofbirth,gender,address,joining_date,(SELECT count(*) FROM `library`.`not_returned` GROUP BY not_returned.member_id HAVING not_returned.member_id=member.member_id) FROM member;";
		try {
			PreparedStatement st = dbcon.prepareStatement(query);
			rs=st.executeQuery();
			data.clear();
			while(rs.next()) {
				data.add(new Member(rs.getString(1),rs.getString(2)+rs.getString(3)+rs.getString(4),rs.getString(7),rs.getDate(5).toLocalDate(),rs.getString(6),rs.getTimestamp(8),rs.getInt(9)));
			}
			memberdetails.setItems(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
}
