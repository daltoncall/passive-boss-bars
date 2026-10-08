# Changelog

## 1.0.1

- Cache each entity's aggression state for the current world tick.
- Reuse one mutable state object per loaded entity instead of allocating a new record for each player check.
- Preserve the existing linger and targeted-player behavior.

## 1.0.0

- Initial release.
