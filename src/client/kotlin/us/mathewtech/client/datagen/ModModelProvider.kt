package us.mathewtech.client.datagen

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.MultiVariant
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.properties.Half
import net.minecraft.world.level.block.state.properties.SlabType
import net.minecraft.world.level.block.state.properties.StairsShape
import us.mathewtech.CarpetedMod
import us.mathewtech.block.CarpetedSlabBlock
import us.mathewtech.block.CarpetedStairBlock
import us.mathewtech.registry.ModBlocks
import java.util.Optional

class ModModelProvider(output: FabricPackOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockModelGenerators: BlockModelGenerators) {
        ModBlocks.slabsByBase.forEach { (base, carpeted) ->
            createSlabModels(blockModelGenerators, base, carpeted)
        }

        ModBlocks.stairsByBase.forEach { (base, carpeted) ->
            createStairModels(blockModelGenerators, base, carpeted)
        }
    }

    override fun generateItemModels(itemModelGenerators: ItemModelGenerators) {
        ModBlocks.slabsByBase.values.forEach { block ->
            itemModelGenerators.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId(block, "white")))
        }

        ModBlocks.stairsByBase.values.forEach { block ->
            itemModelGenerators.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(modelId(block, "white")))
        }
    }

    private fun createSlabModels(generators: BlockModelGenerators, base: SlabBlock, block: CarpetedSlabBlock) {
        val bottomByColor = DyeColor.entries.associateWith { color ->
            val model = SLAB_TEMPLATE.create(
                modelId(block, color.getName()),
                textureMapping(base, color),
                generators.modelOutput
            )
            BlockModelGenerators.plainVariant(model)
        }
        val topByColor = DyeColor.entries.associateWith { color ->
            val model = TOP_SLAB_TEMPLATE.create(
                modelId(block, "${color.getName()}_top"),
                textureMapping(base, color),
                generators.modelOutput
            )
            BlockModelGenerators.plainVariant(model)
        }
        val doubleByColor = DyeColor.entries.associateWith { color ->
            val model = DOUBLE_SLAB_TEMPLATE.create(
                modelId(block, "${color.getName()}_double"),
                textureMapping(base, color),
                generators.modelOutput
            )
            BlockModelGenerators.plainVariant(model)
        }

        generators.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(block).with(
                PropertyDispatch.initial(CarpetedSlabBlock.CARPET, SlabBlock.TYPE).generate { color, type ->
                    when (type) {
                        SlabType.BOTTOM -> bottomByColor.getValue(color)
                        SlabType.TOP -> topByColor.getValue(color)
                        SlabType.DOUBLE -> doubleByColor.getValue(color)
                    }
                }
            )
        )
    }

    private fun createStairModels(generators: BlockModelGenerators, base: StairBlock, block: CarpetedStairBlock) {
        val straightByColor = DyeColor.entries.associateWith { color ->
            BlockModelGenerators.plainVariant(
                STAIR_TEMPLATE.create(modelId(block, color.getName()), textureMapping(base, color), generators.modelOutput)
            )
        }
        val innerByColor = DyeColor.entries.associateWith { color ->
            BlockModelGenerators.plainVariant(
                INNER_STAIR_TEMPLATE.create(modelId(block, "${color.getName()}_inner"), textureMapping(base, color), generators.modelOutput)
            )
        }
        val outerByColor = DyeColor.entries.associateWith { color ->
            BlockModelGenerators.plainVariant(
                OUTER_STAIR_TEMPLATE.create(modelId(block, "${color.getName()}_outer"), textureMapping(base, color), generators.modelOutput)
            )
        }

        generators.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(block).with(
                PropertyDispatch.initial(
                    CarpetedStairBlock.CARPET,
                    StairBlock.FACING,
                    StairBlock.HALF,
                    StairBlock.SHAPE
                ).generate { color, facing, half, shape ->
                    stairVariant(
                        straightByColor.getValue(color),
                        innerByColor.getValue(color),
                        outerByColor.getValue(color),
                        facing,
                        half,
                        shape
                    )
                }
            )
        )
    }

    private fun textureMapping(base: Block, carpetColor: DyeColor): TextureMapping {
        val carpet = Blocks.CARPET.pick(carpetColor)
        return TextureMapping.cubeBottomTop(base)
            .put(CARPET, TextureMapping.getBlockTexture(carpet))
    }

    private fun stairVariant(
        straight: MultiVariant,
        inner: MultiVariant,
        outer: MultiVariant,
        facing: Direction,
        half: Half,
        shape: StairsShape
    ): MultiVariant {
        var variant = when (shape) {
            StairsShape.STRAIGHT -> straight
            StairsShape.INNER_LEFT, StairsShape.INNER_RIGHT -> inner
            StairsShape.OUTER_LEFT, StairsShape.OUTER_RIGHT -> outer
        }

        if (half == Half.TOP) {
            variant = variant.with(BlockModelGenerators.X_ROT_180)
        }

        rotationFor(facing, shape)?.let {
            variant = variant.with(it)
        }

        return variant.with(BlockModelGenerators.UV_LOCK)
    }

    private fun rotationFor(facing: Direction, shape: StairsShape) = when (shape) {
        StairsShape.STRAIGHT, StairsShape.INNER_RIGHT, StairsShape.OUTER_RIGHT -> when (facing) {
            Direction.EAST -> null
            Direction.SOUTH -> BlockModelGenerators.Y_ROT_90
            Direction.WEST -> BlockModelGenerators.Y_ROT_180
            Direction.NORTH -> BlockModelGenerators.Y_ROT_270
            else -> null
        }
        StairsShape.INNER_LEFT, StairsShape.OUTER_LEFT -> when (facing) {
            Direction.SOUTH -> null
            Direction.WEST -> BlockModelGenerators.Y_ROT_90
            Direction.NORTH -> BlockModelGenerators.Y_ROT_180
            Direction.EAST -> BlockModelGenerators.Y_ROT_270
            else -> null
        }
    }

    companion object {
        private val CARPET: TextureSlot = TextureSlot.create("carpet")

        private val SLAB_TEMPLATE = ModelTemplate(
            Optional.of(CarpetedMod.id("block/carpeted_slab")),
            Optional.empty(),
            TextureSlot.BOTTOM,
            TextureSlot.TOP,
            TextureSlot.SIDE,
            CARPET
        )
        private val TOP_SLAB_TEMPLATE = ModelTemplate(
            Optional.of(CarpetedMod.id("block/top_carpeted_slab")),
            Optional.empty(),
            TextureSlot.BOTTOM,
            TextureSlot.TOP,
            TextureSlot.SIDE,
            CARPET
        )
        private val DOUBLE_SLAB_TEMPLATE = ModelTemplate(
            Optional.of(CarpetedMod.id("block/double_carpeted_slab")),
            Optional.empty(),
            TextureSlot.BOTTOM,
            TextureSlot.TOP,
            TextureSlot.SIDE,
            CARPET
        )
        private val STAIR_TEMPLATE = ModelTemplate(
            Optional.of(CarpetedMod.id("block/carpeted_stairs")),
            Optional.empty(),
            TextureSlot.BOTTOM,
            TextureSlot.TOP,
            TextureSlot.SIDE,
            CARPET
        )
        private val INNER_STAIR_TEMPLATE = ModelTemplate(
            Optional.of(CarpetedMod.id("block/inner_carpeted_stairs")),
            Optional.empty(),
            TextureSlot.BOTTOM,
            TextureSlot.TOP,
            TextureSlot.SIDE,
            CARPET
        )
        private val OUTER_STAIR_TEMPLATE = ModelTemplate(
            Optional.of(CarpetedMod.id("block/outer_carpeted_stairs")),
            Optional.empty(),
            TextureSlot.BOTTOM,
            TextureSlot.TOP,
            TextureSlot.SIDE,
            CARPET
        )

        private fun modelId(block: Block, suffix: String): Identifier {
            val blockId = BuiltInRegistries.BLOCK.getKey(block)
            return Identifier.fromNamespaceAndPath(blockId.namespace, "block/${blockId.path}_$suffix")
        }
    }
}
