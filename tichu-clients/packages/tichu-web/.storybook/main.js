const webpack = require("webpack");
module.exports = {
  core: {
    builder: "webpack5",
  },
  webpackFinal: async (config) => {
    // see craco.config.js
    config.plugins.push(new webpack.IgnorePlugin({ resourceRegExp: /^ws$/ }));
    return config;
  },
  stories: ["../src/**/*.stories.@(ts|tsx|js|jsx|mdx)"],
  addons: [
    "@storybook/preset-create-react-app",
    "@storybook/addon-links",
    "@storybook/addon-essentials",
    "@storybook/addon-storysource",
  ],
  typescript: {
    check: true, // type-check stories during Storybook build
  },
};
