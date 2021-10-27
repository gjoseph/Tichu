import { boolean, number, radios } from "@storybook/addon-knobs";

export const sizeKnob = () => {
  return radios("Size", { Small: "small", Regular: "regular" }, "regular");
};

export const styleKnob = () => {
  return radios(
    "Style",
    { Flat: "flat", Fanned: "fanned", Stacked: "stacked" },
    "flat"
  );
};

export const countKnob = (max: number) => {
  return number("How many cards", 3, {
    range: true,
    min: 1,
    max,
    step: 1,
  });
};

export const boolKnob = (label: string) => {
  return boolean(label, false);
};
