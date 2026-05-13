package net.thomilist.showdurability.mixin.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.ItemPickerMenu;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.thomilist.showdurability.access.ShowDurabilityAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( CreativeModeInventoryScreen.class )
public abstract class CreativeModeInventoryScreenTabIconMixin
    extends AbstractContainerScreen<ItemPickerMenu>
{
    @SuppressWarnings( "unused" )
    protected CreativeModeInventoryScreenTabIconMixin(
        final Player player,
        final FeatureFlagSet enabledFeatures,
        final boolean operatorTabEnabled )
    {
        super(
            new CreativeModeInventoryScreen.ItemPickerMenu( player ),
            player.getInventory(),
            CommonComponents.EMPTY
        );
    }

    @Inject( at = @At( "HEAD" ),
             method = "extractLabels(Lnet/minecraft/client/gui/GuiGraphicsExtractor;II)V" )
    protected void renderTabIconStart(
        final GuiGraphicsExtractor context,
        final int mouseX,
        final int mouseY,
        final CallbackInfo ci )
    {
        ((ShowDurabilityAccess) context).show_durability$setTabIconState( true );
    }

    @Inject( at = @At( "RETURN" ),
             method = "extractLabels(Lnet/minecraft/client/gui/GuiGraphicsExtractor;II)V" )
    protected void renderTabIconEnd(
        final GuiGraphicsExtractor context,
        final int mouseX,
        final int mouseY,
        final CallbackInfo ci )
    {
        ((ShowDurabilityAccess) context).show_durability$setTabIconState( false );
    }
}
