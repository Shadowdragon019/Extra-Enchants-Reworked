package net.mcreator.extraenchants.procedures;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.enchantment.EnchantmentHelper;

import net.mcreator.extraenchants.enchantment.CurseOfInsomniaEnchantment;
import net.mcreator.extraenchants.ExtraenchantsModElements;

import java.util.Map;
import java.util.HashMap;

@ExtraenchantsModElements.ModElement.Tag
public class PlayerTikProcedure extends ExtraenchantsModElements.ModElement {
	public PlayerTikProcedure(ExtraenchantsModElements instance) {
		super(instance, 2);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				System.err.println("Failed to load dependency entity for procedure PlayerTik!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				System.err.println("Failed to load dependency world for procedure PlayerTik!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		IWorld world = (IWorld) dependencies.get("world");
		if ((((entity instanceof LivingEntity) ? ((LivingEntity) entity).isSleeping() : false)
				&& ((EnchantmentHelper.getEnchantmentLevel(CurseOfInsomniaEnchantment.enchantment,
						/* @ItemStack */((entity instanceof PlayerEntity)
								? ((PlayerEntity) entity).inventory.armorInventory.get((int) 3)
								: ItemStack.EMPTY)) != 0)))) {
			entity.attackEntityFrom(DamageSource.MAGIC, (float) 4);
			for (int index0 = 0; index0 < (int) ((((new java.util.Random()).nextInt((int) 2 + 1)) + 1)); index0++) {
				if (world instanceof World && !world.getWorld().isRemote) {
					Entity entityToSpawn = new PhantomEntity(EntityType.PHANTOM, world.getWorld());
					entityToSpawn.setLocationAndAngles((entity.getPosX()), ((entity.getPosY()) + 25), (entity.getPosZ()), (float) 0, (float) 0);
					entityToSpawn.setRenderYawOffset((float) 0);
					if (entityToSpawn instanceof MobEntity)
						((MobEntity) entityToSpawn).onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(entityToSpawn)),
								SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
					world.addEntity(entityToSpawn);
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			Entity entity = event.player;
			World world = entity.world;
			double i = entity.getPosX();
			double j = entity.getPosY();
			double k = entity.getPosZ();
			Map<String, Object> dependencies = new HashMap<>();
			dependencies.put("x", i);
			dependencies.put("y", j);
			dependencies.put("z", k);
			dependencies.put("world", world);
			dependencies.put("entity", entity);
			dependencies.put("event", event);
			this.executeProcedure(dependencies);
		}
	}
}
