package com.flight_sim;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;

public class TerrainGeneration {
    public static final int BOX_SIZE = 50;
    public static final int BOXES_PER_ROW = 255;
    private static final int MAX_HEIGHT = 150;
    private static final Color GRASS_SPECULAR_COLOR = Color.LIGHTGREEN;
    private static final Color GRASS_DIFFUSE_COLOR = Color.GREEN;

    private Box[] cubes;

    public void createTerrain(Group group, int boxesPerRow) {
        int boxes = boxesPerRow * boxesPerRow;

        // create new array for the landscape to be stored
        this.cubes = new Box[boxes];

        // materials
        final PhongMaterial grass = new PhongMaterial();
        grass.setSpecularColor(GRASS_SPECULAR_COLOR);
        grass.setDiffuseColor(GRASS_DIFFUSE_COLOR);

        // creating the landscape
        for (int i = 0; i < boxes; i++) {
            this.cubes[i] = new Box(BOX_SIZE, ceil(Math.random() * MAX_HEIGHT), BOX_SIZE);
            this.cubes[i].translateXProperty().set(i % Math.sqrt((double) boxes) * BOX_SIZE);
            this.cubes[i].translateZProperty().set(floor((double) i / Math.sqrt((double) boxes)) * BOX_SIZE);
            this.cubes[i].setMaterial(grass);
            group.getChildren().add(this.cubes[i]);
        }
    }
}
