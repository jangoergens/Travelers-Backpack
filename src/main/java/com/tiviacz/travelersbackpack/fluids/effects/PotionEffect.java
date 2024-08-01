package com.tiviacz.travelersbackpack.fluids.effects;

/*public class PotionEffect extends EffectFluid
{
    public PotionEffect(String uniqueId, Fluid fluid)
    {
        super(uniqueId, fluid, Reference.POTION);
    }

    public PotionEffect(String uniqueId, String modid, String fluidName)
    {
        super(uniqueId, modid, fluidName, Reference.POTION);
    }

    @Override
    public void affectDrinker(FluidStack stack, Level level, Entity entity)
    {
        if(!level.isClientSide && entity instanceof Player player)
        {
           /* for(MobEffectInstance mobEffectInstance : PotionUtils.getMobEffects(FluidUtils.getItemStackFromFluidStack(stack)))
            {
                if(mobEffectInstance.getEffect().value().isInstantenous())
                {
                    mobEffectInstance.getEffect().value().applyInstantenousEffect(player, player, player, mobEffectInstance.getAmplifier(), 1.0D);
                }
                else
                {
                    player.addEffect(new MobEffectInstance(mobEffectInstance));
                }
            }
        }
    }

    @Override
    public boolean canExecuteEffect(FluidStack stack, Level level, Entity entity)
    {
        return stack.getAmount() >= amountRequired;
    }
} */