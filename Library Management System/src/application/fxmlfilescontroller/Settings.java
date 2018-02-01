package application.fxmlfilescontroller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Settings implements Initializable{
	@FXML private ColorPicker themeColor;
	@FXML private Button apply;
	@FXML private Button close;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void applyColor() {
		Color color=themeColor.getValue();
		String hex="#"+Integer.toHexString(color.hashCode());
		System.out.println(hex);
	}
	
	@FXML
	public void closeWindow() {
		Stage stage=(Stage)close.getScene().getWindow();
		stage.close();
	}
}
