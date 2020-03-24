import inquirer from "inquirer";
import { program } from "commander";
import { coerceTeamId } from "./util";
import { Status, WSTichuClient } from "./ws-client";
import { GameOpts, setupQuestions } from "./startup";

// const url = "wss://echo.websocket.org";
const url = "ws://localhost:8080/chat/ignore-for-now";

program
    .storeOptionsAsProperties(false)
    .option("-u, --user <userId>", "your name... until we figure auth out")
    .requiredOption(
        "-g, --game <gameId>",
        "the game id... until we figure out an API to list available games"
    )
    .option("-t, --team <teamId>", "Team 1 or 2", coerceTeamId);

program
    .parseAsync()
    .then(program => program.opts() as GameOpts)
    .then((opts: GameOpts) => {
        return inquirer.prompt(setupQuestions(opts)).then((answers: any) => {
            // Combine CLI opts and answers
            return {
                game: answers.gameId || opts.game,
                user: answers.userId || opts.user,
                team: answers.teamId || opts.team
            } as GameOpts;
        });
    })
    .then((opts: GameOpts) => {
        console.log("Connecting to game ...", opts);
        return new WSTichuClient(opts).connect(url).waitUntilDone();
    })
    .then((status: Status) => {
        console.log("Status:", status);
    })
    .finally(() => {
        console.log("Done");
    });
