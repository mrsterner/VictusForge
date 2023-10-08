package dev.sterner.victus.util;

import dev.sterner.victus.mixin.SpellcastingIllagerEntityAccessor;
import dev.sterner.victus.registry.VictusItemRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.List;

public class WohooHeartGoal extends Goal {

    protected int spellCooldown;
    protected int startTime;

    private final Evoker evoker;
    public final LookAtItemGoal lookGoal = new LookAtItemGoal();

    private ItemEntity target;

    public WohooHeartGoal(Evoker evoker) {
        this.evoker = evoker;
    }

    @Override
    public boolean canUse() {
        if (evoker.getTarget() != null) {
            return false;
        } else if (evoker.isCastingSpell()) {
            return false;
        } else if (evoker.tickCount < this.startTime) {
            return false;
        } else {
            List<ItemEntity> list = evoker.level().getEntitiesOfClass(ItemEntity.class, this.evoker.getBoundingBox().inflate(16, 4, 16), entity -> entity.getItem().getCount() == 1 && entity.getItem().getItem() == VictusItemRegistry.BLANK_HEART_ASPECT.get());
            if (list.isEmpty()) {
                return false;
            } else {
                this.target = list.get(evoker.level().random.nextInt(list.size()));
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() && this.target.getItem().getCount() == 1 && this.target.getItem().getItem() == VictusItemRegistry.BLANK_HEART_ASPECT.get();
    }

    @Override
    public void start() {
        this.spellCooldown = 40;
        ((SpellcastingIllagerEntityAccessor) evoker).victus_setSpellTicks(60);
        this.startTime = evoker.tickCount + 140;

        evoker.playSound(SoundEvents.EVOKER_PREPARE_WOLOLO, 1.0F, 1.0F);
        evoker.setIsCastingSpell(SpellcasterIllager.IllagerSpell.BLINDNESS);
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public void tick() {
        this.spellCooldown--;
        if (this.spellCooldown == 0) {
            target.setItem(new ItemStack(VictusItemRegistry.EVOKING_HEART_ASPECT.get()));
            VictusParticleEvents.spawnConvert(target.level(), target.position());

            evoker.playSound(((SpellcastingIllagerEntityAccessor) evoker).victus_invokeGetCastSpellSound(), 1.0F, 1.0F);
        }
    }

    public class LookAtItemGoal extends Goal {
        public LookAtItemGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return WohooHeartGoal.this.target != null;
        }

        @Override
        public boolean canContinueToUse() {
            return WohooHeartGoal.this.target != null;
        }

        @Override
        public void start() {
            WohooHeartGoal.this.evoker.getNavigation().stop();
        }

        @Override
        public void stop() {
            WohooHeartGoal.this.evoker.setIsCastingSpell(SpellcasterIllager.IllagerSpell.NONE);
        }

        @Override
        public void tick() {
            if (WohooHeartGoal.this.target != null) {
                WohooHeartGoal.this.evoker.getLookControl().setLookAt(WohooHeartGoal.this.target, (float) WohooHeartGoal.this.evoker.getMaxHeadYRot(), (float) WohooHeartGoal.this.evoker.getHeadRotSpeed());
            }
        }
    }
}
