package net.thomilist.showdurability.mixin.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.thomilist.showdurability.ShowDurability;
import net.thomilist.showdurability.access.ShowDurabilityAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( DrawContext.class )
public abstract class ShowDurabilityMixin
    implements ShowDurabilityAccess
{
    // The factor used to scale the text size and move it accordingly.
    // A factor of 2 means the text will be half the original size.
    @Unique
    private static final int SCALE_FACTOR = 2;

    @Unique
    boolean isTabIcon = false;

    @Shadow
    public abstract MatrixStack getMatrices();

    @Override
    public void show_durability$setTabIconState( final boolean isTabIcon )
    {
        this.isTabIcon = isTabIcon;
    }

    @Override
    public boolean show_durability$isTabIcon()
    {
        return this.isTabIcon;
    }

    @Inject( at = @At( "TAIL" ),
             method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;" +
                      "IILjava/lang/String;)V" )
    public void drawItemInSlot( final TextRenderer textRenderer,
                                final ItemStack stack,
                                final int x,
                                final int y,
                                @Nullable final String countOverride,
                                final CallbackInfo info )
    {
        if ( ShowDurability.CONFIG.getVisibility() && !this.show_durability$isTabIcon() )
        {
            final MatrixStack matrices = this.getMatrices();
            matrices.push();

            if ( (stack.getCount() == 1) && stack.isDamageable() )
            {
                final String durability = String.valueOf( stack.getMaxDamage() - stack.getDamage() );
                matrices.translate( 0.0, 0.0, 200.0f );
                matrices.scale( 1.0f / ShowDurabilityMixin.SCALE_FACTOR, 1.0f / ShowDurabilityMixin.SCALE_FACTOR, 1 );
                final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                final VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate( buffer );

                final int xAdjusted =
                    ((ShowDurabilityMixin.SCALE_FACTOR * x) + (16 / ShowDurabilityMixin.SCALE_FACTOR) + 5 + 19) - 2 -
                    textRenderer.getWidth( durability );
                final int yAdjusted =
                    (ShowDurabilityMixin.SCALE_FACTOR * y) + (16 / ShowDurabilityMixin.SCALE_FACTOR) + 1 + 6 + 3;

                textRenderer.draw(
                    durability,
                    xAdjusted,
                    yAdjusted,
                    0xFFFFFF,
                    true,
                    matrices.peek().getPositionMatrix(),
                    immediate,
                    TextRenderer.TextLayerType.NORMAL,
                    0,
                    LightmapTextureManager.MAX_LIGHT_COORDINATE
                );

                immediate.draw();
            }

            matrices.pop();
        }
    }
}
