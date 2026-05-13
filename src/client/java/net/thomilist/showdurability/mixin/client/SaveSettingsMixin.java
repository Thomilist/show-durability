package net.thomilist.showdurability.mixin.client;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.WindowEventHandler;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.thomilist.showdurability.ShowDurability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Minecraft.class )
public abstract class SaveSettingsMixin
    extends ReentrantBlockableEventLoop<Runnable>
    implements WindowEventHandler
{
    protected SaveSettingsMixin( final String name, final boolean propagatesCrashes )
    {
        super( name, propagatesCrashes );
    }

    @Inject( at = @At( "HEAD" ),
             method = "pauseGame(Z)V" )
    public void openPauseMenu( final boolean pause, final CallbackInfo info )
    {
        ShowDurability.CONFIG.save();
    }
}
