package work.lclpnet.mmofurniture.util;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class CommonUtil {

    public static final Set<String> WOOD_TYPES = ImmutableSet.of(
            "oak",
            "spruce",
            "birch",
            "jungle",
            "acacia",
            "dark_oak",
            "crimson",
            "warped"
    );

    public static final Set<String> STONE_TYPES = ImmutableSet.of(
            "stone",
            "granite",
            "diorite",
            "andesite"
    );
}
