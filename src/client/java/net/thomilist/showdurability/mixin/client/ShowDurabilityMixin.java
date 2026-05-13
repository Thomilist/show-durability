package net.thomilist.showdurability.mixin.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.ItemStack;
import net.thomilist.showdurability.ShowDurability;
import net.thomilist.showdurability.access.ShowDurabilityAccess;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( GuiGraphicsExtractor.class )
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
    public abstract Matrix3x2fStack pose();

    @Shadow
    public abstract void text(
        final Font font,
        final @Nullable String str,
        final int x,
        final int y,
        final int color,
        final boolean dropShadow );

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
             method = "itemCount(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;" +
                 "IILjava/lang/String;)V" )
    public void itemCount(
        final Font font,
        final ItemStack itemStack,
        final int x,
        final int y,
        final @Nullable String countText,
        final CallbackInfo info )
    {
        if ( ShowDurability.CONFIG.getVisibility() && !this.show_durability$isTabIcon() )
        {
            final Matrix3x2fStack pose = this.pose();
            pose.pushMatrix();

            if ( (itemStack.getCount() == 1) && itemStack.isDamageableItem() )
            {
                final String durability = String.valueOf( itemStack.getMaxDamage() - itemStack.getDamageValue() );
                pose.translate( 0.0f, 0.0f );
                pose.scale( 1.0f / ShowDurabilityMixin.SCALE_FACTOR, 1.0f / ShowDurabilityMixin.SCALE_FACTOR );
                final int textX =
                    ((ShowDurabilityMixin.SCALE_FACTOR * x) + (16 / ShowDurabilityMixin.SCALE_FACTOR) + 5 + 19) - 2 -
                        font.width( durability );
                final int textY = (ShowDurabilityMixin.SCALE_FACTOR * y) + (16 / ShowDurabilityMixin.SCALE_FACTOR) + 1 +
                    6 + 3;
                this.text( font, durability, textX, textY, CommonColors.WHITE, true );
            }

            pose.popMatrix();
        }
    }
}
