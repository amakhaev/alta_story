package com.alta.scene.messageBox;

import com.alta.scene.entities.MessageBoxFrame;
import com.google.common.base.Strings;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.*;
import org.newdawn.slick.Image;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.RoundedRectangle;

import java.awt.Font;
import java.awt.*;
import java.util.ArrayList;

/**
 * Provides the implementation of {@link MessageBoxFrame}
 */
@Slf4j
public class MessageBoxFrameImpl implements MessageBoxFrame {

    private static final Color ROYAL_BLUE = new Color(6, 6, 6, 120);
    private static final Color NAVY = new Color(30, 30, 30, 150);
    private static final int FACE_IMAGE_WIDTH = 112;
    private static final int FACE_IMAGE_HEIGHT = 112;

    /**
     * Creates the {@link MessageBoxFrameImpl} by given parameters.
     *
     * @param startCoordinates  - the start coordinates of message box.
     * @param width             - the width of message box.
     * @param height            - the height of message box.
     * @param marginRight       - the width margin to right.
     * @param marginLeft        - the width margin to left.
     * @param marginTop         - the width margin to top.
     * @return created {@link MessageBoxFrameImpl} instance.
     */
    @Builder
    public static MessageBoxFrameImpl create(Point startCoordinates,
                                             int width,
                                             int height,
                                             int marginRight,
                                             int marginLeft,
                                             int marginTop,
                                             TextAlignment textAlignment) {
        MessageBoxFrameImpl messageBoxEntity = new MessageBoxFrameImpl(startCoordinates, width, height);
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

    private SpriteSheet faceSet;
    private Point currentFaceCoordinates;
    private FaceSetDescriptor faceSetDescriptor;

    /**
     * Initialize new instance of {@link MessageBoxFrameImpl}
     */
    private MessageBoxFrameImpl(Point startCoordinates, int width, int height) {
        this.startCoordinates = startCoordinates;
        this.width = width;
        this.height = height;
        this.textAlignment = TextAlignment.LEFT;
        this.currentTextCoordinates = new Point(0, 0);
        this.currentFaceCoordinates = new Point(0, 0);
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


        this.unicodeFont = new UnicodeFont(new Font("Verdana", Font.ITALIC, 16));
        this.unicodeFont.addGlyphs(32, 1200);

        this.unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.white));
        this.unicodeFont.setPaddingAdvanceY(10);

        this.unicodeFont.addAsciiGlyphs();

        try {
            this.unicodeFont.loadGlyphs();
        } catch (SlickException e) {
            log.error(e.getMessage());
        }

        // for correct formatting of text message
        this.setText(this.text);
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

        this.updateFaceCoordinates();
        if (this.faceSetDescriptor != null && this.faceSet == null) {
            this.faceSet = this.createFaceSetSpriteSheet();
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

        if (this.faceSet != null &&
                this.faceSetDescriptor != null &&
                this.currentFaceCoordinates.x != 0 &&
                this.currentFaceCoordinates.y != 0) {
            Image image = this.faceSet.getSubImage(
                    this.faceSetDescriptor.getTileToShowX(), this.faceSetDescriptor.getTileToShowY()
            );
            image.draw(this.currentFaceCoordinates.x, this.currentFaceCoordinates.y, FACE_IMAGE_WIDTH, FACE_IMAGE_HEIGHT);
            graphics.setLineWidth(8);
            graphics.setColor(new Color(255, 255, 255, 128));
            graphics.drawRoundRect(
                    this.currentFaceCoordinates.x - 5,
                    this.currentFaceCoordinates.y - 5,
                    FACE_IMAGE_WIDTH + 5,
                    FACE_IMAGE_HEIGHT + 5,
                    5
            );
        }
    }

    /**
     * Sets the text to be shown.
     */
    void setText(String text) {
        this.text = this.doFormatText(text);
        this.currentText = "";
    }

    /**
     * Sets the face set sprite sheet to be shown.
     *
     * @param faceSetDescriptor - the descriptor of sprite sheet to show face set.
     */
    void setFaceSet(FaceSetDescriptor faceSetDescriptor) {
        this.faceSetDescriptor = faceSetDescriptor;
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

    /**
     * Force hides the message box.
     */
    void hide() {
        this.visible = false;
        this.currentText = null;
        this.text = null;
        this.hideTimeout = -1;
        this.currentHideTimeout = -1;
        this.faceSetDescriptor = null;
        this.faceSet = null;
    }

    /**
     * Completes the animation of text rendering.
     */
    void completeDrawingImmediately() {
        this.currentText = this.text;
    }

    /**
     * Indicates when drawing of text is in progress.
     */
    boolean isDrawingInProgress() {
        return this.text != null && !this.currentText.equals(this.text);
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
            this.currentTextCoordinates.y = this.startCoordinates.y + this.marginTop + defaultTextMargin;

            if (this.faceSet != null && this.faceSetDescriptor != null) {
                this.currentTextCoordinates.x += FACE_IMAGE_WIDTH + defaultTextMargin;
            }
        } else if (this.textAlignment == TextAlignment.CENTER) {
            this.currentTextCoordinates.x = (this.width - this.startCoordinates.x) / 2 -
                    this.unicodeFont.getWidth(this.text) / 2;
            this.currentTextCoordinates.y = this.height / 2 - this.unicodeFont.getHeight(this.currentText) / 2;
        }
    }

    private void updateFaceCoordinates() {
        if (this.faceSet == null || this.faceSetDescriptor == null) {
            return;
        }

        final int defaultMargin = 5;
        this.currentFaceCoordinates.x = this.startCoordinates.x + this.marginLeft + 12;
        this.currentFaceCoordinates.y = this.startCoordinates.y + this.marginTop + (this.height / 2 - FACE_IMAGE_HEIGHT / 2);
    }

    private String doFormatText(String text) {
        if (this.unicodeFont == null || Strings.isNullOrEmpty(text)) {
            return text;
        }

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        StringBuilder formatStringBuilder = new StringBuilder();
        java.util.List<String> result = new ArrayList<>();

        for (String word: words) {
            if (this.unicodeFont.getWidth(currentLine + word) > this.width - this.marginLeft - this.marginRight) {
                formatStringBuilder.append(currentLine);
                formatStringBuilder.append("\n");
                currentLine = new StringBuilder();
            }

            if (this.unicodeFont.getHeight(formatStringBuilder.toString()) > this.height - this.marginTop) {
                result.add(formatStringBuilder.toString());
                formatStringBuilder.setLength(0);
            }

            currentLine.append(word).append(" ");
        }
        formatStringBuilder.append(currentLine);
        result.add(formatStringBuilder.toString());

        formatStringBuilder.setLength(0);
        return String.join(" ", result);
    }

    private SpriteSheet createFaceSetSpriteSheet() {
        try {
            return new SpriteSheet(
                    this.faceSetDescriptor.getFaceSetFilePath(),
                    this.faceSetDescriptor.getFaceSetTileWidth(),
                    this.faceSetDescriptor.getFaceSetTileHeight()
            );
        } catch (SlickException e) {
            log.error("Can't create face set sprite sheet: {}", e.getMessage());
            return null;
        }
    }

    enum TextAlignment {
        CENTER,
        LEFT
    }
}
