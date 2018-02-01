package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*
 *  main application class which loads home window for whole project
 */
public class Main extends Application {	
	
	public static Stage mainWindow;
	
	@Override
	public void start(Stage primaryStage) {
		mainWindow=primaryStage;
		try {
			Parent root=FXMLLoader.load(getClass().getResource("fxmlfiles/projectControllerfxml.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			Image image=new Image(getClass().getResourceAsStream("images/icons/logo.png"));
			primaryStage.getIcons().add(image);
			primaryStage.setTitle("Library Management System");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
	
}
