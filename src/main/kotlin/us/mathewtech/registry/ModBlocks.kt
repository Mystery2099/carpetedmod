package us.mathewtech.registry

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.state.BlockBehaviour
import us.mathewtech.CarpetedMod
import us.mathewtech.block.CarpetedSlabBlock
import us.mathewtech.block.CarpetedStairBlock
import us.mathewtech.block.WeatheringCarpetedSlabBlock
import us.mathewtech.block.WeatheringCarpetedStairBlock
import us.mathewtech.item.CarpetedBlockItem

object ModBlocks {
    private val mutableSlabsByBase = linkedMapOf<SlabBlock, CarpetedSlabBlock>()
    private val mutableStairsByBase = linkedMapOf<StairBlock, CarpetedStairBlock>()

    val slabsByBase: Map<SlabBlock, CarpetedSlabBlock>
        get() = mutableSlabsByBase

    val stairsByBase: Map<StairBlock, CarpetedStairBlock>
        get() = mutableStairsByBase

    fun initialize() {
        if (mutableSlabsByBase.isNotEmpty() || mutableStairsByBase.isNotEmpty()) {
            return
        }

        val (slabs, stairs) = vanillaSlabsAndStairs()
        slabs.forEach(::registerCarpetedSlab)
        stairs.forEach(::registerCarpetedStair)
    }

    fun carpetedSlabFor(block: Block): CarpetedSlabBlock? {
        return mutableSlabsByBase[block]
    }

    fun carpetedStairFor(block: Block): CarpetedStairBlock? {
        return mutableStairsByBase[block]
    }

    fun carpetedForBase(baseBlock: Block): Block? {
        return when (baseBlock) {
            is SlabBlock -> carpetedSlabFor(baseBlock)
            is StairBlock -> carpetedStairFor(baseBlock)
            else -> null
        }
    }

    private fun registerCarpetedSlab(baseBlock: SlabBlock): CarpetedSlabBlock {
        val block = registerCarpeted(baseBlock, ::carpetedSlabFactory) as CarpetedSlabBlock

        mutableSlabsByBase[baseBlock] = block
        return block
    }

    private fun registerCarpetedStair(baseBlock: StairBlock): CarpetedStairBlock {
        val block = registerCarpeted(baseBlock, ::carpetedStairFactory) as CarpetedStairBlock

        mutableStairsByBase[baseBlock] = block
        return block
    }

    private fun <T : Block> registerCarpeted(
        baseBlock: T,
        factoryFor: (T) -> (BlockBehaviour.Properties) -> Block
    ): Block {
        return register(
            BuiltInRegistries.BLOCK.getKey(baseBlock).path,
            factoryFor(baseBlock),
            BlockBehaviour.Properties.ofFullCopy(baseBlock),
            true
        )
    }

    private fun carpetedSlabFactory(baseBlock: SlabBlock): (BlockBehaviour.Properties) -> Block {
        return if (baseBlock is WeatheringCopper) {
            { properties -> WeatheringCarpetedSlabBlock(baseBlock, properties) }
        } else {
            { properties -> CarpetedSlabBlock(baseBlock, properties) }
        }
    }

    private fun carpetedStairFactory(baseBlock: StairBlock): (BlockBehaviour.Properties) -> Block {
        return if (baseBlock is WeatheringCopper) {
            { properties -> WeatheringCarpetedStairBlock(baseBlock, properties) }
        } else {
            { properties -> CarpetedStairBlock(baseBlock, properties) }
        }
    }

    private fun register(
        name: String,
        blockFactory: (BlockBehaviour.Properties) -> Block,
        properties: BlockBehaviour.Properties,
        shouldRegisterItem: Boolean
    ): Block {
        val blockKey: ResourceKey<Block> = keyOfBlock(name)
        val block: Block = blockFactory(properties.setId(blockKey))

        if (shouldRegisterItem) {
            val itemKey: ResourceKey<Item> = keyOfItem(name)
            val blockItem = CarpetedBlockItem(block, Item.Properties().setId(itemKey).useBlockDescriptionPrefix())
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem)
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block)
    }

    private fun keyOfBlock(name: String): ResourceKey<Block> {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CarpetedMod.MOD_ID, name))
    }

    private fun keyOfItem(name: String): ResourceKey<Item> {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CarpetedMod.MOD_ID, name))
    }

    private fun vanillaSlabsAndStairs(): Pair<List<SlabBlock>, List<StairBlock>> {
        val slabs = ArrayList<SlabBlock>()
        val stairs = ArrayList<StairBlock>()

        BuiltInRegistries.BLOCK.forEach { block ->
            if (BuiltInRegistries.BLOCK.getKey(block).namespace != "minecraft") {
                return@forEach
            }

            when (block) {
                is SlabBlock -> slabs.add(block)
                is StairBlock -> stairs.add(block)
            }
        }

        val sortKey = { value: Block -> BuiltInRegistries.BLOCK.getKey(value).path }
        return slabs.sortedBy(sortKey) to stairs.sortedBy(sortKey)
    }
}
