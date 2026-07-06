package us.mathewtech.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory
import us.mathewtech.CarpetedMod
import java.io.Reader
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path

data class CarpetedModConfigData(
    var enableCarpetApplication: Boolean = true,
    var enableCarpetRecoloring: Boolean = true,
    var enableCarpetRemoval: Boolean = true,
    var preserveCarpetedDropsWithSilkTouch: Boolean = true
)

object CarpetedModConfig {
    private val LOGGER = LoggerFactory.getLogger("${CarpetedMod.MOD_ID}/config")
    private val GSON = GsonBuilder().setPrettyPrinting().create()
    private val path: Path = FabricLoader.getInstance().configDir.resolve("${CarpetedMod.MOD_ID}.json")

    @Volatile
    private var data = CarpetedModConfigData()

    fun initialize() {
        data = load()
        save()
    }

    fun snapshot(): CarpetedModConfigData = data.copy()

    fun replace(updated: CarpetedModConfigData) {
        data = updated.copy()
        save()
    }

    fun canApplyCarpet(): Boolean = data.enableCarpetApplication

    fun canRecolorCarpet(): Boolean = data.enableCarpetRecoloring

    fun canRemoveCarpet(): Boolean = data.enableCarpetRemoval

    fun preservesSilkTouchDrops(): Boolean = data.preserveCarpetedDropsWithSilkTouch

    private fun load(): CarpetedModConfigData {
        if (Files.notExists(path)) {
            return CarpetedModConfigData()
        }

        return try {
            Files.newBufferedReader(path).use(::readConfig) ?: CarpetedModConfigData()
        } catch (exception: Exception) {
            LOGGER.warn("Failed to read config from {}", path, exception)
            CarpetedModConfigData()
        }
    }

    private fun save() {
        try {
            Files.createDirectories(path.parent)
            Files.newBufferedWriter(path).use(::writeConfig)
        } catch (exception: Exception) {
            LOGGER.warn("Failed to write config to {}", path, exception)
        }
    }

    private fun readConfig(reader: Reader): CarpetedModConfigData? {
        return try {
            GSON.fromJson(reader, CarpetedModConfigData::class.java)
        } catch (_: JsonParseException) {
            null
        }
    }

    private fun writeConfig(writer: Writer) {
        GSON.toJson(data, writer)
    }
}
