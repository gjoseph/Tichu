import { dirname, join } from "path";
module.exports = {
  //  we'll likely need a way to exclude websocket ("ws") like we did with webpack/craco!?

  stories: ["../src/**/*.@(mdx|stories.@(ts|tsx|js|jsx))"],

  addons: [
    getAbsolutePath("@storybook/addon-links"),
    getAbsolutePath("@storybook/addon-essentials"),
    getAbsolutePath("@storybook/addon-storysource"),
    "@chromatic-com/storybook",
  ],
  staticDirs: ["../public"],

  typescript: {
    // type-check stories during Storybook build
    check: true,

    reactDocgen: "react-docgen-typescript",
  },

  framework: {
    name: getAbsolutePath("@storybook/react-vite"),
    options: {},
  },

  docs: {},
};

function getAbsolutePath(value) {
  return dirname(require.resolve(join(value, "package.json")));
}
