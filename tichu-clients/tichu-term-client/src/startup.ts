import { Actions } from "./messages";

export interface GameOpts {
    room: string;
    user: string;
    team: number;
}

export const setupQuestions = (opts: GameOpts) => [
    {
        when: opts.user === undefined,
        type: "input",
        name: "userId",
        validate: (input: string) => input.length > 0,
    },
    {
        when: opts.team === undefined,
        type: "list",
        name: "teamId",
        choices: [
            { name: "Team #1", value: 0 },
            { name: "Team #2", value: 1 },
        ], // this should not be a --cli or setup question anyway
    },
    {
        type: "confirm",
        name: "confirm",
        message: "Start ?",
    },
    {
        when: (answers: any) => {
            return answers.action === Actions.play;
        },
        type: "checkbox",
        name: "cards",
        message: "Pick cards to play",
        choices: [
            { name: "first", value: 1 },
            { name: "second", value: 2 },
        ],
    },
];