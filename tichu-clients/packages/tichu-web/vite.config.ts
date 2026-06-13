import { defineConfig } from "vitest/config";
import reactRefresh from "@vitejs/plugin-react";
// import eslint from "vite-plugin-eslint";
import svgrPlugin from "vite-plugin-svgr";

export default defineConfig({
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
    setupFiles: "./src/test/setup.ts",
  },
});
