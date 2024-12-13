import { fixupConfigRules, fixupPluginRules } from "@eslint/compat";
import jsxA11Y from "eslint-plugin-jsx-a11y";
import _import from "eslint-plugin-import";
import react from "eslint-plugin-react";
import reactHooks from "eslint-plugin-react-hooks";
import typescriptEslint from "@typescript-eslint/eslint-plugin";
import tsParser from "@typescript-eslint/parser";
import path from "node:path";
import { fileURLToPath } from "node:url";
import js from "@eslint/js";
import { FlatCompat } from "@eslint/eslintrc";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const compat = new FlatCompat({
  baseDirectory: __dirname,
  recommendedConfig: js.configs.recommended,
  allConfig: js.configs.all,
});

export default [
  {
    ignores: ["**/build", "**/lib", "**/node_modules"],
  },
  ...fixupConfigRules(
    compat.extends(
      "eslint:recommended",
      "plugin:@typescript-eslint/recommended-type-checked",
      "plugin:@typescript-eslint/stylistic-type-checked",
      "plugin:react/recommended",
      "plugin:react-hooks/recommended",
      "prettier",
    ),
  ),
  {
    plugins: {
      "jsx-a11y": jsxA11Y,
      import: fixupPluginRules(_import),
      react: fixupPluginRules(react),
      "react-hooks": fixupPluginRules(reactHooks),
      "@typescript-eslint": fixupPluginRules(typescriptEslint),
    },

    languageOptions: {
      parser: tsParser,
      ecmaVersion: 5,
      sourceType: "script",

      parserOptions: {
        project: true,
      },
    },

    settings: {
      react: {
        version: "detect",
      },
    },
  },
  {
    files: ["**/*"],

    rules: {
      "@typescript-eslint/no-empty-function": "warn",
      "@typescript-eslint/no-inferrable-types": "off",
    },
  },
  {
    files: ["**/*.stories.tsx"],

    rules: {
      "import/no-anonymous-default-export": "off",
      "@typescript-eslint/no-unsafe-assignment": "off",
    },
  },
];
