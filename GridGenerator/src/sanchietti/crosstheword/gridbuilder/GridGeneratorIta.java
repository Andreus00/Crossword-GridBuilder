package sanchietti.crosstheword.gridbuilder;

import java.io.FileWriter;
import java.io.IOException;

public class GridGeneratorIta extends GridGenerator{

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
	 */
	@Override
	public char[][] build() {
		this.grid = new char[height][width];
		if(minWordLength > maxWordLength/2){
			minWordLength = 3;
			System.out.println("minWordLength not allowed, it will be automatically set to the default value " + 3);
		}
		this.gridChecker = new GridChecker(grid, width, height, blocks, maxWordLength, minWordLength, oMirror, vMirror);

		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				grid[i][j] = ' ';

		// calculate the width, the height and the spaces of the
		// grid without mirroring
		finalWidth = this.width / (1 + (vMirror ? 1 : 0));
		finalHeight = this.height / (1 + (oMirror ? 1 : 0));
		finalBlocks = this.blocks;

		assert finalBlocks < finalWidth * finalHeight;

		while (finalBlocks > 0) {
			int w = (int) (Math.random() * finalWidth);
			int h = (int) (Math.random() * finalHeight);
			char c = grid[h][w];

			// System.out.println("|"+c+"|");

			if (c != '*') {
				grid[h][w] = '*';
				finalBlocks--;
			}
		}
		
		this.gridChecker.checkConnections();

		if (oMirror) {
			for (int x = 0; x < finalHeight; x++)
				for(int j = 0; j < width; j++)
					if (Math.random() * 20 < 20 - dirt)
						grid[height - x - 1][j] = grid[x][j];
		}
		if (vMirror) {
			for (int x = 0; x < finalWidth; x++)
				for (int j = 0; j < height; j++) {
					if (Math.random() * 20 < 20 - dirt)
						grid[j][width - x - 1] = grid[j][x];
				}
		}

		checkGrid();

		EngLenChecker elc = new EngLenChecker(grid, maxWordLength, minWordLength);
		elc.map();
		
		return grid;
	}
}
