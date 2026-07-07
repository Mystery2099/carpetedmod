package us.mathewtech.util

import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.mathewtech.block.CarpetedBlock
import us.mathewtech.config.CarpetedModConfig
import us.mathewtech.item.CarpetedItemStacks
import us.mathewtech.registry.ModBlockTags
import us.mathewtech.registry.ModCriteria

object CarpetedDropUtil {
    fun drops(block: Block, carpeted: CarpetedBlock, state: BlockState, params: LootParams.Builder): List<ItemStack> {
        if (
            CarpetedModConfig.preservesSilkTouchDrops() &&
            !state.`is`(ModBlockTags.DISABLE_SILK_TOUCH_PRESERVATION) &&
            hasSilkTouch(params)
        ) {
            (params.getOptionalParameter(LootContextParams.THIS_ENTITY) as? ServerPlayer)?.let(ModCriteria::silkTouchedCarpetedBlock)

            return listOf(
                CarpetedItemStacks.create(
                    block,
                    carpeted.getCarpetColorFromState(state),
                    carpeted.getCarpetSurfaceFromState(state)
                )
            )
        }

        return listOf(ItemStack(carpeted.baseBlock.asItem()), ItemStack(carpeted.getCarpetItemFromState(state)))
    }

    private fun hasSilkTouch(params: LootParams.Builder): Boolean {
        val tool = params.getParameter(LootContextParams.TOOL)
        return tool.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
            .entrySet()
            .any { entry -> entry.key.`is`(Enchantments.SILK_TOUCH) && entry.intValue > 0 }
    }
}
