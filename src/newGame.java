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
 * @version A2
 */

public class newGame {
	private Board board;
	private Stage window;
	private Canvas canvas;
	private GraphicsContext gc;
	private Label message;
	private Label errorMessage;
	// store the value if a disc has been put on the board
	private boolean disks[] = { false, false, false, false, false, false, false, false, false, false, false, false };
	// number of red and blue on the board
	private int numOfRed = 0;
	private int numOfBlue = 0;
	// next disc player to move,1 for red,2 for blue
	private int nextmove = 1;
	//can a player eat other's dice
	private boolean makeEat=false;
	//the index of dice be chosen to be ate
	private int chooseToEat;
	

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
		message = new Label("Game in progress");
		message.setLayoutX(140);
		message.setLayoutY(360);
		message.setMinSize(250, 100);
		message.setAlignment(Pos.CENTER_LEFT);
		message.setFont(Font.font(30));
		
		
		errorMessage=new Label("");
		errorMessage.setLayoutX(200);
		errorMessage.setLayoutY(20);
		errorMessage.setAlignment(Pos.CENTER_LEFT);
		errorMessage.setFont(Font.font(18));
		errorMessage.setTextFill(Color.RED);

		// update the scene
		this.update();

		Group root = new Group();
		root.getChildren().add(this.canvas);
		root.getChildren().add(board.getCanvas());
		root.getChildren().add(message);
		root.getChildren().add(errorMessage);
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
						message.setText("Game in progress");
						return true;
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
						message.setText("Game in progress");
						return true;
					}
				} else {// error
					message.setText("Invalid move");
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
				int currentmove=nextmove;
				//cannot eat
				if(!makeEat){
					int position=board.position(mouseEvent.getSceneX(),mouseEvent.getSceneY());
					boolean hasPlaced=place(mouseEvent.getSceneX(), mouseEvent.getSceneY());
					mouseEvent.consume();
					if(hasPlaced){
					    update();
					    if (Model.canEat(currentmove, position)) {
							makeEat = true;
						}
					}
					
				}
				//can eat
				else{
					chooseToEat = select(mouseEvent.getSceneX(),mouseEvent.getSceneY());
					mouseEvent.consume();
					
					//choose a valid dice to eat
					if(chooseToEat!=-1){
						int toEat=Model.valueAt(chooseToEat);
						//choose the right dice to eat
						if(toEat==nextmove){
							//the chosen opponent's dice can be ate 
							if(Model.canBeAte(chooseToEat,nextmove)){
								eat(chooseToEat);
								update();
								makeEat = false;
								
							}
							//the chosen opponent's dice cannot be ate 
							else{
								dispError("Invalid Move");
								
							}
						}
					}
					//choose an invalid dice to eat
					else{
						dispError("Invalid Move");			
                    }
				}
				//check if all the 12 dices has be put on the board
				if ((numOfRed + numOfBlue) == 12 && !makeEat){
					//move on to the next stage
				    new Game(window,nextmove);
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
	 * This function takes the number of dice to be eat and perform the eat operation
	 * 
	 * @param select
	 * 				the index of the dice to eat 
	 */
	public void eat(int select){
		Model.setValue(select, 0);
		dispError("");
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
		return -1;
	}
	
	/**
	 * This function manipulate the content of Label errorMessage and display the error message 
	 * 
	 * @param text
	 * 				the message to be displayed
	 */
	private void dispError(String text) {
		errorMessage.setText(text);
	}
	

}
