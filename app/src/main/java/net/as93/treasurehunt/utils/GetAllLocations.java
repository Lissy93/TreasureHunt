package net.as93.treasurehunt.utils;

import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.GetReqFetchLegs;
import net.as93.treasurehunt.utils.apiRequests.GetReqReachedLocations;

public class GetAllLocations implements ControllerThatMakesARequest {

    IGetLocations gl;

    public GetAllLocations(IGetLocations grl, String huntName){
        this.gl = grl;
        GetReqFetchLegs rl =  new GetReqFetchLegs(this, huntName);
        rl.execute();
    }

    @Override
    public void thereAreResults(Object results) {
        gl.locationsReturned(results);
    }
}
