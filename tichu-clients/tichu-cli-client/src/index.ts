import inquirer from "inquirer";
import {Actions} from "./actions";

inquirer.prompt([
    {
        type: 'list',
        name: 'action',
        message: 'What do you want to do?',
        choices: [Actions.init, Actions.join, Actions.ready, Actions.play, Actions.pass]
    },
    {
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
    console.log("Answers:", answers.constructor.name, answers)
}).catch((error: any) => {
    console.log("Error", error.constructor.name);
    if (error.isTtyError) {
        // Prompt couldn't be rendered in the current environment
    } else {
        // Something else when wrong
    }
});
