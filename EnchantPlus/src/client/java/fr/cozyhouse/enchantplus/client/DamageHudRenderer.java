package fr.cozyhouse.enchantplus.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DamageHudRenderer {
    private static final long DISPLAY_MILLIS = 2000L; // durée d'affichage : 2 s
    private static final int MAX_ENTRIES = 8;          // nombre max de dégâts affichés
    private static final List<DamageNumber> ACTIVE = new ArrayList<>();

    public static void addDamage(float amount) {
        ACTIVE.add(new DamageNumber(amount, Util.getMillis()));
        if (ACTIVE.size() > MAX_ENTRIES) {
            ACTIVE.removeFirst();
        }
    }

    // Signature d'un HudElement : (GuiGraphicsExtractor, DeltaTracker)
    public static void render(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        if (ACTIVE.isEmpty()) return;

        long now = Util.getMillis();

        // Suppression des entrées expirées
        Iterator<DamageNumber> it = ACTIVE.iterator();
        while (it.hasNext()) {
            if (now - it.next().createdMillis > DISPLAY_MILLIS) it.remove();
        }
        if (ACTIVE.isEmpty()) return;

        Font font = Minecraft.getInstance().font;

        // Largeur du fond selon le texte le plus long
        int boxWidth = 0;
        for (DamageNumber dn : ACTIVE) {
            boxWidth = Math.max(boxWidth, font.width(dn.text()));
        }
        boxWidth += 8;

        int lineHeight = 10;
        int x = 6;
        int y = 6;

        // Fond semi-transparent
        graphics.fill(x - 2, y - 2, x + boxWidth, y + ACTIVE.size() * lineHeight + 2, 0x80000000);

        // Texte avec fondu progressif (alpha décroissant)
        for (DamageNumber dn : ACTIVE) {
            float age = (float) (now - dn.createdMillis) / DISPLAY_MILLIS; // 0 -> 1
            int alpha = (int) (255 * (1.0f - age));
            int color = (alpha << 24) | 0xFFFFFF; // blanc, alpha décroissant
            graphics.text(font, dn.text(), x, y, color, true); // true = ombre portée
            y += lineHeight;
        }
    }

    private record DamageNumber(float damage, long createdMillis) {
        String text() {
            return String.format(Locale.ROOT, "%.1f", damage);
        }
    }
}
