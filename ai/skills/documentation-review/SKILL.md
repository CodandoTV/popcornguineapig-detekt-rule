---
name: documentation-review
description: Scans docs/ for broken links, stale config examples, and inconsistencies with README and source code
---

# documentation-review

When invoked, this skill will:

## 1. Scan internal links in `docs/`

- Read every `.md` file under `docs/`.
- For each internal link (e.g., `[text](./some-file.md)` or `[text](some-file.md)`):
  - Resolve the path relative to the docs directory.
  - Verify the target file exists.
  - Report missing files as errors.

## 2. Check external links

- For each external URL (e.g., `https://...`):
  - Attempt a HEAD request (or GET fallback) using `curl -s -o /dev/null -w "%{http_code}" <url>`.
  - Flag any URL returning a 4xx or 5xx status code.
  - Timeout after 10 seconds per URL.
  - Do **not** fail on network errors (just note them as "unreachable").

## 3. Verify JSON config examples match the actual schema

- Read the source model classes:
  - `src/main/kotlin/.../domain/models/DependencyRulesConfig.kt`
  - `src/main/kotlin/.../domain/models/RuleConfig.kt`
- Compare the fields, types, and optionality against JSON examples in `docs/` and `README.md`.
- Flag any difference (e.g., a field in the example that doesn't exist in the schema, or a field in the schema that's missing from the docs).
- Expected top-level schema fields: `packagePrefix` (String, required), `debug` (Boolean, optional), `rules` (Array, required).
- Expected per-rule fields: `filePattern` (String, required), `dependenciesAllowed` (Boolean, optional), `exclusiveDependencies` (String[], optional), `forbiddenDependencies` (String[], optional).

## 4. Confirm all rules described in docs exist in source

- Extract rule names mentioned in `docs/2-existing-rules.md` (e.g., "ArchitectureRulesRule").
- Verify a corresponding class exists under `src/main/kotlin/.../presentation/rules/`.
- Report any documented rule that has no source implementation, and any source rule that has no documentation.

## 5. Cross-check setup instructions with README

- Compare the setup instructions in `docs/1-getting-started.md` with those in `README.md`.
- Look for contradictions:
  - Different dependency coordinates (e.g., `com.github.codandotv` vs `io.github.codandotv`).
  - Different config file paths or formats.
  - Different evaluation order descriptions.
  - Different field descriptions or required/optional flags.
- Report all inconsistencies.

## 6. Report results

- Print a summary with:
  - Number of files scanned.
  - Number of links checked (internal + external).
  - Number of config example schema checks.
  - Number of rule-to-source cross-checks.
  - List of all issues found (grouped by category: broken links, schema mismatches, missing rules, setup inconsistencies).
  - If no issues found, report "All checks passed."
