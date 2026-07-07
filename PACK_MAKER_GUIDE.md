# Pack Maker Guide

`carpeted-mod` 1.0 works in two parts:

- The mod registers carpeted versions of every vanilla slab and stair at startup.
- Datapacks can then change what items work, how those blocks behave, and which carpeting actions are allowed.

When there is a good vanilla or common tag to use, the mod already leans on it.
For example, carpeting recipes use `#minecraft:wool_carpets` directly, and dyes
can come from `minecraft:dyes` or `c:dyes`. Datagen fills `carpet_dyes` tags with
`#minecraft:dyes`, `#c:dyes`, and per-color `#c:dyes/<color>` references.

This is a Fabric mod, so `forge:dyes` tags are not referenced by default. Pack
makers can still add `#forge:dyes` (or any other tag) to `carpet_dyes` if another
mod or datapack provides those tags.

## Item Tags

These tags control what items the mod accepts.

**Migration:** `carpeted-mod:carpets` was removed. Use `#minecraft:wool_carpets`
instead. Per-color tags `carpeted-mod:carpets/<color>` remain for custom carpet
items that are not vanilla wool carpets.

- `minecraft:wool_carpets`
  Items in this tag can be used to apply carpet. Add custom carpet items here.
- `carpeted-mod:carpets/<color>`
  Tells the mod what color a non-vanilla carpet item counts as. Vanilla carpets
  are recognized automatically.
- `carpeted-mod:carpet_dyes`
  Items in this tag can recolor carpeted blocks.
- `carpeted-mod:carpet_dyes/<color>`
  Tells the mod what color a dye item counts as.
- `carpeted-mod:carpet_removers`
  Items in this tag can remove carpet.

Example item tag:

```json
{
  "replace": false,
  "values": [
    "minecraft:shears"
  ]
}
```

## Recipe Data

Carpeting, recoloring, and removal now use custom recipe data under:

- `data/<namespace>/recipe/carpeting/`
- `data/<namespace>/recipe/recoloring/`
- `data/<namespace>/recipe/removal/`

Each recipe says:

- which block is the input
- which item tag or ingredient is used
- which block is the result

Simple example:

```json
{
  "type": "carpeted-mod:carpeting",
  "ingredient": "#minecraft:wool_carpets",
  "input_block": "minecraft:oak_slab",
  "result_block": "carpeted-mod:oak_slab"
}
```

Because these are normal recipe JSON files, Fabric resource conditions can also be used on them when needed.

If Roughly Enough Items is installed, any carpeting, recoloring, or removal
recipe you add shows up in the matching REI category. Displays read from
`RecipeManager` and expand tag ingredients automatically.

## Drops

Block drops are not loot-table driven. Breaking behavior is handled in code via
`getDrops()` and `CarpetedDropUtil`. Pack makers cannot override drops through
loot tables — use the `disable_silk_touch_preservation` block tag if you need
Silk Touch to drop the base block and carpet separately.

## Block Tags

These tags change what already-supported blocks are allowed to do.

- `carpeted-mod:disable_carpeting`
  Stops carpet from being applied to matching base blocks.
- `carpeted-mod:disable_recoloring`
  Stops matching carpeted blocks from being recolored.
- `carpeted-mod:disable_removal`
  Stops carpet from being removed from matching carpeted blocks.
- `carpeted-mod:disable_silk_touch_preservation`
  Makes matching carpeted blocks drop the base block and carpet separately, even with Silk Touch.
- `carpeted-mod:hide_from_carpeted_tab`
  Hides matching carpeted blocks from the creative tab.

The mod also generates some reference tags:

- `carpeted-mod:supported_base_blocks`
- `carpeted-mod:supported_base_slabs`
- `carpeted-mod:supported_base_stairs`
- `carpeted-mod:carpeted_blocks`
- `carpeted-mod:carpeted_slabs`
- `carpeted-mod:carpeted_stairs`

Example block tag:

```json
{
  "replace": false,
  "values": [
    "carpeted-mod:oak_stairs"
  ]
}
```

## Current Limit

Datapacks cannot create new carpeted block ids in 1.0, because tags load after block registration.

So in 1.0, datapacks can change behavior for the vanilla carpeted blocks the mod already registers, but they cannot add brand new carpeted variants for modded slabs or stairs yet.
