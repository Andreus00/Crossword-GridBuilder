package sanchietti.crosstheword.gridbuilder;

import java.io.FileWriter;
import java.io.IOException;

 public class Main{
     public static void main(String[] args) {

		/*
		in this example i am going to create some Grids
		*/

		for(int height = 10; height <= 14; height++){
			for(int width = height; width <= 14; width++){
				try{
					char[][] grid = GridGeneratorEng.getBuilder().setSize(width, height).setBlocks(width * height / 4).setVerticalMirror().setHorizontalMirror().setMaxWordLength(10).setMinWordLength(3).build();

					FileWriter myWriter = new FileWriter("grid_" + width + "x" + height + ".txt");
					String s = "";
					for(int j = 0; j < height; j++){
						for(int k = 0; k < width - 1; k++)
							s += grid[j][k] + ",";
						s += grid[j][width - 1] + "\n";
					}
					myWriter.write(s);
					myWriter.close();

				}
				catch(Exception e){
					if(e instanceof IndexOutOfBoundsException){
						System.out.println("Error In Main");
						System.out.println();
						e.printStackTrace();
					}
					else if(e instanceof IOException){
						e.printStackTrace();
					}
					else if(e instanceof TimeOutException){
						e.printStackTrace();
						continue;
					}
				}
			}
		}
		System.out.println("finished");
	}
}
