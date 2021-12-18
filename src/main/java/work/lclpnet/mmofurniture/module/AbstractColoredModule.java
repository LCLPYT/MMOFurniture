package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;

public abstract class AbstractColoredModule implements IModule {

    @Override
    public void register() {
        Arrays.stream(DyeColor.values()).map(DyeColor::getName).forEach(colorName -> {
            Block parent = Registry.BLOCK.get(new Identifier(getParentPath(colorName)));
            if (!Blocks.AIR.equals(parent)) registerVariant(colorName, parent);
        });
    }

    protected String getParentPath(String color) {
        return String.format("%s_wool", color);
    }

    protected abstract void registerVariant(String color, Block parent);
}
