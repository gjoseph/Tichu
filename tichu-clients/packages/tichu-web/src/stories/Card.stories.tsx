import { actions } from "@storybook/addon-actions";
import React from "react";
import { AllCards, cardFromName } from "tichu-client-ts-lib";
import { CardBack } from "../components/CardBack";
import { CardView } from "../components/CardView";
import { Args, Meta } from "@storybook/react";
import { makeStory } from "./stories";

export default {
  title: "Single cards",
  component: CardView,
  parameters: {
    controls: {
      include: ["cardName", "size", "selected"],
    },
  },
  args: {
    size: "regular",
    selected: false,
  },
} as Meta;

const events = actions("onClick");

export const MahJong = makeStory((args: Args) => (
  <CardView
    card={cardFromName("*1")}
    selected={args.selected}
    size={args.size}
    {...events}
  />
));
export const Dog = makeStory((args: Args) => (
  <CardView
    card={cardFromName("*H")}
    selected={args.selected}
    size={args.size}
    {...events}
  />
));
export const Phoenix = makeStory((args: Args) => (
  <CardView
    card={cardFromName("*P")}
    selected={args.selected}
    size={args.size}
    {...events}
  />
));
export const Dragon = makeStory((args: Args) => (
  <CardView
    card={cardFromName("*D")}
    selected={args.selected}
    size={args.size}
    {...events}
  />
));

export const Card_Front = makeStory(
  (args: Args) => {
    return (
      <CardView
        card={cardFromName(args.cardName)}
        selected={args.selected}
        size={args.size}
        {...events}
      />
    );
  },
  {
    cardName: "B3",
  },
  {
    cardName: {
      control: "select",
      options: ["Card", ...AllCards.map((c) => c.shortName), "*1"],
    },
  },
);
Card_Front.storyName = "Card Front (with controls)";

export const Card_Back = makeStory((args: Args) => {
  return <CardBack size={args.size} {...events} />;
});
Card_Back.storyName = "Card Back (with controls)";
