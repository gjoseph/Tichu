import { createTheme, ThemeProvider } from "@mui/material/styles";
import React from "react";
import { Preview } from "@storybook/react";

// TODO this pbly doesn't work anymore since upgrade to sb7/8
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
// TODO this pbly doesn't work anymore since upgrade to sb7/8
export const tags = ["autodocs"];

const muiTheme = createTheme();

const preview: Preview = {
  decorators: [
    (Story) => (
      <ThemeProvider theme={muiTheme}>
        {/* 👇 Decorators in Storybook also accept a function. Replace <Story/> with Story() to enable it  */}
        <Story />
      </ThemeProvider>
    ),
  ],
};
export default preview;
