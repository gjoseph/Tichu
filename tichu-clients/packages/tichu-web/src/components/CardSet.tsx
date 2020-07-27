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
  totalCount: number;
  size: CardSize;
}

const withPosition = <P extends object>(
  Component: React.ComponentType<P>
): React.FC<P & WithPositionProps> => ({
  idx,
  totalCount,
  size,
  ...props
}: WithPositionProps) => {
  // Not sure how best to name this -- this is the "centered index" where 0 is the middle element of the list
  const midIdx = idx - (totalCount - 1) / 2;
  const translateX = idx * (size === "small" ? 2 : 6);
  const translateY = Math.abs(midIdx * midIdx * (size === "small" ? 0.1 : 0.3));
  const rotation = midIdx * (size === "small" ? 2 : 6);
  const styles = {
    position: "absolute" as "absolute",
    transformOrigin: "center",
    transform: `translate(${translateX}em, ${translateY}em) rotate(${rotation}deg)`,
  };
  return (
    <div style={styles}>
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
          totalCount={cards.length}
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
          totalCount={count}
          size={cardSize}
        />
      ))}
    </CardSetContainer>
  );
};
