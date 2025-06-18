package com.github.wallev.maidsoulkitchen.inventory.container.item;

@SuppressWarnings("all")
public enum BagType {
    /**
     * use {@link BagType#INPUT }
     */
    @Deprecated(since = "0.1.4")
    INGREDIENT("Ingredient", "ingredient", 3, 0, 27, ColorA.RED),
    /**
     * use {@link BagType#INPUT }
     */
    @Deprecated(since = "0.1.4")
    START_ADDITION("StartAddition", "start_addition", 1, 27, 36, ColorA.YELLOW),
    /**
     * use {@link BagType#INPUT }
     */
    @Deprecated(since = "0.1.4")
    INGREDIENT_ADDITION("IngredientAddition", "ingredient_addition", 1, 36, 45, ColorA.CYAN),
    /**
     * use {@link BagType#INPUT }
     */
    @Deprecated(since = "0.1.4")
    OUTPUT_ADDITION("OutputAddition", "output_addition", 1, 45, 54, ColorA.ORANGE),
    OUTPUT("Output", "output", 1, 54, 63, ColorA.GREEN),
    INPUT("Input", "input", 6, 0, 54, ColorA.GREEN),
    ;

    public static final BagType[] ALL_VALS = BagType.values();
    public static final BagType[] DISPLAY_VALS = {INGREDIENT, OUTPUT};
    public static final BagType[] VALS = {INGREDIENT, START_ADDITION, INGREDIENT_ADDITION, OUTPUT_ADDITION, OUTPUT};
    public static final BagType[] INPUT_VALS = {INGREDIENT, START_ADDITION, INGREDIENT_ADDITION, OUTPUT_ADDITION};
    public static final BagType OUTPUT_VAL = OUTPUT;

    public final String name;
    public final String translateKey;
    public final int size;
    public final int startIndex;
    public final int endIndex;
    public final ColorA color;

    BagType(String name, String translateKey, int size, int startIndex, int endIndex, ColorA color) {
        this.name = name;
        this.translateKey = translateKey;
        this.size = size;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.color = color;
    }

    // TODO
    // 先这样...
    public enum ColorA {
        RED(1.0F, 0, 0),
        YELLOW(0.7F, 0.8F, 0),
        CYAN(0, 0.8F, 0.8F),
        ORANGE(0.9F, 0.8F, 0),
        GREEN(0, 1.0F, 0);

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
