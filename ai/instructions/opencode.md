# OpenCode Conventions

## Configuration
- Config file: `openspec.json` (root)
- Skill paths: `["ai/skills"]`
- Instructions pointer: `["AGENTS.md"]`

## Skills
OpenCode uses the `skill` tool to load skills. Skills are referenced by name from `ai/skills/`. The system prompt will match task descriptions to `available_skills` automatically when the skill definition in `SKILL.md` has a matching `description` field.

## Context Files
- `AGENTS.md` — loaded automatically via `openspec.json`
- Do not duplicate AGENTS.md content in other files

## Task Workflow
1. Agent reads AGENTS.md for project context
2. Agent lists `ai/skills/` to find matching skill
3. Agent reads matching `SKILL.md` before proceeding
4. Agent implements task, runs validation, reports result
