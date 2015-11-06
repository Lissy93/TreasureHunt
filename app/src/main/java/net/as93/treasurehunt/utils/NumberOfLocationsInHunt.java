package net.as93.treasurehunt.utils;

import net.as93.treasurehunt.models.Leg;
import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.GetReqFetchLegs;

import java.util.ArrayList;

public class NumberOfLocationsInHunt implements ControllerThatMakesARequest {

    IShowNumberOfLocations snl;

    public NumberOfLocationsInHunt(IShowNumberOfLocations snl, String huntName) {
        this.snl = snl;
        GetReqFetchLegs fetchAllLegs;
        fetchAllLegs = new GetReqFetchLegs(this, huntName);
        fetchAllLegs.execute();
    }

    @Override
    public void thereAreResults(Object results) {
        snl.numberOfLocationsReturned(((ArrayList<Leg>)results).size());
    }
}
