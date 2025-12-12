package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.ObjLoader;
import terrains.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();
//        StaticShader shader = new StaticShader();
//        Renderer renderer = new Renderer(shader);

        // OpenGL vuole i vertici in ordine antiorario di default
        /* float[] vertices = {
                // triangolo in basso a sinistra
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                // triangolo in alto a destra
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };*/

        /* quadrato
        float[] vertices = {
                -0.5f, 0.5f, 0f, //V0
                -0.5f, -0.5f, 0f, //V1
                0.5f, -0.5f, 0f, //V2
                0.5f, 0.5f, 0f //V3
        };

        int[] indices = {
                0, 1, 3, //triangolo in alto a sinistra (V0,V1,V3)
                3,1,2 //triangolo in basso a destra (V3,V1,V2)
        };

        float[] textureCoords = {
                0,0, //V0
                0,1, //V1
                1,1, //v2
                1,0 //V3
        }; */

        //cubo
//        float[] vertices = {
//                -0.5f,0.5f,-0.5f,
//                -0.5f,-0.5f,-0.5f,
//                0.5f,-0.5f,-0.5f,
//                0.5f,0.5f,-0.5f,
//
//                -0.5f,0.5f,0.5f,
//                -0.5f,-0.5f,0.5f,
//                0.5f,-0.5f,0.5f,
//                0.5f,0.5f,0.5f,
//
//                0.5f,0.5f,-0.5f,
//                0.5f,-0.5f,-0.5f,
//                0.5f,-0.5f,0.5f,
//                0.5f,0.5f,0.5f,
//
//                -0.5f,0.5f,-0.5f,
//                -0.5f,-0.5f,-0.5f,
//                -0.5f,-0.5f,0.5f,
//                -0.5f,0.5f,0.5f,
//
//                -0.5f,0.5f,0.5f,
//                -0.5f,0.5f,-0.5f,
//                0.5f,0.5f,-0.5f,
//                0.5f,0.5f,0.5f,
//
//                -0.5f,-0.5f,0.5f,
//                -0.5f,-0.5f,-0.5f,
//                0.5f,-0.5f,-0.5f,
//                0.5f,-0.5f,0.5f
//
//        };
//
//        float[] textureCoords = {
//
//                0,0,
//                0,1,
//                1,1,
//                1,0,
//                0,0,
//                0,1,
//                1,1,
//                1,0,
//                0,0,
//                0,1,
//                1,1,
//                1,0,
//                0,0,
//                0,1,
//                1,1,
//                1,0,
//                0,0,
//                0,1,
//                1,1,
//                1,0,
//                0,0,
//                0,1,
//                1,1,
//                1,0
//
//
//
//        };
//
//        int[] indices = {
//                0,1,3,
//                3,1,2,
//                4,5,7,
//                7,5,6,
//                8,9,11,
//                11,9,10,
//                12,13,15,
//                15,13,14,
//                16,17,19,
//                19,17,18,
//                20,21,23,
//                23,21,22
//
//
//        };


//        RawModel model = loader.loadToVAO(vertices, indices, textureCoords);
//        RawModel model = ObjLoader.loadObjModel("dragon", loader);
//        //ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
//        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white")));
//        ModelTexture texture = staticModel.getTexture();
//        texture.setShineDamper(10);
//        texture.setReflectivity(1);
        RawModel model = ObjLoader.loadObjModel("tree", loader);
        TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));

        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i=0;i<500;i++){
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
        }


//        Entity entity = new Entity(staticModel, new Vector3f(0,0,-30),0,0,0,1);
        Light light = new Light(new Vector3f(3000,2000,3000), new Vector3f(1,1,1));

        Terrain terrain = new Terrain(0,0, loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(1,0, loader, new ModelTexture(loader.loadTexture("grass")));

        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();
//        while (!Display.isCloseRequested()){
////            entity.increasePosition(0, 0, -0.1f);
//            entity.increaseRotation(0,0,0);
//            camera.move();
//            renderer.render(light, camera);
//            renderer.processEntity(entity);
////            renderer.prepare();
////            //game logic
////            shader.start();
////            shader.loadLight(light);
////            shader.loadViewMatrix(camera);
////            renderer.render(entity, shader);
////            shader.stop();
//            DisplayManager.updateDisplay();
//        }

        while(!Display.isCloseRequested()){
            camera.move();

            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            for(Entity entity:entities){
                renderer.processEntity(entity);
            }
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }


        renderer.cleanUp();
//        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
