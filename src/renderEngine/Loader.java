package renderEngine;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    // tengo traccia di VAO e VBO che creo
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    public RawModel loadToVAO(float[] positions, int[] indices, float[] textureCoords, float[] normals) {

        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, positions, 3); //inserisco le coordinate dei vertici
        storeDataInAttributeList(1, textureCoords, 2); //inserisco la mappatura della texture
        storeDataInAttributeList(2, normals, 3); //inserisco la mappatura delle normali
        unbindVAO();

        // positions.length/3 -> numero di vertici del modello, divido per 3 perché ogni vertice ha 3 dimensioni
        // return new RawModel(vaoID, positions.length/3);
        // con l'uso dell'IndexBuffer cambia la length del RawModel perché ora passo la lunghezza degli indici
        return new RawModel(vaoID, indices.length);

    }

    // metodo che carica la texture
    public int loadTexture(String fileName) {

        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int textureId = texture.getTextureID();
        textures.add(textureId);
        return textureId;

    }

    public void cleanUp() {
        for(int vao:vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo:vbos){
            GL15.glDeleteBuffers(vbo);
        }
        for(int texture: textures){
            GL11.glDeleteTextures(texture);
        }
    }

    // ritorna l'id del VAO
    private int createVAO(){

        int vaoID = GL30.glGenVertexArrays();
        // ogni volta che ne creo una la aggiungo alla lista
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;

    }

    // metodo che salva il VBO in uno degli attributi del VAO
    private void storeDataInAttributeList(int attributeNumber, float[] data, int coordinateSize){

        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        // salvo nel VBO
        // GL15.GL_STATIC_DRAW indica che carico i dati una volta e verrano usati molte volte per il rendering
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        // salvo nel VAO
        // in ordine: numero degli attributi, dimensione di ogni vertice, tipo di dati, normalizzazione,
        // distanza fra i vertici(se ci sono altri dati in mezzo), da dove iniziano i dati
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT,false,0,0);
        // unbind del VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

    }

    // metodo che rilascia il VAO
    private void unbindVAO(){

        GL30.glBindVertexArray(0);

    }

    // metodo che crea l'IndexBuffer per salvare la posizione degli indici dei vertici
    private void bindIndicesBuffer(int[] indices) {

        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

    }

    // metodo che salva la posizione dei vertiti nel buffer
    private IntBuffer storeDataInIntBuffer(int[] data) {

        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;

    }

    // metodo che salva i dati nel VBO, devono essere salvati come Float Buffer
    private FloatBuffer storeDataInFloatBuffer(float[] data){

        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;

    }
}
