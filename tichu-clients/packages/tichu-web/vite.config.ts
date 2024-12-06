import { defineConfig } from "vitest/config";
import reactRefresh from "@vitejs/plugin-react";
import eslint from "vite-plugin-eslint";
import svgrPlugin from "vite-plugin-svgr";

// https://vitejs.dev/config/
export default defineConfig({
  // This changes the out put dir from dist to build
  // comment this out if that isn't relevant for your project
  build: {
    outDir: "build",
  },
  plugins: [
    reactRefresh(),
    eslint(),
    svgrPlugin({
      svgrOptions: {
        icon: true,
      },
    }),
  ],
  test: {
    environment: "jsdom",
    globals: false, // this is the default and I agree -- let's use explicit imports instead
    setupFiles: "./src/test/setup.js",
  },
});
