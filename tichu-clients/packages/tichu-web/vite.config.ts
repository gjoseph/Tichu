import { defineConfig } from "vitest/config";
import reactRefresh from "@vitejs/plugin-react";
// import eslint from "vite-plugin-eslint";
import svgrPlugin from "vite-plugin-svgr";

export default defineConfig({
  resolve: {
    preserveSymlinks: true, // we need this for the build to work with workspaces, not 100% why
  },
  plugins: [
    reactRefresh(),
    // eslint(), NFC why this goes and try to lint ../tichu-client-ts-lib/lib/index.js
    svgrPlugin({
      svgrOptions: {
        icon: true,
      },
    }),
  ],
  test: {
    environment: "jsdom",
    globals: false, // this is the default and I agree -- let's use explicit imports instead
    setupFiles: "./src/test/setup.ts",
  },
});
