import React, { FC } from "react";
import { CardBack } from "./CardBack";
import { CardView } from "./CardView";
import { Card } from "tichu-client-ts-lib";
import styles from "./Card.module.css";
import { classes } from "../util";
import { visitEnumValue } from "ts-enum-util";

type CardSize = "small" | "regular";
type SetStyle = "flat" | "fanned" | "stacked";

interface CardSetProps {
  cards: Card[];
  cardSize?: CardSize;
  style: SetStyle;
  handleSelect: (isSelected: boolean, card: Card) => void;
}

interface WithPositionProps {
  idx: number;
  totalCount: number;
  size: CardSize;
  style: SetStyle;
}

const calcTransform = (
  style: SetStyle,
  size: CardSize,
  idx: number,
  totalCount: number
) => {
  // Not sure how best to name this -- this is the "centered index" where 0 is the middle element of the list
  const midIdx = idx - (totalCount - 1) / 2;
  // Not sure these magic numbers will forever apply to all styles but it kinda works for now
  const sizeFactor = size === "small" ? 3 : 6;
  return visitEnumValue(style).with({
    flat: () => {
      const tx = idx * sizeFactor;
      const ty = 0;
      const rot = 0;
      return { tx, ty, rot };
    },
    fanned: () => {
      const tx = idx * sizeFactor;
      const ty = Math.abs(midIdx * midIdx * (size === "small" ? 0.1 : 0.3));
      const rot = midIdx * sizeFactor;
      return { tx, ty, rot };
    },
    stacked: () => {
      // TODO increase randomness with number of cards ?
      const tx = Math.random() * 0.5;
      const ty = Math.random() * 0.5;
      const rot = Math.random() * 4;
      return { tx, ty, rot };
    },
  });
};

const withPosition = <P extends object>(
  Component: React.ComponentType<P>
): React.FC<P & WithPositionProps> => ({
  idx,
  totalCount,
  size,
  style,
  ...props
}: WithPositionProps) => {
  const { tx, ty, rot } = calcTransform(style, size, idx, totalCount);
  const styles = {
    position: "absolute" as "absolute", // typescript bug ? https://github.com/microsoft/TypeScript/issues/11465
    transformOrigin: "center",
    transform: `translate(${tx}em, ${ty}em) rotate(${rot}deg)`,
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
  style,
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
          style={style}
          handleSelect={handleSelect}
        />
      ))}
    </CardSetContainer>
  );
};

export const CardBacks: FC<{
  count: number;
  cardSize?: CardSize;
  style: SetStyle;
}> = ({ count, cardSize = "small", style }) => {
  return (
    <CardSetContainer cardSize={cardSize}>
      {Array.from(new Array(count), (ignoreNull, idx) => (
        <PositionedCardBack
          key={idx}
          idx={idx}
          totalCount={count}
          size={cardSize}
          style={style}
        />
      ))}
    </CardSetContainer>
  );
};
