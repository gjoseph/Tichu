// TODO improve API and usage such that
// - only relevant-to-us options are exposed
// - stacking / notification center works (currently we have a badge but can't see other notifs)
// - dedupe notifications

type NotificationKey = string;
type NotificationSeverity = "warning" | "info";
type NotificationOptions = {
  severity: NotificationSeverity;
  autoHideDuration?: number;
};

export interface Notifier {
  send(message: string, options?: NotificationOptions): NotificationKey;
}

export class ToolpadNotifier implements Notifier {
  constructor(private notifications: /*UseNotification*/ any) {}

  send(message: string, options?: NotificationOptions): NotificationKey {
    return this.notifications.show(message, options);
  }
}
