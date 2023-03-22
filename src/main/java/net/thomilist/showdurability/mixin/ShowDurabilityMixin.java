package net.thomilist.showdurability.mixin;

import net.thomilist.showdurability.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.jetbrains.annotations.Nullable;
import java.lang.String;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.SynchronousResourceReloader;

@Mixin(ItemRenderer.class)
public abstract class ShowDurabilityMixin implements SynchronousResourceReloader
{
    @Inject(at = @At("TAIL"), method = "renderGuiItemOverlay(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
    public void renderGuiItemOverlay(MatrixStack matrices, TextRenderer textRenderer, ItemStack stack, int x, int y, @Nullable String countLabel, CallbackInfo info)
    {
        if (Settings.getVisibility())
        {
            matrices.push();

            if (stack.getCount() == 1 && stack.isDamageable())
            {
                // The factor used to scale the text size and move it accordingly.
                // A factor of 2.0f means the text will be half the original size.
                float scaleFactor = 2.0f;

                String durability = String.valueOf(stack.getMaxDamage() - stack.getDamage());
                matrices.translate(0.0, 0.0, 200.0f);
                matrices.scale(1.0f / scaleFactor, 1.0f / scaleFactor, 1);
                VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
                textRenderer.draw(
                    durability,
                    (float)(scaleFactor * x + (16 / scaleFactor) + 5 + 19 - 2 - textRenderer.getWidth(durability)),
                    (float)(scaleFactor * y + (16 / scaleFactor) + 1 + 6 + 3),
                    0xFFFFFF,
                    true,
                    matrices.peek().getPositionMatrix(),
                    immediate,
                    TextRenderer.TextLayerType.NORMAL,
                    0,
                    LightmapTextureManager.MAX_LIGHT_COORDINATE);
                immediate.draw();
            }

            matrices.pop();
        }
    }
}
