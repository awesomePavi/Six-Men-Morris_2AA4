import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * 
 */

/**
 * This class currently allows for simply moving pieces around no game dynamics
 * 
 * @author Pavi
 */
public class Game {
	// graphic interface
	private Board board;
	private Scene scene;
	private Canvas canvas;
	private GraphicsContext gc;
	// current players move
	private int nextmove = 1;
	// the current selected piece
	private int selected = 0;

	/**
	 * The game constructor takes in the application window and the first player
	 * then ad's mouse events for user input
	 * 
	 * @param primaryStage
	 *            the application window
	 * @param player
	 *            the first player
	 */
	public Game(Stage primaryStage, int player) {
		// current players move
		nextmove = player;

		//to handle the iwndow graphics
		this.canvas = new Canvas(350, 400);
		this.gc = canvas.getGraphicsContext2D();
		board = new Board(350, 400, 25, 75);
		
		//update and create the window graphics
		update();
		
		//add graphics objects to window
		Group root = new Group();
		root.getChildren().add(board.getCanvas());
		root.getChildren().add(this.canvas);
		
		//get scene for UI to be added
		scene = new Scene(root);
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// if something has already been selected then place it
				if (selected != 0)
					place(mouseEvent.getSceneX(), mouseEvent.getSceneY());
				// if nothing has been selected then select an object
				else
					selected = select(mouseEvent.getSceneX(), mouseEvent.getSceneY());
				//update the selected highlight
				update();
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

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * This function takes in the x and y locations and then moves the selected disk accordingly
	 * 
	 * @param x
	 *            the mouse x position relative to the canvas
	 * @param y
	 *            the mouse y position relative to the canvas
	 */
	private void place(double x, double y) {
		//the positon the user pressed on
		int toPlace = board.position(x, y);
		//if their is no piece at the choosen position and the mouse is on a position
		if (toPlace >=1 && Model.valueAt(toPlace) == 0) {
			//place the player's piece and remove it from the last position
			Model.setValue(toPlace, nextmove);
			Model.setValue(selected, 0);
			//change the current player's move state
			if (nextmove == 1)
				nextmove = 2;
			else
				nextmove = 1;
			//no disk is selected anymore
			selected = 0;
			//do not show error message
			return;
		}
		dispError("Invalid Move");
	}

	/**
	 * This function takes i nthe mosue location and returns the disk the user has selcted if it is their own disk
	 * 
		 * @param x
	 *            the mouse x position relative to the canvas
	 * @param y
	 *            the mouse y position relative to the canvas
	 * @return the disk the user has selected if the chosen disk was their's
	 */
	private int select(double x, double y) {
		//selected disk
		int toSelect = board.position(x, y);
		//if the disk belongs to the user and the mouse is on a position than return
		if (toSelect >=0 && Model.valueAt(toSelect) == nextmove)
			return toSelect;
		//if the user selected a disk that is not their's or no disk at all
		dispError("Invalid piece");
		return 0;
	}

	/**
	 * highlights the current position the suer is mousing over
	 * 
		 * @param x
	 *            the mouse x position relative to the canvas
	 * @param y
	 *            the mouse y position relative to the canvas
	 */
	private void highlight(double x, double y) {
		update();
		board.highlight(x, y, Color.AQUA);
	}

	/**
	 * used to display error's above the board, erases previous message on new message
	 * 
	 * @param text the error to be displayed
	 */
	private void dispError(String text) {
		// erase any previous error
		gc.setFill(Color.WHITE);
		gc.fillRect(25, 30, 300, 20);
		// diplay new error
		gc.setFill(Color.RED);
		gc.fillText(text, 25, 50, 300);
	}

	/**
	 * updates the current board and highlights the selected disk
	 */
	private void update() {
		board.update();
		//if a  position on the board is selcted highlight it
		if (selected != 0)
			board.strokePos(selected, Color.WHITE);
	}

}
