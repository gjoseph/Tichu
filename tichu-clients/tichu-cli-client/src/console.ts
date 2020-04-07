// copied then adapted from wscat
export class Console {
    private stdout: NodeJS.WriteStream;

    constructor() {
        this.stdout = process.stdout;
    }

    static get Colors() {
        return {
            Red: "\u001b[31m",
            Green: "\u001b[32m",
            Yellow: "\u001b[33m",
            Blue: "\u001b[34m",
            Default: "\u001b[39m"
        };
    }

    static get Types() {
        return {
            Incoming: "< ",
            Control: "",
            Error: "error: "
        };
    }

    print(type: string, msg: string, color: string) {
        this.stdout.write(color + type + msg + Console.Colors.Default + "\n");
    }

    clear() {
        this.stdout.write("\u001b[2K\u001b[3D");
    }
}
