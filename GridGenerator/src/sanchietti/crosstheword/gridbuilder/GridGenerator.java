package sanchietti.crosstheword.gridbuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;

public class GridGenerator {

	/**
	 * representation of the grid
	 */
	private char[][] grid;
	/**
	 * width of the grid
	 */
	private int width;
	/**
	 * height of the grid
	 */
	private int height;
	/**
	 * number of spaces to generate
	 */
	private int spaces;
	/**
	 * orizontal mirror
	 */
	private boolean oMirror;
	/*
	 * vertical mirror
	 */
	private boolean vMirror;
	/**
	 * this parameter si used when the grid has to be mirrored. this value can be a
	 * number between 1 and 10. Higher is the number and higher is the chance of
	 * every black box to not be copied
	 */
	private int dirt = 0;
	/**
	 * this field is used to choose the maximum length oft the words
	 */
	private int maxWordLength = 13;

	private final int THETA = 30;

	private GridGenerator() {

	}

	public static GridGenerator getBuilder() {
		return new GridGenerator();
	}

	/**
	 * method used to set the size of the grid
	 * 
	 * @param w with of the grid
	 * @param h height of the grid
	 * @return this
	 */
	public GridGenerator setSize(int w, int h) {
		this.width = w;
		this.height = h;
		return this;
	}

	/**
	 * method used to set the number of black boxes to put in the grid
	 * 
	 * @param s
	 * @return this
	 */
	public GridGenerator setSpaces(int s) {
		this.spaces = s;
		return this;
	}

	/**
	 * method used to set to true the vertical mirroring of the grid
	 * 
	 * @return this
	 */
	public GridGenerator setVerticalMirror() {
		this.vMirror = true;
		return this;
	}

	/**
	 * method used to set the maximum numbers of characters for the words
	 * 
	 * @param i max number
	 * @return this
	 */
	public GridGenerator setMaxWordLength(int i) {
		this.maxWordLength = i;
		return this;
	}

	/**
	 * method used to set to true the orizontal mirroring of the grid
	 * 
	 * @return
	 */
	public GridGenerator setHorizontalMirror() {
		this.oMirror = true;
		return this;
	}

	public GridGenerator setDirt(int d) {
		if (d < 0 && d >= 10)
			throw new IllegalArgumentException("The dirt value ca only be 0 <= dirt <= 10");
		this.dirt = d;
		return this;
	}

	/**
	 * method that builds the grid
	 * 
	 * @return
	 */
	public char[][] build() {
		this.grid = new char[height][width];

		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
			grid[i][j] = ' ';

		// calculate the width, the height and the spaces of the
		// grid without mirroring
		int finalWidth = this.width / (1 + (vMirror ? 1 : 0));
		int finalHeight = this.height / (1 + (oMirror ? 1 : 0));
		int finalSpaces = this.spaces;

		assert finalSpaces < finalWidth * finalHeight;

		while (finalSpaces > 0) {
			int w = (int) (Math.random() * finalWidth);
			int h = (int) (Math.random() * finalHeight);
			char c = grid[h][w];

			// System.out.println("|"+c+"|");

			if (c != '*') {
				grid[h][w] = '*';
				finalSpaces--;
			}
		}

		// ------------------
		// TODO
		// now i have to parse the columns and the rows and see if there are too long
		// words
		// parsing columns: i have 2 possibilities:
		// DONE 1)there is not mirroring (i have to check in every column if there are
		// too long words)
		// TODO 2)there is mirroring (i need an other passage that checks if, once the
		// mirroring is done, there will be too long words)

		///////////////// rows
		for (int i = 0; i < finalHeight; i++) {
			int spaceCounter = 0;
			for (int j = 0; j < finalWidth; j++) {
				if (spaceCounter > maxWordLength) {

					// i have to generate a '*' between j and j-maxWordLength
					int start = j - (spaceCounter);
					int offset = (int) (Math.random() * spaceCounter);
					grid[i][start + offset] = '*';

					// then i have to set the spaceCounter to j-positionOfNewPoint
					spaceCounter = j - (start + offset);
				}
				if (grid[i][j] == ' ')
					spaceCounter++;
				else
					spaceCounter = 0;
			}
			if (oMirror && spaceCounter * 2 > maxWordLength) {

				int lastPoint = 0;
				do {
					lastPoint = (int) (Math.random() * (spaceCounter) + 1);
				} while ((lastPoint) * 2 > maxWordLength);
				grid[i][finalWidth - lastPoint] = '*';
				// System.out.println("Added Mirroring Point: " + i + " - " + (finalWidth -
				// lastPoint));
			}
		}

		/////////////////////// Columns
		for (int i = 0; i < finalWidth; i++) {
			int spaceCounter = 0;
			for (int j = 0; j < finalHeight; j++) {
				if (spaceCounter > maxWordLength) {
					// i have to generate a '*' between j and j-maxWordLength
					int start = j - spaceCounter;
					int offset = (int) (Math.random() * spaceCounter);
					grid[start + offset][i] = '*';
					// System.out.println("New point at: " + i + " - " + start+offset);
					// then i have to set the spaceCounter to j-positionOfNewPoint
					spaceCounter = j - (start + offset);
				}
				if (grid[j][i] == ' ')
					spaceCounter++;
				else
					spaceCounter = 0;
			}
			if (vMirror && spaceCounter * 2 > maxWordLength) {

				int lastPoint = 0;
				do {
					lastPoint = (int) (Math.random() * (spaceCounter) + 1);
				} while ((lastPoint) * 2 > maxWordLength);
				grid[finalWidth - lastPoint][i] = '*';
				// System.out.println("Added Mirroring Point: " + i + " - " + (finalWidth -
				// lastPoint));
			}
		}

		///////////////////////

		// Do the same for the rows
		// ------------------------------

		if (oMirror) {
			for (int x = 0; x < finalHeight; x++)
				if (Math.random() * 20 < 20 - dirt)
					grid[height - x - 1] = grid[x];
		}
		if (vMirror) {
			for (int x = 0; x < finalWidth; x++)
				for (int j = 0; j < height; j++) {
					if (Math.random() * 20 < 20 - dirt)
						grid[j][width - x - 1] = grid[j][x];
				}
		}

		// ------------
		// TODO add a check for zones that are not connected between eachother
		// ------------


		return grid;
	}

	@Override
	public String toString() {
		String s = "";
		for (char[] row : grid) {
			for (char el : row) {
				s += el + "|";
			}
			s += "\n";
		}
		return s;
	}

	public static void main(String[] args) throws IOException {
		for(int i = 0; i < 10; i ++){
			char[][] grid = GridGenerator.getBuilder().setSize(14,14).setSpaces(9).setHorizontalMirror().setVerticalMirror().setMaxWordLength(10).buildEng();
			//System.out.println(GridGenerator.getBuilder().setSize(14, 14).setOrizontalMirror().setVerticalMirror().setDirt(0).build().toString());
			try{
				FileWriter myWriter = new FileWriter("GridGenerator\\src\\sanchietti\\crosstheword\\gridbuilder\\grids\\grid"+i+".txt");
				String s = "";
				for(int j = 0; j < grid.length; j++){
					for(int k = 0; k < grid[0].length - 1; k++)
						s += grid[j][k] + ",";
					s += grid[j][grid[0].length - 1] + "\n";
				}
				System.out.println(s);
				myWriter.write(s);
				myWriter.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}



	public char[][] buildEng() {

		this.grid = new char[height][width];

		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				grid[i][j] = ((i % 2 == 1 && j % 2 == 1) || i == 0 || i == height-1 || j == 0 || j == width - 1) && !(Math.random()*100 < 15) ? '*' : ' ';

		
		///////////////// rows
		for (int i = 0; i < height; i++) {
			int spaceCounter = 0;
			for (int j = 0; j < width; j++) {
				if (spaceCounter > maxWordLength) {

					// i have to generate a '*' between j and j-maxWordLength
					int start = j - (spaceCounter);
					int offset = (int) (Math.random() * spaceCounter);
					grid[i][start + offset] = '*';

					// then i have to set the spaceCounter to j-positionOfNewPoint
					spaceCounter = j - (start + offset);
				}
				if (grid[i][j] == ' ')
					spaceCounter++;
				else
					spaceCounter = 0;
			}
			if (oMirror && spaceCounter * 2 > maxWordLength) {

				int lastPoint = 0;
				do {
					lastPoint = (int) (Math.random() * (spaceCounter) + 1);
				} while ((lastPoint) * 2 > maxWordLength);
				grid[i][width - lastPoint] = '*';
				// System.out.println("Added Mirroring Point: " + i + " - " + (finalWidth -
				// lastPoint));
			}
		}

		/////////////////////// Columns
		for (int i = 0; i < width; i++) {
			int spaceCounter = 0;
			for (int j = 0; j < height; j++) {
				if (spaceCounter > maxWordLength) {
					// i have to generate a '*' between j and j-maxWordLength
					int start = j - spaceCounter;
					int offset = (int) (Math.random() * spaceCounter);
					grid[start + offset][i] = '*';
					// System.out.println("New point at: " + i + " - " + start+offset);
					// then i have to set the spaceCounter to j-positionOfNewPoint
					spaceCounter = j - (start + offset);
				}
				if (grid[j][i] == ' ')
					spaceCounter++;
				else
					spaceCounter = 0;
			}
			if (vMirror && spaceCounter * 2 > maxWordLength) {

				int lastPoint = 0;
				do {
					lastPoint = (int) (Math.random() * (spaceCounter) + 1);
				} while ((lastPoint) * 2 > maxWordLength);
				grid[width - lastPoint][i] = '*';
				// System.out.println("Added Mirroring Point: " + i + " - " + (finalWidth -
				// lastPoint));
			}
		}

		return grid;
	}


}
