package sanchietti.crosstheword.gridbuilder;

import java.io.FileWriter;
import java.io.IOException;

public class main {
    public static void main(String[] args) {
		int a = 0;
        String type = "ITA";
        
        int height1 = 14;//beginnng h
        int height2 = 14;//ending h
        int width2 = 14;//ending w

		for(int height = height1; height <= height2; height++){
			for(int width = height; width <= width2; width++){
				try{
					for(a = 0; a < 10; a++){
						char[][] grid = GridGeneratorIta.getBuilder().setSize(width, height).setVerticalMirror().setHorizontalMirror().setBlocks(width * height / 7).setMaxWordLength(13).setMinWordLength(3).build();

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
