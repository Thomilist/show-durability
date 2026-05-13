package net.thomilist.showdurability;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path configPath;
    private boolean visible = true;

    public Config()
    {
        this.configPath = Minecraft.getInstance().gameDirectory
            .toPath()
            .resolve( "config" )
            .resolve( "show-durability.json" );

        this.load();
    }

    public void load()
    {
        try
        {
            final String visibilitySettingJson = Files.readString( this.configPath );
            this.visible = Config.GSON.fromJson( visibilitySettingJson, Boolean.class );
        }
        catch ( final IOException e )
        {
            ShowDurability.LOGGER.warn( "Failed to read config file", e );
            this.save();
        }
        catch ( final JsonSyntaxException e )
        {
            ShowDurability.LOGGER.warn( "Invalid JSON syntax in config file", e );
            this.save();
        }
        catch ( final NullPointerException e )
        {
            ShowDurability.LOGGER.warn( "Config is empty or null", e );
            this.save();
        }
    }

    public void save()
    {
        try
        {
            final String visibilitySettingJson = Config.GSON.toJson( this.visible );
            Files.writeString( this.configPath, visibilitySettingJson );
        }
        catch ( final IOException e )
        {
            ShowDurability.LOGGER.error( "Unable to write to config file", e );
        }
    }

    public void toggleVisibility()
    {
        this.visible = !this.visible;
    }

    public boolean getVisibility()
    {
        return this.visible;
    }
}
