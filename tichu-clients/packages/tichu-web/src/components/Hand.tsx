import React, { FC } from "react";
import { Card, cardFromName } from "tichu-client-ts-lib";
import { CardSet } from "./CardSet";

const SendButton: FC<{ enabled: boolean; handleClick: () => void }> = ({
  enabled,
  handleClick,
}) => {
  return (
    <button disabled={!enabled} onClick={handleClick}>
      send
    </button>
  );
};

export const Hand: FC = (props) => {
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
      "SEND: ",
      selectedCards.map((c) => c.shortName)
    );
  };

  return (
    <div>
      <CardSet
        handleSelect={handleSelect}
        cards={[cardFromName("*P"), cardFromName("GJ"), cardFromName("K6")]}
      />
      <SendButton handleClick={handleSend} enabled={selectedCards.length > 0} />
      <p>Selected cards: {selectedCards.map((c) => c.name).join(", ")}</p>
    </div>
  );
};
