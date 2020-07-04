Learning lerna:

- `$(npm bin)/lerna bootstrap --hoist`
  - Updates `./package-lock.json` but doesn't modify `packages/*/package.json`
- Common deps can be added to ./package.json
  - can they? should they? Do they still need to be in `packages/*/package.json` ?
- hoisting causes issue with CRA -- for some reason the wrong version of webpack ends up installed in ./

Couldn't get typescript project references to play nice with CRA so tichu-client-ts-lib is its own module.

Unclear if I need all the differences in my tsconfig.json files...

Also see regen-lock-files.sh
npx lerna bootstrap
#npm install does not work from within subpackages but other scripts seem to work fine
#lerna add can be used to add a dependency, but there doesn't seem to be a lerna remove command... npm uninstall also fails
## https://github.com/lerna/lerna/issues/1229

npm run term-client -- -- -- -r 1 # sorta works but seems to hang (without all the -- the arguments aren't passed down and fails on "unknown arg")
npm run web
