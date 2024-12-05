package net.incongru.tichu.action;

import java.util.Objects;
import javax.annotation.Nonnull;
import net.incongru.tichu.model.UserId;

public interface ActionResponse<R extends ActionResponse.Result> {
    // Acting player should get a different log than everyone else
    // so this object can't be _exactly_ what we send back over the wire, filter it according to audience
    //        String publicLog(); // i18n? maybe use sthg like https://github.com/kohsuke/localizer

    //        String actorLog(); // i18n?

    // TODO a generic Fail response? or is that sthg we'd do in a try/catch block elsewhere

    UserId actor();

    Action.ActionType forAction();

    R result();

    Message message();

    interface Result {
        default boolean isSuccessful() {
            return true;
        }
    }

    static class Message {

        private final String text;

        public Message(@Nonnull String text) {
            this.text = text;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Message message1 = (Message) o;
            return Objects.equals(text, message1.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(text);
        }

        @Override
        public String toString() {
            return "Message{" + "message='" + text + '\'' + '}';
        }
    }
}
