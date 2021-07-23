import java.io.IOException;

public class DirectorsFilter implements Filter{
    private String myDirector;

    public DirectorsFilter(String director) {
        myDirector = director;
    }

    @Override
    public boolean satisfies(String id) {
        String currDirector = "";
        try {
            currDirector = MovieDatabase.getDirector(id);
        }
        catch (IOException e){
            System.out.println("Error: could not find files");
            System.exit(1);
        }
        return myDirector.contains(currDirector);
    }
}