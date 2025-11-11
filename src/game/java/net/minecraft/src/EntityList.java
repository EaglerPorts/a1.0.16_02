package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

import net.peyton.eagler.minecraft.suppliers.EntitySupplier;

public class EntityList {
	private static Map stringToClassMapping = new HashMap();
	private static Map classToIDMapping = new HashMap();
	private static Map stringToIDMapping = new HashMap();

	private static void addMapping(Class var0, EntitySupplier var1, String var2) {
		stringToClassMapping.put(var2.toLowerCase(), var1);
		classToIDMapping.put(var0, var2);
		stringToIDMapping.put(var2.toLowerCase(), var2);
	}

	public static Entity createEntityByName(String var0, World var1) {
		Entity var2 = null;

		try {
			EntitySupplier var3 = (EntitySupplier)stringToClassMapping.get(var0.toLowerCase());
			if(var3 != null) {
				var2 = (Entity)var3.createEntity(var1);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		return var2;
	}

	public static Entity createEntityFromNBT(NBTTagCompound var0, World var1) {
		Entity var2 = null;

		try {
			EntitySupplier var3 = (EntitySupplier)stringToClassMapping.get(var0.getString("id").toLowerCase());
			if(var3 != null) {
				var2 = (Entity)var3.createEntity(var1);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		if(var2 != null) {
			var2.readFromNBT(var0);
		} else {
			System.out.println("Skipping Entity with id " + var0.getString("id"));
		}

		return var2;
	}

	public static String getEntityString(Entity var0) {
		return (String)classToIDMapping.get(var0.getClass());
	}

	static {
		addMapping(EntityArrow.class, EntityArrow::new, "Arrow");
		addMapping(EntitySnowball.class, EntitySnowball::new, "Snowball");
		addMapping(EntityItem.class, EntityItem::new, "Item");
		addMapping(EntityPainting.class, EntityPainting::new, "Painting");
		addMapping(EntityLiving.class, EntityLiving::new, "Mob");
		addMapping(EntityMob.class, EntityMob::new, "Monster");
		addMapping(EntityCreeper.class, EntityCreeper::new, "Creeper");
		addMapping(EntitySkeleton.class, EntitySkeleton::new, "Skeleton");
		addMapping(EntitySpider.class, EntitySpider::new, "Spider");
		addMapping(EntityGiantZombie.class, EntityGiantZombie::new, "Giant");
		addMapping(EntityZombie.class, EntityZombie::new, "Zombie");
		addMapping(EntitySlime.class, EntitySlime::new, "Slime");
		addMapping(EntityPig.class, EntityPig::new, "Pig");
		addMapping(EntitySheep.class, EntitySheep::new, "Sheep");
		addMapping(EntityCow.class, EntityCow::new, "Cow");
		addMapping(EntityChicken.class, EntityChicken::new, "Chicken");
		addMapping(EntityTNTPrimed.class, EntityTNTPrimed::new, "PrimedTnt");
		addMapping(EntityFallingSand.class, EntityFallingSand::new, "FallingSand");
		addMapping(EntityMinecart.class, EntityMinecart::new, "Minecart");
		addMapping(EntityBoat.class, EntityBoat::new, "Boat");
	}
}
