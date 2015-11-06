package net.as93.treasurehunt.utils;

import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.GetReqReachedLocations;

import java.util.ArrayList;

public class GetReachedLocations implements ControllerThatMakesARequest {

    IGetStrings gl;

    public GetReachedLocations(IGetStrings gl, String huntName, String playerName){
        this.gl = gl;
        GetReqReachedLocations rl =  new GetReqReachedLocations(this, huntName, playerName);
        rl.execute();
    }

    @Override
    public void thereAreResults(Object results) {
        gl.stringsReturned((ArrayList<String>) results);
    }
}
