package sanchietti.crosstheword.gridbuilder;

import java.util.HashMap;
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
	 * this parameter si used when the grid has to be mirrored.
	 * this value can be a number between 1 and 10.
	 * Higher is the number and higher is the chance of every black box
	 * to not be copied
	 */
	private int dirt = 1;
	/**
	 * this field is used to choose the maximum length oft the words
	 */
	private int maxWordLength = 13;
	
	/**
	 * This is the map of row -> TreeSet that contains all the position of the elements. 
	 * Theese information will be used to check the grid generated (lenght of the words)
	 */
	private HashMap<Integer, TreeSet<Integer>> rows = new HashMap<>();
	/**
	 * This is the map of colunms -> TreeSet that contains all the position of the elements. 
	 * Theese information will be used to check the grid generated (lenght of the words)
	 */
	private HashMap<Integer, TreeSet<Integer>> columns = new HashMap<>();
	
	
	private final int THETA = 30;
	
	private GridGenerator() {
		
	}
	
	public static GridGenerator getBuilder() { return new GridGenerator(); }
	
	/**
	 * method used to set the size of the grid
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
	 * @param s
	 * @return this
	 */
	public GridGenerator setSpaces(int s) {
		this.spaces = s;
		return this;
	}
	
	/**
	 * method used to set to true the vertical mirroring of the grid
	 * @return this
	 */
	public GridGenerator setVerticalMirror() {
		this.vMirror = true;
		return this;
	}
	
	/**
	 * method used to set the maximum numbers of characters for the words
	 * @param i max number
	 * @return this
	 */
	public GridGenerator setMaxWordLength(int i) {
		this.maxWordLength = i;
		return this;
	}
	
	/**
	 * method used to set to true the orizontal mirroring of the grid
	 * @return
	 */
	public GridGenerator setOrizontalMirror() {
		this.oMirror = true;
		return this;
	}
	
	public GridGenerator setDirt(int d) {
		assert d > 0 && d <= 10;
		this.dirt = d;
		return this;
	}
	
	/**
	 * method that builds the grid
	 * @return
	 */
	public char[][] build() {
		this.grid = new char[height][width];
				
		//calculate the width, the height and the spaces of the 
		//grid without mirroring
		int finalWidth = this.width/(1 + (vMirror ? 1 : 0));
		int finalHeight = this.height/(1 + (oMirror ? 1 : 0));
		int finalSpaces = this.spaces;
		
		for(int i = 0; i < finalHeight; i++)
			rows.put(i, new TreeSet<>());
		for(int i = 0; i < finalWidth; i++)
			columns.put(i, new TreeSet<>());
		
		assert finalSpaces < finalWidth * finalHeight;
		
		while(finalSpaces > 0) {
			int w = (int)(Math.random()*finalWidth);
			int h = (int)(Math.random()*finalHeight);
			char c = grid[h][w];
			
			System.out.println("|"+c+"|");
			
			if(c != '*') {
				grid[h][w] = '*';
				finalSpaces--;
				
				//When i add a '*' i have to add his position in the hashmap of the columns and in the hashmap of the rows
			columns.get(w).add(h);
			rows.get(h).add(w);
			
			}
		}
		
		//------------------
		//TODO
		//now i have to parse the columns and the rows and see if there are too long words
		
		//parsing columns: i have 2 possibilities:
		//		1)there is not mirroring (i have to check in every column if there are too long words)
		//		2)there is mirroring (i need an other passage that checks if, once the mirroring is done, there will be too long words)
		//
		//Do the same for the rows
		//------------------------------
		
		if(oMirror) {
			for(int x = 0; x < finalHeight; x++)
				if(Math.random()*20 < 20 - dirt)
					grid[height-x-1] = grid[x];
		}
		if(vMirror) {
			for(int x = 0; x < finalWidth; x++)
				for(int j = 0; j < height; j++) {
					if(Math.random()*20 < 20 - dirt)
						grid[j][width-x-1] = grid[j][x];
				}
		}
		
		//------------
		//TODO add a check for the mid col\row
		//------------
		
		//------------
		//TODO add a check for zones that are not connected between eachother
		//------------
		
		
		return grid;
	}
	
	
	private void checkDistance() {
		
	}
	
	@Override
	public String toString() {
		String s = "";
		for(char[] row : grid) {
			for(char el : row) {
				s += el + "|";
			}
			s += "\n";
		}
		return s;
	}
	
	public static void main(String[] args) {
		System.out.println(GridGenerator.getBuilder().setSize(20, 20).setSpaces(20).setOrizontalMirror().setVerticalMirror().setDirt(1).build().toString());
	}
}
