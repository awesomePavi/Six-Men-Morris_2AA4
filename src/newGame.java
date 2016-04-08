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
import java.util.Iterator;

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
	//determine if AI is playing
	boolean AI_=false;
	//ranodm nums
	Random rand;

	/**
	 * 
	 * @param primaryStage
	 *            - main display window of new game
	 */
	public newGame(Stage primaryStage,boolean AI) {
		
		AI_=AI;
		if (AI_)
			Model.initalize();
		
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
						disks[numOfBlue + 6] = true;
						numOfBlue++;
						nextmove = 1;
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
						disks[numOfRed] = true;
						numOfRed++;
						nextmove =  2;
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
		rand= new Random();
		nextmove = 2;//rand.nextInt(2)+1;
		//if player 2's turn and battling an AI
		if (nextmove==2 && AI_){
			//the AI amkes i't's move and returns it back to the user
			AIPlace();
			nextmove = 1;
		}
			

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
							if (AI_)
								nextmove=2;
							
							makeEat = true;
							dispMessage("Remove One Piece");
						}
						//if it's the ai's turn and the other player is not eating anything
						if (AI_ && nextmove==2 && !makeEat)
							nextmove=AITurn();
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
								if (AI_ && nextmove==2)
									nextmove=AITurn();

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
					new Game(window, nextmove,AI_);
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
	public void eat(int select) {
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
	
	/**
	 * This method is implemented to allow the AI to be integrated with the current system, by the AI making ti's move and returning it back to the player
	 * 
	 * @param curturn - the current player's turn
	 * @return - the next players turn
	 */
	private int AITurn(){
		if (numOfBlue < 6) {
		//if it's the Ai's turn
			AIPlace();
		}
			return 1;
	}
	
	/**
	 * Determins where the ai palces it's next peice
	 */
	private void AIPlace(){
		//current board info and random to help with placemnent
		int toSelect = rand.nextInt(16);
		int[] board = Model.getCurBoard();
		//if AI's first turn
		if (numOfBlue==0){
			System.out.println("AI Makes First Move");
			//place randomly along a spot which can make up too two mills
			while (board[toSelect] != 0 || Model.getAdj(toSelect).length!=2){
				toSelect = rand.nextInt(16);
			}
			Model.setValue(toSelect, 2);
			disks[numOfBlue + 6] = true;
			numOfBlue++;
			return;
		//if the game has already begun
		}else{
			//Ai tries to make path of 3
			for (int i=0;i<16;i++){
				//check all 16 spots on the board to see if, a mill can be made by the AI
				if (board[i]==2 && (Model.getPath(i)!=null)){
					//checks all mills possible on that path
					 Iterator<Integer> possibleSpots = Model.getPath(i).keySet().iterator();
					while(possibleSpots.hasNext()){
						//if a second spot along that path has a ai piece then
						int tmp =possibleSpots.next();
						if (board[tmp]==2){
							//if the final spot does not have anythign on it then complete the mill
							if (board[Model.getPath(i).get(tmp)] == 0){
								System.out.println("AI removes a Piece");
								Model.setValue(Model.getPath(i).get(tmp), 2);
								disks[numOfBlue + 6] = true;
								numOfBlue++;
								if (Model.canEat(2, Model.getPath(i).get(tmp)))
									AI_Eat();
								return;
							}
						}
					}
				}
			}
			
			//Stop user from cmpleting path of 3
			for (int i=0;i<16;i++){
				//check through all 16 spots o nthe board to see if the player ahs any pieces
				if (board[i]==1 && (Model.getPath(i)!=null)){
					//if the player, has a peice check if they have any other peices along a mill
					 Iterator<Integer> possibleSpots = Model.getPath(i).keySet().iterator();
					while(possibleSpots.hasNext()){
						//if the user has two of three peices o na mill then
						int tmp =possibleSpots.next();
						if (board[tmp]==1){
							//if the last spot on the mill is empty then palce a piece their to stop the player
							if (board[Model.getPath(i).get(tmp)] == 0){
								System.out.println("AI Blocks User");
								Model.setValue(Model.getPath(i).get(tmp), 2);
								disks[numOfBlue + 6] = true;
								numOfBlue++;
								return;
							}
						}
					}
				}
			}
			

			//Ai tries to make path of 2
			for (int i=0;i<16;i++){
				//if an ai piece is found then place ajaceent too it if the mill on that apth is still availble 
				if (board[i]==2 && (Model.getPath(i)!=null)){
					 Iterator<Integer> possibleSpots = Model.getPath(i).keySet().iterator();
					while(possibleSpots.hasNext()){
						int tmp =possibleSpots.next();
						if (board[tmp]==0){
							//if the mill is still availble place adajcent to the other ai piece
							if (board[Model.getPath(i).get(tmp)] == 0){
								System.out.println("AI builds Path");
								Model.setValue(tmp, 2);
								disks[numOfBlue + 6] = true;
								numOfBlue++;
								return;
							}
						}
					}
				}
			}
			

			//Ai tries to start path
			boolean hasSpots =false;
			//check if any spots area avialbe along two seperate mills
			for (int i=0;i<16;i++){
				if(board[i] != 0 && Model.getAdj(i).length!=2){
					hasSpots=true;
				}
			}
			//if spots are availbe along two sided mills then rnadomly palce at one of these spots 
			if (hasSpots){
				while (board[toSelect] != 0 || Model.getAdj(toSelect).length!=2){
					toSelect = rand.nextInt(16);
				}
			//if no spots are availble with two mills palce randomly
			}else{
				while (board[toSelect] != 0){
					toSelect = rand.nextInt(16);
				}
			}
			
			System.out.println("AI Makes a Move");
			Model.setValue(toSelect, 2);
			disks[numOfBlue + 6] = true;
			numOfBlue++;
			return;
	
		}
	}
	
	/**
	 * Called when the AI makes a move and completes a mill
	 */
	private void AI_Eat(){
		int[] board = Model.getCurBoard();
		//try to eat a red peice which is on a mill of size 2
		for (int i=0;i<16;i++){
			//cehck all 16 spots for a red peice  if found check if any spots o nthat apth ahve a red piece
			if (board[i]==1 && (Model.getPath(i)!=null)){
				 Iterator<Integer> possibleSpots = Model.getPath(i).keySet().iterator();
				while(possibleSpots.hasNext()){
					//if so then remvoe that second piece so the paleyr amy not complete a mill
					int tmp =possibleSpots.next();
					if (board[tmp]==1){
						if (board[Model.getPath(i).get(tmp)] == 0){
							Model.setValue(tmp, 0);
							return;
						}
					}
				}
			}
		}
		
		//try to eat something on a path, aka any piece
		for (int i=0;i<16;i++){
			if (board[i]==1 && (Model.getPath(i)!=null)){
				Model.setValue(i, 0);
				return;
			}
		}
		
		//eat anything
		for (int i=0;i<16;i++){
			if (board[i]==1){
				Model.setValue(i, 0);
				return;
			}
		}
	}

}
