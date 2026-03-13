import { vi } from "vitest";

vi.mock("nanoid", () => ({
  default: {
    nanoid: () => {
      /* fake for tests*/
    },
  },
}));

// import { afterEach } from 'vitest'
// import { cleanup } from '@testing-library/react'
//
// // runs a clean after each test case (e.g. clearing jsdom)
// afterEach(() => {
//   cleanup();
// })
