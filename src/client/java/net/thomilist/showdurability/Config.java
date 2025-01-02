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
    private final Path configPath;
    private boolean visible = true;

    public Config()
    {
        this.configPath = MinecraftClient.getInstance().runDirectory
            .toPath()
            .resolve( "config" )
            .resolve( "show-durability.json" );

        this.load();
    }

    public void load()
    {
        final String visibilitySettingJson;

        try
        {
            visibilitySettingJson = Files.readString( this.configPath );
        }
        catch ( final IOException e )
        {
            this.save();
            return;
        }

        final Gson gson = new Gson();

        try
        {
            this.visible = gson.fromJson( visibilitySettingJson, Boolean.class );
        }
        catch ( final JsonSyntaxException e )
        {
            ShowDurability.LOGGER.error( "Invalid JSON syntax:", e );
        }
    }

    public void save()
    {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String visibilitySettingJson = gson.toJson( this.visible );

        try
        {
            Files.writeString( this.configPath, visibilitySettingJson );
        }
        catch ( final IOException e )
        {
            ShowDurability.LOGGER.error( "Unable to write to config file:", e );
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
