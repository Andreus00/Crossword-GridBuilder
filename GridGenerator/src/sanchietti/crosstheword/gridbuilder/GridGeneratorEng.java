package sanchietti.crosstheword.gridbuilder;

import java.util.concurrent.TimeoutException;

public class GridGeneratorEng extends GridGenerator {

	private GridGeneratorEng() {

	}

	public static GridGeneratorEng getBuilder() {
		return new GridGeneratorEng();
	}

	@Override
	public char[][] build() throws TimeoutException {
		this.grid = new char[height][width];
		if(minWordLength > maxWordLength/2){
			minWordLength = 3;
			System.out.println("minWordLength not allowed, it will be automatically set to the default value " + 3);
		}

		this.gridChecker = new GridChecker(grid, width, height, blocks, maxWordLength, minWordLength, oMirror, vMirror);

		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				grid[i][j] = ((i % 2 == 1 && j % 2 == 1) || i == 0 || i == height-1 || j == 0 || j == width - 1) && !(Math.random()*100 < 10) ? '*' : ' ';

		checkGrid();

		EngLenChecker elc = new EngLenChecker(grid, maxWordLength, minWordLength);
		elc.map();

		return grid;
	}
}
