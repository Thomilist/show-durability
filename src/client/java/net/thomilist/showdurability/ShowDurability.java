package net.thomilist.showdurability;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowDurability
    implements ClientModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger( "ShowDurability" );
    public static final Config CONFIG = new Config();

    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create( Identifier.of(
        "showdurability",
        "main"
    ) );
    private static KeyBinding KEY_BINDING;

    @Override
    public void onInitializeClient()
    {
        ShowDurability.KEY_BINDING = KeyBindingHelper.registerKeyBinding( new KeyBinding(
            "key.showdurability.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            ShowDurability.CATEGORY
        ) );

        ClientTickEvents.END_CLIENT_TICK.register( client -> {
            while ( ShowDurability.KEY_BINDING.wasPressed() )
            {
                ShowDurability.CONFIG.toggleVisibility();
            }
        } );

        ShowDurability.LOGGER.info( "Show Durability initialised." );
    }
}