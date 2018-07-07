/**
 * Copyright © 2018 Aiden Vaughn "ItsTheKais"
 *
 * This file is part of Outfox.
 *
 * The code of Outfox is free and available under the terms of the latest version of the GNU Lesser General
 * Public License. Outfox is distributed with no warranty, implied or otherwise. Outfox should have come with
 * a copy of the GNU Lesser General Public License; if not, see: <https://www.gnu.org/licenses/>
 */

package kais.outfox.proxy;

import kais.outfox.OutfoxConfig;
import kais.outfox.fox.EntityFox;
import kais.outfox.fox.RenderFox;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends ServerProxy {

    public void registerRender() {

        RenderingRegistry.registerEntityRenderingHandler(EntityFox.class, new IRenderFactory<EntityFox>() {

            @Override
            public Render<? super EntityFox> createRenderFor(RenderManager m) { return new RenderFox(m); }
        });
    }

    public void doParticlesSimple(String type, EntityFox fox) {

        double px, py, pz, vx, vy, vz;

        int i = fox.getRNG().nextInt(4) + 3;
        for (int j = 0; j < i; ++j) {

            px = fox.posX + fox.getRNG().nextFloat() * fox.width - fox.width;
            py = fox.getEntityBoundingBox().minY + 0.3D + fox.getRNG().nextFloat() * fox.height;
            pz = fox.posZ + fox.getRNG().nextFloat() * fox.width - fox.width;

            vx = fox.getRNG().nextGaussian() * 0.02D;
            vy = fox.getRNG().nextGaussian() * 0.02D;
            vz = fox.getRNG().nextGaussian() * 0.02D;

            doParticleEffect(type, fox, px, py, pz, vx, vy, vz);
        }
    }

    public void doParticleEffect(String type, EntityFox fox, double xpos, double ypos, double zpos, double xvel, double yvel, double zvel) {

        EnumParticleTypes particle = null;
        boolean enabled = OutfoxConfig.search.search_particles;

        switch (type) {

        case "block_cancel":
            particle = enabled ? EnumParticleTypes.SMOKE_NORMAL : null;
            break;

        case "block_set":
            particle = enabled ? EnumParticleTypes.VILLAGER_HAPPY : null;
            break;

        case "block_notallowed":
            particle = enabled ? EnumParticleTypes.VILLAGER_ANGRY : null;
            break;

        case "searching":
            particle = enabled ? EnumParticleTypes.CLOUD : null;
            break;

        case "shake":
            particle = EnumParticleTypes.WATER_SPLASH;
            break;

        default: break;
        }

        if (particle != null) { fox.world.spawnParticle(particle, true, xpos, ypos, zpos, xvel, yvel, zvel); }
    }
}