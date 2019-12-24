package lanse505.epicurious.client;

import com.mojang.blaze3d.platform.GlStateManager;
import lanse505.epicurious.content.farming.composting.CompostBinTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class CompostBinTESR extends TileEntityRenderer<CompostBinTile> {


    public static void renderBlockModel(World world, BlockPos pos, BlockState state, boolean translateToOrigin) {

        buff().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        if (translateToOrigin) {
            buff().setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
        }
        final BlockRendererDispatcher blockrendererdispatcher = mc().getBlockRendererDispatcher();
        final BlockModelShapes modelShapes = blockrendererdispatcher.getBlockModelShapes();
        final IBakedModel ibakedmodel = modelShapes.getModel(state);
        bind(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        for (final BlockRenderLayer layer : BlockRenderLayer.values()) {
            if (state.getBlock().canRenderInLayer(state, layer)) {
                ForgeHooksClient.setRenderLayer(layer);
                blockrendererdispatcher.getBlockModelRenderer().renderModel(world, ibakedmodel, state, pos, buff(), false, new Random(), 0, EmptyModelData.INSTANCE);
            }
        }
        ForgeHooksClient.setRenderLayer(null);
        if (translateToOrigin) {
            buff().setTranslation(0, 0, 0);
        }
        Tessellator.getInstance().draw();
    }

    public static void bind(ResourceLocation texture) {

        mc().getTextureManager().bindTexture(texture);
    }

    public static BufferBuilder buff() {

        return tess().getBuffer();
    }

    public static Tessellator tess() {

        return Tessellator.getInstance();
    }

    public static Minecraft mc() {

        return Minecraft.getInstance();
    }

    @Override
    public void render(CompostBinTile te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();

        GlStateManager.translated(x, y, z);
        renderFillAmount(te, partialTicks);

        GlStateManager.popMatrix();
    }

    private void renderFillAmount(CompostBinTile te, float partialTicks) {
        if (te.getCompost() >= 1) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.pushMatrix();

            GlStateManager.translatef(1.5f / 16f, 1f / 16f, 1.5f / 16f);
            GlStateManager.scalef(13f / 16f, (te.getCompost() + partialTicks) * 0.0085f, 13f / 16f);

            renderBlockModel(getWorld(), te.getPos(), Blocks.COARSE_DIRT.getDefaultState(), true);

            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
        }
    }
}
