package dev.nelmin.ndcore.builders;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * SkullBuilder is an extension of the {@link ItemBuilder} class designed specifically
 * for creating and customizing player skulls in Paper. It provides additional methods
 * for setting the owner and applying custom textures to the skull. <p>
 * This class offers a fluent API, allowing method chaining for constructing and
 * modifying the skull properties.
 */
public class SkullBuilder extends ItemBuilder {
    /**
     * The constructed skull item
     */
    private final ItemStack itemStack;

    /**
     * The metadata specific to the skull item
     */
    private final SkullMeta skullMeta;

    public SkullBuilder() {
        super(Material.PLAYER_HEAD, 1);
        this.itemStack = super.toItem();
        this.skullMeta = (SkullMeta) this.itemStack.getItemMeta();
    }

    /**
     * Sets the skull's owner using a player name <p>
     */
    public SkullBuilder owner(@NotNull String owner) {
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
        updateSuper();
        return this;
    }

    /**
     * Sets the skull's owner using an OfflinePlayer instance <p>
     */
    public SkullBuilder owner(@NotNull OfflinePlayer owner) {
        skullMeta.setOwningPlayer(owner);
        updateSuper();
        return this;
    }

    /**
     * Sets the skull's owner using a UUID <p>
     */
    public SkullBuilder owner(@NotNull UUID uuid) {
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        updateSuper();
        return this;
    }

    /**
     * Sets the skull's texture using a URL <p>
     */
    public SkullBuilder texture(@NotNull String texture) {
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL(texture));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid skin texture URL", e);
        }
        skullMeta.setPlayerProfile(profile);
        updateSuper();
        return this;
    }

    @Override
    @NotNull
    public ItemStack toItem() {
        updateSuper();
        return itemStack;
    }

    private void updateSuper() {
        this.itemStack.setItemMeta(skullMeta);
        super.setItemMeta(skullMeta);
        super.setRawItemStack(itemStack);
    }
}