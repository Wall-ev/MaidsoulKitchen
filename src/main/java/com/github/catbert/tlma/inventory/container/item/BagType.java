package com.github.catbert.tlma.inventory.container.item;

public enum BagType {
    INGREDIENT("Ingredient", 3, 0 ,27, ColorA.RED),
    START_ADDITION("StartAddition", 1, 27, 36, ColorA.YELLOW),
    INGREDIENT_ADDITION("IngredientAddition", 1, 36,45, ColorA.CYAN),
    OUTPUT_ADDITION("OutputAddition", 1, 45, 54, ColorA.ORANGE),
    OUTPUT("Output", 1, 54, 63, ColorA.GREEN);
    public final String name;
    public final int size;
    public final int startIndex;
    public final int endIndex;
    public final ColorA color;

    BagType(String name, int size, int startIndex, int endIndex, ColorA color) {
        this.name = name;
        this.size = size;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.color = color;
    }

    // TODO
    // 先这样...
    public enum ColorA {
        RED(1.0F, 0 ,0),
        YELLOW(0.7F, 0.8F ,0),
        CYAN(0, 0.8F, 0.8F),
        ORANGE(0.9F, 0.8F, 0),
        GREEN(0, 1.0F ,0);

        private final float red;
        private final float green;
        private final float blue;

        ColorA(float red, float green, float blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public float getRed() {
            return red;
        }

        public float getGreen() {
            return green;
        }

        public float getBlue() {
            return blue;
        }
    }
}
