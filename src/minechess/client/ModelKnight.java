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

public class ModelKnight extends ModelBase{
    // fields
    ModelRenderer Base1;
    ModelRenderer Base2;
    ModelRenderer Body1;
    ModelRenderer Body2;
    ModelRenderer Neck;
    ModelRenderer Head;
    ModelRenderer Ear1;
    ModelRenderer Ear2;
    ModelRenderer UpperLip;
    ModelRenderer Chin;
    ModelRenderer TopHair1;
    ModelRenderer TopHair2;

    public ModelKnight(){
        textureWidth = 64;
        textureHeight = 32;

        Base1 = new ModelRenderer(this, 0, 0);
        Base1.addBox(0F, 0F, 0F, 10, 2, 10);
        Base1.setRotationPoint(-5F, 22F, -5F);
        Base1.setTextureSize(64, 32);
        Base1.mirror = true;
        setRotation(Base1, 0F, 0F, 0F);
        Base2 = new ModelRenderer(this, 0, 0);
        Base2.addBox(0F, 0F, 0F, 8, 5, 8);
        Base2.setRotationPoint(-4F, 17F, -4F);
        Base2.setTextureSize(64, 32);
        Base2.mirror = true;
        setRotation(Base2, 0F, 0F, 0F);
        Body1 = new ModelRenderer(this, 0, 0);
        Body1.addBox(0F, 0F, 0F, 6, 4, 6);
        Body1.setRotationPoint(-3F, 13F, -2F);
        Body1.setTextureSize(64, 32);
        Body1.mirror = true;
        setRotation(Body1, 0F, 0F, 0F);
        Body2 = new ModelRenderer(this, 0, 0);
        Body2.addBox(0F, 0F, 0F, 6, 4, 6);
        Body2.setRotationPoint(-3F, 9F, -1F);
        Body2.setTextureSize(64, 32);
        Body2.mirror = true;
        setRotation(Body2, 0F, 0F, 0F);
        Neck = new ModelRenderer(this, 0, 0);
        Neck.addBox(0F, 0F, 0F, 6, 4, 6);
        Neck.setRotationPoint(-3F, 5F, 0F);
        Neck.setTextureSize(64, 32);
        Neck.mirror = true;
        setRotation(Neck, 0F, 0F, 0F);
        Head = new ModelRenderer(this, 0, 0);
        Head.addBox(0F, 0F, 0F, 6, 4, 9);
        Head.setRotationPoint(-3F, 1F, -2F);
        Head.setTextureSize(64, 32);
        Head.mirror = true;
        setRotation(Head, 0F, 0F, 0F);
        Ear1 = new ModelRenderer(this, 0, 0);
        Ear1.addBox(0F, 0F, 0F, 1, 1, 1);
        Ear1.setRotationPoint(3F, 1F, 4F);
        Ear1.setTextureSize(64, 32);
        Ear1.mirror = true;
        setRotation(Ear1, 0F, 0F, 0F);
        Ear2 = new ModelRenderer(this, 0, 0);
        Ear2.addBox(0F, 0F, 0F, 1, 1, 1);
        Ear2.setRotationPoint(-4F, 1F, 4F);
        Ear2.setTextureSize(64, 32);
        Ear2.mirror = true;
        setRotation(Ear2, 0F, 0F, 0F);
        UpperLip = new ModelRenderer(this, 0, 0);
        UpperLip.addBox(0F, 0F, 0F, 4, 1, 2);
        UpperLip.setRotationPoint(-2F, 2F, -4F);
        UpperLip.setTextureSize(64, 32);
        UpperLip.mirror = true;
        setRotation(UpperLip, 0F, 0F, 0F);
        Chin = new ModelRenderer(this, 0, 0);
        Chin.addBox(0F, 0F, 0F, 4, 2, 2);
        Chin.setRotationPoint(-2F, 4F, -4F);
        Chin.setTextureSize(64, 32);
        Chin.mirror = true;
        setRotation(Chin, 0F, 0F, 0F);
        TopHair1 = new ModelRenderer(this, 0, 0);
        TopHair1.addBox(0F, 0F, 0F, 4, 1, 6);
        TopHair1.setRotationPoint(-2F, 0F, 0F);
        TopHair1.setTextureSize(64, 32);
        TopHair1.mirror = true;
        setRotation(TopHair1, 0F, 0F, 0F);
        TopHair2 = new ModelRenderer(this, 0, 0);
        TopHair2.addBox(0F, 0F, 0F, 2, 1, 3);
        TopHair2.setRotationPoint(-1F, -1F, 2F);
        TopHair2.setTextureSize(64, 32);
        TopHair2.mirror = true;
        setRotation(TopHair2, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Base1.render(f5);
        Base2.render(f5);
        Body1.render(f5);
        Body2.render(f5);
        Neck.render(f5);
        Head.render(f5);
        Ear1.render(f5);
        Ear2.render(f5);
        UpperLip.render(f5);
        Chin.render(f5);
        TopHair1.render(f5);
        TopHair2.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z){
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

}
