/*
 * MovieDatabase stores information for thousands of movies (depending on the input file)
 * Includes methods for easy access to movie data such as genre, director, year released, etc.
 */

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class MovieDatabase {
    private static HashMap<String, Movie> ourMovies;

    public static void initialize(String moviefile) throws IOException{

            if (ourMovies == null) {
                ourMovies = new HashMap<String, Movie>();
                String[] pathNames = {"src", "Data", moviefile};
                String path = String.join(File.separator, pathNames);
                loadMovies(path);
            }

    }

    private static void initialize(){
        try {
            if (ourMovies == null) {
                ourMovies = new HashMap<String, Movie>();
                loadMovies("src\\Data\\ratedmoviesfull.csv");
            }
        }
        catch (IOException e){
            System.out.println("Error: could not find files - MovieDataBase Constructor");
            System.exit(1);
        }
    }


    private static void loadMovies(String filename) throws IOException {
        Reader reader = new FileReader(filename);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());
        for (CSVRecord record : parser) {
            // Create movie from each record and add to ourMovies
            String id = record.get("id");
            String title = record.get("title");
            String year = record.get("year");
            String genres = record.get("genre");
            String director = record.get("director");
            String country = record.get("country");
            String poster = record.get("poster");
            int minutes = Integer.parseInt(record.get("minutes"));
            Movie currMovie = new Movie(id, title, year, genres, director, country, poster, minutes);
            ourMovies.put(currMovie.getID(), currMovie);
        }
    }

    public static boolean containsID(String id){
        initialize();
        return ourMovies.containsKey(id);
    }

    public static int getYear(String id) throws IOException{

        initialize();
        return ourMovies.get(id).getYear();
    }

    public static String getGenres(String id) throws IOException{
        initialize();
        return ourMovies.get(id).getGenres();
    }

    public static String getTitle(String id) throws IOException{
        initialize();
        return ourMovies.get(id).getTitle();
    }

    public static Movie getMovie(String id) throws IOException{
        initialize();
        return ourMovies.get(id);
    }

    public static String getPoster(String id) throws IOException{
        initialize();
        return ourMovies.get(id).getPoster();
    }

    public static int getMinutes(String id) throws IOException{
        initialize();
        return ourMovies.get(id).getMinutes();
    }

    public static String getCountry(String id) throws IOException{
        initialize();
        return ourMovies.get(id).getCountry();
    }

    public static String getDirector(String id)throws IOException {
        initialize();
        return ourMovies.get(id).getDirector();
    }

    public static int size() {
        return ourMovies.size();
    }

    // Returns a copy of the movie database where movies satisfy the filter
    public static ArrayList<String> filterBy(Filter f) throws IOException{
        initialize();
        ArrayList<String> list = new ArrayList<String>();
        for(String id : ourMovies.keySet()) {
            if (f.satisfies(id)) {
                list.add(id);
            }
        }
        return list;
    }

}
