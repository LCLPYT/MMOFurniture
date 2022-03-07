package work.lclpnet.mmofurniture.client.render.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.math.Vec3f;
import work.lclpnet.mmofurniture.block.DoorMatBlock;
import work.lclpnet.mmofurniture.blockentity.DoorMatBlockEntity;

import java.util.List;

public class DoorMatBlockEntityRenderer implements BlockEntityRenderer<DoorMatBlockEntity> {

    private final TextRenderer textRenderer;

    public DoorMatBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.textRenderer = context.getTextRenderer();
    }

    @Override
    public void render(DoorMatBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getMessage() != null) {
            BlockState state = entity.getCachedState();
            if (state.getBlock() instanceof DoorMatBlock) {
                matrices.push();

                matrices.translate(0.5, 0.0626, 0.5);

                int rotation = state.get(DoorMatBlock.DIRECTION).getHorizontal();
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90F * rotation + 180F));
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90F));

                matrices.scale(1.0F, -1.0F, -1.0F);
                matrices.scale(0.0125F, 0.0125F, 0.0125F);

                List<OrderedText> lines = textRenderer.wrapLines(StringVisitable.plain(entity.getMessage()), 60);
                matrices.translate(0.0, -(lines.size() * textRenderer.fontHeight - 1.0) / 2.0, 0);

                for (int j = 0; j < lines.size(); j++) {
                    matrices.push();
                    matrices.translate(-textRenderer.getWidth(lines.get(j)) / 2.0, (j * textRenderer.fontHeight), 0.0);
                    textRenderer.draw(lines.get(j), 0, 0, overlay, false, matrices.peek().getPositionMatrix(), vertexConsumers, false, 0, light);
                    matrices.pop();
                }
                matrices.pop();
            }
        }
    }
}
