# AI Context Structure — Readme

## Why This Structure Exists

OpenCode is the only AI assistant configured for this repository. Instead of scattering
rules across per-assistant files, `AGENTS.md` is the single source of truth and is
auto-loaded into every session. Task-specific guidance lives in `.opencode/skills/` and
is loaded on demand via opencode's auto-discovery.

## Architecture

```
                  ┌──────────────────┐
                  │    opencode.json │
                  │  instructions:[  │
                  │    AGENTS.md ]   │
                  └────────┬─────────┘
                           │ loads
                           ▼
                  ┌──────────────────┐
                  │    AGENTS.md     │
                  │  single source   │
                  │  (incl. module   │
                  │   graph)         │
                  └────────┬─────────┘
                           │ auto-discovers
                           ▼
                  ┌──────────────────┐
                  │ .opencode/skills │
                  │  per task        │
                  └──────────────────┘
```

## File Inventory

| File | Purpose |
|---|---|
| `AGENTS.md` | Master context — project, rules, commands, workflow, skills, and the module dependency graph. Auto-loaded by OpenCode. |
| `opencode.json` | OpenCode config — `instructions: ["AGENTS.md"]`. |
| `.opencode/skills/documentation-review/SKILL.md` | How to review docs for inconsistencies. |
| `.opencode/skills/minimum-requirements/SKILL.md` | How to check + update min dependency versions. |
| `.opencode/skills/trigger-release/SKILL.md` | How to bump version, update changelog, and trigger publish. |

## Maintenance Rules

- **AGENTS.md is the only file that needs ongoing maintenance** for project context.
- When adding a new skill: create `.opencode/skills/<skill-name>/SKILL.md` — no other
  file needs updating (OpenCode scans the folder at runtime).
- Do not add per-assistant context files (Claude, Cursor, Copilot, Gemini). OpenCode is
  the sole supported assistant; anything that would go in such a file belongs in
  `AGENTS.md`.
