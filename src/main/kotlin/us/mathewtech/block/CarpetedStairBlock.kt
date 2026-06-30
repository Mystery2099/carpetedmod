package us.mathewtech.block

import com.mojang.math.OctahedralGroup
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.Half
import net.minecraft.world.level.block.state.properties.StairsShape
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class CarpetedStairBlock(
    override val baseBlock: StairBlock,
    properties: Properties
) : StairBlock(baseBlock.defaultBlockState(), properties), CarpetedBlock {

    companion object {
        val CARPET: EnumProperty<DyeColor> = EnumProperty.create("carpet", DyeColor::class.java)
        val CARPET_SURFACE: EnumProperty<CarpetSurface> = EnumProperty.create("carpet_surface", CarpetSurface::class.java)

        private val BOTTOM_STRAIGHT_CARPET: VoxelShape = Shapes.rotate(Shapes.or(
            box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0),
            box(7.0, 9.0, 0.0, 16.0, 17.0, 16.0)
        ), OctahedralGroup.ROT_90_Y_POS)
        private val BOTTOM_INNER_CARPET: VoxelShape = Shapes.rotate(Shapes.or(
            box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0),
            box(7.0, 9.0, 0.0, 16.0, 17.0, 16.0),
            box(0.0, 9.0, 7.0, 7.0, 17.0, 16.0)
        ), OctahedralGroup.BLOCK_ROT_Y_270)
        private val BOTTOM_OUTER_CARPET: VoxelShape = Shapes.rotate(Shapes.or(
            box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0),
            box(7.0, 8.0, 7.0, 16.0, 17.0, 16.0)
        ), OctahedralGroup.BLOCK_ROT_Y_180)

        private val BOTTOM_STRAIGHT_BY_FACING: Map<Direction, VoxelShape> = Shapes.rotateHorizontal(BOTTOM_STRAIGHT_CARPET)
        private val BOTTOM_INNER_BY_FACING: Map<Direction, VoxelShape> = Shapes.rotateHorizontal(BOTTOM_INNER_CARPET)
        private val BOTTOM_OUTER_BY_FACING: Map<Direction, VoxelShape> = Shapes.rotateHorizontal(BOTTOM_OUTER_CARPET)
        private val TOP_STRAIGHT_BY_FACING: Map<Direction, VoxelShape> = Shapes.rotateHorizontal(BOTTOM_STRAIGHT_CARPET, OctahedralGroup.INVERT_Y)
        private val TOP_INNER_BY_FACING: Map<Direction, VoxelShape> = Shapes.rotateHorizontal(BOTTOM_INNER_CARPET, OctahedralGroup.INVERT_Y)
        private val TOP_OUTER_BY_FACING: Map<Direction, VoxelShape> = Shapes.rotateHorizontal(BOTTOM_OUTER_CARPET, OctahedralGroup.INVERT_Y)
    }

    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(CARPET, DyeColor.WHITE)
                .setValue(CARPET_SURFACE, CarpetSurface.TREAD)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(CARPET, CARPET_SURFACE)
    }

    override fun getCarpetColorFromState(state: BlockState): DyeColor {
        return state.getValue(CARPET)
    }

    override fun getCarpetSurfaceFromState(state: BlockState): CarpetSurface {
        return state.getValue(CARPET_SURFACE)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return Shapes.or(super.getShape(state, level, pos, context), carpetShape(state))
    }

    override fun getDrops(state: BlockState, params: LootParams.Builder): List<ItemStack> {
        return listOf(ItemStack(baseBlock.asItem()), ItemStack(getCarpetItemFromState(state)))
    }

    private fun carpetShape(state: BlockState): VoxelShape {
        val facing = carpetShapeFacing(state)
        val top = state.getValue(HALF) == Half.TOP

        return when (state.getValue(SHAPE)) {
            StairsShape.STRAIGHT -> if (top) TOP_STRAIGHT_BY_FACING.getValue(facing) else BOTTOM_STRAIGHT_BY_FACING.getValue(facing)
            StairsShape.INNER_LEFT, StairsShape.INNER_RIGHT -> if (top) TOP_INNER_BY_FACING.getValue(facing) else BOTTOM_INNER_BY_FACING.getValue(facing)
            StairsShape.OUTER_LEFT, StairsShape.OUTER_RIGHT -> if (top) TOP_OUTER_BY_FACING.getValue(facing) else BOTTOM_OUTER_BY_FACING.getValue(facing)
        }
    }

    private fun carpetShapeFacing(state: BlockState): Direction {
        val facing = state.getValue(FACING)

        return when (state.getValue(SHAPE)) {
            StairsShape.STRAIGHT,
            StairsShape.OUTER_LEFT,
            StairsShape.INNER_RIGHT -> facing
            StairsShape.INNER_LEFT -> facing.counterClockWise
            StairsShape.OUTER_RIGHT -> facing.clockWise
        }
    }
}
