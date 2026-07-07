# Version 1 Checklist

Use this file to record in-game test results. Mark items with `[x]` when they pass, and add notes under each section when something fails or feels wrong.

## Core Placement

- [x] Carpet can be applied to a supported bottom slab.
- [x] Carpet can be applied to a supported top slab.
- [x] Carpet cannot be applied to a double slab.
- [x] Carpet can be applied to a supported bottom stair tread.
- [x] Carpet can be applied to a supported upside-down stair tread.
- [x] Carpet cannot be applied to unsupported block faces.
- [x] Carpet item is consumed in survival.
- [x] Carpet item is not consumed in creative.
- [x] Carpet item on an already-carpeted block does not replace, consume, or duplicate anything.

Notes:
If player is facing down, the carpet gets applied to stairs but when facing them, it does not... the point of the tread option was because the wool covers the front and top.

## Dye Recoloring

- [x] Dye recolors a carpeted slab.
- [x] Dye recolors a carpeted stair.
- [x] Dye is consumed in survival.
- [x] Dye is not consumed in creative.
- [x] Same-color dye does nothing and is not consumed.
- [x] Recolored block updates its world model.
- [x] Recolored block updates its pick/Silk Touch item model.
- [x] Recolored block updates its item name.

Notes:


## Carpet Removal

- [x] Shears remove carpet from a carpeted slab.
- [x] Shears remove carpet from a carpeted stair.
- [x] Carpet remover restores the original base block state.
- [x] Removed carpet is returned to the player when inventory has space.
- [x] Removed carpet drops on the ground when inventory is full.
- [x] Damageable carpet remover loses durability in survival.
- [x] Damageable carpet remover does not lose durability in creative.
- [ ] Non-damageable items in `carpeted-mod:carpet_removers` still remove carpet.
- [ ] Items not in `carpeted-mod:carpet_removers` do not remove carpet.

Notes:


## Drops

- [x] Breaking carpeted slab without Silk Touch drops the base slab.
- [x] Breaking carpeted slab without Silk Touch drops the carpet item.
- [x] Breaking carpeted stair without Silk Touch drops the base stair.
- [x] Breaking carpeted stair without Silk Touch drops the carpet item.
- [x] Breaking carpeted slab with Silk Touch drops the carpeted slab item.
- [x] Breaking carpeted stair with Silk Touch drops the carpeted stair item.
- [x] Silk Touch item preserves carpet color.
- [x] Silk Touch item places with the preserved carpet color.
- [x] No drops are voided.
- [x] No extra drops are duplicated.

Notes:


## Pick Block

- [x] Pick block on carpeted slab gives carpeted slab item.
- [x] Pick block on carpeted stair gives carpeted stair item.
- [x] Picked item preserves carpet color.
- [x] Picked item has the correct colored item model.
- [x] Picked item has the correct dynamic item name.
- [x] Picked item places with the preserved carpet color.

Notes:


## State Preservation

- [x] Bottom slab state is preserved when carpet is applied.
- [x] Top slab state is preserved when carpet is applied.
- [x] Slab waterlogged state is preserved.
- [x] Stair facing is preserved.
- [x] Stair half is preserved.
- [x] Stair shape is preserved.
- [x] Stair waterlogged state is preserved.
- [x] Removing carpet restores the original slab state.
- [x] Removing carpet restores the original stair state.

Notes:


## Shapes And Models

- [x] Bottom slab visual model matches hitbox well enough.
- [x] Top slab visual model matches hitbox well enough.
- [x] Bottom stair visual model matches hitbox well enough.
- [x] Top stair visual model matches hitbox well enough.
- [x] Straight stair shapes look correct.
- [x] Inner stair shapes look correct.
- [x] Outer stair shapes look correct.
- [x] All stair facings rotate correctly.
- [x] No missing model warnings appear in logs.
- [x] No missing texture warnings appear in logs.

Notes:


## Supported Blocks

- [x] All registered vanilla slabs can receive carpet.
- [x] All registered vanilla stairs can receive carpet.
- [x] Wooden slabs/stairs use axe behavior as expected.
- [x] Stone slabs/stairs use pickaxe behavior as expected.
- [x] Tool tier requirements match the base block.
- [x] Explosion resistance feels consistent with the base block.
- [x] Sound/friction/other copied block properties feel consistent with the base block.

Notes: Creative tab is in. REI shows carpeting, recoloring, and removal from recipe data.

## REI

- [x] Carpeting category shows base block + carpet ingredient → carpeted block.
- [x] Recoloring category shows carpeted block + dye → recolored block.
- [x] Removal category shows carpeted block + remover → base block + carpet.
- [x] Displays come from `RecipeManager`, not hardcoded per-block lists.
- [x] Tag ingredients expand in REI (e.g. `#minecraft:wool_carpets`).

Notes:


## Generated Data

- [x] Full datagen runs successfully with the correct Java version.
- [x] Generated blockstates are stable.
- [x] Generated block models are stable.
- [x] Generated item definitions are stable.
- [x] Generated recipes are stable (carpeting, recoloring, removal).
- [x] Generated advancements are stable (root, dye, remove, silk touch).
- [x] Generated lang file is stable.
- [x] Generated block tags are stable.
- [x] Generated item tags are stable (including forge dye compatibility).
- [x] No generated files churn unexpectedly after rerunning datagen.
- [x] No loot table datagen — drops handled in code via `CarpetedDropUtil`.

Notes:


## Logs

- [x] No server errors during carpet placement.
- [x] No server errors during dye recoloring.
- [x] No server errors during carpet removal.
- [x] No server errors during normal breaking.
- [x] No server errors during Silk Touch breaking.
- [x] No client resource/model warnings relevant to this mod.

Notes:


## README / Documentation

- [x] Document carpet application.
- [x] Document dye recoloring.
- [x] Document carpet remover item tag.
- [x] Document Silk Touch behavior.
- [x] Document supported block scope.
- [x] Document known Version 1 limitations.
- [x] Document REI integration and recipe-driven displays.
- [x] Document code-driven drops (no loot tables).
- [x] Document advancements.

Notes:
README and pack-maker docs cover player interactions, tags, recipes, drops,
REI, and scope without going deep into implementation details.


## Final Version 1 Decision

- [ ] Core gameplay feels natural.
- [ ] No known item loss bugs remain.
- [ ] No known duplication bugs remain.
- [ ] Models are acceptable for Version 1.
- [ ] Drops and Silk Touch are acceptable for Version 1.
- [ ] Architecture feels suitable to carry into Version 2.

Final notes:
