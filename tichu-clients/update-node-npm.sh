set -o errexit
set -o errtrace
set -o functrace
set -o nounset

# ensure pwd = dir of $0
dir0="$(dirname $0)"
dir0_resolved="$(readlink -f ${dir0})"
cd "${dir0_resolved}"

#echo Updating nodejs asdf plugin...
#not doing this since that would overwrite my fix for npm_location(can't install -g modules
#asdf plugin update nodejs

if [ "$#" -eq 2 ]; then
  NODE_VERSION=$1
  NPM_VERSION=$2
else
  echo Working out latest versions...
  CURRENT=$(asdf current --no-header nodejs | awk '{print $2}')
  CURRENT_MAJOR=$(echo "${CURRENT}" | grep -Eo '^(\d)+\.') # includes dot just for explicitness, asdf is happy with it
  LATEST_ON_CURRENT_MAJOR=$(asdf latest nodejs "${CURRENT_MAJOR}")
  LATEST=$(asdf latest nodejs)
  echo Current nodejs: $CURRENT
  echo Latest nodejs on same major version as current: $LATEST_ON_CURRENT_MAJOR
  echo Latest nodejs: $LATEST

  NODE_VERSION=$LATEST_ON_CURRENT_MAJOR
  NPM_VERSION=$(npm view npm version)
fi


echo Setting nodejs to $NODE_VERSION ...
asdf set -p nodejs "${NODE_VERSION}"
asdf install nodejs
asdf current nodejs

echo Updating npm to $NPM_VERSION ...
npm install -g npm@$NPM_VERSION

PACKAGE_JSON_FILES=(package.json $(find packages -name package.json))
echo Updating .engine in "${PACKAGE_JSON_FILES[@]}" ...
for PACKAGE_JSON in  "${PACKAGE_JSON_FILES[@]}"; do
  echo Updating $PACKAGE_JSON
  cat $PACKAGE_JSON | jq ".engines = {
  \"node\" : \"${NODE_VERSION}\",
  \"npm\" : \"${NPM_VERSION}\"
  }" > temp.json \
    && mv temp.json $PACKAGE_JSON
done

echo Setting up npm as per .engine.npm
./npm-setup.sh
echo Refresh engine in lock file
npm install --ignore-scripts --no-audit --package-lock-only --no-engine-strict
echo Sanity check with npm install
npm install
echo Run prettier check
npm run prettier-check-all