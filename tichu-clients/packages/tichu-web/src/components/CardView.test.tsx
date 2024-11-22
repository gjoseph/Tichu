import React from "react";
import { render } from "@testing-library/react";
import { CardView } from "./CardView";
import { cardFromName } from "tichu-client-ts-lib";
import "@testing-library/jest-dom";

test("renders the card name", () => {
  const { getByText } = render(
    <CardView card={cardFromName("*P")} onClick={() => {}} selected={false} />,
  );
  expect(getByText(/Phoenix/)).toBeInTheDocument();
});
