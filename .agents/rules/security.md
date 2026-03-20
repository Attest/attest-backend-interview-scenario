# Security Rules

## Overview
This is a self-contained interview scenario with no network exposure, authentication, or secrets management. Security considerations are minimal but documented here for completeness.

## Dependencies
- **Jackson Databind** `2.17.1` -- used for JSON deserialisation from local classpath resources.
  - Keep this dependency up to date; Jackson has had CVEs in older versions.

## Data Handling
- All data is static JSON loaded from the classpath (`src/main/resources/survey-data/`).
- No user input is accepted at runtime (no HTTP endpoints, no CLI arguments processed).
- No database connections or external service calls.

## General Guidance
- Do not introduce dependencies without checking for known vulnerabilities.
- If HTTP endpoints are added in future, validate and sanitise all input.
- Do not commit secrets, API keys, or credentials to this repository.
- `CLAUDE.local.md` and `.claude/settings.local.json` are gitignored to prevent leaking local agent configuration.
