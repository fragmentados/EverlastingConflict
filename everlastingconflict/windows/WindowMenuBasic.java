package everlastingconflict.windows;

import org.newdawn.slick.*;

import static everlastingconflict.elements.util.ElementosComunes.MENU_COLOR;

public class WindowMenuBasic extends Window {

    private TrueTypeFont titleFont;
    private TrueTypeFont subTitleFont;
    private static final String title = "THE EVERLASTING CONFLICT";
    public static String subTitle;

    @Override
    public void init(GameContainer container) throws SlickException {
        titleFont = new TrueTypeFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 50),
                true);
        subTitleFont = new TrueTypeFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 24),
                true);
    }

    public void startGame(GameContainer container) {}

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        Input input = container.getInput();
        if (input.isKeyPressed(Input.KEY_ENTER)) {
            startGame(container);
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            WindowMain.windowSwitch(container, "Menu");
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setBackground(MENU_COLOR);
        titleFont.drawString(
                (WindowCombat.VIEWPORT_SIZE_WIDTH - (title.length() * 24)) / 2, 0, title);
        if (subTitle != null) {
            subTitleFont.drawString(
                    (WindowCombat.VIEWPORT_SIZE_WIDTH - (subTitle.length() * 10)) / 2,
                    WindowCombat.responsiveY(30), subTitle);
        }
    }
}
