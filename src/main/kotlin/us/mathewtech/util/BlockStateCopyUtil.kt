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
        var newState = carpeted.defaultBlockState()

        newState = newState.setValue(SlabBlock.TYPE, original.getValue(SlabBlock.TYPE))
        newState = newState.setValue(SlabBlock.WATERLOGGED, original.getValue(SlabBlock.WATERLOGGED))

        return newState
            .setValue(CarpetedSlabBlock.CARPET, color)
            .setValue(CarpetedSlabBlock.CARPET_SURFACE, surface)
    }

    fun copyCarpetedToBaseSlab(state: BlockState, carpeted: CarpetedSlabBlock): BlockState {
        var baseState = carpeted.baseBlock.defaultBlockState()

        baseState = baseState.setValue(SlabBlock.TYPE, state.getValue(SlabBlock.TYPE))
        baseState = baseState.setValue(SlabBlock.WATERLOGGED, state.getValue(SlabBlock.WATERLOGGED))

        return baseState
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
        var newState = carpeted.defaultBlockState()

        newState = newState.setValue(StairBlock.FACING, original.getValue(StairBlock.FACING))
        newState = newState.setValue(StairBlock.HALF, original.getValue(StairBlock.HALF))
        newState = newState.setValue(StairBlock.SHAPE, original.getValue(StairBlock.SHAPE))
        newState = newState.setValue(StairBlock.WATERLOGGED, original.getValue(StairBlock.WATERLOGGED))

        return newState
            .setValue(CarpetedStairBlock.CARPET, color)
            .setValue(CarpetedStairBlock.CARPET_SURFACE, surface)
    }

    fun copyCarpetedToBaseStairs(state: BlockState, carpeted: CarpetedStairBlock): BlockState {
        var baseState = carpeted.baseBlock.defaultBlockState()

        baseState = baseState.setValue(StairBlock.FACING, state.getValue(StairBlock.FACING))
        baseState = baseState.setValue(StairBlock.HALF, state.getValue(StairBlock.HALF))
        baseState = baseState.setValue(StairBlock.SHAPE, state.getValue(StairBlock.SHAPE))
        baseState = baseState.setValue(StairBlock.WATERLOGGED, state.getValue(StairBlock.WATERLOGGED))

        return baseState
    }
}
