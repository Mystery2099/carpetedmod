package us.mathewtech.registry

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.DyeColor
import us.mathewtech.CarpetedMod
import us.mathewtech.block.CarpetSurface
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
                    val iconBlock = ModBlocks.slabsByBase.values.firstOrNull()
                    if (iconBlock != null) {
                        CarpetedItemStacks.create(iconBlock, DyeColor.RED, CarpetSurface.TOP)
                    } else {
                        net.minecraft.world.item.ItemStack.EMPTY
                    }
                }
                .displayItems { _, output ->
                    ModBlocks.slabsByBase.values.forEach { block ->
                        DyeColor.entries.forEach { color ->
                            output.accept(CarpetedItemStacks.create(block, color, CarpetSurface.TOP))
                        }
                    }

                    ModBlocks.stairsByBase.values.forEach { block ->
                        DyeColor.entries.forEach { color ->
                            output.accept(CarpetedItemStacks.create(block, color, CarpetSurface.TREAD))
                        }
                    }
                }
                .build()
        )
    }
}
