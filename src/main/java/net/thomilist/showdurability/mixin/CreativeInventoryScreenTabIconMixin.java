package net.thomilist.showdurability.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen.CreativeScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenTexts;
import net.thomilist.showdurability.access.ShowDurabilityAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenTabIconMixin extends AbstractInventoryScreen<CreativeScreenHandler>
{
    public CreativeInventoryScreenTabIconMixin(PlayerEntity player, FeatureSet enabledFeatures, boolean operatorTabEnabled)
    {
        super(new CreativeInventoryScreen.CreativeScreenHandler(player), player.getInventory(), ScreenTexts.EMPTY);
    }

    @Inject(at = @At("HEAD"), method = "renderTabIcon(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/item/ItemGroup;)V")
    protected void renderTabIconStart(DrawContext context, ItemGroup group, CallbackInfo ci)
    {
        ((ShowDurabilityAccess)context).setTabIconState(true);
    }

    @Inject(at = @At("RETURN"), method = "renderTabIcon(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/item/ItemGroup;)V")
    protected void renderTabIconEnd(DrawContext context, ItemGroup group, CallbackInfo ci)
    {
        ((ShowDurabilityAccess)context).setTabIconState(false);
    }
}
