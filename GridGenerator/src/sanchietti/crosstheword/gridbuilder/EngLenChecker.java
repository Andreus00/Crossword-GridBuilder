package sanchietti.crosstheword.gridbuilder;

import java.util.HashMap;
import java.util.Map;

public class EngLenChecker {

    private HashMap<Integer, Double> wordFrequence;
    private char[][] grid;
    private int width;
    private int height;
    private int minWordLength;
    private int maxWordLength;

    private void setFreq(){
        this.wordFrequence = new HashMap<>();
        this.wordFrequence.putAll(Map.of(2, 0.679, 3, 4.730, 4, 7.151, 5, 10.804, 6, 13.674, 7, 14.751, 8, 13.616, 9, 11.356, 10, 8.679, 11, 5.913));
        this.wordFrequence.putAll(Map.of(12, 3.792, 13, 2.329, 14, 1.232, 15, 0.685, 16, 0.290, 17, 0.162, 18, 0.066, 19, 0.041, 20, 0.016));
    }

    public EngLenChecker(char[][] grid, int maxWL, int minWL){
        setFreq();
        this.grid = grid;
        this.maxWordLength = maxWL;
        this.minWordLength = minWL;
    }

    private HashMap<Integer, Integer> HorizontalWordsFrequency = new HashMap<>();
    private HashMap<Integer, Integer> VerticalWordsFrequency = new HashMap<>();

    public void map(){
        width = grid[0].length;
        height = grid.length;

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
        System.out.println("Map of horizontal words: " + HorizontalWordsFrequency);
        System.out.println("Map of vertical words: " + VerticalWordsFrequency);
        estimateFreq();
    }

    private void estimateFreq(){
        int tot = HorizontalWordsFrequency.values().stream().reduce(0, (x, y) -> x + y) + VerticalWordsFrequency.values().stream().reduce(0, (x, y) -> x + y);
        System.out.println("total of words: " + tot);
        HashMap<Integer,Double> mergedMap = new HashMap<>();
        for(int i = minWordLength; i <= maxWordLength; i++){
            int res1 = HorizontalWordsFrequency.get(i) == null ? 0 : HorizontalWordsFrequency.get(i);
            int res2 = VerticalWordsFrequency.get(i) == null ? 0 : VerticalWordsFrequency.get(i);
            mergedMap.put(i, (double) res1 + res2);
        }
        
        mergedMap.forEach((k, v) -> mergedMap.put(k, (v * 100)/tot));
        System.out.println("percent of words " + mergedMap.values());
        System.out.println("\n");
    }

}
