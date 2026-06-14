---
name: release-notes
description: Reads current version, analyzes unreleased commits, bumps version, and updates CHANGELOG.md
---

# release-notes

When invoked, this skill will:

1. **Read current version** from `popcornguineapig-detekt-rule/version.properties` (the `VERSION` property).

2. **Get the latest git tag** by running `git tag --list | sort -V | tail -1`. Expected current tag: `v1.0.0`.

3. **Check for unreleased commits ahead of the latest tag** by running:
   ```
   git log <latest-tag>..HEAD --oneline
   ```
   If there are no commits, warn the user and exit.

4. **Categorize commits** for semantic version bump by parsing each commit message:
   - `breaking` or `!` (in conventional commit style like `feat!:`) → **major** bump
   - `feat` or `feature` → **minor** bump
   - `fix`, `patch`, `chore`, `docs`, `refactor`, `test`, `style`, `perf` → **patch** bump
   - If mixed categories, take the highest: major > minor > patch.

5. **Increment the version** accordingly in `popcornguineapig-detekt-rule/version.properties`:
   - Major: increment X (e.g., 1.0.0 → 2.0.0)
   - Minor: increment Y (e.g., 1.0.0 → 1.1.0)
   - Patch: increment Z (e.g., 1.0.0 → 1.0.1)

6. **Create or update `CHANGELOG.md`** at root level with a new version entry:
   ```markdown
   # Changelog

   ## [1.0.1] - YYYY-MM-DD

   ### Added
   - (new features)

   ### Fixed
   - (bug fixes)

   ### Changed
   - (other changes)
   ```
   Group commits under the appropriate heading (`Added`, `Fixed`, `Changed`).
   Use today's date in ISO 8601 format (YYYY-MM-DD).

7. **Prompt the user** to commit and tag:
   - Ask: "Shall I create a commit with message `release: vX.Y.Z` and tag `vX.Y.Z`?"
   - If yes, run:
     ```
     git add popcornguineapig-detekt-rule/version.properties CHANGELOG.md
     git commit -m "release: vX.Y.Z"
     git tag vX.Y.Z
     ```
   - Inform the user they still need to `git push && git push --tags`.
