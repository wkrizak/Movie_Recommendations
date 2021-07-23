import java.io.IOException;

public class YearFilter implements Filter {
    private int myYearMin;
    private int myYearMax;

    public YearFilter(int yearMin, int yearMax) {
        myYearMin = yearMin;
        myYearMax = yearMax;

        // If input years are swapped, fix here
        if (yearMax < yearMin){
            myYearMax = yearMin;
            myYearMin = yearMax;
        }
    }

    @Override
    public boolean satisfies(String id) {
        int currYear = 0;
        try {
            currYear = MovieDatabase.getYear(id);
        }
        catch (IOException e){
            System.out.println("Error: could not find files");
            System.exit(1);
        }
        if (myYearMin <= currYear && myYearMax >= currYear){
            return true;
        }
        return false;
    }

}
