package us.mathewtech.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import us.mathewtech.CarpetedMod

object ModItemTags {
    val CARPET_REMOVERS: TagKey<Item> = TagKey.create(Registries.ITEM, CarpetedMod.id("carpet_removers"))
    val CARPET_DYES: TagKey<Item> = carpetedTag("carpet_dyes")
    val CARPET_DYES_BY_COLOR: Map<DyeColor, TagKey<Item>> = DyeColor.entries.associateWith { color ->
        carpetedTag("carpet_dyes/${color.getName()}")
    }
    val COMMON_DYES: TagKey<Item> = commonTag("dyes")
    val FORGE_DYES: TagKey<Item> = forgeTag("dyes")
    val COMMON_DYES_BY_COLOR: Map<DyeColor, TagKey<Item>> = DyeColor.entries.associateWith { color ->
        commonTag("dyes/${color.getName()}")
    }
    val FORGE_DYES_BY_COLOR: Map<DyeColor, TagKey<Item>> = DyeColor.entries.associateWith { color ->
        forgeTag("dyes/${color.getName()}")
    }

    private fun commonTag(path: String): TagKey<Item> {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", path))
    }

    private fun forgeTag(path: String): TagKey<Item> {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("forge", path))
    }

    private fun carpetedTag(path: String): TagKey<Item> {
        return TagKey.create(Registries.ITEM, CarpetedMod.id(path))
    }
}
