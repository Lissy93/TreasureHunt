package net.as93.treasurehunt.utils.calls;

import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.PostReqReachLocation;

public class SubmitReachLocation implements ControllerThatMakesARequest {

    IReturnResponseCode rrc;

    public SubmitReachLocation(IReturnResponseCode rrc, String huntName, String userName, String locationName, String dateTime) {
        this.rrc = rrc;
        PostReqReachLocation rl = new PostReqReachLocation(this, huntName, userName, locationName, dateTime);
        rl.execute();
    }

    @Override
    public void thereAreResults(Object results) {
        rrc.requestComplete(Integer.parseInt((String)results));
    }
}
