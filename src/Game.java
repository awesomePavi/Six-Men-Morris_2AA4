import java.util.Iterator;
import java.util.Random;

import javafx.application.Platform;
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
 * This class currently allows for simply moving pieces around no game dynamics
 * 
 * @author Pavi, Ziyi Jin
 * @version A2
 */
public class Game {
	// graphic interface
	private Board board;
	private Scene scene;
	private Canvas canvas;
	private GraphicsContext gc;
	// the player perform current move
	private int currentmove;
	// the player perform next move
	private int nextmove;
	// the current selected piece
	private int selected = -1;
	// the chosen dice to eat
	private int chooseToEat;
	// if a player can eat other's dice
	private boolean makeEat = false;
	// message display
	private Label upperMessage;
	private Label lowerMessage;
	//determines weather vs. AI or not
	boolean AI_=false;

	/**
	 * The game constructor takes in the application window and the first player
	 * then ad's mouse events for user input
	 * 
	 * @param primaryStage
	 *            the application window
	 * @param player
	 *            the first player
	 * @param AI
	 * 			  true if vs AI false if not
	 */
	public Game(Stage primaryStage, int player,boolean AI) {
		AI_=AI;
		// current players move
		nextmove = player;
		// interface root to add features to
		Group root = new Group();
		// get scene for UI to be added
		scene = new Scene(root);

		// to handle the window graphics
		this.canvas = new Canvas(500, 400);
		this.gc = canvas.getGraphicsContext2D();
		board = new Board(500, 400, 100, 50);

		// GUI messages to user on the upper and lower half of the game
		// displays game state and any errors
		upperMessage = new Label("");
		upperMessage.setLayoutX(0);
		upperMessage.setLayoutY(350);
		upperMessage.setMinSize(500, 50);
		upperMessage.setAlignment(Pos.CENTER);
		upperMessage.setFont(Font.font(30));
		lowerMessage = new Label("");
		lowerMessage.setLayoutX(0);
		lowerMessage.setLayoutY(0);
		lowerMessage.setMinSize(500, 50);
		lowerMessage.setAlignment(Pos.CENTER);
		lowerMessage.setFont(Font.font(30));
		// set current message
		dispMessage("Game In Progress");

		// the main menu button from the game, on left side of board, with board
		// height
		Button maineMenuButton = new Button("Main Menu");
		maineMenuButton.setLayoutX(0);
		maineMenuButton.setLayoutY(50);
		maineMenuButton.setMinHeight(300);
		maineMenuButton.setMinWidth(90);
		// so the button is not highlighted
		maineMenuButton.setFocusTraversable(false);

		// handle button press whilst having other mosue listners
		maineMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				// reset all game states, to allow for a new game
				Model.reset();
				// go back to main menu
				new Menu().start(primaryStage);
			}
		});

		// this button saves the game and retunrs to the main menu, right side
		// of game board
		Button saveGameButton = new Button("Save Game");
		saveGameButton.setLayoutX(410);
		saveGameButton.setLayoutY(50);
		saveGameButton.setMinHeight(300);
		saveGameButton.setMinWidth(90);
		saveGameButton.setFocusTraversable(false);

		// handle button press whilst having other mosue listners
		saveGameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				// save current game than go back to main menu
				new LoadSaveGame(primaryStage, nextmove);
			}
		});

		// add mousepress events for actual game piece interaction
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				dispMessage("Game In Progress");
				currentmove = nextmove;
				// cannot eat
				if (!makeEat) {
					// if something has already been selected then place it
					if (selected != -1) {

						boolean moved = moveTo(mouseEvent.getSceneX(), mouseEvent.getSceneY(), selected);
						if (moved) {
							update();
							// check if can eat
							if (Model.canEat(currentmove,
									board.position(mouseEvent.getSceneX(), mouseEvent.getSceneY()))) {
								makeEat = true;
								dispMessage("Remove One Piece");
								mouseEvent.consume();
							}

						} else {
							selected = -1;
							mouseEvent.consume();

						}
					}
					// if nothing has been selected then select an
					// object
					else {
						selected = select(mouseEvent.getSceneX(), mouseEvent.getSceneY());
						mouseEvent.consume();
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
				update();
				// check for a winner or a draw, 3 is a draw
				int win = Model.checkWin(nextmove);
				if (win == 1 || win == 2 || win == 3) {
					new GameOver(primaryStage, win);
				}
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

		// add Graphics to the root panel
		root.getChildren().add(board.getCanvas());
		root.getChildren().add(this.canvas);
		root.getChildren().add(upperMessage);
		root.getChildren().add(lowerMessage);
		root.getChildren().add(maineMenuButton);
		if (!AI_)
			root.getChildren().add(saveGameButton);
		// update to show current board info
		update();
		// background Colour
		scene.setFill(new Color(0.4, 0.4, 0.4, 1.0));

		// set the window and show the graphics
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * This function takes in the x and y locations and then moves the selected
	 * disk accordingly.
	 * 
	 * @param x
	 *            the mouse x position relative to the canvas
	 * @param y
	 *            the mouse y position relative to the canvas
	 * @return return true if dice is successfully moved and false otherwise
	 */
	private boolean moveTo(double x, double y, int from) {
		// the positon the user pressed on
		int toPlace = board.position(x, y);
		// if their is no piece at the choosen position and the mouse is on a
		// position. position starts from 0 to 15
		if (toPlace >= 0 && Model.valueAt(toPlace) == 0 && Model.canMoveTo(from, toPlace)) {
			// place the player's piece and remove it from the last position
			Model.setValue(toPlace, nextmove);
			Model.setValue(selected, 0);
			// move that was made, {colour,from, to}
			int[] move = { nextmove, selected, toPlace };
			Model.trackMoves(move);
			// change the current player's move state
			if (nextmove == 1)
				nextmove = incrTurn(nextmove);
			else
				nextmove = incrTurn(nextmove);
			// no disk is selected anymore
			selected = -1;
			return true;
		}
		dispError("Invalid Move");
		return false;
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
		Model.moveTrackReset();
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
	 * Displayes the given error message above and below the board in red
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
	 * dispalys the messages above and below the board in blue
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
	 * updates the current board and highlights the selected disk
	 */
	private void update() {
		board.update();
		// if a position on the board is selcted highlight it
		if (selected != -1)
			board.strokePos(selected, Color.WHITE);
	}
	
	/**
	 * This method is implemented to allow the AI to be integrated with the current system, by the AI making ti's move and returning it back to the player
	 * 
	 * @param curturn - the current player's turn
	 * @return - the enxt players turn
	 */
	private int incrTurn(int curturn){
		//if it's the Ai's turn
		if (curturn ==1 && AI_){
			AIMove();
			return 1;
		}
		
		//if it's player one's turn make it player two's turn and vice versa
		if (curturn ==1 ){
			return 2;
		}else{
			return 1;
		}
	}
	
	/**
	 * This is the logic behind the AI's move's i nthe game
	 */
	private void AIMove(){
		//get current board info
		int[] board = Model.getCurBoard();
		//if AI is already in a mill, then break the mill
		for (int i=0;i<16;i++){
			//cheks if a AI piece is found and if it is on a mill path
			if (board[i]==2 && (Model.getPath(i)!=null)){
				//iterate through positions along the mill path to see if an AI piece is their
				 Iterator<Integer> possibleSpots = Model.getPath(i).keySet().iterator();
				while(possibleSpots.hasNext()){
					int tmp =possibleSpots.next();
					//if an AI piece is found
					if (board[tmp]==2){
						//at this point 2/3 spots on a mill are knwon to belong to th eAI, if the last path belongs to the AI then
						if (board[Model.getPath(i).get(tmp)] == 2){
							//move the firstpiece of the mill to an empty spot
							int[] check = Model.getAdj(i);
							//finds empty spot and moves piece too it
							for (int i1=0; i1<check.length;i1++){
								if (board[check[i1]]==0){
									Model.setValue(check[i1], 2);
									Model.setValue(i, 0);
									// move that was made, {colour,from, to}
									int[] move = { 2, i,check[i1]};
									Model.trackMoves(move);
									return;
								}
							}
							
							//if the first peice si trapped in then check the enxt piece
							check = Model.getAdj(tmp);
							for (int i1=0; i1<check.length;i1++){
								if (board[check[i1]]==0){
									Model.setValue(check[i1], 2);
									Model.setValue(tmp, 0);
									// move that was made, {colour,from, to}
									int[] move = { 2, tmp,check[i1]};
									Model.trackMoves(move);
									return;
								}
							}
							//if the second peice si trapped in then check thelast piece
							check = Model.getAdj(Model.getPath(i).get(tmp));
							for (int i1=0; i1<check.length;i1++){
								if (board[check[i1]]==0){
									Model.setValue(check[i1], 2);
									Model.setValue(Model.getPath(i).get(tmp), 0);
									// move that was made, {colour,from, to}
									int[] move = { 2, Model.getPath(i).get(tmp),check[i1]};
									Model.trackMoves(move);
									return;
								}
							}
						}
					}
				}
			}
		}
		
		//if the AI does not have a mill it tries to form one
		for (int i=0;i<16;i++){
			//checks all 16 board positions for an AI piece if found then
			if (board[i]==2 && (Model.getPath(i)!=null)){
				//check if two of three spots on mill are AI pieces 
				 Iterator<Integer> possibleSpots = Model.getPath(i).keySet().iterator();
				while(possibleSpots.hasNext()){
					int tmp =possibleSpots.next();
					if (board[tmp]==2){
						//if so and the final spot is empty, then check if an AI piece is adjacent to it and can be moved into it
						if (board[Model.getPath(i).get(tmp)] == 0){
							//check adjacent spots
							int[] check = Model.getAdj(Model.getPath(i).get(tmp));
							for (int i1=0; i1<check.length;i1++){
								//if an AI piece can be mvoes into it then do so
								if (board[check[i1]]==2 && check[i1] !=i && check[i1] != tmp){
									Model.setValue(Model.getPath(i).get(tmp), 2);
									Model.setValue(check[i1], 0);
									// move that was made, {colour,from, to}
									int[] move = { 2, check[i1], Model.getPath(i).get(tmp) };
									Model.trackMoves(move);
									if (Model.canEat(2, Model.getPath(i).get(tmp)))
										AI_Eat();
									return;
								}
							}
						}
					}
				}
			}
		}
		
		//try to block player
		//Stop user from cmpleting path of 3
		for (int i=0;i<16;i++){
			//chekcs if user has a apth of size two yet
			if (board[i]==1 && (Model.getPath(i)!=null)){
				 Iterator<Integer> possibleSpots = Model.getPath(i).keySet().iterator();
				while(possibleSpots.hasNext()){
					int tmp =possibleSpots.next();
					if (board[tmp]==1){
						//if so and the last path is empty then chek if any AI pieces may fill it in
						if (board[Model.getPath(i).get(tmp)] == 0){
							//check adajcent spots
							int[] check = Model.getAdj(Model.getPath(i).get(tmp));
							for (int i1=0; i1<check.length;i1++){
								//if an AI piece can move in then move it in
								if (board[check[i1]]==2){
									Model.setValue(Model.getPath(i).get(tmp), 2);
									Model.setValue(check[i1], 0);
									// move that was made, {colour,from, to}
									int[] move = { 2, check[i1], Model.getPath(i).get(tmp) };
									Model.trackMoves(move);
									return;
								}
							}
						}
					}
				}
			}
		}
		

		//Ai tries to make path of 2
		for (int i=0;i<16;i++){
			//look if the AI has a piece along a mill and is able to expand theat mill to size of two then do so
			if (board[i]==2 && (Model.getPath(i)!=null)){
				 Iterator<Integer> possibleSpots = Model.getPath(i).keySet().iterator();
				while(possibleSpots.hasNext()){
					int tmp =possibleSpots.next();
					//if the spot beside the mill is empty then fill it if possible
					if (board[tmp]==0){
						//check adjacent spots
						int[] check = Model.getAdj(tmp);
						for (int i1=0; i1<check.length;i1++){
							//if an adjacent spot has a ai peice in it then move it to form the mill of size 2
							if (board[check[i1]]==2){
								Model.setValue(tmp, 2);
								Model.setValue(check[i1], 0);
								// move that was made, {colour,from, to}
								int[] move = { 2, check[i1], tmp };
								Model.trackMoves(move);
								return;
							}
						}
					}
				}
			}
		}
		
		//ai moves a piece at random
		Random rand = new Random();
		int toSelect = rand.nextInt(16);
//if the spot chosen to move to has nothign in it
			while (board[toSelect] != 0){
				int[] check = Model.getAdj(toSelect);
				//look for adjacent posittions to that spot
				for (int i1=0; i1<check.length;i1++){
					//if ana adjacent spot has an AI piece then move it into that spot
					if (board[check[i1]]==2){
						Model.setValue(toSelect, 2);
						Model.setValue(check[i1], 0);
						// move that was made, {colour,from, to}
						int[] move = { 2, check[i1], toSelect };
						Model.trackMoves(move);
						return;
					}
				}
				//if not choose another rnadom spot
				toSelect = rand.nextInt(16);
			}
				
		
		
		
	}
	
	/**
	 * Called when the AI has formed a mill and ia about to eat a apiece
	 */
	private void AI_Eat(){
		int[] board = Model.getCurBoard();
		//try to eat something with two pieces goin on 3
		for (int i=0;i<16;i++){
			//look for two player pieces on path to making a mill
			if (board[i]==1 && (Model.getPath(i)!=null)){
				 Iterator<Integer> possibleSpots = Model.getPath(i).keySet().iterator();
				while(possibleSpots.hasNext()){
					int tmp =possibleSpots.next();
					if (board[tmp]==1){
						//if the player has two pieces down check if their are any adjacent pieces whcih can make a mill next round
						if (board[Model.getPath(i).get(tmp)] == 0){
							//check adjacent pieces, for player pieces
							int[] possibleMoves = Model.getAdj(Model.getPath(i).get(tmp));
							for (int j=0;j< possibleMoves.length;j++){
								//if a aplyer piece is found then rmeove a piece of the possible mill
								if (board[possibleMoves[j]]==1){
									Model.setValue(tmp, 0);
									return;
								}
							}
						}
					}
				}
			}
		}
		//try to eat something blocking you
		//if user is blocking ai from formin path destroy them
		for (int i=0;i<16;i++){
			//look for AI pieces
			if (board[i]==2 && (Model.getPath(i)!=null)){
				//if found look if AI ahs two pieces o na mill
				 Iterator<Integer> possibleSpots = Model.getPath(i).keySet().iterator();
				while(possibleSpots.hasNext()){
					int tmp =possibleSpots.next();
					if (board[tmp]==2){
						//if the ai has two places on a mill check if the user is blcoking the alst spot
						if (board[Model.getPath(i).get(tmp)] == 1){
							//if so destroy the player
							Model.setValue(Model.getPath(i).get(tmp), 0);
							return;
						}else if(board[Model.getPath(i).get(tmp)] == 0){
							//if not check if the player is besdie it and can block the ai next round
							int[] possibleMoves = Model.getAdj(Model.getPath(i).get(tmp));
							for (int j=0;j< possibleMoves.length;j++){
								//if so then remvoe that peice
								if (board[possibleMoves[j]]==1){
									Model.setValue(possibleMoves[j], 0);
									return;
								}
							}
						}
					}
				}
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
