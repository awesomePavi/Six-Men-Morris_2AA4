import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This is the main menu, and the encapsulation of the entire application
 * 
 * @author Ziyi Jin. Pavithran Pathmarajah
 * @version A1
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

		// make a vertical box layout
		VBox layout1 = new VBox(30);
		layout1.setAlignment(Pos.CENTER);
		// add current buttons into the layout
		layout1.getChildren().addAll(newGameButton, existingGameButton,loadGameButton, exit);

		// create new scene using current layout and display it to the window
		Scene menu = new Scene(layout1, 500, 500);
		window.setScene(menu);
		window.show();
	}

}
