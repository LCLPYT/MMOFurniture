package work.lclpnet.mmofurniture.module;

import net.minecraft.item.ItemGroup;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.RockPathBlock;

public class RockPathModule implements IModule {

    @Override
    public void register() {
        new MMOBlockRegistrar(new RockPathBlock())
                .register(MMOFurniture.identifier("rock_path"), ItemGroup.DECORATIONS);
    }
}
