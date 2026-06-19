# Gemini Code Conventions

## Context Loading
- `.gemini/context.md` is read automatically at session start
- `.gemini/context.md` is a **pointer only** — read AGENTS.md for full context

## Best Practices
- Use `ai/skills/` for task-specific guidance — list the directory, read the matching `SKILL.md`
- Use `ai/instructions/` for platform-specific patterns
- Do not store rules or project structure in .gemini/context.md — everything lives in AGENTS.md
