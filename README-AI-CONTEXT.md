# AI Context Structure — Readme

## Why This Structure Exists

Multiple AI coding assistants (Claude Code, Cursor, GitHub Copilot, Gemini Code,
OpenCode) each look for their own context file in a repository. Without
centralization, these files diverge — rules, structure, and project knowledge
get duplicated and go stale.

This repo uses **AGENTS.md** as the single source of truth. Every native init
file is a one-line pointer to it. All detailed guidance lives under `ai/`.

## Architecture

```
                    ┌──────────────────┐
                    │   AGENTS.md      │
                    │  (master init)   │
                    └────────┬─────────┘
                             │ references
                             ▼
                    ┌──────────────────┐
                    │      ai/         │
                    └────────┬─────────┘
                             │
              ┌──────────────┼──────────────┐
              ▼              ▼              ▼
      ┌────────────┐ ┌────────────┐ ┌────────────┐
      │ instructions│ │  skills/   │ │ module-    │
      │ per platform│ │ per task   │ │ graph.md   │
      └────────────┘ └────────────┘ └────────────┘

  ┌──────────────┐     reads     ┌──────────────┐
  │   OpenCode   │ ─────────────→│ openspec.json│
  │ Claude Code  │ ─────────────→│ CLAUDE.md    │
  │   Cursor     │ ─────────────→│ .cursorrules │
  │   Copilot    │ ─────────────→│ copilot-inst.│
  │ Gemini Code  │ ─────────────→│ .gemini/ctx  │
  └──────────────┘               └──────┬───────┘
                                        │ points to
                                        ▼
                                  ┌──────────────┐
                                  │  AGENTS.md   │
                                  └──────────────┘
```

## File Inventory

| File | Read By | Purpose |
|---|---|---|
| `AGENTS.md` | **All assistants** (via native pointer) | Master context — project, rules, workflow, skills |
| `ai/module-graph.md` | Humans + assistants | Explicit module dependency diagram |
| `ai/instructions/opencode.md` | OpenCode | Platform-specific conventions |
| `ai/instructions/claude-code.md` | Claude Code | Platform-specific conventions |
| `ai/instructions/cursor.md` | Cursor | Platform-specific conventions |
| `ai/instructions/copilot.md` | Copilot | Platform-specific conventions |
| `ai/instructions/gemini-code.md` | Gemini Code | Platform-specific conventions |
| `ai/skills/documentation-review/SKILL.md` | Any assistant | How to review docs for inconsistencies |
| `ai/skills/minimum-requirements/SKILL.md` | Any assistant | How to check + update min dependency versions |
| `ai/skills/release-notes/SKILL.md` | Any assistant | How to bump version and generate changelog |
| `openspec.json` | OpenCode | OpenCode config — points to AGENTS.md + ai/skills |
| `CLAUDE.md` | Claude Code | Lightweight pointer → AGENTS.md |
| `.cursorrules` | Cursor | Lightweight pointer → AGENTS.md |
| `.github/copilot-instructions.md` | Copilot | Lightweight pointer → AGENTS.md |
| `.gemini/context.md` | Gemini Code | Lightweight pointer → AGENTS.md |

## Maintenance Rules

- **AGENTS.md is the only file that needs ongoing maintenance.** Everything else
  is either auto-loaded or a pointer.
- When adding a new skill: create `ai/skills/<skill-name>/SKILL.md` — no other
  file needs updating (assistants scan the folder at runtime).
- When adding a new instruction file: create `ai/instructions/<platform>.md` and
  add a row to the Platform Context table in AGENTS.md.
- Native init files must **never** contain rules or project structure — pointer only.
