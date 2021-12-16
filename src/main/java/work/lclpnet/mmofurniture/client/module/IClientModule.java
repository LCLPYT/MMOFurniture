package work.lclpnet.mmofurniture.client.module;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;

@Environment(EnvType.CLIENT)
public interface IClientModule {

    void registerClient();
}
