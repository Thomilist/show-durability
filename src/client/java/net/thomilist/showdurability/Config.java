package net.thomilist.showdurability;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config
{
    private static Path CONFIG_PATH;
    private static boolean VISIBLE = true;

    public static void initialise()
    {
        Config.CONFIG_PATH = MinecraftClient.getInstance().runDirectory
            .toPath()
            .resolve( "config" )
            .resolve( "show-durability.json" );
        Config.load();
    }

    public static void load()
    {
        final String visibilitySettingJson;

        try
        {
            visibilitySettingJson = Files.readString( Config.CONFIG_PATH );
        }
        catch ( final IOException e )
        {
            Config.save();
            return;
        }

        final Gson gson = new Gson();

        try
        {
            Config.VISIBLE = gson.fromJson( visibilitySettingJson, Boolean.class );
        }
        catch ( final JsonSyntaxException e )
        {
            ShowDurability.LOGGER.error( "Invalid JSON syntax:", e );
        }
    }

    public static void save()
    {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String visibilitySettingJson = gson.toJson( Config.VISIBLE );

        try
        {
            Files.writeString( Config.CONFIG_PATH, visibilitySettingJson );
        }
        catch ( final IOException e )
        {
            ShowDurability.LOGGER.error( "Unable to write to config file:", e );
        }
    }

    public static void toggleVisibility()
    {
        Config.VISIBLE = !Config.VISIBLE;
    }

    public static boolean getVisibility()
    {
        return Config.VISIBLE;
    }
}
