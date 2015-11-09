package net.as93.treasurehunt.utils.calls;

import net.as93.treasurehunt.controllers.fragments.HuntSummaryFragment;
import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.PostReqRegisterPlayer;

/**
 * Created by Alicia on 06/11/2015.
 */
public class RegisterUserOnHunt implements ControllerThatMakesARequest {

    HuntSummaryFragment hsf;

    public RegisterUserOnHunt(String username, String huntName, HuntSummaryFragment hsf) {
        this.hsf = hsf;
        PostReqRegisterPlayer registerPlayerRequest = new PostReqRegisterPlayer(username, huntName, this);
        registerPlayerRequest.execute();
    }

    @Override
    public void thereAreResults(Object results) {
        hsf.userRegisterRequestComplete((String) results);
    }
}
