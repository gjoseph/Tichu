import inquirer from "inquirer";
import { program } from "commander";
import { coerceTeamId } from "./util";
import { Status, WSTichuClient } from "./ws-client";
import { GameOpts, setupQuestions } from "./startup";

const url = "ws://localhost:8080/room/some-room-id";

program
    .storeOptionsAsProperties(false)
    .option("-u, --user <userId>", "your name... until we figure auth out")
    .requiredOption(
        "-r, --room <roomId>",
        "the room id... until we figure out an API to list available rooms"
    )
    .option("-t, --team <teamId>", "Team 1 or 2", coerceTeamId);

program
    .parseAsync()
    .then(program => program.opts() as GameOpts)
    .then((opts: GameOpts) => {
        return inquirer.prompt(setupQuestions(opts)).then((answers: any) => {
            // Combine CLI opts and answers
            return {
                room: answers.roomId ?? opts.room,
                user: answers.userId ?? opts.user,
                team: answers.teamId ?? opts.team
            } as GameOpts;
        });
    })
    .then((opts: GameOpts) => {
        console.log("Connecting to room ...", opts);
        return new WSTichuClient(opts).connect(url).waitUntilDone();
    })
    .then((status: Status) => {
        console.log("Status:", status);
    })
    .finally(() => {
        console.log("Done");
    });
