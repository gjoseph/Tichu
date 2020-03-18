import inquirer from "inquirer";
import {Actions} from "./actions";
import {program} from "commander";
import {coerceTeamId} from "./util";
import {Setup} from "./model";
import {Status, WSTichuClient} from "./ws-client";



program
    .storeOptionsAsProperties(false)
    .option("-u, --user <userId>", 'your name... until we figure auth out')
    .requiredOption("-g, --game <gameId>", "the game id... until we figure out an API to list available games")
    .option("-t, --team <teamId>", "Team 1 or 2", coerceTeamId);

program.parseAsync()
    .then(program => program.opts())
    .then((opts: any) => {
        return inquirer.prompt([
            {
                when: opts.user === undefined,
                type: "input",
                name: "userId",
                validate: (input: string) => input.length > 0
            }, {
                when: opts.team === undefined,
                type: "list",
                name: "teamId",
                choices: [1, 2],
            }, {
                type: "confirm",
                name: "confirm",
                message: 'Start ?',
            }, {
                when: (answers: any) => {
                    return answers.action === Actions.play
                },
                type: "checkbox",
                name: "cards",
                message: "Pick cards to play",
                choices: [
                    {name: "first", value: 1},
                    {name: "second", value: 2},
                ]
            }
        ]).then((answers: any) => {
            // Combine CLI opts and answers
            return new Setup(answers.gameId || opts.game, answers.userId || opts.user, answers.teamId || opts.team);
        })
    }).then((setup: Setup) => {
    console.log("Connecting to game ...", setup)
    return new WSTichuClient()
        .connect()
        .waitUntilDone()

}).then((status: Status) => {
    console.log("Status:", status)
}).finally(() => {
    console.log("Done")
});
