package us.mathewtech.interaction

import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.advancements.triggers.CriteriaTriggers
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Half
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import us.mathewtech.block.CarpetSurface
import us.mathewtech.block.CarpetedSlabBlock
import us.mathewtech.block.CarpetedStairBlock
import us.mathewtech.config.CarpetedModConfig
import us.mathewtech.registry.ModBlockTags
import us.mathewtech.registry.ModCriteria
import us.mathewtech.registry.ModItemTags
import us.mathewtech.registry.ModRecipes
import us.mathewtech.recipe.AbstractCarpetInteractionRecipe
import us.mathewtech.recipe.CarpetInteractionRecipeInput
import us.mathewtech.util.CarpetColorUtil
import us.mathewtech.util.CarpetedCopperUtil
import us.mathewtech.util.StateCopyUtil

object CarpetInteractionHandler {
    fun initialize() {
        UseBlockCallback.EVENT.register(::interact)
    }

    private fun interact(player: Player, level: Level, hand: InteractionHand, hitResult: BlockHitResult): InteractionResult {
        val stack = player.getItemInHand(hand)
        if (stack.isEmpty) {
            return InteractionResult.PASS
        }

        val state = level.getBlockState(hitResult.blockPos)

        if (isCarpetedBlock(state)) {
            when {
                stack.`is`(Items.HONEYCOMB) -> return waxCarpetedCopper(player, level, hand, state, hitResult)
                stack.item is AxeItem -> return scrapeOrUnwaxCarpetedCopper(player, level, hand, state, hitResult)
            }
        }

        if (stack.`is`(ModItemTags.CARPET_REMOVERS) && CarpetedModConfig.canRemoveCarpet()) {
            return removeCarpet(player, level, hand, state, hitResult)
        }

        if (CarpetedModConfig.canRecolorCarpet()) {
            CarpetColorUtil.colorFromDyeItemStack(stack)?.let { dyeColor ->
                return dyeCarpet(player, level, hand, state, hitResult, dyeColor)
            }
        }

        if (!CarpetedModConfig.canApplyCarpet()) {
            return InteractionResult.PASS
        }

        val carpetColor = CarpetColorUtil.colorFromCarpetItem(stack.item) ?: return InteractionResult.PASS
        return applyCarpet(player, level, hand, state, hitResult, carpetColor)
    }

    private fun waxCarpetedCopper(
        player: Player,
        level: Level,
        hand: InteractionHand,
        state: BlockState,
        hitResult: BlockHitResult
    ): InteractionResult {
        val waxedState = CarpetedCopperUtil.waxedState(state).orElse(null)
        if (waxedState == null) {
            return InteractionResult.PASS
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS
        }

        val stack = player.getItemInHand(hand)
        if (player is ServerPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, hitResult.blockPos, stack)
        }
        stack.shrink(1)
        level.setBlock(hitResult.blockPos, waxedState, 11)
        level.gameEvent(GameEvent.BLOCK_CHANGE, hitResult.blockPos, GameEvent.Context.of(player, waxedState))
        level.levelEvent(player, 3003, hitResult.blockPos, 0)

        return InteractionResult.SUCCESS_SERVER
    }

    private fun scrapeOrUnwaxCarpetedCopper(
        player: Player,
        level: Level,
        hand: InteractionHand,
        state: BlockState,
        hitResult: BlockHitResult
    ): InteractionResult {
        val scraped = CarpetedCopperUtil.previousOxidationState(state)
        val unwaxed = CarpetedCopperUtil.unwaxedState(state)
        val newState = when {
            scraped.isPresent -> scraped.get()
            unwaxed.isPresent -> unwaxed.get()
            else -> return InteractionResult.PASS
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS
        }

        level.setBlock(hitResult.blockPos, newState, 11)
        level.gameEvent(GameEvent.BLOCK_CHANGE, hitResult.blockPos, GameEvent.Context.of(player, newState))

        val sound = when {
            scraped.isPresent -> SoundEvents.AXE_SCRAPE
            else -> SoundEvents.AXE_WAX_OFF
        }
        val particle = when {
            scraped.isPresent -> 3005
            else -> 3004
        }
        level.playSound(player, hitResult.blockPos, sound, SoundSource.BLOCKS, 1.0F, 1.0F)
        level.levelEvent(player, particle, hitResult.blockPos, 0)

        val stack = player.getItemInHand(hand)
        if (!player.isCreative && stack.isDamageableItem) {
            stack.hurtAndBreak(1, player, hand)
        }

        return InteractionResult.SUCCESS_SERVER
    }

    private fun applyCarpet(
        player: Player,
        level: Level,
        hand: InteractionHand,
        state: BlockState,
        hitResult: BlockHitResult,
        carpetColor: DyeColor
    ): InteractionResult {
        if (state.`is`(ModBlockTags.DISABLE_CARPETING)) {
            return InteractionResult.PASS
        }

        if (level.isClientSide) {
            return if (canApplyCarpet(state, hitResult.direction)) InteractionResult.SUCCESS else InteractionResult.PASS
        }

        val recipe = recipeFor(ModRecipes.CARPETING_TYPE, level, state, player.getItemInHand(hand))
            ?: return InteractionResult.PASS
        val newState = carpetedStateFor(state, recipe.resultBlock(), hitResult.direction, carpetColor)
            ?: return InteractionResult.PASS

        return finishConsumableCarpetChange(player, level, hand, hitResult, newState, ModCriteria::appliedCarpet)
    }

    private fun dyeCarpet(
        player: Player,
        level: Level,
        hand: InteractionHand,
        state: BlockState,
        hitResult: BlockHitResult,
        dyeColor: DyeColor
    ): InteractionResult {
        if (state.`is`(ModBlockTags.DISABLE_RECOLORING)) {
            return InteractionResult.PASS
        }

        if (level.isClientSide) {
            return if (canDyeCarpet(state, hitResult.direction)) InteractionResult.SUCCESS else InteractionResult.PASS
        }

        val recipe = recipeFor(ModRecipes.RECOLORING_TYPE, level, state, player.getItemInHand(hand))
            ?: return InteractionResult.PASS
        val newState = recoloredStateFor(state, recipe.resultBlock(), hitResult.direction, dyeColor)
            ?: return InteractionResult.PASS

        return finishConsumableCarpetChange(player, level, hand, hitResult, newState, ModCriteria::dyedCarpet)
    }

    private fun removeCarpet(player: Player, level: Level, hand: InteractionHand, state: BlockState, hitResult: BlockHitResult): InteractionResult {
        if (state.`is`(ModBlockTags.DISABLE_REMOVAL)) {
            return InteractionResult.PASS
        }

        if (level.isClientSide) {
            return if (canRemoveCarpet(state)) InteractionResult.SUCCESS else InteractionResult.PASS
        }

        val recipe = recipeFor(ModRecipes.REMOVAL_TYPE, level, state, player.getItemInHand(hand))
            ?: return InteractionResult.PASS
        val newState = removedCarpetStateFor(state, recipe.resultBlock()) ?: return InteractionResult.PASS

        val carpetStack = carpetStackFrom(state)

        level.setBlock(hitResult.blockPos, newState, 3)
        player.inventory.placeItemBackInInventory(carpetStack)
        val toolStack = player.getItemInHand(hand)
        if (!player.isCreative && toolStack.isDamageableItem) {
            toolStack.hurtAndBreak(1, player, hand)
        }
        if (player is ServerPlayer) {
            ModCriteria.removedCarpet(player)
        }
        playShearsSound(level, player, hitResult)

        return InteractionResult.SUCCESS_SERVER
    }

    private fun canApplyCarpet(state: BlockState, face: Direction): Boolean {
        return when (state.block) {
            is SlabBlock -> canApplyCarpetToSlab(state, face)
            is StairBlock -> canApplyCarpetToStair(state, face)
            else -> false
        }
    }

    private fun canApplyCarpetToSlab(state: BlockState, face: Direction): Boolean {
        return state.`is`(ModBlockTags.SUPPORTED_BASE_SLABS) &&
            StateCopyUtil.isSupportedSlabState(state) &&
            canPlaceOnSlabSurface(face)
    }

    private fun canApplyCarpetToStair(state: BlockState, face: Direction): Boolean {
        return state.`is`(ModBlockTags.SUPPORTED_BASE_STAIRS) && canPlaceOnStairTread(state, face)
    }

    private fun canDyeCarpet(state: BlockState, face: Direction): Boolean {
        return when (state.block) {
            is CarpetedSlabBlock -> canPlaceOnSlabSurface(face)
            is CarpetedStairBlock -> canPlaceOnStairTread(state, face)
            else -> false
        }
    }

    private fun canRemoveCarpet(state: BlockState): Boolean {
        return isCarpetedBlock(state)
    }

    private fun carpetedStateFor(
        state: BlockState,
        resultBlock: Block,
        face: Direction,
        carpetColor: DyeColor
    ): BlockState? {
        return when (resultBlock) {
            is CarpetedSlabBlock -> {
                if (!canApplyCarpetToSlab(state, face)) return null
                StateCopyUtil.copySlabToCarpeted(state, resultBlock, carpetColor, CarpetSurface.TOP)
            }
            is CarpetedStairBlock -> {
                if (!canApplyCarpetToStair(state, face)) return null
                StateCopyUtil.copyStairsToCarpeted(state, resultBlock, carpetColor, CarpetSurface.TREAD)
            }
            else -> null
        }
    }

    private fun recoloredStateFor(
        state: BlockState,
        resultBlock: Block,
        face: Direction,
        dyeColor: DyeColor
    ): BlockState? {
        return when (resultBlock) {
            is CarpetedSlabBlock -> {
                if (!canPlaceOnSlabSurface(face) || alreadyHasCarpetColor(state, resultBlock, dyeColor)) return null
                StateCopyUtil.copyCarpetedToCarpetedSlab(state, resultBlock, dyeColor)
            }
            is CarpetedStairBlock -> {
                if (!canPlaceOnStairTread(state, face) || alreadyHasCarpetColor(state, resultBlock, dyeColor)) return null
                StateCopyUtil.copyCarpetedToCarpetedStairs(state, resultBlock, dyeColor)
            }
            else -> null
        }
    }

    private fun removedCarpetStateFor(state: BlockState, resultBlock: Block): BlockState? {
        return when (resultBlock) {
            is SlabBlock -> StateCopyUtil.copyCarpetedToBaseSlab(state, resultBlock)
            is StairBlock -> StateCopyUtil.copyCarpetedToBaseStairs(state, resultBlock)
            else -> null
        }
    }

    private fun alreadyHasCarpetColor(state: BlockState, block: CarpetedSlabBlock, color: DyeColor): Boolean {
        return state.block == block && state.getValue(CarpetedSlabBlock.CARPET) == color
    }

    private fun alreadyHasCarpetColor(state: BlockState, block: CarpetedStairBlock, color: DyeColor): Boolean {
        return state.block == block && state.getValue(CarpetedStairBlock.CARPET) == color
    }

    private fun finishConsumableCarpetChange(
        player: Player,
        level: Level,
        hand: InteractionHand,
        hitResult: BlockHitResult,
        newState: BlockState,
        criterion: (ServerPlayer) -> Unit
    ): InteractionResult {
        level.setBlock(hitResult.blockPos, newState, 3)
        if (!player.isCreative) {
            player.getItemInHand(hand).consume(1, player)
        }
        if (player is ServerPlayer) {
            criterion(player)
        }
        playWoolPlaceSound(level, player, hitResult)

        return InteractionResult.SUCCESS_SERVER
    }

    private fun carpetStackFrom(state: BlockState): ItemStack {
        return when (val block = state.block) {
            is CarpetedSlabBlock -> ItemStack(block.getCarpetItemFromState(state))
            is CarpetedStairBlock -> ItemStack(block.getCarpetItemFromState(state))
            else -> ItemStack.EMPTY
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

    private fun isCarpetedBlock(state: BlockState): Boolean {
        return state.block is CarpetedSlabBlock || state.block is CarpetedStairBlock
    }

    private fun canPlaceOnStairTread(state: BlockState, face: Direction): Boolean {
        if (!state.hasProperty(StairBlock.HALF)) {
            return false
        }

        return when (state.getValue(StairBlock.HALF)) {
            Half.BOTTOM -> face == Direction.UP || face == state.getValue(StairBlock.FACING)
            Half.TOP -> face == Direction.DOWN || face == state.getValue(StairBlock.FACING)
        }
    }

    private fun <T : AbstractCarpetInteractionRecipe> recipeFor(
        type: RecipeType<T>,
        level: Level,
        state: BlockState,
        stack: ItemStack
    ): T? {
        return serverRecipeAccess(level)
            .getRecipeFor(type, CarpetInteractionRecipeInput(state, stack), level)
            .orElse(null)
            ?.value()
    }

    private fun serverRecipeAccess(level: Level) = (level as ServerLevel).recipeAccess()
}
