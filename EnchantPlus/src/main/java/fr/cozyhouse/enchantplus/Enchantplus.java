package fr.cozyhouse.enchantplus;

import fr.cozyhouse.enchantplus.network.DamagePayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class Enchantplus implements ModInitializer {

    public static final String MOD_ID = "damagenumbers";

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.clientboundPlay().register(DamagePayload.TYPE, DamagePayload.STREAM_CODEC);

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, damageTaken) ->{
            if (source.getEntity() instanceof ServerPlayer player){
                ServerPlayNetworking.send(player, new DamagePayload(damageTaken));
            }
            return true;
        });
    }
}
