package sanchietti.crosstheword.gridbuilder;

import java.io.FileWriter;
import java.io.IOException;

 public class main {
     public static void main(String[] args) {

		int a = 0;
		for(int height = 10; height <= 30; height++){
			for(int width = height; width <= 30; width++){
				for(a = 0; a < 3; a++){
					try{
						char[][] grid = GridGeneratorEng.getBuilder().setSize(width, height).setBlocks(width * height / 7).setMaxWordLength(10).setMinWordLength(3).build();

						try{
							FileWriter myWriter = new FileWriter("GridGenerator\\src\\sanchietti\\crosstheword\\gridbuilder\\grids\\GridEng\\grid_" + width + "x" + height + "_" + "ENG" + "_" +a+"_maxWL10_minWL3.txt");
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
					catch(Exception e){
						if(e instanceof IndexOutOfBoundsException){
							System.out.println("Error In Main");
							System.out.println();
							e.printStackTrace();
						}
						else if(e instanceof TimeOutException){
							a--;
						}
					}
				}
			}
		}
		System.out.println("finished");

		////////////////////////////////////////////////

		for(int height = 10; height <= 30; height++){
			for(int width = height; width <= 30; width++){
				for(a = 3; a < 6; a++){
					try{
						char[][] grid = GridGeneratorEng.getBuilder().setSize(width, height).setBlocks(width * height / 7).setHorizontalMirror().setVerticalMirror().setMaxWordLength(13).setMinWordLength(3).build();

						try{
							FileWriter myWriter = new FileWriter("GridGenerator\\src\\sanchietti\\crosstheword\\gridbuilder\\grids\\GridEng\\grid_" + width + "x" + height + "_" + "ENG" + "_" +a+"_maxWL13_minWL3.txt");
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
					catch(Exception e){
						if(e instanceof IndexOutOfBoundsException){
							System.out.println("Error In Main");
							System.out.println();
							e.printStackTrace();
						}
						else if(e instanceof TimeOutException){
							a--;
						}
					}
				}
			}
		}
		System.out.println("finished");

		/////////////////////////////////////////////////////

		for(int height = 10; height <= 30; height++){
			for(int width = height; width <= 30; width++){
				for(a = 6; a < 9; a++){
					try{
						char[][] grid = GridGeneratorEng.getBuilder().setSize(width, height).setHorizontalMirror().setVerticalMirror().setBlocks(width * height / 7).setMaxWordLength(12).setMinWordLength(4).build();

						try{
							FileWriter myWriter = new FileWriter("GridGenerator\\src\\sanchietti\\crosstheword\\gridbuilder\\grids\\GridEng\\grid_" + width + "x" + height + "_" + "ENG" + "_" +a+"_maxWL12_minWL4.txt");
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
					catch(Exception e){
						if(e instanceof IndexOutOfBoundsException){
							System.out.println("Error In Main");
							System.out.println();
							e.printStackTrace();
						}
						else if(e instanceof TimeOutException){
							a--;
						}
					}
				}
			}
		}
		System.out.println("finished");
	}
}
