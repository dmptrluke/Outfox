/**
 * Copyright © 2018 Aiden Vaughn "ItsTheKais"
 *
 * This file is part of Outfox.
 *
 * The code of Outfox is free and available under the terms of the latest version of the GNU Lesser General
 * Public License. Outfox is distributed with no warranty, implied or otherwise. Outfox should have come with
 * a copy of the GNU Lesser General Public License; if not, see: <https://www.gnu.org/licenses/>
 */

package kais.outfox.fox;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelFox extends ModelBase {

    private ModelRenderer foxBody;
    private ModelRenderer foxHead;
    private ModelRenderer foxForeR;
    private ModelRenderer foxForeL;
    private ModelRenderer foxHindR;
    private ModelRenderer foxHindL;
    private ModelRenderer foxTail;

    public ModelFox() {

        float f = 0.0F;
        this.foxBody = new ModelRenderer(this, 0, 9);
        this.foxBody.addBox(-2.5F, -2.5F, -2.0F, 5, 5, 12, f);
        this.foxBody.setRotationPoint(f, 16.5F, -4F);
        this.foxBody.setTextureOffset(32, 0).addBox(-4.5F, -3.0F, -1.5F, 9, 0, 6); //ribbon
        this.foxHead = new ModelRenderer(this, 0, 0);
        this.foxHead.addBox(-3.0F, -3.0F, -6.0F, 6, 5, 4, f);
        this.foxHead.setRotationPoint(f, 16F, -4F);
        this.foxHead.setTextureOffset(0, 9).addBox(-1.5F, -0.5F, -9.0F, 3, 2, 3); //snout
        this.foxHead.setTextureOffset(20, 0).addBox(1.5F, -4.0F, -5.0F, 2, 3, 4).mirror = true; //left ear
        this.foxHead.setTextureOffset(20, 0).addBox(-3.5F, -4.0F, -5.0F, 2, 3, 4).mirror = true; //right ear
        this.foxForeR = new ModelRenderer(this, 22, 13);
        this.foxForeR.addBox(-1.0F, f, -1.0F, 2, 5, 2, f);
        this.foxForeR.setRotationPoint(-1.5F, 19.0F, -4.0F);
        this.foxForeL = new ModelRenderer(this, 22, 13);
        this.foxForeL.mirror = true;
        this.foxForeL.addBox(-1.0F, f, -1.0F, 2, 5, 2, f);
        this.foxForeL.setRotationPoint(1.5F, 19.0F, -4.0F);
        this.foxHindR = new ModelRenderer(this, 30, 13);
        this.foxHindR.addBox(-1.0F, f, -1.0F, 2, 5, 2, f);
        this.foxHindR.setRotationPoint(-1.5F, 19.0F, 4.0F);
        this.foxHindL = new ModelRenderer(this, 30, 13);
        this.foxHindL.mirror = true;
        this.foxHindL.addBox(-1.0F, f, -1.0F, 2, 5, 2, f);
        this.foxHindL.setRotationPoint(1.5F, 19.0F, 4.0F);
        this.foxTail = new ModelRenderer(this, 22, 0);
        this.foxTail.addBox(-1.5F, -0.5F, 0.5F, 3, 3, 10, f);
        this.foxTail.setRotationPoint(f, 14F, 5.0F);
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        if (this.isChild) {

            float f = 1.5F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 2.5F * scale, 2.0F * scale);
            this.foxHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
            GlStateManager.translate(0.0F, 12.0F * scale, 0.0F);
            this.foxBody.render(scale);
            this.foxForeR.render(scale);
            this.foxForeL.render(scale);
            this.foxHindR.render(scale);
            this.foxHindL.render(scale);
            this.foxTail.render(scale);
            GlStateManager.popMatrix();
        }
        else {

            this.foxBody.render(scale);
            this.foxHead.render(scale);
            this.foxForeR.render(scale);
            this.foxForeL.render(scale);
            this.foxHindR.render(scale);
            this.foxHindL.render(scale);
            this.foxTail.render(scale);
        }
    }

    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {

        EntityFox fox = (EntityFox)entitylivingbaseIn;

        if (fox.isSitting()) {

            this.foxForeR.setRotationPoint(-1.5F, 20.0F, -2.0F);
            this.foxForeL.setRotationPoint(1.5F, 20.0F, -2.0F);
            this.foxHindR.setRotationPoint(-1.5F, 20.0F, 5.0F);
            this.foxHindL.setRotationPoint(1.5F, 20.0F, 5.0F);
            this.setRotation(this.foxForeR, -90.0F, 0.0F, 0.0F);
            this.setRotation(this.foxForeL, -90.0F, 0.0F, 0.0F);
            this.setRotation(this.foxHindR, -90.0F, 20.0F, 0.0F);
            this.setRotation(this.foxHindL, -90.0F, -20.0F, 0.0F);
            this.setRotation(this.foxTail, -25F, 0.0F, 0.0F);
            this.foxHead.offsetY = 0.19F;
            this.foxBody.offsetY = 0.19F;
            this.foxTail.offsetY = 0.19F;
            this.foxForeR.offsetY = 0.19F;
            this.foxForeL.offsetY = 0.19F;
            this.foxHindR.offsetY = 0.19F;
            this.foxHindR.offsetZ = 0.063F;
            this.foxHindL.offsetY = 0.19F;
            this.foxHindL.offsetZ = 0.063F;
        }
        else {

            this.foxForeR.setRotationPoint(-1.5F, 19.0F, -4.0F);
            this.foxForeL.setRotationPoint(1.5F, 19.0F, -4.0F);
            this.foxHindR.setRotationPoint(-1.5F, 19.0F, 4.0F);
            this.foxHindL.setRotationPoint(1.5F, 19.0F, 4.0F);
            this.foxForeR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * -limbSwingAmount;
            this.foxForeL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.foxHindR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.foxHindL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * -limbSwingAmount;
            this.foxHindR.rotateAngleY = 0.0F;
            this.foxHindL.rotateAngleY = 0.0F;
            this.setRotation(this.foxTail, -15.0F, 0.0F, 0.0F);
            this.foxHead.offsetY = 0.0F;
            this.foxBody.offsetY = 0.0F;
            this.foxTail.offsetY = 0.0F;
            this.foxForeR.offsetY = 0.0F;
            this.foxForeL.offsetY = 0.0F;
            this.foxHindR.offsetY = 0.0F;
            this.foxHindR.offsetZ = 0.0F;
            this.foxHindL.offsetY = 0.0F;
            this.foxHindL.offsetZ = 0.0F;
        }

        this.foxHead.rotateAngleZ = fox.getShakeAngle(partialTickTime, -0.08F);
        this.foxBody.rotateAngleZ = fox.getShakeAngle(partialTickTime, -0.16F);
        this.foxTail.rotateAngleZ = fox.getShakeAngle(partialTickTime, -0.2F);
        this.foxTail.rotateAngleY = MathHelper.cos(limbSwing * 0.6662F) * 0.5F * limbSwingAmount;
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {

        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        this.foxHead.rotateAngleX = headPitch / (180.0F / (float)Math.PI);
        this.foxHead.rotateAngleY = netHeadYaw / (180.0F / (float)Math.PI);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {

        model.rotateAngleX = (float)Math.toRadians(x);
        model.rotateAngleY = (float)Math.toRadians(y);
        model.rotateAngleZ = (float)Math.toRadians(z);
    }
}