# Vanta Naming and Versioning Scheme

## Mod Versioning

Vanta uses semantic versioning:

- MAJOR: breaking changes in config format, module behavior contracts, or public API expectations.
- MINOR: new optimization domains or substantial non-breaking feature additions.
- PATCH: bug fixes, tuning, and compatibility updates with no intentional behavior breaks.

Example: 0.2.0

## Release Tag Format

Release tags use:

v{mod_version}-mc{minecraft_version}

Example:

v0.2.0-mc1.21.11

## Artifact Naming

Artifacts are produced with this base name pattern:

vanta-mc{minecraft_version}-{mod_version}.jar

Example:

vanta-mc1.21.11-0.2.0.jar

## Branch Naming (recommended)

- feat/<domain>-<short-description>
- fix/<domain>-<short-description>
- perf/<domain>-<short-description>
- chore/<short-description>

Examples:

- feat/minecart-density-hud
- perf/crystal-particle-budget
