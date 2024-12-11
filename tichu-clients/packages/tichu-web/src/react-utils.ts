import React from "react";

/**
 * Allows managing a state value and retrieve/store it in `window.localStorage`.
 *
 * `<T>` specifies the type of the state object; this function inherently lets it be undefined (so no need to pass `<Foo|undefined>`, `<Foo>` will do.
 *
 * Copied and adapted from https://joshwcomeau.com/react/persisting-react-state-in-localstorage/
 *
 * @param key the key used to store this state in localStorage against.
 * @param defaultValue is an optional function that returns T or undefined.
 * @return an array of [current value, dispatcher to set the value to a new value, a function to unset and remove the value from the store]
 */
export function useStoredState<T>(
  key: string,
  defaultValue?: () => T | undefined,
): [
  T | undefined,
  React.Dispatch<React.SetStateAction<T | undefined>>,
  () => void,
] {
  const [value, setValue] = React.useState<T | undefined>((): T | undefined => {
    const stickyValue = window.localStorage.getItem(key);
    // localStorage returns an explicit null rather than undefined here. We could != undefined, but I'd rather stick
    // to the correct equality check than approximate consistency.
    if (stickyValue !== null) {
      try {
        return JSON.parse(stickyValue) as T;
      } catch (e: unknown) {
        console.error(
          `Could not parse value for ${key}: ${stickyValue}, restoring defaults.`,
          e,
        );
        window.localStorage.removeItem(key);
      }
    }
    return defaultValue ? defaultValue() : undefined;
  });
  React.useEffect(() => {
    if (value === undefined) {
      window.localStorage.removeItem(key);
    } else {
      window.localStorage.setItem(key, JSON.stringify(value));
    }
  }, [key, value]);
  const removeValue = () => {
    window.localStorage.removeItem(key);
    setValue(undefined);
  };
  return [value, setValue, removeValue];
}

/**
 * Copied and adapted from https://medium.com/the-non-traditional-developer/checking-the-network-connection-with-a-react-hook-ec3d8e4de4ec
 */
export function useNetworkAvailability() {
  const [isOnline, setNetwork] = React.useState(window.navigator.onLine);
  const updateNetwork = () => {
    setNetwork(window.navigator.onLine);
  };
  React.useEffect(() => {
    window.addEventListener("offline", updateNetwork);
    window.addEventListener("online", updateNetwork);
    return () => {
      window.removeEventListener("offline", updateNetwork);
      window.removeEventListener("online", updateNetwork);
    };
  });
  return isOnline;
}
