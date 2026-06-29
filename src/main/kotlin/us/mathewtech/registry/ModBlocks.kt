package us.mathewtech.registry

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import us.mathewtech.CarpetedMod
import us.mathewtech.block.CarpetedSlabBlock
import us.mathewtech.block.CarpetedStairBlock

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

        vanillaSlabs().forEach(::registerCarpetedSlab)
        vanillaStairs().forEach(::registerCarpetedStair)
    }

    fun carpetedSlabFor(block: Block): CarpetedSlabBlock? {
        return mutableSlabsByBase[block]
    }

    fun carpetedStairFor(block: Block): CarpetedStairBlock? {
        return mutableStairsByBase[block]
    }

    private fun registerCarpetedSlab(baseBlock: SlabBlock): CarpetedSlabBlock {
        val name = BuiltInRegistries.BLOCK.getKey(baseBlock).path
        val block = register(
            name,
            { properties -> CarpetedSlabBlock(baseBlock, properties) },
            BlockBehaviour.Properties.ofFullCopy(baseBlock),
            true
        ) as CarpetedSlabBlock

        mutableSlabsByBase[baseBlock] = block
        return block
    }

    private fun registerCarpetedStair(baseBlock: StairBlock): CarpetedStairBlock {
        val name = BuiltInRegistries.BLOCK.getKey(baseBlock).path
        val block = register(
            name,
            { properties -> CarpetedStairBlock(baseBlock, properties) },
            BlockBehaviour.Properties.ofFullCopy(baseBlock),
            true
        ) as CarpetedStairBlock

        mutableStairsByBase[baseBlock] = block
        return block
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
            val blockItem = BlockItem(block, Item.Properties().setId(itemKey).useBlockDescriptionPrefix())
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

    private fun vanillaSlabs(): List<SlabBlock> {
        return listOf(
            Blocks.OAK_SLAB,
            Blocks.SPRUCE_SLAB,
            Blocks.BIRCH_SLAB,
            Blocks.JUNGLE_SLAB,
            Blocks.ACACIA_SLAB,
            Blocks.DARK_OAK_SLAB,
            Blocks.MANGROVE_SLAB,
            Blocks.CHERRY_SLAB,
            Blocks.BAMBOO_SLAB,
            Blocks.BAMBOO_MOSAIC_SLAB,
            Blocks.PALE_OAK_SLAB,
            Blocks.STONE_SLAB,
            Blocks.SMOOTH_STONE_SLAB,
            Blocks.SANDSTONE_SLAB,
            Blocks.CUT_SANDSTONE_SLAB,
            Blocks.PETRIFIED_OAK_SLAB,
            Blocks.COBBLESTONE_SLAB,
            Blocks.BRICK_SLAB,
            Blocks.STONE_BRICK_SLAB,
            Blocks.MUD_BRICK_SLAB,
            Blocks.NETHER_BRICK_SLAB,
            Blocks.QUARTZ_SLAB,
            Blocks.RED_SANDSTONE_SLAB,
            Blocks.CUT_RED_SANDSTONE_SLAB,
            Blocks.PURPUR_SLAB,
            Blocks.PRISMARINE_SLAB,
            Blocks.PRISMARINE_BRICK_SLAB,
            Blocks.DARK_PRISMARINE_SLAB,
            Blocks.POLISHED_GRANITE_SLAB,
            Blocks.SMOOTH_RED_SANDSTONE_SLAB,
            Blocks.MOSSY_STONE_BRICK_SLAB,
            Blocks.POLISHED_DIORITE_SLAB,
            Blocks.MOSSY_COBBLESTONE_SLAB,
            Blocks.END_STONE_BRICK_SLAB,
            Blocks.SMOOTH_SANDSTONE_SLAB,
            Blocks.SMOOTH_QUARTZ_SLAB,
            Blocks.GRANITE_SLAB,
            Blocks.ANDESITE_SLAB,
            Blocks.RED_NETHER_BRICK_SLAB,
            Blocks.POLISHED_ANDESITE_SLAB,
            Blocks.DIORITE_SLAB,
            Blocks.BLACKSTONE_SLAB,
            Blocks.POLISHED_BLACKSTONE_SLAB,
            Blocks.POLISHED_BLACKSTONE_BRICK_SLAB,
            Blocks.COBBLED_DEEPSLATE_SLAB,
            Blocks.POLISHED_DEEPSLATE_SLAB,
            Blocks.DEEPSLATE_TILE_SLAB,
            Blocks.DEEPSLATE_BRICK_SLAB
        ).filterIsInstance<SlabBlock>()
    }

    private fun vanillaStairs(): List<StairBlock> {
        return listOf(
            Blocks.OAK_STAIRS,
            Blocks.SPRUCE_STAIRS,
            Blocks.BIRCH_STAIRS,
            Blocks.JUNGLE_STAIRS,
            Blocks.ACACIA_STAIRS,
            Blocks.DARK_OAK_STAIRS,
            Blocks.MANGROVE_STAIRS,
            Blocks.CHERRY_STAIRS,
            Blocks.BAMBOO_STAIRS,
            Blocks.BAMBOO_MOSAIC_STAIRS,
            Blocks.PALE_OAK_STAIRS,
            Blocks.STONE_STAIRS,
            Blocks.COBBLESTONE_STAIRS,
            Blocks.BRICK_STAIRS,
            Blocks.STONE_BRICK_STAIRS,
            Blocks.MUD_BRICK_STAIRS,
            Blocks.NETHER_BRICK_STAIRS,
            Blocks.SANDSTONE_STAIRS,
            Blocks.QUARTZ_STAIRS,
            Blocks.RED_SANDSTONE_STAIRS,
            Blocks.PURPUR_STAIRS,
            Blocks.PRISMARINE_STAIRS,
            Blocks.PRISMARINE_BRICK_STAIRS,
            Blocks.DARK_PRISMARINE_STAIRS,
            Blocks.POLISHED_GRANITE_STAIRS,
            Blocks.SMOOTH_RED_SANDSTONE_STAIRS,
            Blocks.MOSSY_STONE_BRICK_STAIRS,
            Blocks.POLISHED_DIORITE_STAIRS,
            Blocks.MOSSY_COBBLESTONE_STAIRS,
            Blocks.END_STONE_BRICK_STAIRS,
            Blocks.SMOOTH_SANDSTONE_STAIRS,
            Blocks.SMOOTH_QUARTZ_STAIRS,
            Blocks.GRANITE_STAIRS,
            Blocks.ANDESITE_STAIRS,
            Blocks.RED_NETHER_BRICK_STAIRS,
            Blocks.POLISHED_ANDESITE_STAIRS,
            Blocks.DIORITE_STAIRS,
            Blocks.BLACKSTONE_STAIRS,
            Blocks.POLISHED_BLACKSTONE_STAIRS,
            Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS,
            Blocks.COBBLED_DEEPSLATE_STAIRS,
            Blocks.POLISHED_DEEPSLATE_STAIRS,
            Blocks.DEEPSLATE_TILE_STAIRS,
            Blocks.DEEPSLATE_BRICK_STAIRS
        ).filterIsInstance<StairBlock>()
    }
}
