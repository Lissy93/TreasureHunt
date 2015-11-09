package net.as93.treasurehunt.utils.calls;

import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.GetReqFetchLegs;

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
