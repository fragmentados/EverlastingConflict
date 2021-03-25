package everlastingconflict.elementosvisuales;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ComboBoxOption {
    public String text;
    public String description;
    public Image sprite;

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

}
