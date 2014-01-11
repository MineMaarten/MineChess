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

public class ModelKing extends ModelBase{
    // fields
    ModelRenderer Base1;
    ModelRenderer Base2;
    ModelRenderer Body;
    ModelRenderer Top;
    ModelRenderer LongCrossPiece;
    ModelRenderer ShortCrossPiece1;
    ModelRenderer ShortCrossPiece2;
    ModelRenderer Collar1;
    ModelRenderer Collar2;
    ModelRenderer Collar3;
    ModelRenderer Collar4;

    public ModelKing(){
        textureWidth = 64;
        textureHeight = 32;

        Base1 = new ModelRenderer(this, 0, 0);
        Base1.addBox(0F, 0F, 0F, 12, 3, 12);
        Base1.setRotationPoint(-6F, 21F, -6F);
        Base1.setTextureSize(64, 32);
        Base1.mirror = true;
        setRotation(Base1, 0F, 0F, 0F);
        Base2 = new ModelRenderer(this, 0, 0);
        Base2.addBox(0F, 0F, 0F, 10, 3, 10);
        Base2.setRotationPoint(-5F, 18F, -5F);
        Base2.setTextureSize(64, 32);
        Base2.mirror = true;
        setRotation(Base2, 0F, 0F, 0F);
        Body = new ModelRenderer(this, 0, 0);
        Body.addBox(0F, 0F, 0F, 8, 12, 8);
        Body.setRotationPoint(-4F, 6F, -4F);
        Body.setTextureSize(64, 32);
        Body.mirror = true;
        setRotation(Body, 0F, 0F, 0F);
        Top = new ModelRenderer(this, 0, 0);
        Top.addBox(0F, 0F, 0F, 10, 2, 10);
        Top.setRotationPoint(-5F, 4F, -5F);
        Top.setTextureSize(64, 32);
        Top.mirror = true;
        setRotation(Top, 0F, 0F, 0F);
        LongCrossPiece = new ModelRenderer(this, 0, 0);
        LongCrossPiece.addBox(0F, 0F, 0F, 2, 8, 2);
        LongCrossPiece.setRotationPoint(-1F, -4F, -1F);
        LongCrossPiece.setTextureSize(64, 32);
        LongCrossPiece.mirror = true;
        setRotation(LongCrossPiece, 0F, 0F, 0F);
        ShortCrossPiece1 = new ModelRenderer(this, 0, 0);
        ShortCrossPiece1.addBox(0F, 0F, 0F, 2, 2, 2);
        ShortCrossPiece1.setRotationPoint(1F, -2F, -1F);
        ShortCrossPiece1.setTextureSize(64, 32);
        ShortCrossPiece1.mirror = true;
        setRotation(ShortCrossPiece1, 0F, 0F, 0F);
        ShortCrossPiece2 = new ModelRenderer(this, 0, 0);
        ShortCrossPiece2.addBox(0F, 0F, 0F, 2, 2, 2);
        ShortCrossPiece2.setRotationPoint(-3F, -2F, -1F);
        ShortCrossPiece2.setTextureSize(64, 32);
        ShortCrossPiece2.mirror = true;
        setRotation(ShortCrossPiece2, 0F, 0F, 0F);
        Collar1 = new ModelRenderer(this, 0, 0);
        Collar1.addBox(0F, 0F, 0F, 1, 2, 11);
        Collar1.setRotationPoint(-6F, 2F, -6F);
        Collar1.setTextureSize(64, 32);
        Collar1.mirror = true;
        setRotation(Collar1, 0F, 0F, 0F);
        Collar2 = new ModelRenderer(this, 0, 0);
        Collar2.addBox(0F, 0F, 0F, 1, 2, 11);
        Collar2.setRotationPoint(5F, 2F, -5F);
        Collar2.setTextureSize(64, 32);
        Collar2.mirror = true;
        setRotation(Collar2, 0F, 0F, 0F);
        Collar3 = new ModelRenderer(this, 0, 0);
        Collar3.addBox(0F, 0F, 0F, 11, 2, 1);
        Collar3.setRotationPoint(-6F, 2F, 5F);
        Collar3.setTextureSize(64, 32);
        Collar3.mirror = true;
        setRotation(Collar3, 0F, 0F, 0F);
        Collar4 = new ModelRenderer(this, 0, 0);
        Collar4.addBox(0F, 0F, 0F, 11, 2, 1);
        Collar4.setRotationPoint(-5F, 2F, -6F);
        Collar4.setTextureSize(64, 32);
        Collar4.mirror = true;
        setRotation(Collar4, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Base1.render(f5);
        Base2.render(f5);
        Body.render(f5);
        Top.render(f5);
        LongCrossPiece.render(f5);
        ShortCrossPiece1.render(f5);
        ShortCrossPiece2.render(f5);
        Collar1.render(f5);
        Collar2.render(f5);
        Collar3.render(f5);
        Collar4.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z){
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

}
