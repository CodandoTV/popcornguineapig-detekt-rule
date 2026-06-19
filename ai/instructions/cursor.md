# Cursor Conventions

## Context Loading
- `.cursorrules` at root is read automatically at session start
- `.cursorrules` is a **pointer only** — read AGENTS.md for full context

## Best Practices
- Use `ai/skills/` for task-specific guidance — list the directory, read the matching `SKILL.md`
- Use `ai/instructions/` for platform-specific patterns
- Do not store rules or project structure in .cursorrules — everything lives in AGENTS.md
