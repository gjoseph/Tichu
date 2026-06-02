#!/usr/bin/env zsh
set -euo pipefail

# ensure pwd = dir of $0
dir0="$(dirname $0)"
dir0_resolved="$(readlink -f ${dir0})"
cd "${dir0_resolved}"

if [ "$#" -eq 2 ]; then
  NODE_VERSION=$1
  NPM_VERSION=$2
else
  echo Working out latest versions...
  CURRENT=$(mise tool node --json | jq -r '.active_versions[0]')
  CURRENT_MAJOR=$(echo "${CURRENT}" | grep -Eo '^(\d)+')
  LATEST_ON_CURRENT_MAJOR=$(mise latest node@"${CURRENT_MAJOR}")
  LATEST=$(mise latest node)
  echo Current nodejs: $CURRENT
  echo Latest nodejs on same major version as current: $LATEST_ON_CURRENT_MAJOR
  echo Latest nodejs: $LATEST

  NODE_VERSION=$LATEST_ON_CURRENT_MAJOR
  NPM_VERSION=$(npm view npm version)
fi

echo node and npm --version
mise exec -- node --version
mise exec -- npm --version
echo
echo Setting node to $NODE_VERSION in mise.toml ...
mise use node@"${NODE_VERSION}"
echo
echo node and npm --version
mise exec -- node --version
mise exec -- npm --version
echo

PACKAGE_JSON_FILES=(package.json $(fd package.json packages/))
echo Updating .engine in "${PACKAGE_JSON_FILES[@]}" ...
for PACKAGE_JSON in  "${PACKAGE_JSON_FILES[@]}"; do
  echo Updating $PACKAGE_JSON
  local tmp=$(mktemp)
  echo tmp file: $tmp
  cat $PACKAGE_JSON | jq ".engines = {
  \"node\" : \"${NODE_VERSION}\",
  \"npm\" : \"${NPM_VERSION}\"
  }" > "$tmp" && mv "$tmp" $PACKAGE_JSON
done

# Chain commands in a single 'mise exec' process so the path shim works
mise exec -- bash -c "
  echo Refresh engine in lock file
  mise exec -- npm install --ignore-scripts --no-audit --package-lock-only --no-engine-strict

  echo Updating .packageManager in root package.json
  corepack enable npm
  corepack use npm@${NPM_VERSION}

  echo Verifying active npm version:
  npm --version

  echo Sanity check with npm install
  npm install

  echo Run prettier check
  npm run prettier-check-all
"
