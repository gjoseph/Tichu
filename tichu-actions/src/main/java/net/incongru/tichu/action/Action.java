package net.incongru.tichu.action;

import net.incongru.tichu.model.Play;

public interface Action {
    // type ?
    // description? (would be useful for a log in the client/ui for example)
    Result exec(GameContext ctx);

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

    interface Error extends Result {
        String error();// TODO message, i18n, etc
    }

    class AbstractPlayResult implements Result {
        protected Play.PlayResult playResult;

        AbstractPlayResult(Play.PlayResult playResult) {
            this.playResult = playResult;
        }

        public Play.PlayResult playResult() {
            return playResult;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "{" +
                    "playResult=" + playResult +
                    '}';
        }
    }

    static class SuccessPlayResult extends AbstractPlayResult implements Success {
        public SuccessPlayResult(Play.PlayResult playResult) {
            super(playResult);
        }
    }

    static class ErrorPlayResult extends AbstractPlayResult implements Error {
        public ErrorPlayResult(Play.PlayResult playResult) {
            super(playResult);
        }

        @Override
        public String error() {
            return playResult().message();
        }
    }

}
