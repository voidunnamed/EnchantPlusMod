package fr.cozyhouse.enchantplus.client;

import fr.cozyhouse.enchantplus.Enchantplus;
import fr.cozyhouse.enchantplus.network.DamagePayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;

public class EnchantplusClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Réception des dégâts. Le handler tourne sur le thread réseau,
        // on repasse donc sur le thread principal avant de toucher le HUD.
        ClientPlayNetworking.registerGlobalReceiver(DamagePayload.TYPE, (payload, context) -> {
            context.client().execute(() -> DamageHudRenderer.addDamage(payload.damage()));
        });

        // Affichage dans le HUD, juste avant la couche du chat
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath(Enchantplus.MOD_ID, "damage_numbers"),
                DamageHudRenderer::render
        );
    }
}
