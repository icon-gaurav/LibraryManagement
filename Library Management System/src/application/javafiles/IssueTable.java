package application.javafiles;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IssueTable {
	private StringProperty memberId;
	private StringProperty memberName;
	private StringProperty bookId;
	private StringProperty bookName;
	private StringProperty issueId;
	private LocalDate issueDate;
	private LocalDate lastDate;
	
	public IssueTable(String memberId,String memberName, String bookId, String bookName, String issueId, LocalDate issueDate, LocalDate lastDate) {
		this.memberId=new SimpleStringProperty(memberId);
		this.memberName=new SimpleStringProperty(memberName);
		this.bookId=new SimpleStringProperty(bookId);
		this.bookName=new SimpleStringProperty(bookName);
		this.issueId=new SimpleStringProperty(issueId);
		this.issueDate=issueDate;
		this.lastDate=lastDate;
		
	}
	
	public String getMemberId() {
		return memberId.get();
	}

	public void setMemberId(StringProperty memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName.get();
	}

	public void setMemberName(StringProperty memberName) {
		this.memberName = memberName;
	}

	public String getBookId() {
		return bookId.get();
	}

	public void setBookId(StringProperty bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName.get();
	}

	public void setBookName(StringProperty bookName) {
		this.bookName = bookName;
	}

	public String getIssueId() {
		return issueId.get();
	}

	public void setIssueId(StringProperty issueId) {
		this.issueId = issueId;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDate getLastDate() {
		return lastDate;
	}

	public void setLastDate(LocalDate lastDate) {
		this.lastDate = lastDate;
	}
	
}
