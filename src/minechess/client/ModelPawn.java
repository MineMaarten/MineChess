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

public class ModelPawn extends ModelBase{
    // fields
    ModelRenderer Base1;
    ModelRenderer Base2;
    ModelRenderer Base3;
    ModelRenderer LowerMiddel1;
    ModelRenderer LowerMiddle2;
    ModelRenderer Middle1;
    ModelRenderer Cross3;
    ModelRenderer Cross2;
    ModelRenderer Top1;
    ModelRenderer Cross1;
    ModelRenderer Top3;
    ModelRenderer Top2;
    ModelRenderer Cross4;
    ModelRenderer Cross5;

    public ModelPawn(){
        textureWidth = 64;
        textureHeight = 32;

        Base1 = new ModelRenderer(this, 0, 0);
        Base1.addBox(0F, 0F, 0F, 12, 2, 12);
        Base1.setRotationPoint(-6F, 22F, -6F);
        Base1.setTextureSize(64, 32);
        Base1.mirror = true;
        setRotation(Base1, 0F, 0F, 0F);
        Base2 = new ModelRenderer(this, 0, 0);
        Base2.addBox(0F, 0F, 0F, 9, 2, 9);
        Base2.setRotationPoint(-4.5F, 21.5F, -4.5F);
        Base2.setTextureSize(64, 32);
        Base2.mirror = true;
        setRotation(Base2, 0F, 0F, 0F);
        Base3 = new ModelRenderer(this, 0, 0);
        Base3.addBox(0F, 0F, 0F, 10, 3, 10);
        Base3.setRotationPoint(-5F, 19.5F, -5F);
        Base3.setTextureSize(64, 32);
        Base3.mirror = true;
        setRotation(Base3, 0F, 0F, 0F);
        LowerMiddel1 = new ModelRenderer(this, 0, 0);
        LowerMiddel1.addBox(0F, 0F, 0F, 11, 1, 11);
        LowerMiddel1.setRotationPoint(-5.5F, 20F, -5.5F);
        LowerMiddel1.setTextureSize(64, 32);
        LowerMiddel1.mirror = true;
        setRotation(LowerMiddel1, 0F, 0F, 0F);
        LowerMiddle2 = new ModelRenderer(this, 0, 0);
        LowerMiddle2.addBox(0F, 0F, 0F, 9, 1, 9);
        LowerMiddle2.setRotationPoint(-4.5F, 19F, -4.5F);
        LowerMiddle2.setTextureSize(64, 32);
        LowerMiddle2.mirror = true;
        setRotation(LowerMiddle2, 0F, 0F, 0F);
        Middle1 = new ModelRenderer(this, 0, 0);
        Middle1.addBox(0F, 0F, 0F, 5, 6, 5);
        Middle1.setRotationPoint(-2.5F, 13F, -2.5F);
        Middle1.setTextureSize(64, 32);
        Middle1.mirror = true;
        setRotation(Middle1, 0F, 0F, 0F);
        Cross3 = new ModelRenderer(this, 0, 0);
        Cross3.addBox(0F, 0F, 0F, 3, 2, 1);
        Cross3.setRotationPoint(-1.5F, 6.5F, -0.5F);
        Cross3.setTextureSize(64, 32);
        Cross3.mirror = true;
        setRotation(Cross3, 0F, 0F, 0F);
        Cross2 = new ModelRenderer(this, 0, 0);
        Cross2.addBox(0F, 0F, 0F, 1, 4, 1);
        Cross2.setRotationPoint(-0.5F, 6F, -0.5F);
        Cross2.setTextureSize(64, 32);
        Cross2.mirror = true;
        setRotation(Cross2, 0F, 0F, 0F);
        Top1 = new ModelRenderer(this, 0, 0);
        Top1.addBox(0F, 0F, 0F, 6, 6, 6);
        Top1.setRotationPoint(-3F, 9.6F, -3F);
        Top1.setTextureSize(64, 32);
        Top1.mirror = true;
        setRotation(Top1, 0.0174533F, 0F, 0F);
        Cross1 = new ModelRenderer(this, 0, 0);
        Cross1.addBox(0F, 0F, 0F, 1, 2, 3);
        Cross1.setRotationPoint(-0.5F, 6.5F, -1.5F);
        Cross1.setTextureSize(64, 32);
        Cross1.mirror = true;
        setRotation(Cross1, 0F, 0F, 0F);
        Top3 = new ModelRenderer(this, 0, 0);
        Top3.addBox(0F, 0F, 0F, 7, 4, 7);
        Top3.setRotationPoint(-3.5F, 10.5F, -3.5F);
        Top3.setTextureSize(64, 32);
        Top3.mirror = true;
        setRotation(Top3, 0.0174533F, 0F, 0F);
        Top2 = new ModelRenderer(this, 0, 0);
        Top2.addBox(0F, 0F, 0F, 8, 4, 8);
        Top2.setRotationPoint(-4F, 11F, -4F);
        Top2.setTextureSize(64, 32);
        Top2.mirror = true;
        setRotation(Top2, 0.0174533F, 0F, 0F);
        Cross4 = new ModelRenderer(this, 0, 0);
        Cross4.addBox(0F, 0F, 0F, 3, 2, 1);
        Cross4.setRotationPoint(-1.4F, 6.5F, 0.7F);
        Cross4.setTextureSize(64, 32);
        Cross4.mirror = true;
        setRotation(Cross4, 0F, 0.7853982F, 0F);
        Cross5 = new ModelRenderer(this, 0, 0);
        Cross5.addBox(0F, 0F, 0F, 3, 2, 1);
        Cross5.setRotationPoint(-0.7F, 6.5F, -1.4F);
        Cross5.setTextureSize(64, 32);
        Cross5.mirror = true;
        setRotation(Cross5, 0F, -0.7853982F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Base1.render(f5);
        Base2.render(f5);
        Base3.render(f5);
        LowerMiddel1.render(f5);
        LowerMiddle2.render(f5);
        Middle1.render(f5);
        Cross3.render(f5);
        Cross2.render(f5);
        Top1.render(f5);
        Cross1.render(f5);
        Top3.render(f5);
        Top2.render(f5);
        Cross4.render(f5);
        Cross5.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z){
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
