package net.thomilist.showdurability;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Settings
{
    private static Path configFile;
    private static boolean visible = true;

    public static void initialise()
    {
        configFile =
            MinecraftClient.getInstance().runDirectory.toPath()
                .resolve("config")
                .resolve("show-durability.json");
        load();
        return;
    }

    public static void load()
    {
        String visibilitySettingJson;

        try
        {
            visibilitySettingJson = Files.readString(configFile);
        }
        catch (IOException e)
        {
            save();
            return;
        }

        Gson gson = new Gson();

        try
        {
            visible = gson.fromJson(visibilitySettingJson, Boolean.class);
        }
        catch (JsonSyntaxException e)
        {
            e.printStackTrace();
            ShowDurabilityMod.LOGGER.error("Invalid JSON syntax.", e);
            return;
        }

        return;
    }

    public static void save()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String visibilitySettingJson = gson.toJson(visible);

        try
        {
            Files.writeString(configFile, visibilitySettingJson);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ShowDurabilityMod.LOGGER.error("Unable to write to config file.", e);
        }

        return;
    }

    public static void toggleVisibility()
    {
        visible = !visible;
        return;
    }

    public static boolean getVisibility()
    {
        return visible;
    }
}
