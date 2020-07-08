set -o errexit
set -o errtrace
set -o functrace
set -o nounset

rm -f package-lock.json packages/*/package-lock.json

# Remove node_modules
npx lerna clean -y

# Install root -- same command as Renovate uses.
npm install --ignore-scripts --no-audit --package-lock-only

# Lerna bootstrap -- same command as Renovate uses. Not sure if/what the -- flags do.
npx lerna bootstrap --no-ci --ignore-scripts # -- --ignore-scripts --no-audit --package-lock-only

# Reformat
npm run prettier-fix-all

git diff --exit-code

# For some reason this produces different results:
# npm i && npx lerna exec --concurrency 1 -- npm i
