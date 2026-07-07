package us.mathewtech.registry

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import us.mathewtech.CarpetedMod
import us.mathewtech.block.CarpetSurface
import us.mathewtech.block.CarpetedBlock
import us.mathewtech.item.CarpetedItemStacks

object ModCreativeModeTabs {
    private val CARPETED_BLOCKS_KEY: ResourceKey<CreativeModeTab> = ResourceKey.create(
        BuiltInRegistries.CREATIVE_MODE_TAB.key(),
        CarpetedMod.id("carpeted_blocks")
    )

    fun initialize() {
        Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            CARPETED_BLOCKS_KEY,
            FabricCreativeModeTab.builder()
                .title(Component.translatable("itemGroup.carpeted-mod.carpeted_blocks"))
                .icon {
                    val iconBlock = ModBlocks.slabsByBase.values.firstOrNull(::isVisibleInTab)
                    if (iconBlock != null) {
                        CarpetedItemStacks.create(iconBlock, DyeColor.RED, CarpetSurface.TOP)
                    } else {
                        ItemStack.EMPTY
                    }
                }
                .displayItems { _, output ->
                    addVisibleBlocks(output, ModBlocks.slabsByBase.values, CarpetSurface.TOP)
                    addVisibleBlocks(output, ModBlocks.stairsByBase.values, CarpetSurface.TREAD)
                }
                .build()
        )
    }

    private fun addVisibleBlocks(
        output: CreativeModeTab.Output,
        blocks: Collection<CarpetedBlock>,
        surface: CarpetSurface
    ) {
        for (block in blocks) {
            if (!isVisibleInTab(block)) {
                continue
            }

            for (color in DyeColor.entries) {
                output.accept(carpetedStack(block, color, surface))
            }
        }
    }

    private fun isVisibleInTab(block: CarpetedBlock): Boolean {
        return !block.block().defaultBlockState().`is`(ModBlockTags.HIDE_FROM_CARPETED_TAB)
    }

    private fun carpetedStack(block: CarpetedBlock, color: DyeColor, surface: CarpetSurface): ItemStack {
        return CarpetedItemStacks.create(block.block(), color, surface)
    }

    private fun CarpetedBlock.block(): Block {
        return this as Block
    }
}
