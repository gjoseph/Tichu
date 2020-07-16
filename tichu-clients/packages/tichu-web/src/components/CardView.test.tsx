import React from "react";
import { render } from "@testing-library/react";
import { CardView } from "./CardView";
import { cardFromName } from "tichu-client-ts-lib";

test("renders the card name", () => {
  const { getByText } = render(
    <CardView card={cardFromName("*P")} handleSelect={() => {}} />
  );
  expect(getByText(/Phoenix/)).toBeInTheDocument();
});
