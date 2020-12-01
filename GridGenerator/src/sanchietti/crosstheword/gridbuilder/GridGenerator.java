package sanchietti.crosstheword.gridbuilder;

import java.util.concurrent.TimeoutException;

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
	 * number of blocks to generate. 
	 * This parameter may not be respected if there are too long words and so they have to be reduced (i suppose this is the right word ahahah)
	 */
	protected int blocks;
	/**
	 * Horizontal mirror
	 */
	protected boolean hMirror;
	/*
	 * vertical mirror
	 */
	protected boolean vMirror;
	/**
	 * this parameter is used when the grid needs to be mirrored. this value can be a
	 * number between 1 and 10. Higher is the number and higher is the chance of
	 * every black box to not be copied
	 */
	protected int dirt = 0;
	/**
	 * maximum length of the words
	 */
	protected int maxWordLength = 13;
	/**
	 * minimum length of the words
	 */
	protected int minWordLength = 3;
	/**
	 * checker used to see if a grid is valid or not
	 */
	protected GridChecker gridChecker;
	/**
	 * seconds to wait before abort the generation of a grid. (sometimes there are infinite loops with greater grids)
	 */
	protected final int TIMEOUT = 1; //sec
	/**
	 * value set at the beginning of the generation process of the grid
	 */
	protected double beginTime;
	/**
	 * width of the grid without mirroring
	 */
	protected int finalWidth;
	/**
	 * height of the grid without mirroring
	 */
	protected int finalHeight;

	protected GridGenerator() { }
	/**
	 * this method is the getter that needs to be overwritten by the classes that extends GridGenerator
	 * @return
	 */
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
	/**
	 * method used to set the minimum numbers of characters for the words
	 * 
	 * @param i min number
	 * @return this
	 */
	public GridGenerator setMinWordLength(int i) {
		this.minWordLength = i;
		return this;
	}

	/**
	 * method used to set the horizontal mirroring of the grid to true 
	 * 
	 * @return
	 */
	public GridGenerator setHorizontalMirror() {
		this.hMirror = true;
		return this;
	}
	/**
	 * setter for the dirt field
	 * @param d
	 * @return
	 */
	public GridGenerator setDirt(int d) {
		if (d < 0 && d >= 10)
			throw new IllegalArgumentException("The dirt value ca only be 0 <= dirt <= 10");
		this.dirt = d;
		return this;
	}
	
	/**
	 * method that builds the grid
	 * @return
	 * @throws TimeoutException
	 */
	public abstract char[][] build() throws TimeoutException;
	/**
	 * method that checks the grid and fixes long words or not connected zones
	 */
	protected void checkGrid() throws TimeoutException {
		beginTime = System.nanoTime()/1e9;
		gridChecker.finalWidth = width;
		gridChecker.finalHeight = height;
		do {
			this.gridChecker.checkConnections();
			if(System.nanoTime()/1e9 - beginTime > 1)
				throw new TimeoutException("time out");
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
