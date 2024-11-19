import { Args, ArgTypes, StoryFn } from "@storybook/react";

export const makeStory = (
  storyFn: StoryFn,
  args: Partial<Args> | undefined = undefined,
  argTypes: Partial<ArgTypes<Args>> | undefined = undefined
): StoryFn => {
  const theStory: StoryFn = storyFn;
  theStory.args = args;
  theStory.argTypes = argTypes;
  return theStory;
};

export const disableControls = {
  controls: {
    hideNoControlsWarning: true,
    include: [],
  },
};
