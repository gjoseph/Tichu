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

type SendCardsFunction = (cards: Card[]) => void;
export const Hand: FC<{ sendCards: SendCardsFunction; cardsInHand: Card[] }> = (
  props
) => {
  const [selectedCards, setSelectedCards] = React.useState(new Array<Card>());

  // should selected cards be in state?

  const handleSelect = (isSelecting: boolean, card: Card) => {
    setSelectedCards((selectedCards) =>
      isSelecting
        ? selectedCards.concat(card)
        : selectedCards.filter((c) => c !== card)
    );
  };

  const handleSend = () => {
    console.log(
      "SENDING: ",
      selectedCards.map((c) => c.shortName),
      "to",
      props.sendCards
    );
    props.sendCards(selectedCards);
    console.log("SENT");
    setSelectedCards([]);
  };

  return (
    <div>
      <CardSet
        handleSelect={handleSelect}
        cards={props.cardsInHand}
        style="fanned"
      <SendButton
        handleClick={handleSend}
        enabled
        cardCount={selectedCards.length}
      />
      <p>Selected cards: {selectedCards.map((c) => c.name).join(", ")}</p>
    </div>
  );
};
