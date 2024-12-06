package net.incongru.tichu.model.plays;

import static net.incongru.tichu.model.card.CardSpecials.Dog;
import static net.incongru.tichu.model.card.CardSpecials.Dragon;
import static net.incongru.tichu.model.card.CardSpecials.MahJong;
import static net.incongru.tichu.model.card.CardSpecials.Phoenix;

import com.google.common.base.Preconditions;
import java.util.Set;
import net.incongru.tichu.model.card.Card;
import net.incongru.tichu.model.card.CardValue;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 *
 */
public class Single extends AbstractPlay<Single> {

    private final Card card;

    private Single(Set<Card> cards) {
        super(cards);
        Preconditions.checkArgument(
            cards.size() == 1,
            "Should pass a single Card"
        );
        this.card = cards.iterator().next();
    }

    @Override
    public String describe() {
        return name() + " " + card.name();
    }

    private Card getCard() {
        return card;
    }

    @Override
    protected boolean canBePlayedAfterTypeSafe(Single other) {
        if (card.val() == Dog || card.val() == MahJong) {
            return false;
        }

        final CardValue otherCard = other.getCard().val();
        if (card.val() == Phoenix && otherCard != Dragon) {
            return true;
        } else {
            return card.val().playOrder() > otherCard.playOrder();
        }
    }

    public static class Factory implements PlayFactory<Single> {

        @Override
        public @Nullable Single is(Set<Card> cards) {
            if (cards.size() == 1) {
                return new Single(cards);
            }
            return null;
        }
    }
}
