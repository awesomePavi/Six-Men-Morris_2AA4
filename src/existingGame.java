import java.util.Random;

import javafx.event.ActionEvent;
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
 * This class allows the user to place down up to 6 red and 6 blue disks to
 * recreate a game they were previously playing, the program will then check if
 * the layout is viable and if so will continue to play the game
 * 
 * @author Pavithran Pathmarajah
 * @version A1
 */
public class existingGame {
	// the board to be update the current game state
	private Board board;
	// the canvas that will be manipulated
	private Canvas canvas;
	// to allow the canvas to be manipulated
	private GraphicsContext gc;
	//error messages
	private Label errorMsg;
	// button to check and begin game
	private Button ready;
	// the 12 disks that may be placed, false means it has not been placed yet.
	// first 6 are red alst 6 are blue
	private boolean disks[] = { false, false, false, false, false, false, false, false, false, false, false, false };
	// if the user has selected anything to be moved
	private int selected = 0;

	/**
	 * customize window to the needs of the gamebuilder
	 * 
	 * @param the
	 *            window to modify
	 */
	public existingGame(Stage window) {

		this.canvas = new Canvas(500, 500);
		this.gc = canvas.getGraphicsContext2D();
		// create and place board in center
		board = new Board(500, 500, 100, 50);
		// button to check and begin game
		ready = new Button();
		ready.setText("Begin Game");
		ready.setLayoutX(125);
		ready.setLayoutY(375);
		ready.setMinSize(250, 100);
		ready.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				checkAndPLay(window);
			}
		});
		
		errorMsg = new Label("");
		errorMsg.setLayoutX(0);
		errorMsg.setLayoutY(0);
		errorMsg.setMinSize(500, 50);
		errorMsg.setAlignment(Pos.CENTER);
		errorMsg.setFont(Font.font(20));
		errorMsg.setTextFill(Color.RED);

		// draw the window graphics
		this.update();

		// create a group and add the layers of GUI objects onto it
		Group root = new Group();
		root.getChildren().add(this.canvas);
		root.getChildren().add(board.getCanvas());
		root.getChildren().add(ready);
		root.getChildren().add(errorMsg);

		// add GUI to the scene
		Scene scene = new Scene(root);
		/*
		 * add a listner for a mouse being pressed for selection purposes
		 */
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				errorMsg.setText("");
				// if something has already been selected then place it
				if (selected != 0)
					place(mouseEvent.getSceneX(), mouseEvent.getSceneY());
				// if nothing has been selected then select an object
				else
					selected = select(mouseEvent.getSceneX(), mouseEvent.getSceneY());
			}
		});
		/*
		 * add a listner for mouse movement, to highlight anything the mouse is
		 * over, so the user knows that it may be selected
		 */
		scene.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				highlight(mouseEvent.getSceneX(), mouseEvent.getSceneY());

			}
		});
		//background Colour
		scene.setFill(new Color(0.4,0.4,0.4, 1.0));

		// push the scene to the window and display it
		window.setScene(scene);
		window.show();
	}

	/**
	 * if an item is selected and the mouse is pressed again this function will
	 * take the next steps in palcing the selected object
	 * 
	 * @param x
	 *            the mouse x position relative to the canvas
	 * @param y
	 *            the mouse y position relative to the canvas
	 */
	private void place(double x, double y) {
		/*
		 * if the selected disk is not on the board and the mouse is over the
		 * selected spot is a position on the board
		 */
		if (selected < 15 && x > 95 && x < 405 && y > 45 && y < 355 && board.position(x, y) >= 0) {
			// if the user is trying to palce a piece ontop of another
			if (Model.hasValue(board.position(x, y))) {
				gc.setStroke(Color.RED);
				dispError("Error: Cannot Place Over Existing Piece");
				return;
				// the user places a blue piece
			} else if (selected > 6)
				Model.setValue(board.position(x, y), 2);
			// the user palces a red piece
			else
				Model.setValue(board.position(x, y), 1);
			// that piece has been placed
			disks[selected - 1] = true;
			selected = 0;

			/*
			 * if the selected disk is on the board and the selected spot is a *
			 * blue disk (in the holder) and the value on the position on the *
			 * board contains a blue disk
			 */
		} else if (selected > 14 && returnDisk(x, y) > 0 && returnDisk(x, y) < 15 && Model.valueAt(selected - 15) == 2
				&& returnDisk(x, y) > 6) {
			// Return the disk remove it from the board and set it to unused
			disks[returnDisk(x, y) - 1] = false;
			Model.setValue(selected - 15, 0);
			selected = 0;

			/*
			 * if the selected disk is on the board and the selected spot is a
			 * red disk (in the holder) and the value on the position on the
			 * board contains a red disk
			 */
		} else if (selected > 14 && returnDisk(x, y) > 0 && returnDisk(x, y) < 15 && Model.valueAt(selected - 15) == 1
				&& returnDisk(x, y) < 7) {
			// Return the disk remove it from the board and set it to unused
			disks[returnDisk(x, y) - 1] = false;
			Model.setValue(selected - 15, 0);
			selected = 0;

			/*
			 * * if the user is selecting another piece in the holder then swap
			 * to that piece, or if the user double clicks on the same piece
			 * again de-select that piece
			 */
		} else {
			// de-select piece
			if (select(x, y) == selected)
				selected = 0;
			// another piece in a holder
			else if (select(x, y) > 0)
				selected = select(x, y);
		}
		// update the graphics
		update();
	}

	/**
	 * This function takes in coordinates and returns weather a disk may be
	 * returned there
	 * 
	 * @param x
	 *            the mouse x position relative to the canvas
	 * @param y
	 *            the mouse y position relative to the canvas
	 * @return whether a disk may be returned here
	 */
	private int returnDisk(double x, double y) {
		// if the mouse is over the red disk holder
		if (x > 60 && x < 80) {
			/*
			 * cycle through all disks that have been placed and may be
			 * returned, until the disk that the user is on is found
			 */
			for (int i = 0; i < 6; i++)
				if (disks[i] && y > 115 + (i * 30) && y < 135 + (i * 30)) {
					return i + 1;
				}
			// if the mouse is over the blue disk holder
		} else if (x > 420 && x < 440) {
			/*
			 * cycle through all disks that have been placed and may be
			 * returned, until the disk that the user is on is found
			 */
			for (int i = 0; i < 6; i++)
				if (disks[i + 6] && y > 115 + (i * 30) && y < 135 + (i * 30)) {
					return i + 7;
				}
		}
		// a disk may not be returned to the current position
		return 0;
	}

	/**
	 * This function takes in coordinates and returns what has been selected if
	 * anything
	 * 
	 * @param x
	 *            the mouse x position relative to the canvas
	 * @param y
	 *            the mouse y position relative to the canvas
	 * @return what has been selected
	 */
	private int select(double x, double y) {
		// if the mouse is over the red disk holder
		if (x > 60 && x < 80) {
			/*
			 * cycle through all disks that have not been placed yet, until the
			 * disk that the user is on is found
			 */
			for (int i = 0; i < 6; i++)
				if (!disks[i] && y > 115 + (i * 30) && y < 135 + (i * 30)) {
					return i + 1;
				}
			// if the mouse is over the blue disk holder
		} else if (x > 420 && x < 440) {
			/*
			 * cycle through all disks that have not been placed yet, until the
			 * disk that the user is on is found
			 */
			for (int i = 0; i < 6; i++)
				if (!disks[i + 6] && y > 115 + (i * 30) && y < 135 + (i * 30)) {
					return i + 7;
				}
			/*
			 * if the mouse is over the board, and over a position with a piece
			 * on it
			 */
		} else if (x > 95 && x < 405 && y > 45 && y < 355 && board.position(x, y) >= 0
				&& Model.hasValue(board.position(x, y))) {
			// select that piece
			return board.position(x, y) + 15;
		}
		return 0;
	}

	/**
	 * This function takes in x and y coordinates and then highlights any object
	 * it is over
	 * 
	 * @param x
	 *            the mouse x position relative to the canvas
	 * @param y
	 *            the mouse y position relative to the canvas
	 */
	private void highlight(double x, double y) {
		// if the mouse is in an area where highlighting is available
		if (y < 400) {
			// update the graphics layers below then add the highlight layer
			update();
			// highlight colout
			gc.setStroke(Color.AQUA);
			// if red disk holder
			if (x > 60 && x < 80) {
				for (int i = 0; i < 6; i++)
					if (y > 115 + (i * 30) && y < 135 + (i * 30)) {
						gc.strokeOval(59, 114 + (i * 30), 22, 22);
						break;
					}
				// if blue disk holder
			} else if (x > 420 && x < 440) {
				for (int i = 0; i < 6; i++)
					if (y > 115 + (i * 30) && y < 135 + (i * 30)) {
						gc.strokeOval(419, 114 + (i * 30), 22, 22);
						break;
					}
				// if over board
			} else if (x > 95 && x < 405 && y > 45 && y < 355) {
				board.highlight(x, y, Color.AQUA);
			}
		}
	}

	/**
	 * draws the board and holders
	 */
	private void update() {
		board.update();
		// the red and blue disk holders
		gc.setFill(board.getBoardClr());
		gc.fillRect(50, 100, 40, 200);
		gc.fillRect(410, 100, 40, 200);
		gc.setStroke(Color.BLACK);
		gc.strokeRect(50, 100, 40, 200);
		gc.strokeRect(410, 100, 40, 200);
		// outline of the disks
		for (int i = 0; i < 6; i++) {
			gc.strokeOval(60, 115 + (i * 30), 20, 20);
			gc.strokeOval(420, 115 + (i * 30), 20, 20);
		}
		// red disks in holder if it has not been placed
		gc.setFill(Color.RED);
		for (int i = 0; i < 6; i++)
			if (!disks[i])
				gc.fillOval(60, 115 + (i * 30), 20, 20);
		// blue disks in holder if it has not been placed
		gc.setFill(Color.BLUE);
		for (int i = 0; i < 6; i++)
			if (!disks[i + 6])
				gc.fillOval(420, 115 + (i * 30), 20, 20);
		// the selected disk to be outlined in white
		gc.setStroke(Color.WHITE);
		if (selected != 0) {
			// if on the board
			if (selected > 14)
				board.strokePos(selected - 15, Color.WHITE);
			// if in blue holder
			else if (selected > 6)
				gc.strokeOval(419, 114 + ((selected - 7) * 30), 22, 22);
			else
				gc.strokeOval(59, 114 + ((selected - 1) * 30), 22, 22);
		}

	}

	/**
	 * This function takes in a stirng and displays it as a red error message on
	 * top of the board
	 * 
	 * @param text
	 */
	private void dispError(String text) {
		// erase any previous error
		errorMsg.setText(text);
	}

	/**
	 * This function is called before the game is played, and it checks
	 * gameboard to be valid and if so it will begin the game play
	 * 
	 * @param window
	 *            the window to be passed on to the game
	 */
	private void checkAndPLay(Stage window) {
		// if the gameboard is valid, defualt starting player is random
		if (Model.isPossibleBoard())
			new Game(window,new Random().nextInt(2)+1);
		// if not display the error and highlight the problem
		else {
			switch (Model.getError()) {
			case 1:

				// highlight the minimum number of disks that must be placed to
				// meet requirment
				dispError("Error: Each player must have at least 3 pieces on the board.");
				// keeps track of how many red and blue disks are on the board
				int countB = 0, countR = 0;
				// counts the disks that ahve been placed already
				for (int i = 0; i < 6; i++) {
					if (disks[i])
						countR++;
					if (disks[i + 5])
						countB++;
				}
				// highlights the number of available disks that muct be palced
				// to meet the minimum three requirment
				gc.setStroke(Color.LIGHTGREEN);
				for (int i = 0; i < 6; i++) {
					if (!disks[i] && countR < 3) {
						countR++;
						gc.strokeOval(59, 114 + (i * 30), 22, 22);
					}
					if (!disks[i + 6] && countB < 3) {
						countB++;
						gc.strokeOval(419, 114 + (i * 30), 22, 22);
					}
				}

				break;
			case 3:
				// highlights all red disks on board
				dispError("Error: Red player is blocked.");
				for (int i = 0; i < 16; i++) {
					if (Model.valueAt(i) == 1)
						board.strokePos(i, Color.LIGHTGREEN);
				}
				break;
			case 4:
				// highlights all blue disks on board
				dispError("Error: Blue player is blocked.");
				for (int i = 0; i < 16; i++) {
					if (Model.valueAt(i) == 2)
						board.strokePos(i, Color.LIGHTGREEN);
				}
				break;

			}
		}

	}
}
