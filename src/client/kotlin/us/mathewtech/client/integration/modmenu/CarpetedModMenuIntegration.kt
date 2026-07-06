package us.mathewtech.client.integration.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.fabricmc.loader.api.FabricLoader
import us.mathewtech.client.integration.cloth.CarpetedClothConfigScreen

class CarpetedModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*>? {
        if (!FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return null
        }

        return ConfigScreenFactory(CarpetedClothConfigScreen::create)
    }
}
