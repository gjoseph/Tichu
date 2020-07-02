import kleur from "kleur";

export class Log {
  private stdout: NodeJS.WriteStream;

  constructor() {
    this.stdout = process.stdout;
  }

  control(msg: string) {
    this.log(kleur.blue("* " + msg));
  }

  chat(msg: string) {
    this.log(kleur.green("> " + msg));
  }

  activity(msg: string) {
    this.log(kleur.magenta("> " + msg));
  }

  error(msg: string) {
    this.log(kleur.red("Error: " + msg));
  }

  debug(msg: string) {
    this.log(kleur.yellow("Debug: " + msg));
  }

  private log(msg: string) {
    this.stdout.write(msg + "\n");
  }
}
