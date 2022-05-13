package crispytwig.naturalist.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.JumpGoal;

public class RandomJumpGoal extends JumpGoal {
    @Override
    public boolean canUse() {
        return false;
    }
}
