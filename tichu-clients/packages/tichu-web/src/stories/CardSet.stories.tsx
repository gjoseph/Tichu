import { actions } from "@storybook/addon-actions";
import React from "react";
import { AllCards } from "tichu-client-ts-lib";
import { CardBacks, CardSet } from "../components/CardSet";
import { Args, Meta, StoryFn } from "@storybook/react";
import { countControl } from "./controls";
import { makeStory } from "./stories";

export default {
  title: "Card Set",
  component: CardSet, // and CardBacks
  argTypes: {
    cardCount: countControl(56),
    cards: {
      control: false,
    },
    onCardClick: {
      control: false,
    },
  },
  args: {
    cardCount: 3,
    cardSize: "small",
    layout: "flat",
  },
} as Meta;

const events = actions("onCardClick");

export const Card_Set: StoryFn = makeStory((args: Args) => {
  const cards = [...AllCards]
    .sort(() => Math.random() - 0.5)
    .slice(0, args.cardCount)
    .map((c) => ({ ...c, selected: false }));
  return (
    <CardSet
      cards={cards}
      cardSize={args.cardSize}
      layout={args.layout} // not sure why ...args doesn't work.. something to do with types
      {...events}
    />
  );
});

export const Stack_of_56 = makeStory(
  (args: Args) => {
    return (
      <CardSet
        cards={AllCards.map((c) => ({ ...c, selected: false }))}
        cardSize={args.cardSize}
        layout={args.layout}
        {...events}
      />
    );
  },
  {
    layout: "stacked",
  },
  {
    cardCount: {
      control: false,
    },
  }
);

export const Card_Backs: StoryFn = makeStory((args: Args) => {
  return (
    <CardBacks
      count={args.cardCount}
      cardSize={args.cardSize}
      layout={args.layout}
      {...events}
    />
  );
});
