import React from "react";
import { render } from "@testing-library/react";
import App from "./App";

test("renders at least a card", () => {
  const { getByText } = render(<App />);
  expect(getByText(/Phoenix/)).toBeInTheDocument();
});
