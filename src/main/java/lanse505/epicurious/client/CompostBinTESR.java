package lanse505.epicurious.client;

import com.mojang.blaze3d.platform.GlStateManager;
import lanse505.epicurious.content.farming.composting.CompostBinTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class CompostBinTESR extends TileEntityRenderer<CompostBinTile> {

    public int compost;

    @Override
    public void render(CompostBinTile te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushTextureAttributes();
        GlStateManager.pushMatrix();

        GlStateManager.translated(x, y, z);
        GlStateManager.disableRescaleNormal();
        renderFillAmount(te);

        GlStateManager.popAttributes();
        GlStateManager.popMatrix();
    }

    private void renderFillAmount(CompostBinTile te) {
        ItemStack stack = new ItemStack(Items.COARSE_DIRT);
        compost++;
        if (compost >= 1) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();

            GlStateManager.scalef(1.75f, compost * 0.025f, 1.75f);
            GlStateManager.translatef(0.498f, compost * 0.0085f, 0.498f);

            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);

            GlStateManager.popMatrix();
        }
        if (compost > 100) compost = 0;
    }
}
