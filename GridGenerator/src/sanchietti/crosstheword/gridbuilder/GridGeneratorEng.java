package sanchietti.crosstheword.gridbuilder;

public class GridGeneratorEng extends GridGenerator{
    
    private GridGeneratorEng() {

    }

    public static GridGeneratorEng getBuilder(){
        return new GridGeneratorEng();
    }

    @Override
	public char[][] build() {

		this.grid = new char[height][width];

		this.gridChecker = new GridChecker(grid, width, height, blocks, maxWordLength, oMirror, vMirror);

		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				grid[i][j] = ((i % 2 == 1 && j % 2 == 1) || i == 0 || i == height-1 || j == 0 || j == width - 1) && !(Math.random()*100 < 10) ? '*' : ' ';

		checkGrid();

		return grid;
	}
}
