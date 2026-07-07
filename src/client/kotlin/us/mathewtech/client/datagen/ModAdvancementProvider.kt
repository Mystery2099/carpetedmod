package us.mathewtech.client.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.triggers.PlayerTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Items
import us.mathewtech.CarpetedMod
import us.mathewtech.registry.ModCriteria
import us.mathewtech.util.CarpetColorUtil
import java.util.Optional
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class ModAdvancementProvider(
    output: FabricPackOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricAdvancementProvider(output, registriesFuture) {
    override fun generateAdvancement(registryLookup: HolderLookup.Provider, consumer: Consumer<AdvancementHolder>) {
        val background = Identifier.withDefaultNamespace("textures/gui/advancements/backgrounds/adventure.png")

        val root = Advancement.Builder.advancement()
            .display(
                CarpetColorUtil.carpetItem(DyeColor.WHITE),
                Component.translatable("advancements.carpeted-mod.root.title"),
                Component.translatable("advancements.carpeted-mod.root.description"),
                background,
                AdvancementType.TASK,
                false,
                false,
                false
            )
            .addCriterion("applied_carpet", ModCriteria.APPLIED_CARPET.createCriterion(PlayerTrigger.TriggerInstance(Optional.empty())))
            .build(CarpetedMod.id("root"))
        consumer.accept(root)

        val dyeCarpet = Advancement.Builder.advancement()
            .parent(root)
            .display(
                CarpetColorUtil.dyeItem(DyeColor.MAGENTA),
                Component.translatable("advancements.carpeted-mod.dye_carpet.title"),
                Component.translatable("advancements.carpeted-mod.dye_carpet.description"),
                background,
                AdvancementType.TASK,
                false,
                false,
                false
            )
            .addCriterion("dyed_carpet", ModCriteria.DYED_CARPET.createCriterion(PlayerTrigger.TriggerInstance(Optional.empty())))
            .build(CarpetedMod.id("dye_carpet"))
        consumer.accept(dyeCarpet)

        val removeCarpet = Advancement.Builder.advancement()
            .parent(root)
            .display(
                Items.SHEARS,
                Component.translatable("advancements.carpeted-mod.remove_carpet.title"),
                Component.translatable("advancements.carpeted-mod.remove_carpet.description"),
                background,
                AdvancementType.TASK,
                false,
                false,
                false
            )
            .addCriterion("removed_carpet", ModCriteria.REMOVED_CARPET.createCriterion(PlayerTrigger.TriggerInstance(Optional.empty())))
            .build(CarpetedMod.id("remove_carpet"))
        consumer.accept(removeCarpet)

        val silkTouch = Advancement.Builder.advancement()
            .parent(root)
            .display(
                Items.DIAMOND_PICKAXE,
                Component.translatable("advancements.carpeted-mod.silk_touch_carpeted_block.title"),
                Component.translatable("advancements.carpeted-mod.silk_touch_carpeted_block.description"),
                background,
                AdvancementType.TASK,
                false,
                false,
                false
            )
            .addCriterion(
                "silk_touched_carpeted_block",
                ModCriteria.SILK_TOUCHED_CARPETED_BLOCK.createCriterion(PlayerTrigger.TriggerInstance(Optional.empty()))
            )
            .build(CarpetedMod.id("silk_touch_carpeted_block"))
        consumer.accept(silkTouch)
    }
}
