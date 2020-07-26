import { classes } from "./util";

describe("classes()", () => {
  test("concats classnames with spaces", () => {
    expect(classes("a", "b")).toBe("a b");
  });
  test("nulls are ignored", () => {
    expect(classes(null, "a", null, "b", null)).toBe("a b");
  });
  test("undefineds are ignored", () => {
    expect(classes(undefined, "a", undefined, "b", undefined)).toBe("a b");
  });
  test("empty strings and spaces are ignored", () => {
    expect(classes("", "a", "", "b", "   ", "c")).toBe("a b c");
  });
  test("empty array yields empty string", () => {
    expect(classes()).toBe("");
  });
  test("array of nulls yields empty string", () => {
    expect(classes(null, undefined)).toBe("");
  });
  test("array of one yields no additional whitespace", () => {
    expect(classes("a")).toBe("a");
  });
});
