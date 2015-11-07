package net.as93.treasurehunt.utils.apiRequests;

/**
 * Manages all API End Points in one place
 */
public class APIEndPoints {

    // The base URL constant for forming the end points
    private final String BASE_URL =
            "http://sots.brookes.ac.uk/~p0073862/services/hunt";


    /**
     * Forms URL String for save hunts end point
     * @return String URL
     */
    protected final String getUrlForSaveHunts(){
        return BASE_URL+"/createhunt";
    }


    /**
     * Forms URL String for save hunts end point
     * @return String URL
     */
    protected final String getUrlForReachLocation(){
        return BASE_URL+"/reachlocation";
    }


    /**
     * Forms URL String for save location or leg to a hunt
     * @return String URL
     */
    protected final String getUrlForSaveLocation(){
        return BASE_URL+"/addlocation";
    }


    /**
     * Forms URL String for fetch all hunts end point
     * @return String URL
     */
    protected final String getUrlForFetchingAllHunts(){
        return BASE_URL+"/hunts";
    }


    /**
     * Forms URL String for fetch all hunts end point
     * @return String URL
     */
    protected final String getUrlForFetchingPlayersOnHunt(String huntName){
        return BASE_URL+"/players/"+huntName;
    }


    /**
     * Forms URL String for fetch all legs for given hunt end point
     * @return String URL
     */
    protected final String getUrlForFetchingLegsOfHunt(String huntName){
        return BASE_URL+"/locations/"+huntName;
    }


    /**
     * Forms URL String for fetch all legs for given hunt end point
     * @return String URL
     */
    protected final String getUrlForFetchingCompletedLegsOfHunt(String huntName, String playerName){
        return BASE_URL+"/reached/"+huntName+"/"+playerName;
    }


    /**
     * Forms URL String for fetch all legs for given hunt end point
     * @return String URL
     */
    protected final String getUrlForRegisteringPlayer(){
        return BASE_URL+"/starthunt";
    }



}
