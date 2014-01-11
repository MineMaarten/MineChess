package minechess.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * MineChess
 * @author MineMaarten
 * www.minemaarten.com
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 * Models made with Techne, http://techne.zeux.me/Techne
 */

public class ModelRook extends ModelBase{
    // fields
    ModelRenderer Base;
    ModelRenderer Body;
    ModelRenderer LongRookWall1;
    ModelRenderer LongRookWall2;
    ModelRenderer LongRookWall3;
    ModelRenderer LongRookWall4;
    ModelRenderer ShortRookWall1;
    ModelRenderer ShortRookWall2;
    ModelRenderer ShortRookWall3;
    ModelRenderer ShortRookWall4;

    public ModelRook(){
        textureWidth = 64;
        textureHeight = 32;

        Base = new ModelRenderer(this, 0, 0);
        Base.addBox(0F, 0F, 0F, 10, 1, 10);
        Base.setRotationPoint(-5F, 23F, -5F);
        Base.setTextureSize(64, 32);
        Base.mirror = true;
        setRotation(Base, 0F, 0F, 0F);
        Body = new ModelRenderer(this, 0, 0);
        Body.addBox(0F, 0F, 0F, 6, 15, 6);
        Body.setRotationPoint(-3F, 8F, -3F);
        Body.setTextureSize(64, 32);
        Body.mirror = true;
        setRotation(Body, 0F, 0F, 0F);
        LongRookWall1 = new ModelRenderer(this, 0, 0);
        LongRookWall1.addBox(0F, 0F, 0F, 1, 3, 3);
        LongRookWall1.setRotationPoint(3F, 6F, -4F);
        LongRookWall1.setTextureSize(64, 32);
        LongRookWall1.mirror = true;
        setRotation(LongRookWall1, 0F, 0F, 0F);
        LongRookWall2 = new ModelRenderer(this, 0, 0);
        LongRookWall2.addBox(0F, 0F, 0F, 1, 3, 3);
        LongRookWall2.setRotationPoint(3F, 6F, 1F);
        LongRookWall2.setTextureSize(64, 32);
        LongRookWall2.mirror = true;
        setRotation(LongRookWall2, 0F, 0F, 0F);
        LongRookWall3 = new ModelRenderer(this, 0, 0);
        LongRookWall3.addBox(0F, 0F, 0F, 1, 3, 3);
        LongRookWall3.setRotationPoint(-4F, 6F, 1F);
        LongRookWall3.setTextureSize(64, 32);
        LongRookWall3.mirror = true;
        setRotation(LongRookWall3, 0F, 0F, 0F);
        LongRookWall4 = new ModelRenderer(this, 0, 0);
        LongRookWall4.addBox(0F, 0F, 0F, 1, 3, 3);
        LongRookWall4.setRotationPoint(-4F, 6F, -4F);
        LongRookWall4.setTextureSize(64, 32);
        LongRookWall4.mirror = true;
        setRotation(LongRookWall4, 0F, 0F, 0F);
        ShortRookWall1 = new ModelRenderer(this, 0, 0);
        ShortRookWall1.addBox(0F, 0F, 0F, 2, 3, 1);
        ShortRookWall1.setRotationPoint(1F, 6F, 3F);
        ShortRookWall1.setTextureSize(64, 32);
        ShortRookWall1.mirror = true;
        setRotation(ShortRookWall1, 0F, 0F, 0F);
        ShortRookWall2 = new ModelRenderer(this, 0, 0);
        ShortRookWall2.addBox(0F, 0F, 0F, 2, 3, 1);
        ShortRookWall2.setRotationPoint(-3F, 6F, 3F);
        ShortRookWall2.setTextureSize(64, 32);
        ShortRookWall2.mirror = true;
        setRotation(ShortRookWall2, 0F, 0F, 0F);
        ShortRookWall3 = new ModelRenderer(this, 0, 0);
        ShortRookWall3.addBox(0F, 0F, 0F, 2, 3, 1);
        ShortRookWall3.setRotationPoint(1F, 6F, -4F);
        ShortRookWall3.setTextureSize(64, 32);
        ShortRookWall3.mirror = true;
        setRotation(ShortRookWall3, 0F, 0F, 0F);
        ShortRookWall4 = new ModelRenderer(this, 0, 0);
        ShortRookWall4.addBox(0F, 0F, 0F, 2, 3, 1);
        ShortRookWall4.setRotationPoint(-3F, 6F, -4F);
        ShortRookWall4.setTextureSize(64, 32);
        ShortRookWall4.mirror = true;
        setRotation(ShortRookWall4, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Base.render(f5);
        Body.render(f5);
        LongRookWall1.render(f5);
        LongRookWall2.render(f5);
        LongRookWall3.render(f5);
        LongRookWall4.render(f5);
        ShortRookWall1.render(f5);
        ShortRookWall2.render(f5);
        ShortRookWall3.render(f5);
        ShortRookWall4.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z){
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

}
