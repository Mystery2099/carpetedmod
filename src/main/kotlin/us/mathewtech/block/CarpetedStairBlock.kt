package us.mathewtech.block

import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.storage.loot.LootParams

class CarpetedStairBlock(
    override val baseBlock: StairBlock,
    properties: Properties
) : StairBlock(baseBlock.defaultBlockState(), properties), CarpetedBlock {

    companion object {
        val CARPET: EnumProperty<DyeColor> = EnumProperty.create("carpet", DyeColor::class.java)
        val CARPET_SURFACE: EnumProperty<CarpetSurface> = EnumProperty.create("carpet_surface", CarpetSurface::class.java)
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

    override fun getDrops(state: BlockState, params: LootParams.Builder): List<ItemStack> {
        return listOf(ItemStack(baseBlock.asItem()), ItemStack(getCarpetItemFromState(state)))
    }
}
