package us.mathewtech

import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import org.slf4j.LoggerFactory
import us.mathewtech.interaction.CarpetInteractionHandler
import us.mathewtech.registry.ModBlocks
import us.mathewtech.registry.ModCreativeModeTabs
import us.mathewtech.registry.ModCriteria
import us.mathewtech.registry.ModDataComponents

object CarpetedMod : ModInitializer {
	const val MOD_ID: String = "carpeted-mod"

	private val LOGGER = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		ModDataComponents.initialize()
		ModCriteria.initialize()
		ModBlocks.initialize()
		ModCreativeModeTabs.initialize()
		CarpetInteractionHandler.initialize()
		LOGGER.info("Initialized carpeted blocks")
	}

	fun id(path: String): Identifier
		= Identifier.fromNamespaceAndPath(MOD_ID, path)
}
