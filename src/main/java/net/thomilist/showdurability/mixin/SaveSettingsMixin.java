package net.thomilist.showdurability.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.thomilist.showdurability.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class SaveSettingsMixin extends ReentrantThreadExecutor<Runnable> implements WindowEventHandler
{
    public SaveSettingsMixin(String string)
    {
        super(string);
    }

    @Inject(at = @At("HEAD"), method = "openPauseMenu(Z)V")
    public void openPauseMenu(boolean pause, CallbackInfo info)
    {
        Settings.save();
    }
}
