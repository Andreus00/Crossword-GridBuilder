package sanchietti.crosstheword.gridbuilder;

import java.util.HashMap;
import java.util.List;
/**
 * class that gets data from a grid such as word frequency and a map of horizontal and vertical words
 * it's not essential for the grid generator to work but i think thai it's useful to have infos
 * about the generated grid
 */
public class LenChecker {

    private char[][] grid;
    private int width;
    private int height;
    private int minWordLength;
    private int maxWordLength;
    private int tot;
    public LenChecker(char[][] grid, int maxWL, int minWL){
        this.grid = grid;
        this.maxWordLength = maxWL;
        this.minWordLength = minWL;
    }

    private HashMap<Integer, Integer> HorizontalWordsFrequency = new HashMap<>();
    private HashMap<Integer, Integer> VerticalWordsFrequency = new HashMap<>();
    private HashMap<Integer,Double> mergedMap;

    public List<Object> map() {
        width = grid[0].length;
        height = grid.length;

        int spaceCounter;
        //count horizontal words
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
        return List.of(tot, HorizontalWordsFrequency, VerticalWordsFrequency, mergedMap);
    }
    
    private void estimateFreq(){
        tot = HorizontalWordsFrequency.values().stream().reduce(0, (x, y) -> x + y) + VerticalWordsFrequency.values().stream().reduce(0, (x, y) -> x + y);
        System.out.println("total of words: " + tot);
        mergedMap = new HashMap<>();
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
