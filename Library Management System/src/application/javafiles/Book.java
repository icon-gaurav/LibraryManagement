package application.javafiles;

import java.time.LocalDate;
import java.util.Iterator;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Book {
	private StringProperty bookId;
	private StringProperty bookName;
	private StringProperty author;
	private StringProperty publisher;
	private StringProperty category;
	private IntegerProperty totalBooks;
	private DoubleProperty price;
	private StringProperty frontCover;
	private LocalDate publish_date;
	public Book(String bookId, String bookName, ObservableList<String> author, String publisher, String category, Integer totalBooks, Double price, LocalDate published_date, String frontCover) {
		this.bookId=new SimpleStringProperty(bookId);
		this.bookName=new SimpleStringProperty(bookName);
		this.publisher=new SimpleStringProperty(publisher);
		String aut="";
		Iterator<String> name=author.listIterator();
		while(name.hasNext()) {
			aut+=name.next()+", ";
		}
		this.author=new SimpleStringProperty(aut);
		this.category=new SimpleStringProperty(category);
		this.totalBooks=new SimpleIntegerProperty(totalBooks);
		this.price=new SimpleDoubleProperty(price);
		this.frontCover=new SimpleStringProperty(frontCover);
		this.publish_date=published_date;
	}
	public Book(String bookId, String bookName, ObservableList<String> author, String publisher, String category, Integer totalBooks) {
		this.bookId=new SimpleStringProperty(bookId);
		this.bookName=new SimpleStringProperty(bookName);
		this.publisher=new SimpleStringProperty(publisher);
		String aut="";
		Iterator<String> name=author.listIterator();
		while(name.hasNext()) {
			aut+=name.next()+", ";
		}
		this.author=new SimpleStringProperty(aut);
		this.category=new SimpleStringProperty(category);
		this.totalBooks=new SimpleIntegerProperty(totalBooks);
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
	public String getAuthor() {
		return author.get();
	}
	public void setAuthor(StringProperty author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher.get();
	}
	public void setPublisher(StringProperty publisher) {
		this.publisher = publisher;
	}
	public String getCategory() {
		return category.get();
	}
	public void setCategory(StringProperty category) {
		this.category = category;
	}
	public Integer getTotalBooks() {
		return totalBooks.get();
	}
	public void setTotalBooks(IntegerProperty totalBooks) {
		this.totalBooks = totalBooks;
	}
	public Double getPrice() {
		return price.get();
	}
	public void setPrice(DoubleProperty price) {
		this.price = price;
	}
	public StringProperty getFrontCover() {
		return frontCover;
	}
	public void setFrontCover(StringProperty frontCover) {
		this.frontCover = frontCover;
	}
	public LocalDate getPublish_date() {
		return publish_date;
	}
	public void setPublish_date(LocalDate publish_date) {
		this.publish_date = publish_date;
	}
	public StringProperty getbookName() {
		return bookName;
	}
	public StringProperty getbookId() {
		return bookId;
	}
	public StringProperty getauthor() {
		return author;
	}
	public StringProperty getpublisher(){
		return publisher;
	}
	public StringProperty getcategory() {
		return category;
	}
	public IntegerProperty gettotalbooks() {
		return totalBooks;
	}
	public DoubleProperty getprice() {
		return price;
	}
	public String getfrontCover() {
		return frontCover.get();
	}
}
