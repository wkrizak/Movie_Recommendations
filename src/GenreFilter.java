import java.io.IOException;

public class GenreFilter implements Filter{
    private String myGenre;

    public GenreFilter(String genre) {
        myGenre = genre;
    }

    @Override
    public boolean satisfies(String id) {
        String currGenre = "";
        try {
            currGenre = MovieDatabase.getGenres(id);
        }
        catch (IOException e){
            System.out.println("Error: could not find files");
            System.exit(1);
        }
        return currGenre.contains(myGenre);
    }
}
