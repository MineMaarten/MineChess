package chessMod.client;

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

public class ModelBishop extends ModelBase{
    // fields
    ModelRenderer Foot1;
    ModelRenderer Foot2;
    ModelRenderer Foot3;
    ModelRenderer Foot4;
    ModelRenderer Foot5;
    ModelRenderer Foot6;
    ModelRenderer Foot7;
    ModelRenderer Foot8;
    ModelRenderer Grip;
    ModelRenderer Base;
    ModelRenderer Top1;
    ModelRenderer Top2;
    ModelRenderer Top3;
    ModelRenderer Top4;
    ModelRenderer Top5;
    ModelRenderer Top6;
    ModelRenderer Top7;
    ModelRenderer Top8;
    ModelRenderer Top9;
    ModelRenderer Top10;
    ModelRenderer Top11;

    public ModelBishop(){
        textureWidth = 64;
        textureHeight = 32;

        Foot1 = new ModelRenderer(this, 0, 0);
        Foot1.addBox(0F, 0F, 0F, 12, 1, 12);
        Foot1.setRotationPoint(-6F, 23F, -6F);
        Foot1.setTextureSize(64, 32);
        Foot1.mirror = true;
        setRotation(Foot1, 0F, 0F, 0F);
        Foot2 = new ModelRenderer(this, 0, 0);
        Foot2.addBox(0F, 0F, 0F, 11, 1, 11);
        Foot2.setRotationPoint(-5.5F, 22F, -5.5F);
        Foot2.setTextureSize(64, 32);
        Foot2.mirror = true;
        setRotation(Foot2, 0F, 0F, 0F);
        Foot3 = new ModelRenderer(this, 0, 0);
        Foot3.addBox(0F, 0F, 0F, 10, 1, 10);
        Foot3.setRotationPoint(-5F, 21F, -5F);
        Foot3.setTextureSize(64, 32);
        Foot3.mirror = true;
        setRotation(Foot3, 0F, 0F, 0F);
        Foot4 = new ModelRenderer(this, 0, 0);
        Foot4.addBox(0F, 0F, 0F, 9, 1, 9);
        Foot4.setRotationPoint(-4.5F, 20F, -4.5F);
        Foot4.setTextureSize(64, 32);
        Foot4.mirror = true;
        setRotation(Foot4, 0F, 0F, 0F);
        Foot5 = new ModelRenderer(this, 0, 0);
        Foot5.addBox(0F, 0F, 0F, 8, 1, 8);
        Foot5.setRotationPoint(-4F, 19F, -4F);
        Foot5.setTextureSize(64, 32);
        Foot5.mirror = true;
        setRotation(Foot5, 0F, 0F, 0F);
        Foot6 = new ModelRenderer(this, 0, 0);
        Foot6.addBox(0F, 0F, 0F, 7, 1, 7);
        Foot6.setRotationPoint(-3.5F, 18F, -3.5F);
        Foot6.setTextureSize(64, 32);
        Foot6.mirror = true;
        setRotation(Foot6, 0F, 0F, 0F);
        Foot7 = new ModelRenderer(this, 0, 0);
        Foot7.addBox(0F, 0F, 0F, 6, 3, 6);
        Foot7.setRotationPoint(-3F, 15F, -3F);
        Foot7.setTextureSize(64, 32);
        Foot7.mirror = true;
        setRotation(Foot7, 0F, 0F, 0F);
        Foot8 = new ModelRenderer(this, 0, 0);
        Foot8.addBox(0F, 0F, 0F, 5, 8, 5);
        Foot8.setRotationPoint(-2.5F, 7F, -2.5F);
        Foot8.setTextureSize(64, 32);
        Foot8.mirror = true;
        setRotation(Foot8, 0F, 0F, 0F);
        Grip = new ModelRenderer(this, 0, 0);
        Grip.addBox(0F, 0F, 0F, 10, 1, 10);
        Grip.setRotationPoint(-5F, 6F, -5F);
        Grip.setTextureSize(64, 32);
        Grip.mirror = true;
        setRotation(Grip, 0F, 0F, 0F);
        Base = new ModelRenderer(this, 0, 0);
        Base.addBox(0F, 0F, 0F, 7, 3, 7);
        Base.setRotationPoint(-3.5F, 3F, -3.5F);
        Base.setTextureSize(64, 32);
        Base.mirror = true;
        setRotation(Base, 0F, 0F, 0F);
        Top1 = new ModelRenderer(this, 0, 0);
        Top1.addBox(0F, 0F, 0F, 9, 1, 9);
        Top1.setRotationPoint(-4.5F, 2F, -4.5F);
        Top1.setTextureSize(64, 32);
        Top1.mirror = true;
        setRotation(Top1, 0F, 0F, 0F);
        Top2 = new ModelRenderer(this, 0, 0);
        Top2.addBox(0F, 0F, 0F, 8, 1, 8);
        Top2.setRotationPoint(-4F, 1F, -4F);
        Top2.setTextureSize(64, 32);
        Top2.mirror = true;
        setRotation(Top2, 0F, 0F, 0F);
        Top3 = new ModelRenderer(this, 0, 0);
        Top3.addBox(0F, 0F, 0F, 7, 1, 3);
        Top3.setRotationPoint(-3.5F, 0F, 0.5F);
        Top3.setTextureSize(64, 32);
        Top3.mirror = true;
        setRotation(Top3, 0F, 0F, 0F);
        Top4 = new ModelRenderer(this, 0, 0);
        Top4.addBox(0F, 0F, 0F, 6, 1, 3);
        Top4.setRotationPoint(-3F, -1F, 0F);
        Top4.setTextureSize(64, 32);
        Top4.mirror = true;
        setRotation(Top4, 0F, 0F, 0F);
        Top5 = new ModelRenderer(this, 0, 0);
        Top5.addBox(0F, 0F, 0F, 5, 1, 3);
        Top5.setRotationPoint(-2.5F, -2F, -0.5F);
        Top5.setTextureSize(64, 32);
        Top5.mirror = true;
        setRotation(Top5, 0F, 0F, 0F);
        Top6 = new ModelRenderer(this, 0, 0);
        Top6.addBox(0F, 0F, 0F, 4, 1, 3);
        Top6.setRotationPoint(-2F, -3F, -1F);
        Top6.setTextureSize(64, 32);
        Top6.mirror = true;
        setRotation(Top6, 0F, 0F, 0F);
        Top7 = new ModelRenderer(this, 0, 0);
        Top7.addBox(0F, 0F, 0F, 6, 1, 1);
        Top7.setRotationPoint(-3F, -1F, -3F);
        Top7.setTextureSize(64, 32);
        Top7.mirror = true;
        setRotation(Top7, 0F, 0F, 0F);
        Top8 = new ModelRenderer(this, 0, 0);
        Top8.addBox(0F, 0F, 0F, 7, 1, 2);
        Top8.setRotationPoint(-4F, 0F, -3.5F);
        Top8.setTextureSize(64, 32);
        Top8.mirror = true;
        setRotation(Top8, 0F, 0F, 0F);
        Top9 = new ModelRenderer(this, 0, 0);
        Top9.addBox(0F, 0F, 0F, 3, 1, 3);
        Top9.setRotationPoint(-1.5F, -4F, -1.5F);
        Top9.setTextureSize(64, 32);
        Top9.mirror = true;
        setRotation(Top9, 0F, 0F, 0F);
        Top10 = new ModelRenderer(this, 0, 0);
        Top10.addBox(0F, 0F, 0F, 2, 1, 2);
        Top10.setRotationPoint(-1F, -5F, -1F);
        Top10.setTextureSize(64, 32);
        Top10.mirror = true;
        setRotation(Top10, 0F, 0F, 0F);
        Top11 = new ModelRenderer(this, 0, 0);
        Top11.addBox(0F, 0F, 0F, 1, 1, 1);
        Top11.setRotationPoint(-0.5F, -6F, -0.5F);
        Top11.setTextureSize(64, 32);
        Top11.mirror = true;
        setRotation(Top11, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Foot1.render(f5);
        Foot2.render(f5);
        Foot3.render(f5);
        Foot4.render(f5);
        Foot5.render(f5);
        Foot6.render(f5);
        Foot7.render(f5);
        Foot8.render(f5);
        Grip.render(f5);
        Base.render(f5);
        Top1.render(f5);
        Top2.render(f5);
        Top3.render(f5);
        Top4.render(f5);
        Top5.render(f5);
        Top6.render(f5);
        Top7.render(f5);
        Top8.render(f5);
        Top9.render(f5);
        Top10.render(f5);
        Top11.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z){
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

}
