package us.mathewtech.util

import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ColorCollection
import us.mathewtech.registry.ModItemTags

object CarpetColorUtil {
    fun colorFromCarpetItem(item: Item): DyeColor? {
        var result: DyeColor? = null

        ColorCollection.VALUES.forEach { color ->
            if (Blocks.CARPET.pick(color).asItem() == item) {
                result = color
            }
        }

        return result
    }

    fun colorFromDyeItemStack(stack: ItemStack): DyeColor? {
        if (!stack.`is`(ModItemTags.CARPET_DYES)) {
            return null
        }

        stack.get(DataComponents.DYE)?.let { return it }

        DyeColor.entries.forEach { color ->
            if (
                stack.`is`(ModItemTags.CARPET_DYES_BY_COLOR.getValue(color)) ||
                stack.`is`(ModItemTags.COMMON_DYES_BY_COLOR.getValue(color)) ||
                stack.`is`(ModItemTags.FORGE_DYES_BY_COLOR.getValue(color))
            ) {
                return color
            }
        }

        return null
    }

    fun carpetBlock(color: DyeColor): Block {
        return Blocks.CARPET.pick(color)
    }

    fun carpetItem(color: DyeColor): Item {
        return carpetBlock(color).asItem()
    }

    fun dyeItem(color: DyeColor): Item {
        return BuiltInRegistries.ITEM.getValue(
            Identifier.fromNamespaceAndPath("minecraft", "${color.getName()}_dye")
        )
    }
}
