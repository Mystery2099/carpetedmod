package us.mathewtech.client.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider
import net.minecraft.advancements.predicates.StatePropertiesPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import us.mathewtech.block.CarpetedSlabBlock
import us.mathewtech.block.CarpetedStairBlock
import us.mathewtech.registry.ModBlocks
import us.mathewtech.util.CarpetColorUtil
import java.util.concurrent.CompletableFuture

class ModBlockLootProvider(
    output: FabricPackOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricBlockLootSubProvider(output, registriesFuture) {
    override fun generate() {
        ModBlocks.slabsByBase.forEach { (base, carpeted) ->
            add(carpeted, carpetedDropTable(carpeted, base))
        }

        ModBlocks.stairsByBase.forEach { (base, carpeted) ->
            add(carpeted, carpetedDropTable(carpeted, base))
        }
    }

    private fun carpetedDropTable(carpeted: Block, base: Block): LootTable.Builder {
        var table = LootTable.lootTable()
            .withPool(
                applyExplosionDecay(
                    carpeted,
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(base))
                )
            )

        DyeColor.entries.forEach { color ->
            val carpetProperty = when (carpeted) {
                is CarpetedSlabBlock -> CarpetedSlabBlock.CARPET
                is CarpetedStairBlock -> CarpetedStairBlock.CARPET
                else -> return@forEach
            }

            table = table.withPool(
                applyExplosionDecay(
                    carpeted,
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .`when`(
                            LootItemBlockStatePropertyCondition.hasBlockStateProperties(carpeted)
                                .setProperties(
                                    StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(carpetProperty, color)
                                )
                        )
                        .add(LootItem.lootTableItem(CarpetColorUtil.carpetItem(color)))
                )
            )
        }

        return table
    }
}
