package work.lclpnet.mmofurniture;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import work.lclpnet.mmofurniture.module.ChairModule;
import work.lclpnet.mmofurniture.module.IModule;
import work.lclpnet.mmofurniture.module.TableModule;

import java.util.Set;

public class MMOFurniture implements ModInitializer {

    public static final String MOD_ID = "mmofurniture";

    public static final Set<IModule> MODULES = ImmutableSet.of(
            new TableModule(),
            new ChairModule()
    );

    @Override
    public void onInitialize() {
        MODULES.forEach(IModule::register);
    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }
}
