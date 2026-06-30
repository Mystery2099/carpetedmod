package us.mathewtech.client

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import us.mathewtech.client.datagen.ModBlockLootProvider
import us.mathewtech.client.datagen.ModLangProvider
import us.mathewtech.client.datagen.ModModelProvider
import us.mathewtech.registry.ModBlocks

object CarpetedModDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		ModBlocks.initialize()

		val pack = fabricDataGenerator.createPack()
		pack.addProvider(::ModModelProvider)
		pack.addProvider(::ModBlockLootProvider)
		pack.addProvider(::ModLangProvider)
	}
}
