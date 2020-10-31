package sanchietti.crosstheword.gridbuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.BiFunction;

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
	private int blocks;
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
	
	int finalWidth;
	int finalHeight;
	int finalBlocks;
	
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
		
		checkConnections();

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

	private void checkGrid(){
		finalWidth = width;
		finalHeight = height;
		do{
			checkConnections();
		}while(checkMaxWordsLength());
	}

	/**
	 * returns true if the function has changed something in the grid
	 * @return
	 */
	private boolean checkMaxWordsLength(){
		// now i have to parse the columns and the rows and see if there are too long
		// words
		// parsing columns: i have 2 possibilities:
		// 1)	there is not mirroring (i have to check in every column if there are
		// 			too long words)
		// 2)	there is mirroring (i need an other passage that checks if, once the
		// 			mirroring is done, there will be too long words)
		boolean isChanged = false;
		///////////////// rows
		for (int i = 0; i < finalHeight; i++) {
			int spaceCounter = 0;
			for (int j = 0; j < finalWidth; j++) {
				if (spaceCounter > maxWordLength) {
					isChanged = true;
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
				isChanged = true;
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
					isChanged = true;
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
				isChanged = true;
				int lastPoint = 0;
				do {
					lastPoint = (int) (Math.random() * (spaceCounter) + 1);
				} while ((lastPoint) * 2 > maxWordLength);
				grid[finalHeight - lastPoint][i] = '*';
				// System.out.println("Added Mirroring Point: " + i + " - " + (finalWidth -
				// lastPoint));
			}
		}
		return isChanged;
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

	public static void main(String[] args) {
		int a = 0;
		String type = "ITA";

		for(int height = 8; height <= 30; height++){
			for(int width = height; width <= 30; width++){
				try{
					for(a = 0; a < 1; a++){
						char[][] grid = GridGenerator.getBuilder().setSize(width, height).setHorizontalMirror().setVerticalMirror().setBlocks(width * height / 7).setMaxWordLength(12).build();

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
							FileWriter myWriter = new FileWriter("GridGenerator\\src\\sanchietti\\crosstheword\\gridbuilder\\grids\\gridItaTest\\grid_" + width + "x" + height + "_" + type + "_" +a+".txt");
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


	public char[][] buildEng() {

		this.grid = new char[height][width];

		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				grid[i][j] = ((i % 2 == 1 && j % 2 == 1) || i == 0 || i == height-1 || j == 0 || j == width - 1) && !(Math.random()*100 < 15) ? '*' : ' ';

		checkGrid();

		return grid;
	}

	private abstract class Box {
		int x;
		int y;
		
		public Box(int x, int y){
			this.x = x;
			this.y = y;
		}

		public abstract void setZone(Zone z);
		public abstract boolean hasAZone(Zone z);
		public abstract void removeZone(Zone z);

		@Override
		public String toString(){ return x + " - " + y; }
	}

	private class Space extends Box {

		Zone z;

		public Space(int x, int y){
			super(x, y);
		}

		@Override
		public void setZone(Zone z) {
			this.z = z;
		}

		@Override
		public boolean hasAZone(Zone z) {
			return this.z != null;
		}
		@Override
		public String toString(){ return x + " - " + y; }

		@Override
		public void removeZone(Zone z) {
			z.removeElement(this);
			this.z = null;
		}
	}
	private class Block extends Box {

		HashSet<Zone> z = new HashSet<>();

		public Block(int x, int y){
			super(x, y);
		}

		@Override
		public void setZone(Zone z) {
			this.z.add(z);
		}

		@Override
		public boolean hasAZone(Zone z) {
			return this.z.contains(z);
		}

		@Override
		public String toString(){ return x + " - " + y ; }

		@Override
		public void removeZone(Zone z) {
			z.removeElement(this);
			this.z.remove(z);
		}


	}

	private class Zone {

		HashSet<Space> spaces = new HashSet<>();
		HashSet<Block> blocks = new HashSet<>();

		public void addElement(Box box){
			if(box instanceof Space)
				spaces.add((Space) box);
			else
				blocks.add((Block) box);
		}

		public boolean isInside(Box box){
			HashSet<? extends Box> searcInto = box instanceof Space ? spaces : blocks;
			for(Box el : searcInto)
				if(el.x == box.x && el.y == box.y)
				return true;
			return false;
		}

		public void merge(Zone z){
			this.spaces.addAll(z.spaces);
			this.blocks.addAll(z.blocks);
		}

		public void removeElement(Box b){
			HashSet<? extends Box> removeBy = b instanceof Space ? spaces : blocks;
			removeBy.remove(b);
		}
		
	}


	private Box[][] boxGrid;

	private HashSet<Zone> zones = new HashSet<>();


	/**
	 * returns true if there is only 1 zone
	 * @return
	 */
	private boolean checkConnections(){
		createZones();
		return mergeZones();
	}


	/**
	 * this function gets the grid and created the zones of the grid
	 */
	private void 
	createZones(){
		zones = new HashSet<>();
		boxGrid = new Box[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				boxGrid[i][j] = grid[i][j] == ' ' ? new Space(j, i) : new Block(j, i);
		
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++){
				Box el = boxGrid[i][j];
				if(el instanceof Block || el.hasAZone(null))
					continue;

				Zone newZone = new Zone();
				zones.add(newZone);
				newZone.addElement(el);
				el.setZone(newZone);
				recursiveSearch(el, newZone);
			}
	}

	private int attemps = 10;

	/**
	 * returns true if there is only 1 zone
	 * @return
	 */
	private boolean mergeZones() {
		List<Zone> zonesList = new ArrayList<>(zones);
		
		int zoneNumber = zonesList.size();
		if(zoneNumber == 1)
			return true;
		while(zoneNumber > 1){
			zoneNumber = zonesList.size();
			boolean hasMerged = false;
			Zone lastZone = zonesList.get(zoneNumber - 1);
			for(int i = 0; i < zoneNumber - 1; i++){
				HashSet<Block> intersection = new HashSet<>(zonesList.get(i).blocks);
				intersection.retainAll(lastZone.blocks);
				if(intersection.isEmpty())
					continue;
				
				//Merging
				hasMerged = true; // :)
				Block toRemove = (Block) intersection.toArray()[(int) (intersection.size() * Math.random())];

				//add a new space where the block is now
				Space newSpace = (Space) new Space(toRemove.x, toRemove.y);
				boxGrid[toRemove.y][toRemove.x] = newSpace;
				grid[toRemove.y][toRemove.x] = ' ';

				//remove the block from the zones and adding the spaces
				toRemove.z.forEach(zn -> {
					zn.removeElement(toRemove);
					zn.addElement(newSpace);
				});
				//merging the zones
				zonesList.get(i).merge(lastZone);
				zoneNumber--;
				zonesList.remove(lastZone);
				break;
			}
			//if the for cicle ends without a merging it means that the last zone does not connect with the others.
			//i have to delete a brick from the last zone and try again. Can i optimize the choice of the brick to delete? Yes: i can see near to the bricks if there are other bricks with zones. otherwise i go random (love going random)
			if(!hasMerged){
				//ah shit
				try{
					tryDelete(zonesList);
				}
				catch(IndexOutOfBoundsException e){
					
					//System.err.println(this.toString());
					zones = new HashSet<>();
					createZones();
					zonesList = new ArrayList<>(zones);
					zoneNumber = zones.size();
					
					if(attemps < 1 && zoneNumber > 1){
						throw new IndexOutOfBoundsException();
					}
					attemps--;
				}
				
			}
		}
		return false;
	}

	private void tryDelete(List<Zone> zonesList) {
		Zone target = zonesList.get(zonesList.size() - 1);

		//foreach block of the target i have to check if the near Blocks have in the z field an other zone
		ArrayList<Block> notConnected = new ArrayList<>();
		
		for(Block bl : target.blocks){

			for(Direction d : Direction.values()){
				int nextX = bl.x + d.x;
				int nextY = bl.y + d.y;
				
				if(nextX >= 0 && nextX < width && nextY >= 0 && nextY < height && boxGrid[nextY][nextX] instanceof Block){
					Block nearBl = (Block) boxGrid[nextY][nextX];
					if(nearBl.z.size() == 0)
						notConnected.add(nearBl);
					for(Zone zone : nearBl.z){
						if(zone == target)
							continue;
						//se il nearbl ha vicino una zona che non è la target, deve: creare un nuovo spazio; aggiungere lo spazio a boxgrid e alla propria zona, eliminare bl da target,
						Space newSpace = new Space(bl.x, bl.y);
						boxGrid[bl.y][bl.x] = newSpace;
						grid[bl.y][bl.x] = ' ';
						target.removeElement(bl);
						target.addElement(newSpace);
						target.addElement(nearBl);
						nearBl.setZone(target);
						return;
					}
				}
			}
		}

		// se l'algoritmo arriva a questo punto vuol dire che non ci sono zone in comune. Devo quindi scegliere un block a caso dai notConnected e trasformarlo in una zona

		Block notConnectedBlock = notConnected.get((int)((notConnected.size()-1)*Math.random()));
		Space newSpace = new Space(notConnectedBlock.x, notConnectedBlock.y);
		Zone newZone = new Zone();
		newZone.addElement(newSpace);
		zonesList.add(newZone);
		newSpace.setZone(newZone);
		grid[notConnectedBlock.y][notConnectedBlock.x] = ' ';
		boxGrid[notConnectedBlock.y][notConnectedBlock.x] = newSpace;
		recursiveSearch(newSpace, newZone);
	}

	/**
	 * this is a recursive function that gets the input box and his zone. Then:
	 * 		1) adds the boxes near to the input box into the zone zone
	 * 		2) when a box is added to the same zone of the input box, the function is called on that new box
	 * @param b the input box
	 * @param z the zone of the input box
	 */
	private void recursiveSearch(Box b, Zone z){
		if(b instanceof Block)
			return;
		int xB = b.x;
		int yB = b.y;
		
		for(Direction d : Direction.values()){
			int nextX = xB + d.x;
			int nextY = yB + d.y;			
			if(nextX >= 0 && nextX < width && nextY >= 0 && nextY < height){
				Box nextEl = boxGrid[nextY][nextX];
				if(!nextEl.hasAZone(z)){
					nextEl.setZone(z);
					z.addElement(nextEl);
					recursiveSearch(nextEl, z);
				}
			}
		}
	}

	private enum Direction{

		LEFT(-1, 0), RIGHT(1, 0), UP(0, -1), DOWN(0, 1);

		int x;
		int y;

		Direction(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
}
