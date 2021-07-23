/*
 * Main class that is used to create the GUI.
 * Panel 1 - displays movies for the user to rate using a slider.
 *      User can choose to rate more movies, apply filters to recommendations, or see all recommendations.
 * Panel 2 - displays filter options for the user with comboboxes.
 *      User can select a genre and/or the release year.
 *      User can choose to rate more movies or view recommendations.
 * Panel 3 - displays movie recommendations for the user.
 *      This method relies on the RecommendationAlgorithm class to identify recommendations
 *      based on the user's reviews.
 *      User can choose to rate more movies.
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class RecommendationRunner {

    public static Color BACKGROUND = Color.decode("#EDEDEE");

    public static void main(String[] args){
        try {
            getUserRatings();
        }
        catch (Exception e){
            System.out.println("Error: " + e);
            System.exit(1);
        }
    }

    /*
     * Primary method used to set important variables
     * Certain variables are initialized here for ease of reference
     * */
    public static void getUserRatings() throws IOException {
        String moviefile = "ratedmoviesfull.csv";
        String raterfile = "ratings.csv";
        MovieDatabase.initialize(moviefile);
        RaterDatabase.initialize(raterfile);

        String userID = setNewUserID();
        String titleBar = "Movie Recommender";
        setUIFont(new FontUIResource("Helvetica", Font.PLAIN, 15));
        UIManager.put("ScrollBar.thumb", new ColorUIResource(Color.lightGray));
        UIManager.put("ScrollBar.thumbHighlight", new ColorUIResource(Color.darkGray));
        UIManager.put("ScrollBar.thumbDarkShadow", new ColorUIResource(Color.darkGray));
        UIManager.put("ScrollBar.highlight", new ColorUIResource(Color.darkGray));
        UIManager.put("ScrollBar.trackHighlight", new ColorUIResource(Color.darkGray));
        UIManager.put("ComboBox.background", Color.white);
        UIManager.put("ComboBox.selectionForeground", Color.decode("#4f769c"));
        UIManager.put("ComboBox.selectionBackground", Color.white);
        UIManager.put("ComboBox.buttonDarkShadow", Color.gray);
        UIManager.put("ComboBox.buttonBackground", Color.white);
        UIManager.put("ComboBox.buttonHighlight", Color.white);
        UIManager.put("ComboBox.buttonShadow", Color.white);

        final JFrame frame = new JFrame(titleBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 1000);

        // Generate buttons and sliders needed for displayMovieToRate function
        // Pushed button declaration outside of displayMoviesToRate to avoid unnecessary button creation if user rates several pages of movies
        ArrayList<JRadioButton> myButtons = new ArrayList<JRadioButton>();
        ArrayList<JSlider> mySliders = new ArrayList<JSlider>();
        for (int i = 0; i < 10; i++) {
            myButtons.add(generateRadioButton());
            mySliders.add(generateRatingSlider(myButtons.get(i)));
        }

        displayMoviesToRate(frame, userID, myButtons, mySliders);
    }

    /*
     * Panel 1 - Displays movies for the user to rate with sliders
     * User has three options:
     *      Rate more movies - method is called again to display a new set of movies.
     *          Method currently does not check if movie has already been displayed to user.
     *      Apply filters - changes the panel to show filters that the user can add
     *      See recommended movies - changes the panel to show top recommendations
     * Features to implement
     *      Method currently does not check if movie has already been displayed to user.
     *      Add movie poster for each movie
     * */
    private static void displayMoviesToRate(JFrame frame, String userID, ArrayList<JRadioButton> myButtons, ArrayList<JSlider> mySliders) throws IOException {

        ArrayList<String> movieIDs = getItemsToRate();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11, 4));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(BACKGROUND);

        // Reset buttons to default and add movie title + buttons to panel
        // Added html tag to wrap text
        resetButtons(myButtons, mySliders);
        for (int i = 0; i < movieIDs.size(); i++) {
            String movieTitle = MovieDatabase.getTitle(movieIDs.get(i));
            JLabel movieLabel = new JLabel();
            movieLabel.setText("<html>" + movieTitle + "</html>");
            JLabel yearLabel = new JLabel(String.valueOf(MovieDatabase.getYear(movieIDs.get(i))), SwingConstants.CENTER);

            if (i % 2 != 0){
                movieLabel.setBackground(Color.white);
                yearLabel.setBackground(Color.white);
                myButtons.get(i).setBackground(Color.white);
                mySliders.get(i).setBackground(Color.white);
                movieLabel.setOpaque(true);
                yearLabel.setOpaque(true);
                myButtons.get(i).setOpaque(true);
                mySliders.get(i).setOpaque(true);
            }

            panel.add(movieLabel);
            panel.add(yearLabel);
            panel.add(myButtons.get(i));
            panel.add(mySliders.get(i));
        }

        // Create buttons to allow user to move to new panel
        // User can either rate more movies, add filters, or see recommendations
        // Added html and div tags to text to allow text wrap and center alignment
        RoundButton moreRatings = new RoundButton();
        String text = "Rate more movies";
        moreRatings.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
        moreRatings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordUserRatings(myButtons, mySliders, movieIDs, userID);
                try {
                    displayMoviesToRate(frame, userID, myButtons, mySliders);
                } catch (IOException e2) {
                    System.out.println("Error: " + e2);
                }
            }
        });

        RoundButton seeFilters = new RoundButton();
        text = "Apply filters";
        seeFilters.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
        seeFilters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordUserRatings(myButtons, mySliders, movieIDs, userID);
                try {
                    getFilters(frame, userID, myButtons, mySliders);
                }
                catch (IOException e2){
                    System.out.println("Error: " + e2);
                }
            }
        });

        RoundButton seeRecommendations = new RoundButton();
        text = "See recommended";
        seeRecommendations.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
        TrueFilter fTrue = new TrueFilter();
        seeRecommendations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordUserRatings(myButtons, mySliders, movieIDs, userID);
                displayRecommendations(frame, userID, myButtons, mySliders, fTrue);
            }
        });

        panel.add(moreRatings);
        panel.add(new JLabel());
        panel.add(seeFilters);
        panel.add(seeRecommendations);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.validate();
        frame.setVisible(true);
    }

    /*
     * Panel 2 - Displays potential filters for user to add
     * Filter options:
     *      Genre - can only select from list in combo box
     *      Release year - can select min and max release years from combo box
     * User can also go back and rate more movies
     * */
    private static void getFilters(JFrame frame, String userID, ArrayList<JRadioButton> myButtons, ArrayList<JSlider> mySliders) throws IOException {

        ArrayList<String> movieIDs = getItemsToRate();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(BACKGROUND);

        // Add genre combobox for user to select genre filter
        JLabel genreLabel = new JLabel();
        String text = "Genres";
        genreLabel.setText("<html><div style='vertical-align: bottom;'>" + text + "</div></html>");
        String[] genreList = {"All genres", "Action", "Adventure", "Animation", "Comedy", "Documentary", "Drama", "Horror", "Romance", "Sci-Fi", "Thriller"};
        RoundComboBox genreBox = new RoundComboBox(genreList);
        genreBox.setBorder(new RoundedBorder());
        genreBox.setSelectedIndex(0);
        genreBox.setBackground(Color.white);

        JLabel emptyLabel1 = new JLabel();

        panel.add(genreLabel);
        panel.add(emptyLabel1);
        panel.add(genreBox);
        // Adding empty labels to fill gridLayout
        for (int i = 0; i < 5; i++){
            JLabel emptyLabel2 = new JLabel();
            panel.add(emptyLabel2);
        }

        // Add release year combo boxes
        JLabel yearLabelMin = new JLabel();
        yearLabelMin.setText("Earliest release date");
        String[] yearListMin = {"N/A", "1960", "1970", "1980", "1990", "2000", "2010", "2015"};
        RoundComboBox yearBoxMin = new RoundComboBox(yearListMin);
        yearBoxMin.setBorder(new RoundedBorder());
        yearBoxMin.setSelectedIndex(0);
        yearBoxMin.setBackground(Color.white);

        JLabel yearLabelMax = new JLabel();
        yearLabelMax.setText("Latest release date");
        String[] yearListMax = {"N/A", "2015", "2010", "2000", "1990", "1980", "1970", "1960"};
        RoundComboBox yearBoxMax = new RoundComboBox(yearListMax);
        yearBoxMax.setBorder(new RoundedBorder());
        yearBoxMax.setSelectedIndex(0);
        yearBoxMax.setBackground(Color.white);

        panel.add(yearLabelMin);
        panel.add(yearLabelMax);
        panel.add(yearBoxMin);
        panel.add(yearBoxMax);
        // Adding empty labels to fill gridLayout
        for (int i = 0; i < 10; i++){
            JLabel emptyLabel3 = new JLabel();
            panel.add(emptyLabel3);
        }

        // Create buttons to allow user to move to new panel
        // User can either rate more movies or see recommendations
        RoundButton moreRatings = new RoundButton();
        moreRatings.setText("Back - Rate more movies");
        moreRatings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordUserRatings(myButtons, mySliders, movieIDs, userID);
                try {
                    displayMoviesToRate(frame, userID, myButtons, mySliders);
                } catch (IOException e2) {
                    System.out.println("Error: " + e2);
                }
            }
        });
        RoundButton seeRecommendations = new RoundButton();
        seeRecommendations.setText("See recommended");
        seeRecommendations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String genre = genreList[genreBox.getSelectedIndex()];
                String yearMin = yearListMin[yearBoxMin.getSelectedIndex()];
                String yearMax = yearListMax[yearBoxMax.getSelectedIndex()];
                AllFilters fAll = getSelectedFilters(genre, yearMin, yearMax);
                displayRecommendations(frame, userID, myButtons, mySliders, fAll);
            }
        });

        panel.add(moreRatings);
        panel.add(seeRecommendations);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.validate();
        frame.setVisible(true);
    }

    /*
     * Panel 3 - Display list of recommended movies for a specified ID based on their history
     * Movies are sorted based on recommendation from highest to lowest
     * Features to implement
     *      Where no movies are recommended, display top movies overall
     */
    public static void displayRecommendations(JFrame frame, String userID, ArrayList<JRadioButton> myButtons, ArrayList<JSlider> mySliders, Filter f) {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 3));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(BACKGROUND);
        ArrayList<Rating> recommendedMovies = new ArrayList<Rating>();

        try {

            // numSimilarRaters represents the number of similar raters that are considered for recommendations
            // minRaters represents the minimum number of similar raters that must have watched a movie for it to be recommended
            //      This is set to 1 so that the user should almost always receive some recommendations
            RecommendationAlgorithm ra = new RecommendationAlgorithm();
            int numSimilarRaters = 15;
            int minRaters = 1;
            recommendedMovies = ra.getSimilarRatingsFilter(userID, numSimilarRaters, minRaters, f);
            int i = 0;

            // Set headers
            JLabel titleHeader = new JLabel("Movie Title", SwingConstants.CENTER);
            titleHeader.setFont(new Font("Helvetica", Font.BOLD, 15));
            titleHeader.setOpaque(true);

            JLabel genreHeader = new JLabel("Genre", SwingConstants.CENTER);
            genreHeader.setFont(new Font("Helvetica", Font.BOLD, 15));
            genreHeader.setOpaque(true);

            JLabel yearHeader = new JLabel("Year Released", SwingConstants.CENTER);
            yearHeader.setFont(new Font("Helvetica", Font.BOLD, 15));
            yearHeader.setOpaque(true);

            panel.add(titleHeader);
            panel.add(genreHeader);
            panel.add(yearHeader);

            // Get movie details and add to panel
            for (Rating currMovie : recommendedMovies) {
                if (i >= 10) {
                    break;
                }

                String currID = currMovie.getItem();

                JLabel title = new JLabel("", SwingConstants.CENTER);
                String text = MovieDatabase.getTitle(currID);
                title.setText("<html><div style='text-align: center;'>" + text + "</div></html>");

                JLabel genre = new JLabel("", SwingConstants.CENTER);
                text = MovieDatabase.getGenres(currID);
                genre.setText("<html><div style='text-align: center;'>" + text + "</div></html>");

                JLabel yearReleased = new JLabel("", SwingConstants.CENTER);
                text = String.valueOf(MovieDatabase.getYear(currID));
                yearReleased.setText("<html><div style='text-align: center;'>" + text + "</div></html>");

                // Color background of every other row
                if (i % 2 == 0){
                    title.setBackground(Color.white);
                    genre.setBackground(Color.white);
                    yearReleased.setBackground(Color.white);
                    title.setOpaque(true);
                    genre.setOpaque(true);
                    yearReleased.setOpaque(true);
                }

                panel.add(title);
                panel.add(genre);
                panel.add(yearReleased);

                i++;
            }
            // Creates blank fields so that table is organized correctly when fewer than 10 movies are recommended
            while (i < 10) {
                JLabel filler = new JLabel();
                JLabel filler2 = new JLabel();
                JLabel filler3 = new JLabel();
                panel.add(filler);
                panel.add(filler2);
                panel.add(filler3);
                i++;
            }

            // Include button to allow user to go back and rate additional movies
            RoundButton moreRatings = new RoundButton();
            String text = "Back - Rate more movies";
            moreRatings.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
            moreRatings.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        displayMoviesToRate(frame, userID, myButtons, mySliders);
                    } catch (IOException e2) {
                        System.out.println("Error: " + e2);
                    }
                }
            });
            panel.add(moreRatings);

        } catch (Exception e) {
            // Exception is thrown when user does not rate any movies, but clicks to view recommendations
            // Add button to allow user to go back and rate movies
            RoundButton moreRatings = new RoundButton();
            String text = "Please go back to rate movies";
            moreRatings.setText("<html><div style='text-align: center;'>" + text + "</div></html>");
            moreRatings.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        displayMoviesToRate(frame, userID, myButtons, mySliders);
                    } catch (IOException e2) {
                        System.out.println("Error: " + e2);
                    }
                }
            });
            panel.add(moreRatings);
        }

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.validate();
        frame.setVisible(true);
    }


    /*
     * Return ArrayList of movie IDs for the user to rate
     * 10 movie IDs are randomly selected from the MovieDatabase
     * Features to implement:
     *      Display top movies by average rating instead of at random
     *      Add parameter for movies that have already been displayed so that a movie is only displayed once
     * */
    private static ArrayList<String> getItemsToRate() {
        ArrayList<String> movieIDs = new ArrayList<String>();
        ArrayList<Integer> indexesUsed = new ArrayList<Integer>();
        Random rand = new Random(System.currentTimeMillis());

        TrueFilter tf = new TrueFilter();
        try {
            ArrayList<String> allMovies2015 = MovieDatabase.filterBy(tf);
            int size = allMovies2015.size();
            for (int i = 0; i < 10; i++) {
                int idx = rand.nextInt(size);
                while (indexesUsed.contains(idx)) {
                    idx = rand.nextInt(size);
                }
                String currMovie = allMovies2015.get(idx);
                movieIDs.add(currMovie);
                indexesUsed.add(idx);
            }
        } catch (IOException e) {
            System.out.println("Error: IOException in getItemsToRate");
        }
        return movieIDs;
    }


    /*
     * Helper method to create a filter based on user inputs
     * Will only create a genre filter and/or a year filter
     * Add a true filter at the beginning so that if no filter is selected by the user, all movies will be considered
     * */
    private static AllFilters getSelectedFilters(String genre, String yearMin, String yearMax){
        AllFilters fAll = new AllFilters();
        TrueFilter fTrue = new TrueFilter();
        fAll.addFilter(fTrue);

        if (!genre.equals("All genres")){
            GenreFilter fGenre = new GenreFilter(genre);
            fAll.addFilter(fGenre);
        }

        if (!yearMin.equals("N/A")){
            if (!yearMax.equals("N/A")){
                YearFilter fYear = new YearFilter(Integer.parseInt(yearMin), Integer.parseInt(yearMax));
                fAll.addFilter(fYear);
            }
            else {
                YearFilter fYear = new YearFilter(Integer.parseInt(yearMin), Year.now().getValue());
                fAll.addFilter(fYear);
            }
        }
        else if (!yearMax.equals("N/A")){
            YearFilter fYear = new YearFilter(1900, Integer.parseInt(yearMax));
            fAll.addFilter(fYear);
        }
        return fAll;
    }


    /*
     * For each movie displayed, add user rating to RaterDatabase
     * Method ignores movies where "No rating" has been selected (default)
     * */
    private static void recordUserRatings(ArrayList<JRadioButton> myButtons, ArrayList<JSlider> mySliders, ArrayList<String> movieIDs, String userID) {
        for (int i = 0; i < myButtons.size(); i++) {
            JSlider slider = mySliders.get(i);
            JRadioButton rbutton = myButtons.get(i);
            if (!rbutton.isSelected()) {
                RaterDatabase.addRaterRating(userID, movieIDs.get(i), slider.getValue());
            }
        }
    }


    /*
     * Create a single radio button to indicate if user has/has not seen movie
     * */
    private static JRadioButton generateRadioButton() {
        JRadioButton rbutton = new JRadioButton();
        rbutton.setText("No rating");
        rbutton.setSelected(true);
        rbutton.setOpaque(false);
        return rbutton;
    }


    /*
     * Create a single JSlider for user to input rating
     * */
    private static JSlider generateRatingSlider(JRadioButton button) {

        JSlider ratingSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);

        // Create and set position of labels
        Hashtable<Integer, JLabel> sliderPos = new Hashtable<Integer, JLabel>();
        sliderPos.put(0, new JLabel("0"));
        sliderPos.put(2, new JLabel("2"));
        sliderPos.put(4, new JLabel("4"));
        sliderPos.put(6, new JLabel("6"));
        sliderPos.put(8, new JLabel("8"));
        sliderPos.put(10, new JLabel("10"));
        ratingSlider.setLabelTable(sliderPos);
        ratingSlider.setPaintLabels(true);
        ratingSlider.setOpaque(false);

        // Adjusts corresponding radio button to false when user changes the slider
        ratingSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                button.setSelected(false);
            }
        });

        return ratingSlider;
    }


    /*
     * Reset list of radio buttons and sliders to default values
     * Note - Need to reset value of sliders first, or else the changelistener is triggered
     * */
    private static void resetButtons(ArrayList<JRadioButton> myButtons, ArrayList<JSlider> mySliders) {
        for (int i = 0; i < myButtons.size(); i++) {
            mySliders.get(i).setValue(0);
            myButtons.get(i).setSelected(true);
        }
    }


    /*
     * Sets and returns the userID to a value that is not found in the RaterDatabase
     * */
    private static String setNewUserID() {
        String userID = "";
        try {
            userID = String.valueOf(RaterDatabase.size() + 1);
            int i = 2;
            while (RaterDatabase.containsRater(userID)) {
                userID = String.valueOf(RaterDatabase.size() + i);
                i++;
            }
        } catch (Exception e) {
            System.out.println("Error: Exception found in setUserID. " + e);
        }
        return userID;
    }

    /*
     * Sets default font for all components by setting font in UIManager for all keys
     */
    private static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }
}