package net.as93.treasurehunt.utils;

import net.as93.treasurehunt.controllers.fragments.HuntSummaryFragment;
import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.GetReqPlayersOnHunt;


public class FetchRegisteredUsers implements ControllerThatMakesARequest {

    HuntSummaryFragment hsf;

    public FetchRegisteredUsers(String huntName, HuntSummaryFragment hsf) {
        this.hsf = hsf;
        GetReqPlayersOnHunt playersOnHuntRequest = new GetReqPlayersOnHunt(this, huntName);
        playersOnHuntRequest.execute();
    }

    @Override
    public void thereAreResults(Object results) {
        hsf.updatePlayerRegistered(results);
    }
}
