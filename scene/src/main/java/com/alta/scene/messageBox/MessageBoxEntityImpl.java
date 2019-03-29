package com.alta.scene.messageBox;

import com.alta.scene.entities.MessageBoxEntity;
import com.google.common.base.Strings;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.*;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.RoundedRectangle;

import java.awt.Font;
import java.awt.*;

/**
 * Provides the implementation of {@link MessageBoxEntity}
 */
@Slf4j
public class MessageBoxEntityImpl implements MessageBoxEntity {

    private static final Color ROYAL_BLUE = new Color(6, 6, 6, 120);
    private static final Color NAVY = new Color(30, 30, 30, 150);

    /**
     * Creates the {@link MessageBoxEntityImpl} by given parameters.
     *
     * @param startCoordinates  - the start coordinates of message box.
     * @param width             - the width of message box.
     * @param height            - the height of message box.
     * @param marginRight       - the width margin to right.
     * @param marginLeft        - the width margin to left.
     * @param marginTop         - the width margin to top.
     * @return created {@link MessageBoxEntityImpl} instance.
     */
    @Builder
    public static MessageBoxEntityImpl create(Point startCoordinates,
                                              int width,
                                              int height,
                                              int marginRight,
                                              int marginLeft,
                                              int marginTop,
                                              TextAlignment textAlignment) {
        MessageBoxEntityImpl messageBoxEntity = new MessageBoxEntityImpl(startCoordinates, width, height);
        messageBoxEntity.marginRight = marginRight;
        messageBoxEntity.marginLeft = marginLeft;
        messageBoxEntity.marginTop = marginTop;
        if (textAlignment != null) {
            messageBoxEntity.textAlignment = textAlignment;
        }
        return messageBoxEntity;
    }

    private final Point startCoordinates;
    private final int width;
    private final int height;
    private int marginRight;
    private int marginLeft;
    private int marginTop;

    private UnicodeFont unicodeFont;
    private RoundedRectangle backgroundRectangle;
    private GradientFill backgroundGradient;
    private RoundedRectangle backgroundBorder;
    private boolean isInitialized;
    private TextAlignment textAlignment;

    private Point currentTextCoordinates;
    private String currentText;
    private int currentHideTimeout;

    private boolean visible;
    private String text;
    private int hideTimeout;

    /**
     * Initialize new instance of {@link MessageBoxEntityImpl}
     */
    private MessageBoxEntityImpl(Point startCoordinates, int width, int height) {
        this.startCoordinates = startCoordinates;
        this.width = width;
        this.height = height;
        this.textAlignment = TextAlignment.LEFT;
        this.currentTextCoordinates = new Point(0, 0);
    }

    /**
     * Initializes the renderable object in GL context if needed.
     *
     * @param container - the game container instance.
     */
    @Override
    public void initialize(GameContainer container) {
        this.backgroundRectangle = new RoundedRectangle(
                this.startCoordinates.x + this.marginLeft,
                this.startCoordinates.y + this.marginTop,
                this.width - this.marginRight - this.marginLeft,
                this.height,
                10
        );
        this.backgroundBorder = new RoundedRectangle(
                this.startCoordinates.x + this.marginLeft,
                this.startCoordinates.y + this.marginTop,
                this.width - this.marginRight - this.marginLeft,
                this.height,
                10
        );

        this.backgroundGradient = new GradientFill(
                this.backgroundRectangle.getX(),
                this.backgroundRectangle.getY(),
                NAVY,
                this.backgroundRectangle.getWidth(),
                this.backgroundRectangle.getHeight(),
                ROYAL_BLUE
        );


        this.unicodeFont = new UnicodeFont(new Font("Verdana", Font.ITALIC, 18));
        this.unicodeFont.addGlyphs(32, 1200);

        this.unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.white));
        this.unicodeFont.setPaddingAdvanceY(10);

        this.unicodeFont.addAsciiGlyphs();

        try {
            this.unicodeFont.loadGlyphs();
        } catch (SlickException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Updates the message box
     *
     * @param gameContainer - the game container instance
     * @param delta         - the delta between last and current calls
     */
    @Override
    public void onUpdateMessageBox(GameContainer gameContainer, int delta) {
        if (!this.isInitialized) {
            this.initialize(gameContainer);
            this.isInitialized = true;
        }

        if (!this.visible) {
            return;
        }

        if (this.hideTimeout > 0) {
            this.currentHideTimeout += delta;
            if (this.currentHideTimeout >= this.hideTimeout) {
                this.hide();
                return;
            }
        }

        // Imitation of animation for text.
        if (this.currentText.length() < this.text.length()) {
            this.currentText += this.text.substring(this.currentText.length(), this.currentText.length() + 1);
        }

        // If strings already equals then no need to update coordinates
        if (!this.text.equals(this.currentText)) {
            this.updateTextCoordinates();
        }
    }

    /**
     * Renders the object on given coordinates
     *
     * @param graphics - the graphics to render primitives
     */
    @Override
    public void render(Graphics graphics) {
        if (!this.visible || !this.isInitialized) {
            return;
        }

        graphics.fill(this.backgroundRectangle, this.backgroundGradient);
        graphics.setLineWidth(2);
        graphics.setColor(Color.lightGray);
        graphics.draw(this.backgroundBorder);

        if (this.unicodeFont != null && this.text != null) {
            this.unicodeFont.drawString(this.currentTextCoordinates.x, this.currentTextCoordinates.y, this.currentText);
        }
    }

    /**
     * Sets the text to be shown.
     */
    void setText(String text) {
        this.text = text;
        this.currentText = "";
    }

    /**
     * Marks message box as ready for showing.
     *
     * @param hideTimeout - the timeout when message box will be hide
     */
    void show(int hideTimeout) {
        this.visible = true;
        this.hideTimeout = hideTimeout;
        this.currentHideTimeout = 0;
    }

    /**
     * Marks message box as ready for showing.
     */
    void show() {
        this.show(0);
    }

    void hide() {
        this.visible = false;
        this.currentText = null;
        this.text = null;
        this.hideTimeout = -1;
        this.currentHideTimeout = -1;
    }

    private void updateTextCoordinates() {
        if (Strings.isNullOrEmpty(this.currentText)) {
            this.currentTextCoordinates.x = 0;
            this.currentTextCoordinates.y = 0;
            return;
        }

        final int defaultTextMargin = 10;
        if (this.textAlignment == TextAlignment.LEFT) {
            this.currentTextCoordinates.x = this.startCoordinates.x + this.marginLeft + defaultTextMargin;
            this.currentTextCoordinates.y = (this.height - this.startCoordinates.y) / 2 -
                    this.unicodeFont.getHeight(this.currentText) / 2;
        } else if (this.textAlignment == TextAlignment.CENTER) {
            this.currentTextCoordinates.x = (this.width - this.startCoordinates.x) / 2 -
                    this.unicodeFont.getWidth(this.text) / 2;
            this.currentTextCoordinates.y = (this.height - this.startCoordinates.y) / 2 -
                    this.unicodeFont.getHeight(this.currentText) / 2;
        }
    }

    enum TextAlignment {
        CENTER,
        LEFT
    }
}
