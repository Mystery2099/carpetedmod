package us.mathewtech.item

import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import us.mathewtech.block.CarpetSurface
import us.mathewtech.registry.ModDataComponents

object CarpetedItemStacks {
    fun create(block: Block, color: DyeColor, surface: CarpetSurface): ItemStack {
        return ItemStack(block.asItem()).also { stack ->
            stack.set(DataComponents.DYE, color)
            stack.set(ModDataComponents.CARPET_SURFACE, surface)
        }
    }
}

