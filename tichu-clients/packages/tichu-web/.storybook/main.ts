import type { StorybookConfig } from "@storybook/react-vite";

const config: StorybookConfig = {
  framework: {
    name: "@storybook/react-vite",
    options: {},
  },
  stories: ["../src/**/*.@(mdx|stories.@(ts|tsx|js|jsx))"],

  addons: [
    "@storybook/addon-links",
    "@storybook/addon-essentials",
    "@storybook/addon-storysource",
    "@chromatic-com/storybook",
  ],
  staticDirs: ["../public"],
  typescript: {
    // type-check stories during Storybook build
    check: true,
    reactDocgen: "react-docgen-typescript",
  },

  docs: {},

  async viteFinal(config, options) {
    //  we'll likely need a way to exclude websocket ("ws") like we did with webpack/craco!?
    return config;
  },
};

export default config;
