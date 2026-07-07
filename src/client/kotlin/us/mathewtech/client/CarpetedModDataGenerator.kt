package us.mathewtech.client

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import us.mathewtech.client.datagen.ModAdvancementProvider
import us.mathewtech.client.datagen.ModBlockTagProvider
import us.mathewtech.client.datagen.ModItemTagProvider
import us.mathewtech.client.datagen.ModLangProvider
import us.mathewtech.client.datagen.ModModelProvider
import us.mathewtech.client.datagen.ModRecipeProvider
import us.mathewtech.registry.ModBlocks
import us.mathewtech.registry.ModRecipes

object CarpetedModDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		ModBlocks.initialize()
		ModRecipes.initialize()

		val pack = fabricDataGenerator.createPack()
		pack.addProvider(::ModModelProvider)
		pack.addProvider(::ModBlockTagProvider)
		pack.addProvider(::ModItemTagProvider)
		pack.addProvider(::ModRecipeProvider)
		pack.addProvider(::ModAdvancementProvider)
		pack.addProvider(::ModLangProvider)
	}
}
