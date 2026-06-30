package us.mathewtech.block

import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.SlabType
import net.minecraft.world.level.storage.loot.LootParams

class CarpetedSlabBlock(
    override val baseBlock: Block,
    properties: Properties
) : SlabBlock(properties), CarpetedBlock {
    companion object {
        val CARPET: EnumProperty<DyeColor> = EnumProperty.create("carpet", DyeColor::class.java)
        val CARPET_SURFACE: EnumProperty<CarpetSurface> = EnumProperty.create("carpet_surface", CarpetSurface::class.java)
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

    override fun getDrops(state: BlockState, params: LootParams.Builder): List<ItemStack> {
        return listOf(ItemStack(baseBlock.asItem()), ItemStack(getCarpetItemFromState(state)))
    }
}
