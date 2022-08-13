package net.thomilist.showdurability.mixin;

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
public abstract class ShowDurability implements SynchronousResourceReloader
{
    @Inject(at = @At("TAIL"), method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
    public void renderGuiItemOverlay(TextRenderer renderer, ItemStack stack, int x, int y, @Nullable String countLabel, CallbackInfo info)
    {
        MatrixStack matrixStack = new MatrixStack();

        if (stack.getCount() == 1 && stack.isDamageable())
        {
            // The factor used to scale the text size and move it accordingly.
            // A factor of 2.0f means the text will be half the original size.
            float scaleFactor = 2.0f;
            
            String durability = String.valueOf(stack.getMaxDamage() - stack.getDamage());
            float zOffset = ((ItemRenderer)(Object)this).zOffset;
            matrixStack.translate(0.0, 0.0, zOffset + 200.0f);
            matrixStack.scale(1.0f / scaleFactor, 1.0f / scaleFactor, 1);
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            renderer.draw(
                durability,
                (float)(scaleFactor * x + (16 / scaleFactor) + 5 + 19 - 2 - renderer.getWidth(durability)),
                (float)(scaleFactor * y + (16 / scaleFactor) + 1 + 6 + 3),
                0xFFFFFF,
                true,
                matrixStack.peek().getPositionMatrix(),
                (VertexConsumerProvider)immediate,
                false,
                0,
                LightmapTextureManager.MAX_LIGHT_COORDINATE);
            immediate.draw();
        }
    }
}
