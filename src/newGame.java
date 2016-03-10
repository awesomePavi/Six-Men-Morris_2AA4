import java.util.Random;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * 
 * @author Ziyi Jin
 * @version A1
 */

public class newGame {
	private Board board;
	private Stage window;
	private Canvas canvas;
	private GraphicsContext gc;
	private Label message;
	// store the value if a disc has been put on the board
	private boolean disks[] = { false, false, false, false, false, false, false, false, false, false, false, false };
	// number of red and blue on the board
	private int numOfRed = 0;
	private int numOfBlue = 0;
	// next disc player to move,1 for red,2 for blue
	private int nextmove = 1;
	// check if a player has select a disc to move
	private boolean selected = false;
	// the number of disc a player has selected to move
	private int selecteddics;

	/**
	 * 
	 * @param primaryStage
	 *            - main display window of new game
	 */
	public newGame(Stage primaryStage) {

		this.canvas = new Canvas(500, 500);
		this.gc = canvas.getGraphicsContext2D();
		board = new Board(500, 500, 100, 50);
		// message to display
		message = new Label("Game continues");
		message.setLayoutX(180);
		message.setLayoutY(375);
		message.setMinSize(250, 100);
		message.setAlignment(Pos.CENTER_LEFT);
		message.setFont(Font.font(30));

		// update the scene
		this.update();

		Group root = new Group();
		root.getChildren().add(this.canvas);
		root.getChildren().add(board.getCanvas());
		root.getChildren().add(message);
		Scene scene = new Scene(root);

		// run the game
		playgame(scene);

		primaryStage.setScene(scene);
		primaryStage.show();
		window = primaryStage;
	}

	/**
	 * Take in the mouse input and place a disc on the board.
	 * 
	 * @param x
	 *            - the x coordinate value of the position user clicked
	 * @param y
	 *            - the y coordinate value of the position user clicked
	 */
	private void place(double x, double y) {
		// check if it is a valid input
		if (x > 95 && x < 405 && y > 45 && y < 355 && board.position(x, y) >= 0) {
			// if the next disc to be put is blue
			if (nextmove == 2) {
				if (Model.isPossiblePlace(Model.getCurBoard(), board.position(x, y))) {
					if (numOfBlue < 6) {
						Model.setValue(board.position(x, y), 2);
						nextmove = 1;
						disks[numOfBlue + 6] = true;
						numOfBlue++;
						message.setText("Game Continues");
					}
				} else {
					message.setText("Invalid move");

				}
			}
			// if the next disc to be put is red
			else if (nextmove == 1) {
				if (Model.isPossiblePlace(Model.getCurBoard(), board.position(x, y))) {
					if (numOfRed < 6) {
						Model.setValue(board.position(x, y), 1);
						nextmove = 2;
						disks[numOfRed] = true;
						numOfRed++;
						message.setText("Game Continues");
					}
				} else {// error
					message.setText("Invalid move");
				}
			}
		}

	}

	/**
	 * Highlight the disc on the board where the mouse is moved on.
	 * 
	 * @param x
	 *            - the x coordinate value of the position user clicked
	 * @param y
	 *            - the y coordinate value of the position user clicked
	 */
	private void highlight(double x, double y) {
		update();

		gc.setStroke(Color.AQUA);

		if (x > 95 && x < 405 && y > 45 && y < 355) {
			board.highlight(x, y, Color.AQUA);
		} else {
		}
	}

	/**
	 * update the board after a movement
	 */
	private void update() {
		board.update();

		gc.setFill(Color.GRAY);
		gc.fillRect(50, 100, 40, 200);
		gc.fillRect(410, 100, 40, 200);
		gc.setStroke(Color.BLACK);
		for (int i = 0; i < 6; i++) {
			gc.strokeOval(60, 115 + (i * 30), 20, 20);
			gc.strokeOval(420, 115 + (i * 30), 20, 20);
		}
		gc.setFill(Color.RED);
		for (int i = 0; i < 6; i++)
			if (!disks[i])
				gc.fillOval(60, 115 + (i * 30), 20, 20);
		gc.setFill(Color.BLUE);
		for (int i = 0; i < 6; i++)
			if (!disks[i + 6])
				gc.fillOval(420, 115 + (i * 30), 20, 20);

	}

	/**
	 * start a new six men's morris game
	 * 
	 * @param scene
	 *            - the scene of the game
	 */
	private void playgame(Scene scene) {
		// randomly select a color to start
		Random random = new Random();
		int nextturn = random.nextInt(2);
		if (nextturn == 0)
			nextmove = 1;// the 'red' disc player takes the next move
		else if (nextturn == 1)
			nextmove = 2;// the 'blue' disc player takes the next move

		// handle mouse input
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// put the disc on the board
				if ((numOfRed + numOfBlue) < 12)
					place(mouseEvent.getSceneX(), mouseEvent.getSceneY());
				// move the disc
				else {
					new Game(window,nextmove);
					/*
					if (selected) {
						move(mouseEvent.getSceneX(), mouseEvent.getSceneY());
					} else {
						double x = mouseEvent.getSceneX();
						double y = mouseEvent.getSceneY();
						if (x > 95 && x < 405 && y > 45 && y < 355 && board.position(x, y) >= 0) {
							if (Model.valueAt(board.position(x, y)) == nextmove && nextmove == 1) {
								selecteddics = board.position(x, y);
								selected = true;
							} else if (Model.valueAt(board.position(x, y)) == nextmove && nextmove == 2) {
								selecteddics = board.position(x, y);
								selected = true;
							} else {
							}
						}

					}*/

				}
			}
		});

		// handle mouse highlight
		scene.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				highlight(mouseEvent.getSceneX(), mouseEvent.getSceneY());

			}
		});

	}

	/**
	 * move the selected the disc to the new position
	 * 
	 * @param x
	 *            - the x coordinate value of the position user clicked
	 * @param y
	 *            - the y coordinate value of the position user clicked
	 */
	private void move(double x, double y) {
		if (x > 95 && x < 405 && y > 45 && y < 355 && board.position(x, y) >= 0) {
			// the 'red' disc player takes the next move
			if (Model.isPossiblePlace(Model.getCurBoard(), board.position(x, y)) && nextmove == 1) {
				Model.setValue(selecteddics, 0);
				Model.setValue(board.position(x, y), 1);
				selecteddics = -1;
				nextmove = 2;
				selected = false;
			}
			// the 'blue' disc player takes the next move
			else if (Model.isPossiblePlace(Model.getCurBoard(), board.position(x, y)) && nextmove == 2) {
				Model.setValue(selecteddics, 0);
				Model.setValue(board.position(x, y), 2);
				nextmove = 1;
				selecteddics = -1;
				selected = false;
			} else {
				message.setText("Invalid move");
			}
		}

	}

}
