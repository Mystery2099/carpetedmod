# Carpeted Mod

A Fabric mod that lets you put carpet on vanilla slabs and stairs.

Version 1 focuses on vanilla slabs, vanilla stairs, and vanilla carpets. Broader
mod compatibility is planned for a later major version.

## Features

- Apply any vanilla carpet color to supported vanilla slabs and stairs.
- Preserve slab and stair state, including waterlogging.
- Recolor carpeted blocks with dye.
- Remove carpet with shears by default.
- Drop the base block and carpet separately when broken normally.
- Drop a color-preserving carpeted block item when broken with Silk Touch.
- Pick block preserves the carpet color in creative mode.
- Includes a creative tab with every carpeted slab and stair color.
- Four advancements for applying carpet, dyeing, removing, and Silk Touch.
- REI support (optional): carpeting, recoloring, and removal categories built from recipe data.

## Interactions

Use a vanilla carpet item on a supported slab or stair to apply carpet.

For slabs, click the top face. Double slabs are ignored because normal carpet
already works on full blocks.

For stairs, click the tread. The front face also works when it is part of the
carpeted tread shape.

Use dye on an already-carpeted slab or stair to recolor the carpet.

Use shears to remove carpet and get the carpet item back. If your inventory is
full, the carpet drops on the ground.

Using another carpet on an already-carpeted block does nothing. Remove the carpet
first, or use dye to recolor it.

## Tags

Pack makers can adjust these item tags:

- `minecraft:wool_carpets`: items that can apply carpet.
- `carpeted-mod:carpets/<color>`: maps a non-vanilla carpet item to a color.
  Vanilla carpets are recognized automatically.
- `carpeted-mod:carpet_removers`: items that remove carpet. Contains
  `minecraft:shears` by default. Damageable items lose durability in survival;
  non-damageable items still work.
- `carpeted-mod:carpet_dyes`: items allowed to recolor carpeted blocks. Includes
  `#minecraft:dyes` and `#c:dyes` by default.
- `carpeted-mod:carpet_dyes/<color>`: maps a dye item to a color. Datagen wires in
  `#c:dyes/<color>` for each color.

An item with Minecraft's `minecraft:dye` data component still needs to be in
`carpeted-mod:carpet_dyes`. If it is not in the tag, it will not recolor
carpeted blocks.

## Scope

Version 1 supports registered vanilla stairs, registered vanilla slabs, and
vanilla carpets.

Out of scope for Version 1:

- automatic modded stair/slab support
- modded carpet support
- Moonlight Lib integration
- runtime block discovery
- block entities
- multi-loader support

Those are candidates for Version 2.

## Known Notes

- Carpeted blocks are wrapper blocks, not block entities.
- Carpeted block items keep their color using item data components.
- Drops come from `getDrops()` and `CarpetedDropUtil`, not loot tables. That
  keeps Silk Touch color preservation in one place.
- With REI installed, custom carpeting/recoloring/removal recipes show up
  automatically in the matching categories.

## Requirements

- Minecraft 26.2
- Fabric Loader 0.19.3 or newer
- Fabric API
- Fabric Language Kotlin
- Java 25 or newer

## Building

After cloning, generate assets and data before building or running the client:

```sh
./gradlew runDatagen
```

Then build:

```sh
./gradlew build
```

The built jar will be in `build/libs`. Datagen requires Java 25 or newer (same as the mod).

## License

CC0-1.0
