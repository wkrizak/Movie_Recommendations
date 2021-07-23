/*
 * RaterDatabase stores reviews for a number of movie critics
 * New critics can be added to allow for storage of user reviews
 */

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import org.apache.commons.csv.*;

public class RaterDatabase {
    private static HashMap<String,Rater> ourRaters;

    private static void initialize() {
        // this method is only called from addRatings
        if (ourRaters == null) {
            ourRaters = new HashMap<String,Rater>();
        }
    }

    public static void initialize(String filename) throws IOException{
        if (ourRaters == null) {
            ourRaters= new HashMap<String,Rater>();
            addRatings("src\\Data\\" + filename);
        }
    }

    public static void addRatings(String filename) throws IOException {
        initialize();
        Reader reader = new FileReader(filename);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());
        for(CSVRecord rec : parser) {
            String id = rec.get("rater_id");
            String item = rec.get("movie_id");
            String rating = rec.get("rating");
            addRaterRating(id,item,Double.parseDouble(rating));
        }
    }

    public static void addRaterRating(String raterID, String movieID, double rating) {
        initialize();
        Rater rater =  null;
        if (ourRaters.containsKey(raterID)) {
            rater = ourRaters.get(raterID);
        }
        else {
            rater = new EfficientRater(raterID);
        }
        rater.addRating(movieID,rating);
        ourRaters.put(raterID, rater);
    }

    public static Rater getRater(String id) {
        initialize();
        return ourRaters.get(id);
    }

    public static ArrayList<Rater> getRaters() {
        initialize();
        ArrayList<Rater> list = new ArrayList<Rater>(ourRaters.values());

        return list;
    }

    public static int size() {
        return ourRaters.size();
    }

    public static boolean containsRater(String id){
        initialize();
        return ourRaters.containsKey(id);
    }

}
