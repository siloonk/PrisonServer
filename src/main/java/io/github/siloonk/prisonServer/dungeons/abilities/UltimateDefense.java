package io.github.siloonk.prisonServer.dungeons.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
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
            double angle = (theta * i+1);

            double x = centerX + (DISTANCE * Math.cos(angle));
            double z = centerZ + (DISTANCE * Math.sin(angle));

            Location loc = new Location(event.getEntity().getWorld(), x, event.getEntity().getLocation().getY(), z);

            ItemDisplay display = loc.getWorld().spawn(loc, ItemDisplay.class, entity -> {
                entity.setItemStack(ItemStack.of(Material.SHIELD));
                entity.setPersistent(false);
                entity.setTransformation(
                    new Transformation(
                        new Vector3f(),
                        new AxisAngle4f(),
                        new Vector3f(3, 3, 3),
                        new AxisAngle4f()
                    )
                );


            });
        }
    }
}
