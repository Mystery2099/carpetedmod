package us.mathewtech.interaction

import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.core.component.DataComponents
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Half
import net.minecraft.world.phys.BlockHitResult
import us.mathewtech.block.CarpetSurface
import us.mathewtech.block.CarpetedSlabBlock
import us.mathewtech.block.CarpetedStairBlock
import us.mathewtech.registry.ModBlocks
import us.mathewtech.registry.ModItemTags
import us.mathewtech.util.CarpetColorUtil
import us.mathewtech.util.StateCopyUtil

object CarpetInteractionHandler {
    fun initialize() {
        UseBlockCallback.EVENT.register(::interact)
    }

    private fun interact(player: Player, level: Level, hand: InteractionHand, hitResult: BlockHitResult): InteractionResult {
        val pos = hitResult.blockPos
        val state = level.getBlockState(pos)
        val stack = player.getItemInHand(hand)

        if (stack.`is`(ModItemTags.CARPET_REMOVERS)) {
            return removeCarpet(player, level, hand, state, hitResult)
        }

        stack.get(DataComponents.DYE)?.let { dyeColor ->
            return dyeCarpet(player, level, hand, state, hitResult, dyeColor)
        }

        val carpetColor = CarpetColorUtil.colorFromCarpetItem(stack.item) ?: return InteractionResult.PASS
        return applyCarpet(player, level, hand, state, hitResult, carpetColor)
    }

    private fun applyCarpet(
        player: Player,
        level: Level,
        hand: InteractionHand,
        state: BlockState,
        hitResult: BlockHitResult,
        carpetColor: DyeColor
    ): InteractionResult {
        val newState = when (val block = state.block) {
            is CarpetedSlabBlock,
            is CarpetedStairBlock -> return InteractionResult.PASS
            else -> {
                val slab = ModBlocks.carpetedSlabFor(block)
                val stair = ModBlocks.carpetedStairFor(block)

                when {
                    slab != null && StateCopyUtil.isSupportedSlabState(state) -> {
                        if (!canPlaceOnSlabSurface(hitResult.direction)) return InteractionResult.PASS

                        StateCopyUtil.copySlabToCarpeted(state, slab, carpetColor, CarpetSurface.TOP)
                    }
                    stair != null -> {
                        if (!canPlaceOnStairTread(state, hitResult.direction)) return InteractionResult.PASS

                        StateCopyUtil.copyStairsToCarpeted(state, stair, carpetColor, CarpetSurface.TREAD)
                    }
                    else -> return InteractionResult.PASS
                }
            }
        }

        if (level.isClientSide) return InteractionResult.SUCCESS

        level.setBlock(hitResult.blockPos, newState, 3)
        if (!player.isCreative) {
            player.getItemInHand(hand).consume(1, player)
        }
        playWoolPlaceSound(level, player, hitResult)

        return InteractionResult.SUCCESS_SERVER
    }

    private fun dyeCarpet(
        player: Player,
        level: Level,
        hand: InteractionHand,
        state: BlockState,
        hitResult: BlockHitResult,
        dyeColor: DyeColor
    ): InteractionResult {
        val newState = when (state.block) {
            is CarpetedSlabBlock -> {
                if (!canPlaceOnSlabSurface(hitResult.direction)) return InteractionResult.PASS
                if (state.getValue(CarpetedSlabBlock.CARPET) == dyeColor) return InteractionResult.PASS

                state.setValue(CarpetedSlabBlock.CARPET, dyeColor)
            }
            is CarpetedStairBlock -> {
                if (!canPlaceOnStairTread(state, hitResult.direction)) return InteractionResult.PASS
                if (state.getValue(CarpetedStairBlock.CARPET) == dyeColor) return InteractionResult.PASS

                state.setValue(CarpetedStairBlock.CARPET, dyeColor)
            }
            else -> return InteractionResult.PASS
        }

        if (level.isClientSide) return InteractionResult.SUCCESS

        level.setBlock(hitResult.blockPos, newState, 3)
        if (!player.isCreative) {
            player.getItemInHand(hand).consume(1, player)
        }
        playWoolPlaceSound(level, player, hitResult)

        return InteractionResult.SUCCESS_SERVER
    }

    private fun removeCarpet(player: Player, level: Level, hand: InteractionHand, state: BlockState, hitResult: BlockHitResult): InteractionResult {
        val newState = when (val block = state.block) {
            is CarpetedSlabBlock -> StateCopyUtil.copyCarpetedToBaseSlab(state, block)
            is CarpetedStairBlock -> StateCopyUtil.copyCarpetedToBaseStairs(state, block)
            else -> return InteractionResult.PASS
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS
        }

        val carpetStack = carpetStackFrom(state)

        level.setBlock(hitResult.blockPos, newState, 3)
        returnOrDropCarpet(player, carpetStack)
        val toolStack = player.getItemInHand(hand)
        if (!player.isCreative && toolStack.isDamageableItem) {
            toolStack.hurtAndBreak(1, player, hand)
        }
        playShearsSound(level, player, hitResult)

        return InteractionResult.SUCCESS_SERVER
    }

    private fun carpetStackFrom(state: BlockState): ItemStack {
        return when (val block = state.block) {
            is CarpetedSlabBlock -> ItemStack(block.getCarpetItemFromState(state))
            is CarpetedStairBlock -> ItemStack(block.getCarpetItemFromState(state))
            else -> ItemStack.EMPTY
        }
    }

    private fun returnOrDropCarpet(player: Player, stack: ItemStack) {
        if (!player.addItem(stack)) {
            player.drop(stack, false)
        }
    }

    private fun playWoolPlaceSound(level: Level, player: Player, hitResult: BlockHitResult) {
        val soundType = SoundType.WOOL
        level.playSound(
            player,
            hitResult.blockPos,
            soundType.placeSound,
            SoundSource.BLOCKS,
            (soundType.volume + 1.0F) / 2.0F,
            soundType.pitch * 0.8F
        )
    }

    private fun playShearsSound(level: Level, player: Player, hitResult: BlockHitResult) {
        level.playSound(
            player,
            hitResult.blockPos,
            SoundEvents.SHEARS_SNIP,
            SoundSource.BLOCKS,
            1.0F,
            1.0F
        )
    }

    private fun canPlaceOnSlabSurface(face: Direction): Boolean {
        return face == Direction.UP
    }

    private fun canPlaceOnStairTread(state: BlockState, face: Direction): Boolean {
        if (!state.hasProperty(StairBlock.HALF)) {
            return false
        }

        return when (state.getValue(StairBlock.HALF)) {
            Half.BOTTOM -> face == Direction.UP
            Half.TOP -> face == Direction.DOWN
        }
    }
}
