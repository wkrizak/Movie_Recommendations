/*
 * EfficientRater class represents a movie critic
 * Object can store ratings for multiple movies
 * Used in conjunction with RaterDatabase to easily store/retrieve data for a number of critics
 */

import java.util.ArrayList;
import java.util.HashMap;

public class EfficientRater implements Rater {
    private String myID;
    private HashMap<String, Rating> myRatings;

    public EfficientRater(String id) {
        myID = id;
        myRatings = new HashMap<String, Rating>();
    }

    public void addRating(String item, double rating) {
        myRatings.put(item, new Rating(item,rating));
    }

    public boolean hasRating(String item) {
        if (myRatings.containsKey(item)){
            return true;
        }
        return false;
    }

    public String getID() {
        return myID;
    }

    public double getRating(String item) {
        double value = -1;
        if (hasRating(item)) {
            value = myRatings.get(item).getValue();
        }
        return value;
    }

    public int numRatings() {
        return myRatings.size();
    }

    public ArrayList<String> getItemsRated() {
        ArrayList<String> list = new ArrayList<String>();
        for(String movieID : myRatings.keySet()){
            list.add(myRatings.get(movieID).getItem());
        }
        return list;
    }
}
