package us.mathewtech.util

import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.SlabType
import us.mathewtech.block.CarpetSurface
import us.mathewtech.block.CarpetedSlabBlock
import us.mathewtech.block.CarpetedStairBlock

object StateCopyUtil {
    fun copySlabToCarpeted(
        original: BlockState,
        carpeted: CarpetedSlabBlock,
        color: DyeColor,
        surface: CarpetSurface
    ): BlockState {
        return carpeted.defaultBlockState()
            .setValue(SlabBlock.TYPE, original.getValue(SlabBlock.TYPE))
            .setValue(SlabBlock.WATERLOGGED, original.getValue(SlabBlock.WATERLOGGED))
            .setValue(CarpetedSlabBlock.CARPET, color)
            .setValue(CarpetedSlabBlock.CARPET_SURFACE, surface)
    }

    fun copyCarpetedToBaseSlab(state: BlockState, carpeted: CarpetedSlabBlock): BlockState {
        return copyCarpetedToBaseSlab(state, carpeted.baseBlock as SlabBlock)
    }

    fun copyCarpetedToBaseSlab(state: BlockState, baseBlock: SlabBlock): BlockState {
        return baseBlock.defaultBlockState()
            .setValue(SlabBlock.TYPE, state.getValue(SlabBlock.TYPE))
            .setValue(SlabBlock.WATERLOGGED, state.getValue(SlabBlock.WATERLOGGED))
    }

    fun isSupportedSlabState(state: BlockState): Boolean {
        return state.hasProperty(SlabBlock.TYPE) && state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE
    }

    fun copyStairsToCarpeted(
        original: BlockState,
        carpeted: CarpetedStairBlock,
        color: DyeColor,
        surface: CarpetSurface
    ): BlockState {
        return carpeted.defaultBlockState()
            .setValue(StairBlock.FACING, original.getValue(StairBlock.FACING))
            .setValue(StairBlock.HALF, original.getValue(StairBlock.HALF))
            .setValue(StairBlock.SHAPE, original.getValue(StairBlock.SHAPE))
            .setValue(StairBlock.WATERLOGGED, original.getValue(StairBlock.WATERLOGGED))
            .setValue(CarpetedStairBlock.CARPET, color)
            .setValue(CarpetedStairBlock.CARPET_SURFACE, surface)
    }

    fun copyCarpetedToBaseStairs(state: BlockState, carpeted: CarpetedStairBlock): BlockState {
        return copyCarpetedToBaseStairs(state, carpeted.baseBlock)
    }

    fun copyCarpetedToBaseStairs(state: BlockState, baseBlock: StairBlock): BlockState {
        return baseBlock.defaultBlockState()
            .setValue(StairBlock.FACING, state.getValue(StairBlock.FACING))
            .setValue(StairBlock.HALF, state.getValue(StairBlock.HALF))
            .setValue(StairBlock.SHAPE, state.getValue(StairBlock.SHAPE))
            .setValue(StairBlock.WATERLOGGED, state.getValue(StairBlock.WATERLOGGED))
    }

    fun copyCarpetedToCarpetedSlab(state: BlockState, carpeted: CarpetedSlabBlock, color: DyeColor): BlockState {
        return copySlabToCarpeted(
            state,
            carpeted,
            color,
            state.getValue(CarpetedSlabBlock.CARPET_SURFACE)
        )
    }

    fun copyCarpetedToCarpetedStairs(state: BlockState, carpeted: CarpetedStairBlock, color: DyeColor): BlockState {
        return copyStairsToCarpeted(
            state,
            carpeted,
            color,
            state.getValue(CarpetedStairBlock.CARPET_SURFACE)
        )
    }
}
