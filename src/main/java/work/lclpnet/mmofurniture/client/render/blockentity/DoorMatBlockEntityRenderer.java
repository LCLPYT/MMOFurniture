package work.lclpnet.mmofurniture.client.render.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import work.lclpnet.mmofurniture.block.DoorMatBlock;
import work.lclpnet.mmofurniture.blockentity.DoorMatBlockEntity;

import java.util.List;

public class DoorMatBlockEntityRenderer extends BlockEntityRenderer<DoorMatBlockEntity> {

    public DoorMatBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(DoorMatBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getMessage() != null) {
            BlockState state = entity.getCachedState();
            if (state.getBlock() instanceof DoorMatBlock) {
                matrices.push();

                matrices.translate(0.5, 0.0626, 0.5);

                int rotation = state.get(DoorMatBlock.DIRECTION).getHorizontal();
                matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90F * rotation + 180F));
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90F));

                matrices.scale(1.0F, -1.0F, -1.0F);
                matrices.scale(0.0125F, 0.0125F, 0.0125F);

                TextRenderer fontRenderer = this.dispatcher.getTextRenderer();
                List<OrderedText> lines = fontRenderer.wrapLines(StringVisitable.plain(entity.getMessage()), 60);
                matrices.translate(0.0, -(lines.size() * fontRenderer.fontHeight - 1.0) / 2.0, 0);

                for (int j = 0; j < lines.size(); j++) {
                    matrices.push();
                    matrices.translate(-fontRenderer.getWidth(lines.get(j)) / 2.0, (j * fontRenderer.fontHeight), 0.0);
                    fontRenderer.draw(lines.get(j), 0, 0, overlay, false, matrices.peek().getModel(), vertexConsumers, false, 0, light);
                    matrices.pop();
                }
                matrices.pop(); //Pop
            }
        }
    }
}
