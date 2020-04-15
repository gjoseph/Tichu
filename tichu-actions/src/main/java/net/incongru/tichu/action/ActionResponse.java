package net.incongru.tichu.action;

import com.fasterxml.jackson.annotation.JsonGetter;
import net.incongru.tichu.model.UserId;

import javax.annotation.Nonnull;
import java.util.Objects;

public interface ActionResponse<R extends ActionResponse.Result> {
    // Acting player should get a different log than everyone else
    // so this object can't be _exactly_ what we send back over the wire, filter it according to audience
//        String publicLog(); // i18n? maybe use sthg like https://github.com/kohsuke/localizer

//        String actorLog(); // i18n?

    // TODO a generic Fail response? or is that sthg we'd do in a try/catch block elsewhere
    @JsonGetter
    UserId actor();

    @JsonGetter
    Action.ActionType forAction();

    @JsonGetter
    R result();

    @JsonGetter
    Message message();

    interface Result {
        default boolean isSuccessful() {
            return true;
        }
    }

    static class Message {
        private final String message;

        public Message(@Nonnull String message) {
            this.message = message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Message message1 = (Message) o;
            return Objects.equals(message, message1.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(message);
        }

        @Override
        public String toString() {
            return "Message{" +
                   "message='" + message + '\'' +
                   '}';
        }
    }

}
