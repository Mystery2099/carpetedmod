package us.mathewtech.item

import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.jspecify.annotations.Nullable
import us.mathewtech.block.CarpetSurface
import us.mathewtech.block.CarpetedBlock
import us.mathewtech.block.CarpetedSlabBlock
import us.mathewtech.block.CarpetedStairBlock
import us.mathewtech.registry.ModDataComponents

class CarpetedBlockItem(
    block: Block,
    properties: Item.Properties
) : BlockItem(block, properties) {
    private val carpetedBlock = block as CarpetedBlock

    override fun getName(itemStack: ItemStack): Component {
        val color = itemStack.get(DataComponents.DYE) ?: return super.getName(itemStack)
        return Component.translatable(
            "item.carpeted-mod.carpeted_block",
            Component.translatable("color.minecraft.${color.getName()}"),
            Component.translatable(carpetedBlock.baseBlock.descriptionId)
        )
    }

    override fun getPlacementState(context: BlockPlaceContext): @Nullable BlockState? {
        val state = super.getPlacementState(context) ?: return null
        val stack = context.itemInHand
        val color = stack.getOrDefault(DataComponents.DYE, DyeColor.WHITE)
        val surface = stack.getOrDefault(ModDataComponents.CARPET_SURFACE, defaultSurface(state))

        return when (state.block) {
            is CarpetedSlabBlock -> state
                .setValue(CarpetedSlabBlock.CARPET, color)
                .setValue(CarpetedSlabBlock.CARPET_SURFACE, surface)
            is CarpetedStairBlock -> state
                .setValue(CarpetedStairBlock.CARPET, color)
                .setValue(CarpetedStairBlock.CARPET_SURFACE, surface)
            else -> state
        }
    }

    private fun defaultSurface(state: BlockState): CarpetSurface {
        return when (state.block) {
            is CarpetedStairBlock -> CarpetSurface.TREAD
            else -> CarpetSurface.TOP
        }
    }
}
