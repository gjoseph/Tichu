export const parameters = {
  // actions: { argTypesRegex: "^on[A-Z].*" },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
    // expands descriptions/docs
    // expanded: true
  },
  backgrounds: {
    grid: { cellSize: 10, cellAmount: 10, offsetX: 16, offsetY: 16 },
  },
};
export const tags = ["autodocs"];
