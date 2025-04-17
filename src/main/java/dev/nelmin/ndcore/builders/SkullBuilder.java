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
import java.util.Objects;
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

    /**
     * Constructs a new SkullBuilder with a default player head item
     */
    public SkullBuilder() {
        super(Material.PLAYER_HEAD, 1);
        this.itemStack = super.toItem();
        this.skullMeta = (SkullMeta) this.itemStack.getItemMeta();
    }

    /**
     * Sets the skull's owner using a player name
     *
     * @param owner The name of the player to set as owner
     * @return This builder instance
     * @throws IllegalArgumentException if owner is null
     */
    public SkullBuilder owner(@NotNull String owner) {
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(Objects.requireNonNull(owner)));
        updateSuper();
        return this;
    }

    /**
     * Sets the skull's owner using an OfflinePlayer instance
     *
     * @param owner The OfflinePlayer to set as owner
     * @return This builder instance
     * @throws IllegalArgumentException if owner is null
     */
    public SkullBuilder owner(@NotNull OfflinePlayer owner) {
        skullMeta.setOwningPlayer(Objects.requireNonNull(owner));
        updateSuper();
        return this;
    }

    /**
     * Sets the skull's owner using a UUID
     *
     * @param uuid The UUID of the player to set as owner
     * @return This builder instance
     * @throws IllegalArgumentException if uuid is null
     */
    public SkullBuilder owner(@NotNull UUID uuid) {
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(Objects.requireNonNull(uuid)));
        updateSuper();
        return this;
    }

    /**
     * Sets the skull's texture using a URL
     *
     * @param texture The URL of the texture to apply
     * @return This builder instance
     * @throws IllegalArgumentException if texture is null or malformed
     */
    public SkullBuilder texture(@NotNull String texture) throws IllegalArgumentException {
        Objects.requireNonNull(texture);
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

    /**
     * Builds and returns the final skull ItemStack
     *
     * @return The constructed skull ItemStack with all applied modifications
     */
    @Override
    @NotNull
    public ItemStack toItem() {
        updateSuper();
        return itemStack;
    }

    /**
     * Updates the super class with the current skull metadata
     */
    private void updateSuper() {
        this.itemStack.setItemMeta(skullMeta);
        super.setItemMeta(skullMeta);
        super.setRawItemStack(itemStack);
    }
}