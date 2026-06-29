package us.mathewtech.util

import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ColorCollection

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

    fun carpetBlock(color: DyeColor): Block {
        return Blocks.CARPET.pick(color)
    }

    fun carpetItem(color: DyeColor): Item {
        return carpetBlock(color).asItem()
    }
}
