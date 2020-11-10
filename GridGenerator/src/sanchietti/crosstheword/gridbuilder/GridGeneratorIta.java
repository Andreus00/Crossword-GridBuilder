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
		this.gridChecker = new GridChecker(grid, width, height, blocks, maxWordLength, oMirror, vMirror);

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
		
		return grid;
	}

	public static void main(String[] args) {
		int a = 0;
		String type = "ENG";

		for(int height = 30; height <= 30; height++){
			for(int width = height; width <= 30; width++){
				try{
					for(a = 0; a < 1; a++){
						char[][] grid = GridGeneratorEng.getBuilder().setSize(width, height).setVerticalMirror().setHorizontalMirror().setBlocks(width * height / 7).setMaxWordLength(9).build();
						EngLenChecker elc = new EngLenChecker(grid);
						elc.map();
						// if(grid.checkMaxWordsLength()){
						// 	System.out.println("Length");
						// 	System.out.println(grid.toString());
						// 	throw new Error();
						// }
						// if(!grid.checkConnections()){
						// 	System.out.println("Connections");
						// 	System.out.println(grid.toString());
						// 	throw new Error();
						// }

						// System.out.println(a);
						// System.out.println(grid.toString());

						// // s.next();
						

						//System.out.println(GridGenerator.getBuilder().setSize(14, 14).setOrizontalMirror().setVerticalMirror().setDirt(0).build().toString());
						try{
							FileWriter myWriter = new FileWriter("GridGenerator\\src\\sanchietti\\crosstheword\\gridbuilder\\grids\\GridItaTest\\grid_" + width + "x" + height + "_" + type + "_" +a+".txt");
							String s = "";
							for(int j = 0; j < height; j++){
								for(int k = 0; k < width - 1; k++)
									s += grid[j][k] + ",";
								s += grid[j][width - 1] + "\n";
							}
							
							myWriter.write(s);
							myWriter.close();
						}
						catch(IOException e){
							e.printStackTrace();
						}
					}
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Error In Main");
					System.out.println(a);
					e.printStackTrace();
				}
			}
		}
		System.out.println("finished");
	}

}
