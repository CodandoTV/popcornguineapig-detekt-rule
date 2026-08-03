---
name: trigger-release
description: Creates or updates CHANGELOG.md, bumps version, and creates a tag to trigger the publish workflow
---

# trigger-release

When invoked, this skill will:

1. **Verify branch and sync status**:
   - Confirm the current branch is `main`. If not, warn the user and exit.
   - Run `git fetch origin main` and confirm `git rev-list main..origin/main` is empty (local is up to date). If not, warn the user and exit.

2. **Ask the user for the new version and release notes**:
   - Prompt: "What is the new version number?" (e.g., `1.1.0`)
   - Prompt: "Provide a one-sentence release note for this version."

3. **Bump version** in `popcornguineapig-detekt-rule/version.properties`:
   - Replace the `VERSION=` line with the new version provided by the user.

4. **Update CHANGELOG.md** at root level:
   - If `CHANGELOG.md` does not exist, create it with a top-level `# Changelog` heading.
   - Prepend a new entry at the top (after the heading):
     ```markdown
     ## [X.Y.Z] - YYYY-MM-DD
     
     - Release notes sentence provided by the user.
     ```
   - Use today's date in ISO 8601 format (YYYY-MM-DD).

5. **Commit, tag, and push**:
   - Run:
     ```
     git add popcornguineapig-detekt-rule/version.properties CHANGELOG.md
     git commit -m "release: vX.Y.Z"
     git tag vX.Y.Z
     git push origin main
     git push origin vX.Y.Z
     ```
   - Inform the user that pushing the tag will trigger the `publish.yml` workflow, which publishes the release to Maven Central.
