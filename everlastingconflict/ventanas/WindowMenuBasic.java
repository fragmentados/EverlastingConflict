package everlastingconflict.ventanas;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;

public class WindowMenuBasic extends Window {

    private final String title = "THE EVERLASTING CONFLICT";
    public static TextField usuario;

    @Override
    public void init(GameContainer container) throws SlickException {
        usuario = new TextField(container, container.getDefaultFont(), 100, 50, 100, 20);
        usuario.setText("Prueba");
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        Input input = container.getInput();
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            WindowMain.exit(container);
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setBackground(new Color(0.3f, 0.3f, 0.1f, 0f));
        g.drawString(title, (WindowCombat.VIEWPORT_SIZE_WIDTH - (title.length() * 10)) / 2, 0);
        g.drawString("Usuario: ", 20, 50);
        usuario.render(container, g);
    }
}
