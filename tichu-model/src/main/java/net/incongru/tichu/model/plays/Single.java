package net.incongru.tichu.model.plays;

import static net.incongru.tichu.model.Card.CardSpecials.Dog;
import static net.incongru.tichu.model.Card.CardSpecials.Dragon;
import static net.incongru.tichu.model.Card.CardSpecials.MahJong;
import static net.incongru.tichu.model.Card.CardSpecials.Phoenix;

import com.google.common.base.Preconditions;
import java.util.Set;
import net.incongru.tichu.model.Card;

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
        if (card.getVal() == Dog || card.getVal() == MahJong) {
            return false;
        }

        final Card.CardValue otherCard = other.getCard().getVal();
        if (card.getVal() == Phoenix && otherCard != Dragon) {
            return true;
        } else {
            return card.getVal().playOrder() > otherCard.playOrder();
        }
    }

    public static class Factory implements PlayFactory<Single> {

        @Override
        public Single is(Set<Card> cards) {
            if (cards.size() == 1) {
                return new Single(cards);
            }
            return null;
        }
    }
}
