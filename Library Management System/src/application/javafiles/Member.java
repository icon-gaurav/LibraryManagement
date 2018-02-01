package application.javafiles;

import java.sql.*;
import java.time.LocalDate;
import javafx.beans.property.*;

public class Member {
	private StringProperty Id;
	private StringProperty Name;
	private StringProperty Address;
	private LocalDate Dob;
	private StringProperty Gender;
	private Timestamp Joiningdate;
	private IntegerProperty Issuedbook;
	public Member(String id,String name,String address,LocalDate dob,String gender,Timestamp joiningdate,Integer issuedbook) {
		this.Id=new SimpleStringProperty(id);
		this.Name=new SimpleStringProperty(name);
		this.Address=new SimpleStringProperty(address);
		this.Joiningdate=joiningdate;
		this.Dob=dob;
		this.Issuedbook=new SimpleIntegerProperty(issuedbook);
		this.Gender=new SimpleStringProperty(gender);
	}
	public String getName() {
		return this.Name.get();
	}
	public String getId() {
		return this.Id.get();
	}
	public String getAddress() {
		return this.Address.get();
	}
	public String getGender() {
		return this.Gender.get();
	}
	public LocalDate getDob() {
		return this.Dob;
	}
	public Timestamp getJoiningdate() {
		return this.Joiningdate;
	}
	public Integer getIssuedbook() {
		return this.Issuedbook.get();
	}
	public StringProperty IdProperty() {
		return Id;
	}
	public StringProperty NameProperty() {
		return Name;
	}
	public StringProperty AddressProperty() {
		return Address;
	}
	public StringProperty GenderProperty() {
		return Gender;
	}
	public IntegerProperty IssuebookProperty() {
		return Issuedbook;
	}
	public void setId(String value) {
		this.Id.set(value);
	}
	public void setName(String value) {
		this.Name.set(value);
	}
	public void setAddress(String value) {
		this.Address.set(value);
	}
	public void setGender(String value) {
		this.Gender.set(value);
	}
	public void setDob(LocalDate value) {
		this.Dob=value;
	}
	public void setJoiningdate(Timestamp value) {
		this.Joiningdate=value;
	}
	public void setIssuedbook(Integer value) {
		this.Issuedbook.set(value);
	}
}
