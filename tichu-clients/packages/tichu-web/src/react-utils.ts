import React from "react";

/**
 * Allows managing a state value and retrieve/store it in `window.localStorage`.
 *
 * `<T>` specifies the type of the state object; this function inherently lets it be undefined (so no need to pass `<Foo|undefined>`, `<Foo>` will do.
 *
 * Copied and adapted from https://joshwcomeau.com/react/persisting-react-state-in-localstorage/
 *
 * @param key the key used to store this state in localStorage against.
 * @param defaultValue can be a value of type T, a function that returns T or undefined, or undefined.
 * @return an array of [current value, dispatcher to set the value to a new value, a function to unset and remove the value from the store]
 */
export function useStoredState<T>(
  key: string,
  defaultValue?: T | (() => T | undefined) | undefined
): [
  T | undefined,
  React.Dispatch<React.SetStateAction<T | undefined>>,
  () => void
] {
  const [value, setValue] = React.useState<T | undefined>(() => {
    const stickyValue = window.localStorage.getItem(key);
    // localStorage returns an explicit null rather than undefined here. We could != undefined, but I'd rather stick
    // to the correct equality check than approximate consistency.
    if (stickyValue !== null) {
      try {
        return JSON.parse(stickyValue);
      } catch (e) {
        console.error(
          `Could not parse value for ${key}: ${stickyValue}: ${e}, restoring defaults.`
        );
        window.localStorage.removeItem(key);
      }
    }
    return defaultValue;
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
