package amylopez.makelines.gui;

import amylopez.makelines.util.ReflectionUtil;
import org.apache.commons.configuration.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The panel that displays a message
 */
@Singleton
public class MessagePanel extends BackgroundComponent implements Paintable {

    @Inject
    private Configuration config;

    @Inject
    private ReflectionUtil reflectionUtil;

    /**
     * Dimensions of this panel
     */
    private Dimension dimension;

    /**
     * The color the string should be drawn in
     */
    private Color textColor;

    /**
     * The y delta between lines of strings
     */
    private int stringHeight;

    /**
     * The message to print
     */
    private String message;

    /**
     * The message to display (if it's an image)
     */
    private BufferedImage image;

    /**
     * Initialize the message view from configuration
     */
    public void init() {
        String configPath = "gui/message/";

        int posX = config.getInt(configPath + "position/@x");
        int posY = config.getInt(configPath + "position/@y");
        int width = config.getInt(configPath + "dimensions/@width");
        int height = config.getInt(configPath + "dimensions/@height");
        String backgroundColorName = config.getString(configPath + "@color");
        String borderColorName = config.getString(configPath + "@border_color");
        String textColorName = config.getString(configPath + "string/@color");
        stringHeight = config.getInt(configPath + "string/@height");

        setSize(dimension = new Dimension(width, height));
        setLocation(new Point(posX, posY));
        backgroundColor = reflectionUtil.getColor(backgroundColorName);
        borderColor = reflectionUtil.getColor(borderColorName);
        textColor = reflectionUtil.getColor(textColorName);
    }

    /**
     * Display message, makes this panel visible immediately
     * @param message String the message to display
     */
    public void showMessage(final String message) {
        this.message = message;
        setVisible(true);
    }

    /**
     * Display message, makes this panel visible immediately
     * @param image BufferedImage
     */
    public void showMessage(final BufferedImage image) {
        this.image = image;
        setVisible(true);
    }

    public void hideMessage() {
        setVisible(false);
        message = null;
        image = null;
    }

    @Override
    public void paint(Graphics2D g2) {
        if (message != null && !message.isEmpty()) {
            g2.setColor(textColor);
            String[] lines = message.split("\n");

            // determine the top position so the text can be vertically centered
            int top = (dimension.height / 2) - ((lines.length * stringHeight) / 2);

            for (int line = 0; line < lines.length; line++) {
                g2.drawString(lines[line], 6, top + (line * stringHeight));
            }
        } else if (image != null) {
            g2.drawImage(image, 0, 0, null);
        }
    }
}
