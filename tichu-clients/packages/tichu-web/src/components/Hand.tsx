import React, { FC } from "react";
import { Card } from "tichu-client-ts-lib";
import { CardSet } from "./CardSet";

const SendButton: FC<{
  enabled: boolean;
  cardCount: number;
  handleClick: () => void;
}> = ({ enabled, cardCount, handleClick }) => {
  const text = cardCount > 0 ? "Send" : "Pass!";
  return (
    <button disabled={!enabled} onClick={handleClick}>
      {text}
    </button>
  );
};

export type SelectableCard = Card & { selected: boolean };
type CardsToSelectableCards = (cards: Card[]) => SelectableCard[];
type SendCardsFunction = (cards: Card[]) => void;

export const Hand: FC<{ sendCards: SendCardsFunction; cardsInHand: Card[] }> = (
  props,
) => {
  const allUnselected: CardsToSelectableCards = (cards: Card[]) => {
    return cards.map((c) => {
      return { ...c, selected: false };
    });
  };

  const [cards, setCards] = React.useState(allUnselected(props.cardsInHand));
  // register dependency to props.cardsInHand to ensure re-render when it changes
  React.useEffect(() => {
    setCards(allUnselected(props.cardsInHand));
  }, [props.cardsInHand]);

  const handleSelect = (card: Card) => {
    setCards(() => {
      return cards.map((c) => {
        if (c.shortName === card.shortName) {
          c.selected = !c.selected;
        }
        return c;
      });
    });
  };

  const selectedCards = () => cards.filter((c) => c.selected);

  const handleSend = () => {
    props.sendCards(selectedCards());
    // TODO set some sort of loading / waiting for response/confirmation state
    // Deselect all
    setCards(allUnselected(props.cardsInHand));
  };

  return (
    <div>
      <CardSet onCardClick={handleSelect} cards={cards} layout="fanned" />
      <SendButton
        handleClick={handleSend}
        enabled
        cardCount={selectedCards().length}
      />
      <p>
        Selected cards:{" "}
        {selectedCards()
          .map((c) => c.name)
          .join(", ")}
      </p>
    </div>
  );
};
