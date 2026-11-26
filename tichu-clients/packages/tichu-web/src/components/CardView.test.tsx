import { render } from "@testing-library/react";
import { cardFromName } from "tichu-client-ts-lib";
import { expect, test } from "vitest";
import { CardView } from "./CardView";
import "@testing-library/jest-dom/vitest";

test("renders the card name", () => {
  const { getByText } = render(
    <CardView
      card={cardFromName("*P")}
      onClick={() => {
        /* nup */
      }}
      selected={false}
    />,
  );
  expect(getByText(/Phoenix/)).toBeInTheDocument();
});
