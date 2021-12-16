package work.lclpnet.mmofurniture.util;

import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class from the Furniture mod.
 * @author MrCrayfish
 */
public class VoxelShapeHelper
{
    public static VoxelShape combineAll(Collection<VoxelShape> shapes)
    {
        VoxelShape result = VoxelShapes.empty();
        for(VoxelShape shape : shapes)
        {
            result = VoxelShapes.combine(result, shape, BooleanBiFunction.OR);
        }
        return result.simplify();
    }

    public static VoxelShape setMaxHeight(VoxelShape source, double height)
    {
        AtomicReference<VoxelShape> result = new AtomicReference<>(VoxelShapes.empty());
        source.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) ->
        {
            VoxelShape shape = VoxelShapes.cuboid(minX, minY, minZ, maxX, height, maxZ);
            result.set(VoxelShapes.combine(result.get(), shape, BooleanBiFunction.OR));
        });
        return result.get().simplify();
    }

    public static VoxelShape limitHorizontal(VoxelShape source)
    {
        AtomicReference<VoxelShape> result = new AtomicReference<>(VoxelShapes.empty());
        source.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) ->
        {
            VoxelShape shape = VoxelShapes.cuboid(limit(minX), minY, limit(minZ), limit(maxX), maxY, limit(maxZ));
            result.set(VoxelShapes.combine(result.get(), shape, BooleanBiFunction.OR));
        });
        return result.get().simplify();
    }

    public static VoxelShape[] getRotatedShapes(VoxelShape source)
    {
        VoxelShape shapeNorth = rotate(source, Direction.NORTH);
        VoxelShape shapeEast = rotate(source, Direction.EAST);
        VoxelShape shapeSouth = rotate(source, Direction.SOUTH);
        VoxelShape shapeWest = rotate(source, Direction.WEST);
        return new VoxelShape[] { shapeSouth, shapeWest, shapeNorth, shapeEast };
    }

    public static VoxelShape rotate(VoxelShape source, Direction direction)
    {
        double[] adjustedValues = adjustValues(direction, source.getMin(Direction.Axis.X), source.getMin(Direction.Axis.Z), source.getMax(Direction.Axis.X), source.getMax(Direction.Axis.Z));
        return VoxelShapes.cuboid(adjustedValues[0], source.getMin(Direction.Axis.Y), adjustedValues[1], adjustedValues[2], source.getMax(Direction.Axis.Y), adjustedValues[3]);
    }

    private static double[] adjustValues(Direction direction, double var1, double var2, double var3, double var4)
    {
        switch(direction)
        {
            case WEST:
                double var_temp_1 = var1;
                var1 = 1.0F - var3;
                double var_temp_2 = var2;
                var2 = 1.0F - var4;
                var3 = 1.0F - var_temp_1;
                var4 = 1.0F - var_temp_2;
                break;
            case NORTH:
                double var_temp_3 = var1;
                var1 = var2;
                var2 = 1.0F - var3;
                var3 = var4;
                var4 = 1.0F - var_temp_3;
                break;
            case SOUTH:
                double var_temp_4 = var1;
                var1 = 1.0F - var4;
                double var_temp_5 = var2;
                var2 = var_temp_4;
                double var_temp_6 = var3;
                var3 = 1.0F - var_temp_5;
                var4 = var_temp_6;
                break;
            default:
                break;
        }
        return new double[]{var1, var2, var3, var4};
    }

    private static double limit(double value)
    {
        return Math.max(0.0, Math.min(1.0, value));
    }
}
