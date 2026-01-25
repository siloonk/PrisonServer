package io.github.siloonk.prisonServer.dungeons.abilities;

import io.github.siloonk.prisonServer.PDCKeys;
import io.github.siloonk.prisonServer.PrisonServer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class UltimateDefense extends BossAbility{

    private static final int DISTANCE = 3;


    private int shields;
    private int shieldHealth;

    public UltimateDefense(int shields, int shieldHealth) {
        this.shields = shields;
        this.shieldHealth = shieldHealth;
    }

    @Override
    public void trigger(EntityDamageByEntityEvent event) {
        int centerX = event.getEntity().getLocation().getBlockX();
        int centerZ = event.getEntity().getLocation().getBlockZ();
        double theta = ((Math.PI*2) / shields);


        for (int i = 0; i < shields; i++) {
            double startAngle = (theta * i+1);

            double x = centerX + (DISTANCE * Math.cos(startAngle));
            double z = centerZ + (DISTANCE * Math.sin(startAngle));

            Location loc = new Location(event.getEntity().getWorld(), x, event.getEntity().getLocation().getY(), z);

            ItemDisplay display = loc.getWorld().spawn(loc, ItemDisplay.class, entity -> {
                entity.setItemStack(ItemStack.of(Material.SHIELD));
                entity.setPersistent(false);
                entity.setInterpolationDelay(0);
                entity.setInterpolationDuration(1);
            });
            display.getPersistentDataContainer().set(PDCKeys.HEALTH, PersistentDataType.INTEGER, shieldHealth);

            new BukkitRunnable() {

                double angle = startAngle;

                @Override
                public void run() {
                    if (!display.isValid()) {
                        display.remove();
                        cancel();
                        return;
                    }

                    if (display.getPersistentDataContainer().get(PDCKeys.HEALTH, PersistentDataType.INTEGER) <= 0) {
                        display.remove();
                        cancel();
                        return;
                    }

                    angle += 0.05;

                    float x = (float) (DISTANCE * Math.cos(angle));
                    float z = (float) (DISTANCE * Math.sin(angle));

                    Transformation transformation = new Transformation(
                            new Vector3f(x, 1.2f, z),   // translation
                            new AxisAngle4f(),           // left rotation
                            new Vector3f(3, 3, 3),       // scale
                            new AxisAngle4f()            // right rotation
                    );

                    display.setTransformation(transformation);
                }

            }.runTaskTimer(PrisonServer.getInstance(), 0L, 1L);
        }
    }
}
