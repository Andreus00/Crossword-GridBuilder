package sanchietti.crosstheword.gridbuilder;

import java.util.HashMap;
import java.util.Map;

public class EngLenChecker {

    private HashMap<Integer, Double> wordFrequence;
    private char[][] grid;

    private void setFreq(){
        this.wordFrequence = new HashMap<>();
        this.wordFrequence.putAll(Map.of(2, 0.679, 3, 4.730, 4, 7.151, 5, 10.804, 6, 13.674, 7, 14.751, 8, 13.616, 9, 11.356, 10, 8.679, 11, 5.913));
        this.wordFrequence.putAll(Map.of(12, 3.792, 13, 2.329, 14, 1.232, 15, 0.685, 16, 0.290, 17, 0.162, 18, 0.066, 19, 0.041, 20, 0.016));
    }

    public EngLenChecker(char[][] grid){
        setFreq();
        this.grid = grid;
    }

    public void map(){
        int width = grid[0].length;
        int height = grid.length;

        HashMap<Integer, Integer> HorizontalWordsFrequency = new HashMap<>();
        HashMap<Integer, Integer> VerticalWordsFrequency = new HashMap<>();

        int spaceCounter;
        //conut horizontal words
        for(int i = 0; i < height; i++){
            spaceCounter = 0;
            for (int j = 0; j < width; j++){
                if(grid[i][j] == ' '){
                    spaceCounter ++;
                    continue;
                }
                else if(spaceCounter < 2){
                    spaceCounter = 0;
                    continue;
                }
                HorizontalWordsFrequency.compute(spaceCounter, (k, x) -> (x == null) ? 1 : x + 1);
                spaceCounter = 0;
            }
        }

        //count vertical words
        for(int i = 0; i < width; i++){
            spaceCounter = 0;
            for(int j = 0; j < height; j++){
                if(grid[j][i] == ' '){
                    spaceCounter ++;
                    continue;
                }
                else if(spaceCounter < 2){
                    spaceCounter = 0;
                    continue;
                }
                VerticalWordsFrequency.compute(spaceCounter, (k, x) -> (x == null) ? 1 : x + 1);
                spaceCounter = 0;
            }
        }
        System.out.println(HorizontalWordsFrequency);
        System.out.println(VerticalWordsFrequency);
    }

}
