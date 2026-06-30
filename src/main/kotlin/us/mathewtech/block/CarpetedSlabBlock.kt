package us.mathewtech.block

import net.minecraft.core.BlockPos
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.SlabType
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.LevelReader
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import us.mathewtech.item.CarpetedItemStacks
import us.mathewtech.util.CarpetedDropUtil

class CarpetedSlabBlock(
    override val baseBlock: Block,
    properties: Properties
) : SlabBlock(properties), CarpetedBlock {
    companion object {
        val CARPET: EnumProperty<DyeColor> = EnumProperty.create("carpet", DyeColor::class.java)
        val CARPET_SURFACE: EnumProperty<CarpetSurface> = EnumProperty.create("carpet_surface", CarpetSurface::class.java)

        private val BOTTOM_SHAPE: VoxelShape = Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0)
        private val TOP_SHAPE: VoxelShape = Block.box(0.0, 8.0, 0.0, 16.0, 17.0, 16.0)
        private val DOUBLE_SHAPE: VoxelShape = Block.box(0.0, 0.0, 0.0, 16.0, 17.0, 16.0)
    }

    init {
        this.registerDefaultState(
            this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM)
                .setValue(WATERLOGGED, false)
                .setValue(CARPET, DyeColor.WHITE)
                .setValue(CARPET_SURFACE, CarpetSurface.TOP)
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
        return when (state.getValue(TYPE)) {
            SlabType.BOTTOM -> BOTTOM_SHAPE
            SlabType.TOP -> TOP_SHAPE
            SlabType.DOUBLE -> DOUBLE_SHAPE
        }
    }

    override fun getDrops(state: BlockState, params: LootParams.Builder): List<ItemStack> {
        return CarpetedDropUtil.drops(this, this, params)
    }

    override fun getCloneItemStack(level: LevelReader, pos: BlockPos, state: BlockState, includeData: Boolean): ItemStack {
        return CarpetedItemStacks.create(this, getCarpetColorFromState(state), getCarpetSurfaceFromState(state))
    }
}
