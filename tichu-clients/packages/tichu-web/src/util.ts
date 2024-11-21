/**
 * Copied and adapted from https://medium.com/@pppped/compute-an-arbitrary-color-for-user-avatar-starting-from-his-username-with-javascript-cd0675943b66,
 * which seems much simpler and lighter than depending on color-hash.
 */
export const stringToHslColor = (
  s: string,
  // TODO i've had nicer results with 80, 50 and hash<<9, but not sure why
  saturation: number = 80,
  lightness: number = 80,
) => {
  let hash = 0;
  for (let i = 0; i < s.length; i++) {
    hash = s.charCodeAt(i) + ((hash << 5) - hash);
  }

  const h = hash % 360;
  return "hsl(" + h + ", " + saturation + "%, " + lightness + "%)";
};

export const classes = (...classNames: (string | null | undefined)[]) => {
  return (
    classNames
      // Trim (if s is truthy)
      .map((s) => s?.trim())
      // Remove non-truthy strings
      .filter((s) => s)
      .join(" ")
  );
};
