set -o errexit
set -o errtrace
set -o functrace
set -o nounset

echo Deleting package-lock.json and node_modules ...
rm -rf package-lock.json \
       node_modules \
       packages/*/node_modules \
       packages/*/package-lock.json

# Install root -- same command as Renovate uses.
echo Running npm install ...
npm install --ignore-scripts --no-audit --package-lock-only # --force may be needed to work around the cra5/sb6 shenanigans

# Reformat
echo Making it pretty ...
npm run prettier-fix-all

git diff --exit-code
