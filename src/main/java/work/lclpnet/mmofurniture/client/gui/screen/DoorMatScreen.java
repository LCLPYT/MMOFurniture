package work.lclpnet.mmofurniture.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import work.lclpnet.mmocontent.networking.MMONetworking;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.blockentity.DoorMatBlockEntity;
import work.lclpnet.mmofurniture.network.packet.DoorMatMessagePacket;

public class DoorMatScreen extends Screen {

    private static final Identifier GUI_TEXTURE = MMOFurniture.identifier("textures/gui/door_mat_settings.png");

    protected final int backgroundWith = 176;
    protected final int backgroundHeight = 69;

    private final DoorMatBlockEntity doorMatBlockEntity;
    private TextFieldWidget nameField;
    private ButtonWidget btnSave;

    public DoorMatScreen(DoorMatBlockEntity doorMatBlockEntity) {
        super(new TranslatableText("gui.mmofurniture.door_mat_message"));
        this.doorMatBlockEntity = doorMatBlockEntity;
    }

    @Override
    protected void init() {
        if (this.client == null) return;

        int guiLeft = (this.width - this.backgroundWith) / 2;
        int guiTop = (this.height - this.backgroundHeight) / 2;

        this.nameField = new TextFieldWidget(this.textRenderer, guiLeft + 8, guiTop + 18, 160, 18, LiteralText.EMPTY) {

            @Override
            public void write(String textToWrite) {
                int lines = DoorMatScreen.this.textRenderer.wrapLines(StringVisitable.plain(this.getText() + textToWrite), 60).size();
                if (lines <= 2) super.write(textToWrite);
            }
        };

        if (this.doorMatBlockEntity.getMessage() != null)
            this.nameField.setText(this.doorMatBlockEntity.getMessage());

        this.children.add(this.nameField);

        this.btnSave = this.addButton(new ButtonWidget(guiLeft + 7, guiTop + 42, 79, 20, new TranslatableText("gui.button.mmofurniture.save"), button -> {
            if (this.isValidName()) {
                MMONetworking.sendPacketToServer(new DoorMatMessagePacket(this.doorMatBlockEntity.getPos(), this.nameField.getText()));
                if (this.client.player != null) this.client.player.closeScreen();
            }
        }));
        this.btnSave.active = false;

        this.addButton(new ButtonWidget(guiLeft + 91, guiTop + 42, 79, 20, new TranslatableText("gui.button.mmofurniture.cancel"), button -> this.onClose()));
    }

    @Override
    public void tick() {
        super.tick();
        this.nameField.tick();
        this.btnSave.active = this.isValidName();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.client == null) return;

        this.renderBackground(matrices);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(GUI_TEXTURE);
        int startX = (this.width - this.backgroundWith) / 2;
        int startY = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, startX, startY, 0, 0, this.backgroundWith, this.backgroundHeight);

        super.render(matrices, mouseX, mouseY, delta);

        this.textRenderer.draw(matrices, this.title.getString(), startX + 8.0F, startY + 6.0F, 0x404040);
        this.nameField.render(matrices, mouseX, mouseY, delta);
    }

    private boolean isValidName() {
        int lines = this.textRenderer.wrapLines(Text.of(this.doorMatBlockEntity.getMessage()), 45).size();
        return !this.nameField.getText().equals(this.doorMatBlockEntity.getMessage()) && !this.nameField.getText().trim().isEmpty() && lines < 2;
    }
}
