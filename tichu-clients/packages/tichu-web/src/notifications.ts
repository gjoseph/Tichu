// TODO improve API and usage such that
// - only relevant-to-us options are exposed
// - stacking / notification center works (currently we have a badge but can't see other notifs)
// - dedupe notifications
// - understand why toolpad doesn't export UseNotifications interface

import {
  CloseNotification,
  ShowNotification,
} from "@toolpad/core/useNotifications";

type NotificationKey = string;
type NotificationSeverity = "warning" | "info";

interface NotificationOptions {
  severity: NotificationSeverity;
  autoHideDuration?: number;
}

export interface Notifier {
  // eslint-disable-next-line @typescript-eslint/prefer-function-type
  (message: string, options?: NotificationOptions): NotificationKey;
}

export const withToolpad =
  (notifications: UseNotifications): Notifier =>
  (message: string, options?: NotificationOptions): NotificationKey => {
    return notifications.show(message, options);
  };

export interface UseNotifications {
  show: ShowNotification;
  close: CloseNotification;
}
