import java.io.IOException;

public class YearAfterFilter implements Filter {
    private int myYear;

    public YearAfterFilter(int year) {
        myYear = year;
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
        return currYear >= myYear;
    }

}
