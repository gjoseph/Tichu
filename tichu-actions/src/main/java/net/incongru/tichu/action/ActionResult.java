package net.incongru.tichu.action;

import net.incongru.tichu.model.Play;

public interface ActionResult {
    // Acting player should get a different log than everyone else
    // so this object can't be _exactly_ what we send back over the wire, filter it according to audience
//        String publicLog(); // i18n? maybe use sthg like https://github.com/kohsuke/localizer

//        String actorLog(); // i18n?


    interface Success extends ActionResult {
        // maybe return some form of GameView (updated after action)
    }

    interface Error extends ActionResult {
        String error();// TODO message, i18n, etc
    }

    class AbstractPlayResult implements ActionResult {
        protected Play.PlayResult playResult;

        AbstractPlayResult(Play.PlayResult playResult) {
            this.playResult = playResult;
        }

        public Play.PlayResult playResult() {
            return playResult;
        }

    }

    class SuccessPlayResult extends AbstractPlayResult implements Success {
        public SuccessPlayResult(Play.PlayResult playResult) {
            super(playResult);
        }
    }

    class ErrorPlayResult extends AbstractPlayResult implements Error {
        public ErrorPlayResult(Play.PlayResult playResult) {
            super(playResult);
        }

        @Override
        public String error() {
            return playResult().message();
        }
    }
}
