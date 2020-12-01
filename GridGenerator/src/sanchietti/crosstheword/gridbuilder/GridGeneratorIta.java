package sanchietti.crosstheword.gridbuilder;

import java.util.concurrent.TimeoutException;

public class GridGeneratorIta extends GridGenerator {

	private GridGeneratorIta() {
		super();
	}

	public static GridGeneratorIta getBuilder() {
		return new GridGeneratorIta();
	}

	/**
	 * method that builds the grid
	 * 
	 * @return
	 * @throws TimeoutException
	 */
	@Override
	public char[][] build() throws TimeoutException {
		//input check
		if(minWordLength > maxWordLength/2){
			minWordLength = 3;
			System.out.println("minWordLength not allowed, it will be automatically set to the default value " + 3);
		}
		if(blocks >= finalWidth * finalHeight){ throw new Error("blocks number is not valid"); }

		//creating the grid checker
		this.gridChecker = new GridChecker(grid, width, height, blocks, maxWordLength, minWordLength, hMirror, vMirror);
		//creating the grid
		this.grid = new char[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				grid[i][j] = ' ';

		// calculate the width, the height and the spaces of the
		// grid without mirroring
		finalWidth = this.width / (1 + (vMirror ? 1 : 0));
		finalHeight = this.height / (1 + (hMirror ? 1 : 0));
		//placing blocks ('*') in random boxes
		while (blocks > 0) {
			int w = (int) (Math.random() * finalWidth);
			int h = (int) (Math.random() * finalHeight);
			char c = grid[h][w];

			if (c != '*') {
				grid[h][w] = '*';
				blocks--;
			}
		}
		//check if all the zones are connected
		this.gridChecker.checkConnections();
		//horizontal mirror
		if (hMirror) {
			for (int x = 0; x < finalHeight; x++)
				for(int j = 0; j < width; j++)
					if (Math.random() * 20 < 20 - dirt)
						grid[height - x - 1][j] = grid[x][j];
		}
		//vertical mirror
		if (vMirror) {
			for (int x = 0; x < finalWidth; x++)
				for (int j = 0; j < height; j++) {
					if (Math.random() * 20 < 20 - dirt)
						grid[j][width - x - 1] = grid[j][x];
				}
		}
		//check the grid
		checkGrid();

		LenChecker lc = new LenChecker(grid, maxWordLength, minWordLength);
		lc.map();
		
		return grid;
	}
}
