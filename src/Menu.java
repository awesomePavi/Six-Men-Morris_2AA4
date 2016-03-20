import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is the main menu, and the encapsulation of the entire application
 * 
 * @author Ziyi Jin. Pavithran Pathmarajah
 * @version A2
 */
public class Menu extends Application {

	/**
	 * main function to launch application
	 * 
	 * @param args
	 *            no purpose
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	/**
	 * The sets up application on start
	 */
	public void start(Stage window) {
		window.setTitle("Six Men's Morris");
		Text title = new Text("Six Men's Morris");
		title.setFont(Font.font("Verdana",40));
		title.setX(10.0f);
		title.setY(10.0f);
		Text creator = new Text("Emily Ashworth - Ziyi Jin - Pavi Pathmarajah");
		creator.setFont(Font.font("Verdana",15));

		// button for new game
		Button newGameButton = new Button("Play New Game");
		// on button press pass the window on to the newGame class
		newGameButton.setOnAction(e -> {
			new newGame(window);
		});

		// button for continuing a game
		Button existingGameButton = new Button("Play Existing Game");
		// on button press pass the window on to the new existingGame class
		existingGameButton.setOnAction(e -> {
			new existingGame(window);
		});

		// button for continuing a game
		Button loadGameButton = new Button("Load Existing Game");
		// on button press pass the window on to the new existingGame class
		loadGameButton.setOnAction(e -> {
			new LoadSaveGame(window);
		});

		// button for exiting the menu
		Button exit = new Button("Exit");
		// on button press exit the applicaion
		exit.setOnAction(e -> {
			Platform.exit();
		});
		
		newGameButton.setMinWidth(200);
		existingGameButton.setMinWidth(200);
		loadGameButton.setMinWidth(200);
		exit.setMinWidth(200);

		// make a vertical box layout
		VBox layout1 = new VBox(30);
		layout1.setAlignment(Pos.CENTER);
		// add current buttons into the layout
		layout1.getChildren().addAll(title, creator,newGameButton, existingGameButton,loadGameButton, exit);

		// create new scene using current layout and display it to the window
		Scene menu = new Scene(layout1, 500, 500);
		
		//black background
		layout1.setBackground(new Background(new BackgroundFill(new Color(0.4,0.4,0.4, 1.0), CornerRadii.EMPTY, Insets.EMPTY)));
		
		window.setScene(menu);
		window.show();
	}
	
	private void update() {
		
	}
	
	

}
