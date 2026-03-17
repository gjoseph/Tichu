import { fileURLToPath } from "node:url";
import { dirname } from "node:path";
import type { StorybookConfig } from "@storybook/react-vite";

const config: StorybookConfig = {
  framework: {
    name: getAbsolutePath("@storybook/react-vite"),
    options: {},
  },
  stories: ["../src/**/*.@(mdx|stories.@(ts|tsx|js|jsx))"],

  addons: [
    getAbsolutePath("@chromatic-com/storybook"),
    getAbsolutePath("@storybook/addon-links"),
    getAbsolutePath("@storybook/addon-docs"),
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

function getAbsolutePath(value: string): any {
  return dirname(fileURLToPath(import.meta.resolve(`${value}/package.json`)));
}
