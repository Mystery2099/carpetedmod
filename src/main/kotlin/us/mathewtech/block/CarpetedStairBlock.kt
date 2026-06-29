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
    }

    init {
        registerDefaultState(defaultBlockState().setValue(CARPET, DyeColor.WHITE))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(CARPET)
    }

    override fun getCarpetColorFromState(state: BlockState): DyeColor {
        return state.getValue(CARPET)
    }

    override fun getDrops(state: BlockState, params: LootParams.Builder): List<ItemStack> {
        return listOf(ItemStack(baseBlock.asItem()), ItemStack(getCarpetItemFromState(state)))
    }
}
