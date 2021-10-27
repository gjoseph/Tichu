import React, { FC } from "react";
import { visitEnumValue } from "ts-enum-util";
import { classes } from "../util";
import styles from "./Card.module.css";
import { CardBack } from "./CardBack";
import { CardView, OnCardClick } from "./CardView";
import { SelectableCard } from "./Hand";

type CardSize = "small" | "regular";
type CardSetLayout = "flat" | "fanned" | "stacked";

interface CardSetProps {
  cards: SelectableCard[];
  cardSize?: CardSize;
  layout: CardSetLayout;
  onCardClick: OnCardClick;
}

interface WithPositionProps {
  idx: number;
  totalCount: number;
  size: CardSize;
  layout: CardSetLayout;
}

const calcTransform = (
  layout: CardSetLayout,
  size: CardSize,
  idx: number,
  totalCount: number
) => {
  // Not sure how best to name this -- this is the "centered index" where 0 is the middle element of the list
  const midIdx = idx - (totalCount - 1) / 2;
  // Not sure these magic numbers will forever apply to all styles but it kinda works for now
  const sizeFactor = size === "small" ? 3 : 6;
  return visitEnumValue(layout).with({
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
  layout,
  ...props
}: WithPositionProps) => {
  const { tx, ty, rot } = calcTransform(layout, size, idx, totalCount);
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
  onCardClick,
  cardSize = "regular",
  layout,
}) => {
  return (
    <CardSetContainer cardSize={cardSize}>
      {cards.map((card, idx) => (
        <PositionedCardView
          key={idx}
          idx={idx}
          totalCount={cards.length}
          card={card}
          selected={card.selected}
          size={cardSize}
          layout={layout}
          onClick={onCardClick}
        />
      ))}
    </CardSetContainer>
  );
};

export const CardBacks: FC<{
  count: number;
  cardSize?: CardSize;
  layout: CardSetLayout;
}> = ({ count, cardSize = "small", layout }) => {
  return (
    <CardSetContainer cardSize={cardSize}>
      {Array.from(new Array(count), (ignoreNull, idx) => (
        <PositionedCardBack
          key={idx}
          idx={idx}
          totalCount={count}
          size={cardSize}
          layout={layout}
        />
      ))}
    </CardSetContainer>
  );
};
