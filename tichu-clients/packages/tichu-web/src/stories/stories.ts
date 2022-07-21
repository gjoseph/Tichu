import { Args, ArgTypes, Story } from "@storybook/react";

export const makeStory = (
  storyFn: Story,
  args: Partial<Args> | undefined = undefined,
  argTypes: Partial<ArgTypes<Args>> | undefined = undefined
): Story => {
  const theStory: Story = storyFn;
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
