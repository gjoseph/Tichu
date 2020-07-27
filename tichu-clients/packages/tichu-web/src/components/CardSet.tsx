import React, { FC } from "react";
import { CardBack } from "./CardBack";
import { CardView } from "./CardView";
import { Card } from "tichu-client-ts-lib";
import styles from "./Card.module.css";
import { classes } from "../util";

type CardSize = "small" | "regular";

interface CardSetProps {
  cards: Card[];
  cardSize?: CardSize;
  handleSelect: (isSelected: boolean, card: Card) => void;
}

interface WithPositionProps {
  idx: number;
  size: CardSize;
}

const withPosition = <P extends object>(
  Component: React.ComponentType<P>
): React.FC<P & WithPositionProps> => ({
  idx,
  size,
  ...props
}: WithPositionProps) => {
  const space = idx * (size === "small" ? 2 : 6);
  return (
    <div style={{ transform: `translate(${space}em, 0)` }}>
      <Component size={size} {...(props as P)} />
    </div>
  );
};
const PositionedCardView = withPosition(CardView);
const PositionedCardBack = withPosition(CardBack);

const CardSetContainer: FC<{ cardSize?: CardSize }> = ({
  cardSize = "regular",
  children,
}) => {
  const classNames = classes(
    styles.cardSet,
    cardSize === "small" ? styles.smallSet : styles.regularSet
  );
  return <div className={classNames}>{children}</div>;
};

export const CardSet: FC<CardSetProps> = ({
  cards,
  handleSelect,
  cardSize = "regular",
}) => {
  return (
    <CardSetContainer cardSize={cardSize}>
      {cards.map((card, idx) => (
        <PositionedCardView
          key={idx}
          idx={idx}
          card={card}
          size={cardSize}
          handleSelect={handleSelect}
        />
      ))}
    </CardSetContainer>
  );
};

export const CardBacks: FC<{ count: number; cardSize?: CardSize }> = ({
  count,
  cardSize = "small",
}) => {
  return (
    <CardSetContainer cardSize={cardSize}>
      {Array.from(new Array(count), (ignoreNull, idx) => (
        <PositionedCardBack
          key={idx}
          idx={idx}
          size={cardSize}
        />
      ))}
    </CardSetContainer>
  );
};
