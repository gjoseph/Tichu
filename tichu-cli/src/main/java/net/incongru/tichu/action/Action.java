package net.incongru.tichu.action;

import net.incongru.tichu.simu.SimulatedGameContext;

public interface Action {
    // type ?
    // description? (would be useful for a log in the client/ui for example)
    Result exec(SimulatedGameContext ctx);

    enum ActionType {
        init, join, isReady, cheatDeal,
        play, pass
    }

    interface Result {
        // Acting player should get a different log than everyone else
        // so this object can't be _exactly_ what we send back over the wire, filter it according to audience
//        String publicLog(); // i18n? maybe use sthg like https://github.com/kohsuke/localizer

//        String actorLog(); // i18n?
    }

    interface Success extends Result {
        // maybe return some form of GameView (updated after action)
    }

    interface InvalidPlay extends Result {
    }

    interface Error extends Result {
    }

}
