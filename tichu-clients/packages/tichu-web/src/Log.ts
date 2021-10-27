import { ActivityLogMessage } from "./components/ActivityLog";

export class Log {
  constructor(
    readonly isDebug: boolean,
    readonly newActivityLog: (newMessage: ActivityLogMessage) => void
  ) {}

  error(...msg: any[]) {
    console.error(...msg);
    this.newActivityLog({
      debug: true,
      message: [...msg].join(" ➡️ "),
    });
  }

  debug(...msg: any[]) {
    if (this.isDebug) {
      console.debug(...msg);
      this.newActivityLog({
        debug: true,
        message: [...msg].join(" ➡️ "),
      });
    }
  }
}
