set -o errexit
set -o errtrace
set -o functrace
set -o nounset

rm -f package-lock.json packages/*/package-lock.json

# How I thought to do it:
#npm i
#npx lerna exec --concurrency 1 -- npm i
#git diff --exit-code

# How Renovate does it:
npm install --ignore-scripts --no-audit --package-lock-only
npx lerna bootstrap --no-ci --ignore-scripts -- --ignore-scripts --no-audit --package-lock-only
git diff --exit-code

# Still not sure what the difference is ...
