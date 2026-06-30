package us.mathewtech.block

import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import us.mathewtech.util.CarpetColorUtil

interface CarpetedBlock {
    val baseBlock: Block

    fun getCarpetColorFromState(state: BlockState): DyeColor

    fun getCarpetSurfaceFromState(state: BlockState): CarpetSurface

    fun getCarpetItemFromState(state: BlockState): Item {
        return CarpetColorUtil.carpetItem(getCarpetColorFromState(state))
    }
}
