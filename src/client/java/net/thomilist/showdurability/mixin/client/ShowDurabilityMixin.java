package net.thomilist.showdurability.mixin.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.thomilist.showdurability.Settings;
import net.thomilist.showdurability.access.ShowDurabilityAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class ShowDurabilityMixin implements ShowDurabilityAccess
{
    // The factor used to scale the text size and move it accordingly.
    // A factor of 2 means the text will be half the original size.
    @Unique
    private static final int SCALE_FACTOR = 2;

    @Unique
    boolean is_tab_icon = false;

    @Shadow
    public abstract MatrixStack getMatrices();

    @Shadow
    public abstract int drawText(TextRenderer textRenderer, @Nullable String text, int x, int y, int color, boolean shadow);

    @Override
    public void show_durability$setTabIconState(boolean is_tab_icon)
    {
        this.is_tab_icon = is_tab_icon;
    }

    @Override
    public boolean show_durability$isTabIcon()
    {
        return this.is_tab_icon;
    }

    @Inject(at = @At("TAIL"), method = "drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
    public void drawStackOverlay(TextRenderer textRenderer, ItemStack stack, int x, int y, @Nullable String countOverride, CallbackInfo info)
    {
        if (Settings.getVisibility() && !show_durability$isTabIcon())
        {
            MatrixStack matrices = getMatrices();
            matrices.push();

            if (stack.getCount() == 1 && stack.isDamageable())
            {
                String durability = String.valueOf(stack.getMaxDamage() - stack.getDamage());
                matrices.translate(0.0, 0.0, 200.0f);
                matrices.scale(1.0f / SCALE_FACTOR, 1.0f / SCALE_FACTOR, 1);
                final int textX = SCALE_FACTOR * x + (16 / SCALE_FACTOR) + 5 + 19 - 2 - textRenderer.getWidth(durability);
                final int textY = SCALE_FACTOR * y + (16 / SCALE_FACTOR) + 1 + 6 + 3;
                drawText(textRenderer, durability, textX, textY, 0xFFFFFF, true);
            }

            matrices.pop();
        }
    }
}
