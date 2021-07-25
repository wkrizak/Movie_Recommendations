import java.io.IOException;

public class MinutesFilter implements Filter{
    private int minMinutes;
    private int maxMinutes;


    public MinutesFilter(int min, int max) {
        minMinutes = min;
        maxMinutes = max;
    }

    @Override
    public boolean satisfies(String id) {
        int currMinutes = 0;
        try {
            currMinutes = MovieDatabase.getMinutes(id);
        }
        catch (IOException e){
            System.out.println("Error: could not find files");
            System.exit(1);
        }
        if(minMinutes <= currMinutes && maxMinutes >= currMinutes) {
            return true;
        }

        return false;
    }
}

