import React from "react";

export default {
  title: "Welcome",
};

const text = `These are bunch of Storybook stories to help me build Tichu components, learn React, re-learn
React and the entire JS stack every 3 months, and mostly just get frustrated at this abominable
stack. Which is weird, cause it's been around for so long, produces so many amazing apps, and 
full of talented engineers, you wonder how it's not been fixed yet.

Some of these "stories" are written to show/demonstrate/test certain stats/props and combinations thereof,
therefore, I've disabled "controls" specifically, since it just clutters what you'd be looking at `;

// Keeping the story for the sake of having an entry point, but not using sb's demo
export const ToStorybook = () => (
  <div>
    <h1>Welcome</h1>
    <pre>{text}</pre>
  </div>
);

ToStorybook.storyName = "to Storybook";
