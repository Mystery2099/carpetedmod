package us.mathewtech.util

import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import us.mathewtech.registry.ModItemTags
import java.util.IdentityHashMap

object CarpetColorUtil {
    private val carpetItemsByColor: Array<Item> = DyeColor.entries
        .map { Blocks.CARPET.pick(it).asItem() }
        .toTypedArray()
    private val dyeItemsByColor: Array<Item> = DyeColor.entries
        .map { color ->
            BuiltInRegistries.ITEM.getValue(
                Identifier.fromNamespaceAndPath("minecraft", "${color.getName()}_dye")
            )
        }
        .toTypedArray()
    private val carpetColorByItem = IdentityHashMap<Item, DyeColor>(carpetItemsByColor.size)
    private val dyeColorByItem = IdentityHashMap<Item, DyeColor>(dyeItemsByColor.size)

    init {
        DyeColor.entries.forEachIndexed { index, color ->
            carpetColorByItem[carpetItemsByColor[index]] = color
            dyeColorByItem[dyeItemsByColor[index]] = color
        }
    }

    fun colorFromCarpetItem(item: Item): DyeColor? {
        carpetColorByItem[item]?.let { return it }

        val stack = ItemStack(item)
        if (!stack.`is`(ItemTags.WOOL_CARPETS)) {
            return null
        }

        for (color in DyeColor.entries) {
            if (stack.`is`(ModItemTags.CARPETS_BY_COLOR.getValue(color))) {
                carpetColorByItem[item] = color
                return color
            }
        }

        return null
    }

    fun colorFromDyeItemStack(stack: ItemStack): DyeColor? {
        if (!stack.`is`(ModItemTags.CARPET_DYES)) {
            return null
        }

        stack.get(DataComponents.DYE)?.let { return it }
        dyeColorByItem[stack.item]?.let { return it }

        for (color in DyeColor.entries) {
            if (
                stack.`is`(ModItemTags.CARPET_DYES_BY_COLOR.getValue(color)) ||
                stack.`is`(ModItemTags.COMMON_DYES_BY_COLOR.getValue(color))
            ) {
                dyeColorByItem[stack.item] = color
                return color
            }
        }

        return null
    }

    fun carpetBlock(color: DyeColor): Block {
        return Blocks.CARPET.pick(color)
    }

    fun carpetItem(color: DyeColor): Item {
        return carpetItemsByColor[color.ordinal]
    }

    fun dyeItem(color: DyeColor): Item {
        return dyeItemsByColor[color.ordinal]
    }
}
