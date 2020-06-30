// exported for test reasons... there's gotta be a better way
export function coerceTeamId(value: string, ignoredPreviousVal?: number) {
  // Input is 1, 2, or whatever other number really, output is 0 or 1
  return Math.abs((parseInt(value) % 2) - 1);
}
