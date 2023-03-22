package net.thomilist.showdurability;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowDurabilityMod implements ClientModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger("ShowDurability");

    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient()
    {
        Settings.initialise();

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.showdurability.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "category.showdurability"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (keyBinding.wasPressed())
            {
                Settings.toggleVisibility();
            }
        });

        LOGGER.info("ShowDurability initialised.");
    }
}