Learning lerna:

-   `$(npm bin)/lerna bootstrap --hoist`
    -   Updates `./package-lock.json` but doesn't modify `packages/*/package.json`
-   Common deps can be added to ./package.json
    -   can they? Do they still need to be in `packages/*/package.json` ?
-   add scripts to `./package.json` for common commands
    -   .. bootstrap hoist can pbly a pre/postinstall script
-   hoisting causes issue with CRA -- for some reason the wrong version of webpack ends up installed in ./

Couldn't get typescript project references to play nice with CRA so tichu-client-ts-lib is its own module.

Unclear if I need all the differences in my tsconfig.json files...

npx lerna bootstrap
npm run term-client -- -- -- -r 1 # sorta works but seems to hang (without all the -- the arguments aren't passed down and fails on "unknown arg")
npm run web

npm install does not work from within subpackages?
