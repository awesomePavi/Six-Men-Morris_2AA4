import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This is the end-game menu, which is lead to by either a win or a draw
 * 
 * @author Pavithran Pathmarajah
 * @version A2
 */
public class GameOver {
	/**
	 * The constructor takes in the run window and an integer which will
	 * determine if it was a win or a draw, the screen will have a simple
	 * statement and a a main menu button
	 * 
	 * @param window
	 *            - the run window for IO
	 * @param win
	 *            - 1 is red won; 2 is blue won; all else is a draw
	 */
	public GameOver(Stage window, int win) {
		Group root = new Group();
		Scene scene = new Scene(root);
		
		// Message To user
		Label userMessage = new Label("");
		// the minsize forces the window to be 400 x 200
		userMessage.setLayoutX(0);
		userMessage.setLayoutY(0);
		userMessage.setMinSize(400, 200);
		userMessage.setTextFill(Color.WHITE);
		userMessage.setAlignment(Pos.CENTER);
		userMessage.setFont(Font.font(30));

		// main-menu button
		Button mainMenuButton = new Button("Main Menu");
		// place below displayed text
		mainMenuButton.setLayoutX(100);
		mainMenuButton.setLayoutY(125);
		mainMenuButton.setMinHeight(50);
		mainMenuButton.setMinWidth(200);
		mainMenuButton.setFocusTraversable(false);

		// handle button press
		mainMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				// reset the game so a fresh one may be played
				Model.reset();
				// go back to main menu
				new Menu().start(window);
			}
		});

		// if blue player wins
		if (win == 2) {
			userMessage.setText("BLUE IS THE WINNER");
			// background colour
			scene.setFill(Color.BLUE);
		}
		// if red layer wins
		else if (win == 1) {
			userMessage.setText("RED IS THE WINNER");
			scene.setFill(Color.RED);
			// if it is a draw
		} else {
			userMessage.setText("WINNER IS A DRAW");
			// to handle the window graphics
			Canvas canvas = new Canvas(400, 200);
			GraphicsContext gc = canvas.getGraphicsContext2D();
			// draw a red and blue checkered pattern as the background
			boolean curColourBlue = true;
			// across the whole screen make 40x40 boxes
			for (int x = 0; x < 400; x += 40) {
				for (int y = 0; y < 200; y += 40) {
					// colour inversion every loop to get desired effect
					if (curColourBlue) {
						curColourBlue = false;
						gc.setFill(Color.RED);
					} else {
						curColourBlue = true;
						gc.setFill(Color.BLUE);
					}
					// draw cube
					gc.fillRect(x, y, 40, 40);
				}
			}
			// add grpahical background
			root.getChildren().add(canvas);
		}
		
		// add message and than button, if not than the button is unclicable,
		// since the text covers the entire area
		root.getChildren().add(userMessage);
		root.getChildren().add(mainMenuButton);
		window.setScene(scene);
		window.show();
	}

}
