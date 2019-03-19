package cs445.a4;

/**
 * This abstract data type is a predictive engine for video ratings in a streaming video system. It
 * stores a set of users, a set of videos, and a set of ratings that users have assigned to videos.
 */
public interface VideoEngine {

    /**
     * The abstract methods below are declared as void methods with no parameters. You need to
     * expand each declaration to specify a return type and parameters, as necessary. You also need
     * to include a detailed comment for each abstract method describing its effect, its return
     * value, any corner cases that the client may need to consider, any exceptions the method may
     * throw (including a description of the circumstances under which this will happen), and so on.
     * You should include enough details that a client could use this data structure without ever
     * being surprised or not knowing what will happen, even though they haven't read the
     * implementation.
     */

    
    /**
     * Adds a new video to the system.
     * <p> If theVideo is not null, then the system is modified so that it 
     * contains theVideo. 
     * <p> If theVideo is null, then the method will throw NullPointerException without
     * modifying the system. 
     * <p> If theVideo already exists in the system, the method will throw
     * IllegalArgumentException without modifying the system.
     * <p> There is no capacity limit. The system is unordered, thus theVideo will
     * be placed in the system at an unknown location if it is being added to the system.
     * 
     * @param theVideo The video to be added to the system
     * @throws NullPointerException If theVideo is null
     * @throws IllegalArgumentException If theVideo already exists in the system
     */
    public void addVideo(Video theVideo) throws NullPointerException, 
                                                IllegalArgumentException;

    
    /**
     * Removes an existing video from the system.
     * <p> If the system contains theVideo, this method will modify the system so that
     * it no longer contains theVideo. All other entries remain unmodified.
     * <p> If the system does not contain theVideo, this method will throw
     * IllegalArgumentException without modifying the system. 
     * <p> If theVideo is null, the method will throw NullPointerException without
     * modifying the system.
     * 
     * @param theVideo The video to be removed from the system
     * @throws IllegalArgumentException If the system does not contain theVideo
     * @throws NullPointerException If theVideo is null
     */
    public  void removeVideo(Video theVideo) throws 
            IllegalArgumentException, NullPointerException;

    
    /**
     * Adds an existing television episode to an existing television series.
     * <p> If neither theTvEpisode nor theTvSeries is null, this method modifies 
     * theTvSeries so that the collection contains theTvEpisode.
     * <p> If either theTvEpisode or theTvSeries is null, then the method throws 
     * NullPointerException without modifying theTvSeries.
     * <p> If theTvEpisode already exists in theTvSeries, then the method throws
     * IllegalArgumentException without modifying theTvSeries.
     * <p> If neither theTvEpisode nor theTvSeries exists in the system, the method
     * throws IllegalArgumentException without modifying theTvSeries.
     * <p> There is no capacity limit to theTvSeries. Since theTvSeries is unordered,  
     * theTvEpisode will be placed in theTvSeries at an unknown location if it is
     * being added to the collection.
     * 
     * @param theTvEpisode The television episode to be added to the series
     * @param theTvSeries The television series that the episode is being added to
     * @throws NullPointerException If either the episode or the series is null
     * @throws IllegalArgumentException If the episode already exists in the series,
     * or if neither the episode nor the series exists in the system
     */
    public void addToSeries(TvEpisode theTvEpisode, TvSeries theTvSeries)
            throws NullPointerException;

    
    /**
     * Removes a television episode from a television series.
     * <p> If theTvSeries contains theTvEpisode, this method will modify theTvSeries
     * so that it no longer contains theTvEpisode. All other entries remain unmodified.
     * <p> If theTvSeries does not contain theTvEpisode, the method will throw
     * IllegalArgumentException without modifying theTvSeries.
     * <p> If either theTvEpisode or theTvSeries is null, then the method throws
     * NullPointerException without modifying theTvSeries.
     * <p> If neither theTvEpisode nor theTvSeries exists in the system, the method
     * throws IllegalArgumentException without modifying theTvSeries.
     * 
     * @param theTvEpisode the television episode to be removed from the television series
     * @param theTvSeries the television series to remove the television episode from
     * @throws IllegalArgumentException If the series does not contain the episode,
     * or if neither the episode nor the series exists in the system
     * @throws NullPointerException if either the episode or the series is null
     */
    public void removeFromSeries(TvEpisode theTvEpisode, TvSeries theTvSeries) 
            throws IllegalArgumentException, NullPointerException;

    
    /**
     * Sets a user's rating for a video, as a number of stars from 1 to 5.
     * <p> If theUser has not yet rated theVideo, then this set stars as theUser's 
     * rating for theVideo. 
     * <p> If theUser has already rated theVideo, the method throws an IllegalArgumentException.
     * <p> If either theUser or theVideo is null, then the method throws
     * NullPointerException.
     * <p> If stars value is not between 1 and 5, then the method throws IllegalArgumentException.
     * 
     * @param theUser The user to give a rating of a video
     * @param theVideo The video which the user is to rate stars
     * @param stars The rating value the user is to give as he/she is rating the video
     * @throws IllegalArgumentException If the user has already rated the video
     *          ,or if the value of the stars is less than 1 or greater than 5
     * @throws NullPointerException If either the user or the video is null
     */
    public void rateVideo(User theUser, Video theVideo, int stars)
            throws IllegalArgumentException, NullPointerException;

    
    /**
     * Clears a user's rating on a video. 
     * <p> If this user has rated this video and the rating has not already been 
     * cleared, then the rating is cleared and the state will appear as if the rating
     * was never made. 
     * <p> If this user has not rated this video, or if the rating has already been
     * cleared, then this method will throw an IllegalArgumentException.
     *
     * @param theUser user whose rating should be cleared
     * @param theVideo video from which the user's rating should be cleared
     * @throws IllegalArgumentException if the user does not currently have a rating on record for
     * the video
     * @throws NullPointerException if either the user or the video is null
     */
    public void clearRating(User theUser, Video theVideo);

    
    /**
     * Predicts the rating a user will assign to a video that they have not yet rated, as a number
     * of stars from 1 to 5.
     * <p> If theUser has not yet rated theVideo, or the rating has been cleared, 
     * then the predictive engine will predict the rating of theVideo for theUser.
     * Then, return the rating value.
     * <p> If theUser has already rated theVideo, the method throws IllegalArgumentException.
     * <p> If theVideo does not exist in the system, the method throws IllegalArgumentException.
     * <p> If theVideo has never been rated by any user, the method throws IllegalArgumentException.
     * <p> If either theUser or theVideo is null, the method throws NullPointerException.
     * 
     * @param theUser The user whose rating of a video should be predicted
     * @param theVideo The video in which the user's rating will be predicted
     * @throws IllegalArgumentException If there already exists the user's rating
     * for the video, if the video does not exist in the system, or if the video has
     * never been rated by any user.
     * @throws NullPointerException If either the user or the video is null
     * @return the predicted rating the user will assign to a video
     */
    public int predictRating(User theUser, Video theVideo) throws
            IllegalArgumentException, NullPointerException;

    
    /**
     * Suggests a video for a user (e.g.,based on their predicted ratings).
     * <p> If theUser is not null, return a video suggestion
     * <p> If theUser is null, the method throws NullPointerException.
     * <p> If theUser has not rated a single video, or has cleared all the ratings,
     * then the method throws IllegalArgumentException.
     * <p> If the system does not contain any video, the method throws IllegalStateException.
     * <p> If predicting a rating of theVideo throws an exception, this method will
     * throw IllegalStateException.

     * 
     * @param theUser The user the video suggestion is for
     * @throws IllegalStateException If the system contains 0 videos, or if predicting
     * rating of the video failed
     * @throws IllegalArgumentException If the user has not rated any video or has
     * cleared all the ratings
     * @throws NullPointerException If the user is null
     * @return a video suggestion
     */
    public  Video suggestVideo(User theUser) 
            throws IllegalStateException, NullPointerException;


}

