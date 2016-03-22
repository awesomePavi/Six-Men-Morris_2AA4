import java.util.Random;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This class randomly selects a first player, and than allows the users to
 * palce their disks down after each other, if a player gets a mill they ahve
 * the ability to remove another player's peice once all 6 pieces are aplced the
 * game will be passed to the game class and execution will continue their
 * 
 * 
 * @author Ziyi Jin
 * @version A2
 */

public class newGame {
	private Board board;
	private Stage window;
	private Canvas canvas;
	private GraphicsContext gc;
	// message display
	private Label upperMessage;
	private Label lowerMessage;
	// store the value if a disc has been put on the board
	private boolean disks[] = { false, false, false, false, false, false, false, false, false, false, false, false };
	// number of red and blue on the board
	private int numOfRed = 0;
	private int numOfBlue = 0;
	// next disc player to move,1 for red,2 for blue
	private int nextmove = 1;
	// can a player eat other's dice
	private boolean makeEat = false;
	// the index of dice be chosen to be ate
	private int chooseToEat;

	/**
	 * 
	 * @param primaryStage
	 *            - main display window of new game
	 */
	public newGame(Stage primaryStage) {

		this.canvas = new Canvas(500, 400);
		this.gc = canvas.getGraphicsContext2D();
		board = new Board(500, 400, 100, 50);

		// message to display
		upperMessage = new Label("");
		upperMessage.setLayoutX(125);
		upperMessage.setLayoutY(350);
		upperMessage.setMinSize(250, 50);
		upperMessage.setAlignment(Pos.CENTER);
		upperMessage.setFont(Font.font(30));

		lowerMessage = new Label("");
		lowerMessage.setLayoutX(125);
		lowerMessage.setLayoutY(0);
		lowerMessage.setMinSize(250, 50);
		lowerMessage.setAlignment(Pos.CENTER);
		lowerMessage.setFont(Font.font(30));

		dispMessage("Game In Progress");

		// update the scene
		this.update();

		Group root = new Group();
		root.getChildren().add(this.canvas);
		root.getChildren().add(board.getCanvas());
		root.getChildren().add(upperMessage);
		root.getChildren().add(lowerMessage);
		Scene scene = new Scene(root);
		// background Colour
		scene.setFill(new Color(0.4, 0.4, 0.4, 1.0));

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
	private boolean place(double x, double y) {
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
						return true;
					}
				} else {
					dispError("Invalid Move");
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
						return true;
					}
				} else {// error
					dispError("Invalid Move");
				}
			}
		}
		return false;

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

		gc.setFill(board.getBoardClr());
		gc.fillRect(50, 100, 40, 200);
		gc.fillRect(410, 100, 40, 200);
		gc.setStroke(Color.BLACK);
		gc.strokeRect(50, 100, 40, 200);
		gc.strokeRect(410, 100, 40, 200);
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
				dispMessage("Game In Progress");
				// put the disc on the board
				int currentmove = nextmove;
				// cannot eat
				if (!makeEat) {
					int position = board.position(mouseEvent.getSceneX(), mouseEvent.getSceneY());
					boolean hasPlaced = place(mouseEvent.getSceneX(), mouseEvent.getSceneY());
					mouseEvent.consume();
					if (hasPlaced) {
						update();
						if (Model.canEat(currentmove, position)) {
							makeEat = true;
							dispMessage("Remove One Piece");
						}
					}

				}
				// can eat
				else {
					chooseToEat = select(mouseEvent.getSceneX(), mouseEvent.getSceneY());
					mouseEvent.consume();

					// choose a valid dice to eat
					if (chooseToEat != -1) {
						int toEat = Model.valueAt(chooseToEat);
						// choose the right dice to eat
						if (toEat == nextmove) {
							// the chosen opponent's dice can be ate
							if (Model.canBeAte(chooseToEat, nextmove)) {
								eat(chooseToEat);
								update();
								makeEat = false;

							}
							// the chosen opponent's dice cannot be ate
							else {
								dispError("Cannot Eat from Mill");

							}
						}
					}
					// choose an invalid dice to eat
					else {
						dispError("Cannot Eat Self");
					}
				}
				// check if all the 12 dices has be put on the board
				if ((numOfRed + numOfBlue) == 12 && !makeEat) {
					// move on to the next stage
					new Game(window, nextmove);
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
	 * This function takes the number of dice to be eat and perform the eat
	 * operation
	 * 
	 * @param select
	 *            the index of the dice to eat
	 */
	private void eat(int select) {
		Model.setValue(select, 0);
		dispError("");
	}

	/**
	 * This function takes i nthe mosue location and returns the disk the user
	 * has selcted if it is their own disk
	 * 
	 * @param x
	 *            the mouse x position relative to the canvas
	 * @param y
	 *            the mouse y position relative to the canvas
	 * @return the disk the user has selected if the chosen disk was their's
	 */
	private int select(double x, double y) {
		// selected disk
		int toSelect = board.position(x, y);
		// if the disk belongs to the user and the mouse is on a position than
		// return
		if (toSelect >= 0 && Model.valueAt(toSelect) == nextmove)
			return toSelect;
		// if the user selected a disk that is not their's or no disk at all
		dispError("Invalid piece");
		return -1;
	}

	/**
	 * used to display error's above the board, erases previous message on new
	 * message
	 * 
	 * @param text
	 *            the error to be displayed
	 */
	private void dispError(String text) {
		upperMessage.setText(text);
		lowerMessage.setText(text);
		upperMessage.setTextFill(Color.RED);
		lowerMessage.setTextFill(Color.RED);
	}

	/**
	 * used to display message above the board, erases previous message.
	 * 
	 * @param text
	 *            the message to be displayed
	 */
	private void dispMessage(String text) {
		upperMessage.setText(text);
		lowerMessage.setText(text);
		upperMessage.setTextFill(Color.DARKTURQUOISE);
		lowerMessage.setTextFill(Color.DARKTURQUOISE);
	}

}
