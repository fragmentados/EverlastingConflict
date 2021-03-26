package everlastingconflict.elementosvisuales;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ComboBoxOption {
    public String text;
    public String description;
    public Image sprite;
    public Color color;

    public ComboBoxOption(String text) {
        this.text = text;
    }

    public ComboBoxOption(String text, String description) {
        this.text = text;
        this.description = description;
    }

    public ComboBoxOption(String text, String description, String spritePath) {
        this(text, description);
        try {
            sprite = new Image(spritePath);
        } catch (SlickException e) {
        }
    }

    public ComboBoxOption(String text, String description, String spritePath, Color color) {
        this(text, description, spritePath);
        this.color = color;
    }

}
