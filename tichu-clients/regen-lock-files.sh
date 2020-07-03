rm package-lock.json packages/*/package-lock.json && \
npm i && \
npx lerna exec --concurrency 1 -- npm i && \ 
git diff --exit-code
