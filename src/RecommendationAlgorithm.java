/*
 * RecommendationAlgorithm includes the methods used to compare critics and identify movie recommendations
 * This class is ultimately used to identify movie recommendations based on user inputs
 * Recommendations are identified by comparing viewing history and reviews for a specified critic to all critics
 * A similarity rating is calculated for each critic as a function of the reviews
 * Movies are then recommended based on a weighted average rating that takes the similarity rating into consideration
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class RecommendationAlgorithm {

    /*
    * For all movies rated by the user,
    * If the movie was rated by the otherRater, then
    *   Normalize the ratings from user and otherRater (rating - 5)
    *   Calculate the dot product between all normalized ratings
    * */
    private double dotProduct(Rater user, Rater otherRater){
        double runningTotal = 0;

        ArrayList<String> myIDs = user.getItemsRated();

        for(String movieID : myIDs){
            double userScore = user.getRating(movieID);
            double otherScore = otherRater.getRating(movieID);
            if (userScore != -1.0 && otherScore != -1.0){
                userScore = userScore - 5;
                otherScore = otherScore - 5;
                runningTotal += userScore * otherScore;
            }
        }
        return runningTotal;
    }


    /*
    * Helper method for getSimilarRatingsFilter
    * Computes the similarity rating for each rater compared to the specified ID
    * The returned ArrayList of type Rating consists of the rater's ID and similarity score
    * Rater's with a negative similarity score are not included in the returned ArrayList
    * */
    private ArrayList<Rating> getSimilarities(String id){
        ArrayList<Rating> similarRaters = new ArrayList<Rating>();
        Rater me = RaterDatabase.getRater(id);

        for (Rater currRater : RaterDatabase.getRaters()){
            if (!currRater.getID().equals(id)) {
                double dotProd = dotProduct(me, currRater);
                if (dotProd > 0) {
                    similarRaters.add(new Rating(currRater.getID(), dotProd));
                }
            }
        }
        Collections.sort(similarRaters, Collections.reverseOrder());
        return similarRaters;
    }


    /*
    * Returns a list of recommended movies sorted by weighted average rating
    * Weighted average review based on the top numSimilarRaters raters
    * A movie must have been reviewed by at least minRaters raters to be recommended
    * The product of a rater's rating for a particular movie and their similarity
    * rating are used to calculate the weighted average rating
    * */
    public ArrayList<Rating> getSimilarRatingsFilter(String id, int numSimilarRaters, int minRaters, Filter f)throws IOException{
        ArrayList<String> movieIDs = MovieDatabase.filterBy(f);
        ArrayList<Rating> ratedMovies = new ArrayList<Rating>();
        ArrayList<Rating> similarRaters = getSimilarities(id);
        Rater me = RaterDatabase.getRater(id);
        ArrayList<String> watchedMovies = me.getItemsRated();

        for (String movieID : movieIDs){
            int raterCount = 0;
            int idx = 0;
            double runningTotal = 0;
            double avg = 0;
            String title = MovieDatabase.getTitle(movieID);
            if (!watchedMovies.contains(movieID)) {
                while (idx < numSimilarRaters) {
                    if (idx >= similarRaters.size()) {
                        break;
                    }
                    Rating similarityScore = similarRaters.get(idx);
                    Rater currRater = RaterDatabase.getRater(similarityScore.getItem());
                    double wtdRating = currRater.getRating(movieID) * similarityScore.getValue();

                    // If the critic has seen the movie, add their rating
                    if (wtdRating >= 0) {
                        runningTotal += wtdRating;
                        raterCount++;
                    }
                    idx++;
                }

                // If there are at least minRaters, calculate and add the weighted avg to ratedMovies
                if (raterCount >= minRaters && raterCount > 0) {
                    avg = runningTotal / raterCount;
                    ratedMovies.add(new Rating(movieID, avg));
                }
            }
        }
        Collections.sort(ratedMovies,Collections.reverseOrder());
        return ratedMovies;
    }


    /*
     * Get all similar ratings (pass true filter)
     */
    public ArrayList<Rating> getSimilarRatings(String id, int numSimilarRaters, int minRaters)throws IOException{
        // Return rating objects of movie IDs and the weighted avg rating
        return getSimilarRatingsFilter(id, numSimilarRaters, minRaters, new TrueFilter());
    }
}
