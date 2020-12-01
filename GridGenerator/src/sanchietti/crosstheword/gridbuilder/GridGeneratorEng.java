package sanchietti.crosstheword.gridbuilder;

import java.util.concurrent.TimeoutException;
/**
 * Generator for english grids
 */
public class GridGeneratorEng extends GridGenerator {

	private GridGeneratorEng() {}
	/**
	 * getter of the builder
	 * @return
	 */
	public static GridGeneratorEng getBuilder() {
		return new GridGeneratorEng();
	}

	@Override
	public char[][] build() throws TimeoutException {
		//creates the grid as an array
		this.grid = new char[height][width];
		//check
		if(minWordLength > maxWordLength/2){
			minWordLength = 3;
			System.out.println("minWordLength not allowed, it will be automatically set to the default value " + 3);
		}
		//get a grid checker
		this.gridChecker = new GridChecker(grid, width, height, blocks, maxWordLength, minWordLength, hMirror, vMirror);
		//creates the grid randomly
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				grid[i][j] = ((i % 2 == 1 && j % 2 == 1) || i == 0 || i == height-1 || j == 0 || j == width - 1) && !(Math.random()*100 < 10) ? '*' : ' ';
		//check of the grid
		checkGrid();
		//check for the length of the words
		LenChecker lc = new LenChecker(grid, maxWordLength, minWordLength);
		lc.map();

		return grid;
	}
}
