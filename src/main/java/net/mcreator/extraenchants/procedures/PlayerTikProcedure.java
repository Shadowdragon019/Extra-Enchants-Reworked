package net.mcreator.extraenchants.procedures;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.item.BlockItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;

import net.mcreator.extraenchants.enchantment.CurseOfInsomniaEnchantment;
import net.mcreator.extraenchants.enchantment.ColdFeetEnchantment;
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
		ItemStack ColdFeetBlock = ItemStack.EMPTY;
		double RandomNum = 0;
		double OX = 0;
		double OZ = 0;
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
		if (((EnchantmentHelper.getEnchantmentLevel(ColdFeetEnchantment.enchantment,
				/* @ItemStack */((entity instanceof PlayerEntity)
						? ((PlayerEntity) entity).inventory.armorInventory.get((int) 0)
						: ItemStack.EMPTY)) != 0))) {
			OZ = (double) (-2);
			for (int index1 = 0; index1 < (int) (3); index1++) {
				OZ = (double) ((OZ) + 1);
				OX = (double) (-2);
				for (int index2 = 0; index2 < (int) (3); index2++) {
					OX = (double) ((OX) + 1);
					if ((BlockTags.getCollection().getOrCreate(new ResourceLocation(("forge:walkable_lava").toLowerCase(java.util.Locale.ENGLISH)))
							.contains(/* @BlockState */(world.getFluidState(new BlockPos((int) ((entity.getPosX()) + (OX)),
									(int) ((entity.getPosY()) - 1), (int) ((entity.getPosZ()) + (OZ)))).getBlockState()).getBlock()))) {
						RandomNum = (double) ((new java.util.Random()).nextInt((int) 2 + 1));
						if ((0 == (RandomNum))) {
							ColdFeetBlock = new ItemStack(Blocks.OBSIDIAN, (int) (1));
						} else if ((1 == (RandomNum))) {
							ColdFeetBlock = new ItemStack(Blocks.STONE, (int) (1));
						} else if ((2 == (RandomNum))) {
							ColdFeetBlock = new ItemStack(Blocks.COBBLESTONE, (int) (1));
						}
						world.setBlockState(
								new BlockPos((int) ((entity.getPosX()) + (OX)), (int) ((entity.getPosY()) - 1), (int) ((entity.getPosZ()) + (OZ))),
								/* @BlockState */(new Object() {
									public BlockState toBlock(ItemStack _stk) {
										if (_stk.getItem() instanceof BlockItem) {
											return ((BlockItem) _stk.getItem()).getBlock().getDefaultState();
										}
										return Blocks.AIR.getDefaultState();
									}
								}.toBlock((ColdFeetBlock))), 3);
					}
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
