package us.mathewtech.registry

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import us.mathewtech.CarpetedMod

object ModItemTags {
    val CARPET_REMOVERS: TagKey<Item> = TagKey.create(Registries.ITEM, CarpetedMod.id("carpet_removers"))
}

