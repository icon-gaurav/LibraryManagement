package application.fxmlfilescontroller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import application.javafiles.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BookDetails implements Initializable {
	
	@FXML private TableView<Book> bookTable;
	@FXML private TableColumn<Book,String> bookId;
	@FXML private TableColumn<Book,String> bookName;
	@FXML private TableColumn<Book,String> author;
	@FXML private TableColumn<Book,String> publisher;
	@FXML private TableColumn<Book,Integer> totalCopy;
	@FXML private TableColumn<Book,String> category;
	
	private ObservableList<Book> data=FXCollections.observableArrayList();
	private ObservableMap<String,ObservableList<String>> authorList=FXCollections.observableHashMap();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bookId.setCellValueFactory(new PropertyValueFactory<Book,String>("bookId"));
		bookName.setCellValueFactory(new PropertyValueFactory<Book,String>("bookName"));
		author.setCellValueFactory(new PropertyValueFactory<Book,String>("author"));
		publisher.setCellValueFactory(new PropertyValueFactory<Book,String>("publisher"));
		totalCopy.setCellValueFactory(new PropertyValueFactory<Book,Integer>("totalBooks"));
		category.setCellValueFactory(new PropertyValueFactory<Book,String>("category"));
		populateTable();
	}
	
	private void populateTable() {
		String query="SELECT book_id,book_name,publisher_pub_name,category,copy FROM library.book;";
		String query2="SELECT Book_book_id,author_name FROM `library`.`book_has_author`;";
		try {
			Statement st=ProjectController.dbcon.createStatement();
			ResultSet rs=st.executeQuery(query);
			st=ProjectController.dbcon.createStatement();
			ResultSet rs2=st.executeQuery(query2);
			while(rs2.next()) {
				ObservableList<String> list=FXCollections.observableArrayList();
				if(authorList.containsKey(rs2.getString(1))) {
					list=authorList.get(rs2.getString(1));
					list.add(rs2.getString(2));					
				}
				else {
					list.add(rs2.getString(2));
				}
				authorList.put(rs2.getString(1), list);
			}
			while(rs.next()) {
				data.add(new Book(rs.getString(1),rs.getString(2),authorList.get(rs.getString(1)),rs.getString(3),rs.getString(4),rs.getInt(5)));
			}
			bookTable.setItems(data);
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}

}
