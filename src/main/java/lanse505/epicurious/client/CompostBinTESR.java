package lanse505.epicurious.client;

import com.mojang.blaze3d.platform.GlStateManager;
import lanse505.epicurious.content.farming.composting.CompostBinTile;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

public class CompostBinTESR extends TileEntityRenderer<CompostBinTile> {

    @Override
    public void render(CompostBinTile te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushTextureAttributes();
        GlStateManager.pushLightingAttributes();
        GlStateManager.pushMatrix();

        GlStateManager.translated(x, y, z);
        GlStateManager.disableRescaleNormal();
        renderItem(te);

        GlStateManager.popAttributes();
        GlStateManager.popMatrix();
    }

    private void renderItem(CompostBinTile te) {

    }
}
