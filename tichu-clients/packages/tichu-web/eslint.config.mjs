import jsxA11Y from "eslint-plugin-jsx-a11y";
import _import from "eslint-plugin-import";
import react from "eslint-plugin-react";
import reactHooks from "eslint-plugin-react-hooks";
import reactCompiler from "eslint-plugin-react-compiler";
import tsEslint from "typescript-eslint";
import esLint from "@eslint/js";
import eslintPluginPrettierRecommended from "eslint-plugin-prettier/recommended";

export default tsEslint.config(
  esLint.configs.recommended,
  tsEslint.configs.recommendedTypeChecked,
  tsEslint.configs.stylisticTypeChecked,
  react.configs.flat.recommended,
  reactHooks.configs.flat.recommended,
  eslintPluginPrettierRecommended,
  {
    plugins: {
      "jsx-a11y": jsxA11Y,
      import: _import,
      react,
      "react-hooks": reactHooks,
      // not entirely sure what this does but bringing it back in just in case
      "react-compiler": reactCompiler,
    },

    languageOptions: {
      sourceType: "script",
      ecmaVersion: 5,
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
    ignores: ["**/build", "**/lib", "**/node_modules"],
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
);
