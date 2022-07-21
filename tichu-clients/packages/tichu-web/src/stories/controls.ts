export const countControl = (max: number) => {
  return {
    control: {
      type: "range",
      min: 1,
      max: max,
      step: 1,
    },
  };
};
