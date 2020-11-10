package sanchietti.crosstheword.gridbuilder;

public abstract class GridGenerator {

	/**
	 * representation of the grid
	 */
	protected char[][] grid;
	/**
	 * width of the grid
	 */
	protected int width;
	/**
	 * height of the grid
	 */
	protected int height;
	/**
	 * number of blocks to generate
	 */
	protected int blocks;
	/**
	 * orizontal mirror
	 */
	protected boolean oMirror;
	/*
	 * vertical mirror
	 */
	protected boolean vMirror;
	/**
	 * this parameter si used when the grid has to be mirrored. this value can be a
	 * number between 1 and 10. Higher is the number and higher is the chance of
	 * every black box to not be copied
	 */
	protected int dirt = 0;
	/**
	 * this field is used to choose the maximum length oft the words
	 */
	protected int maxWordLength = 13;

	protected int minWordLength = 3;
	/**
	 * this is the checker used to see if a grid is valid or not
	 */
	protected GridChecker gridChecker;

	protected int finalWidth;
	protected int finalHeight;
	protected int finalBlocks;

	protected GridGenerator() { }

	public static GridGenerator getBuilder() {
		throw new Error("Can not instantiate Grid builder");
	};

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
	public GridGenerator setBlocks(int s) {
		this.blocks = s;
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

	public GridGenerator setMinWordLength(int i) {
		this.minWordLength = i;
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
	public abstract char[][] build();

	protected void checkGrid() {
		gridChecker.finalWidth = width;
		gridChecker.finalHeight = height;
		do {
			this.gridChecker.checkConnections();
		} while (this.gridChecker.checkWordsLength());
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
}
