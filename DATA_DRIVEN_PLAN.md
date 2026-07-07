# Data-Driven Architecture Notes

This file tracks the remaining architecture boundary between generated/data-driven
behavior and code-driven behavior. Pack-maker-facing details live in
`PACK_MAKER_GUIDE.md`; player/build details live in `README.md`.

## Current Boundary

Data-driven:

- Datagen is the source of truth for tags, recipes, advancements, models, and lang.
- Item tags control carpet inputs, dye inputs, carpet removers, and per-color
  carpet/dye resolution.
- Block tags control whether registered blocks can be carpeted, recolored,
  removed, Silk Touch preserved, or shown in the custom creative tab.
- Carpeting, recoloring, and removal are custom recipes generated per supported
  vanilla base block.
- REI displays are built from recipe data.

Code-driven:

- Carpeted block ids are registered at startup from the built-in vanilla slab and
  stair registry.
- State transfer between base blocks and carpeted blocks is handled by
  `StateCopyUtil`.
- Drops are handled by `getDrops()` and `CarpetedDropUtil`, because Silk Touch
  must preserve carpet color in item data.

## Why Registration Is Still Code-Driven

Tags, recipes, and normal datapack data load after block and item registration.
They can change behavior for already-registered carpeted blocks, but they cannot
create new block ids.

For Version 1, the mod derives vanilla support from the built-in `minecraft`
block registry, so new vanilla slabs and stairs should not need a manual list.
Automatic support for modded slabs and stairs still needs a larger
registration-time compatibility design.

## Future Work

1. Add a custom datapack JSON format for per-block behavior overrides only if the
   current tag layer proves too limited.
2. Revisit registration-time compatibility for modded slabs and stairs in
   Version 2. A Moonlight-style architecture may be the right shape, but that
   should be treated as a larger design change.
