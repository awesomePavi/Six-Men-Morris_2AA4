import java.io.File;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This class is used to draw the board, the Game; newGame; and existingGame It
 * is a helper to minimize redundancy
 * 
 * @author Pavithran Pathmarajah
 * @version A1
 */
public class Board {
	private Canvas canvas;
	private GraphicsContext gc;
	//the offset of the board
	private double x = 0;
	private double y = 0;
	/*
	 * The position of the positions on the 300x300 image
	 */
	private double posDiskX[] = { 0, 143.75, 287.5, 71.875, 143.75, 215.625, 0, 71.875, 215.625, 287.5, 71.875, 143.75,
			215.625, 0, 143.75, 287.5 };
	private double posDiskY[] = { 0, 0, 0, 71.875, 71.875, 71.875, 143.75, 143.75, 143.75, 143.75, 215.625, 215.625,
			215.625, 287.5, 287.5, 287.5 };

	/**
	 * Create a default canvas that is the size of the board, if nothing is
	 * specified
	 */
	public Board() {
		this.canvas = new Canvas(300, 300);
		gc = canvas.getGraphicsContext2D();
	}

	/**
	 * This constructor will set, the canvas to a choose size and default the
	 * board to the top left
	 * 
	 * @param sizex
	 *            the width of the canvas
	 * @param sizey
	 *            the height of the canvas
	 */
	public Board(int sizex, int sizey) {
		this.canvas = new Canvas(sizex, sizey);
		gc = canvas.getGraphicsContext2D();
	}

	/**
	 * Create a default canvas with the board in a chosen coordinate
	 * 
	 * @param y
	 *            the top position of the board
	 * @param x
	 *            the left position of the board
	 */
	public Board(double x, double y) {
		this.x = x;
		this.y = y;
		this.canvas = new Canvas(300, 300);
		gc = canvas.getGraphicsContext2D();
	}

	/**
	 * Create a canvas of chosen size with the board at chosen coordinates
	 * 
	 * @param sizex
	 *            the width of the canvas
	 * @param sizey
	 *            the height of the canvas
	 * @param x
	 *            the left position of the board
	 * @param y
	 *            the top position of the board
	 */
	public Board(int sizex, int sizey, double x, double y) {
		this.x = x;
		this.y = y;
		this.canvas = new Canvas(sizex, sizey);
		gc = canvas.getGraphicsContext2D();
	}

	/**
	 * This function updates the board and the positions on it
	 */
	public void update() {
		// open the board png taken from the assignment
		//used image for board was unable to use aimge once in jar runanble file hand coded board
		gc.setFill(new Color(1.0,0.94901960784,0.80784313725, 1.0));
		gc.fillRect(x+posDiskX[0]+6.25, y+posDiskY[0]+6.25, 287.5, 287.5);
		gc.setStroke(Color.BLACK);
		gc.strokeLine(x+posDiskX[0]+6.25, y+posDiskY[0]+6.25, x+posDiskX[2]+6.25, y+posDiskY[2]+6.25);
		gc.strokeLine(x+posDiskX[0]+6.25, y+posDiskY[0]+6.25, x+posDiskX[13]+6.25, y+posDiskY[13]+6.25);
		gc.strokeLine(x+posDiskX[2]+6.25, y+posDiskY[2]+6.25, x+posDiskX[15]+6.25, y+posDiskY[15]+6.25);
		gc.strokeLine(x+posDiskX[13]+6.25, y+posDiskY[13]+6.25, x+posDiskX[15]+6.25, y+posDiskY[15]+6.25);
		
		gc.strokeLine(x+posDiskX[3]+6.25, y+posDiskY[3]+6.25, x+posDiskX[5]+6.25, y+posDiskY[5]+6.25);
		gc.strokeLine(x+posDiskX[3]+6.25, y+posDiskY[3]+6.25, x+posDiskX[10]+6.25, y+posDiskY[10]+6.25);
		gc.strokeLine(x+posDiskX[5]+6.25, y+posDiskY[5]+6.25, x+posDiskX[12]+6.25, y+posDiskY[12]+6.25);
		gc.strokeLine(x+posDiskX[10]+6.25, y+posDiskY[10]+6.25, x+posDiskX[12]+6.25, y+posDiskY[12]+6.25);
		
		gc.strokeLine(x+posDiskX[1]+6.25, y+posDiskY[1]+6.25, x+posDiskX[4]+6.25, y+posDiskY[4]+6.25);
		gc.strokeLine(x+posDiskX[6]+6.25, y+posDiskY[6]+6.25, x+posDiskX[7]+6.25, y+posDiskY[7]+6.25);
		gc.strokeLine(x+posDiskX[8]+6.25, y+posDiskY[8]+6.25, x+posDiskX[9]+6.25, y+posDiskY[9]+6.25);
		gc.strokeLine(x+posDiskX[11]+6.25, y+posDiskY[11]+6.25, x+posDiskX[14]+6.25, y+posDiskY[14]+6.25);
		
		// get current board info and draw it
		int tmp[] = Model.getCurBoard();
		// go through all positions on board
		for (int i = 0; i < 16; i++) {
			// set oval to corresponding colour and draw it
			setClr(tmp[i]);
			gc.fillOval(x + posDiskX[i], y + posDiskY[i], 12.5, 12.5);
		}
	}

	/**
	 * this function set's the current fill colour to correspond to that of a
	 * specified integer value
	 * 
	 * @param i
	 *            the value to match to set a colour with respect too
	 */
	private void setClr(int i) {
		// switch statment for values
		switch (i) {
		// empty spot
		case 0:
			gc.setFill(Color.BLACK);
			break;
		// player red
		case 1:
			gc.setFill(Color.RED);
			break;
		// player blue
		case 2:
			gc.setFill(Color.BLUE);
			break;
		// error
		case 3:
			gc.setFill(Color.LIGHTGREEN);
			break;
		// should not be reached if reached their is an error
		default:
			gc.setFill(Color.WHITE);
			break;
		}

	}

	/**
	 * This function takes in x and y coordinates for a mouse pointer and then
	 * outlines a corresponding spot with a specified color
	 * 
	 * @param x
	 *            the x coordinate relative to the canvas
	 * @param y
	 *            the y coordinate relative to the canvas
	 * @param clr
	 *            the colour to highlight with
	 */
	public void highlight(double x, double y, Color clr) {
		// get corresponding position from given coordinates
		int tmp = position(x, y);
		// if their is a corresponding position then highlight
		if (tmp >= 0)
			// outline specified position with specified color
			strokePos(tmp, clr);
	}

	/**
	 * This function outlines a specified position on the board with a specified
	 * Color
	 * 
	 * @param pos
	 *            the position on the board to outline
	 * @param clr
	 *            the colour to outline with
	 */
	public void strokePos(int pos, Color clr) {
		gc.setStroke(clr);
		// thick outline is needed to make it more easily visible
		gc.strokeOval(this.x + posDiskX[pos] + .5, this.y + posDiskY[pos] + .5, 11.5, 11.5);
		gc.strokeOval(this.x + posDiskX[pos] + 1, this.y + posDiskY[pos] + 1, 10.5, 10.5);
		gc.strokeOval(this.x + posDiskX[pos] + 1.5, this.y + posDiskY[pos] + 1.5, 9.5, 9.5);
	}

	/**
	 * This function takes in x and y coordinates and returns a position on the board that the mouse is on
	 * 
	 * @param x the x coordinate relative to the canvas
	 * @param y the x coordinate relative to the canvas
	 * @return the position form 1 to 16 of board positions will be returned
	 */
	public int position(double x, double y) {
		// cycle through all board positions
		for (int i = 0; i < 16; i++) {
			// if the x and y are within a 5 pixel radius of a position it is considered to be on that piece
			if (x > (this.x + posDiskX[i] - 5) && x < (this.x + posDiskX[i] + 17.5) && y > (this.y + posDiskY[i] - 5)
					&& y < (this.y + posDiskY[i] + 17.5)) {
				return i;
			}
		}
		return -1;
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

}
