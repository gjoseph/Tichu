export interface GameOpts {
  room: string;
  user: string;
}

export const setupQuestions = (opts: GameOpts) => [
  {
    when: opts.user === undefined,
    type: "input",
    name: "userId",
    validate: (input: string) => input.length > 0,
  },
  {
    type: "confirm",
    name: "confirm",
    message: "Start ?",
  },
];
