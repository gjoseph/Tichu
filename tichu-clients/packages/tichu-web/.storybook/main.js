import { dirname, join } from "path";
const webpack = require("webpack");
module.exports = {
  webpackFinal: async (config) => {
    // see craco.config.js
    config.plugins.push(new webpack.IgnorePlugin({ resourceRegExp: /^ws$/ }));
    return config;
  },

  stories: ["../src/**/*.@(mdx|stories.@(ts|tsx|js|jsx))"],

  addons: [
    getAbsolutePath("@storybook/preset-create-react-app"),
    getAbsolutePath("@storybook/addon-links"),
    getAbsolutePath("@storybook/addon-essentials"),
    getAbsolutePath("@storybook/addon-storysource"),
    "@chromatic-com/storybook",
  ],

  typescript: {
    // type-check stories during Storybook build
    check: true,

    reactDocgen: "react-docgen-typescript",
  },

  framework: {
    name: getAbsolutePath("@storybook/react-webpack5"),
    options: {},
  },

  docs: {},
};

function getAbsolutePath(value) {
  return dirname(require.resolve(join(value, "package.json")));
}
