// src/main/java/com/dmsproject.dijkstravisualizer/UIEdge.java
package com.dmsproject.dijkstravisualizer;

import javafx.animation.StrokeTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class UIEdge extends Group {

    private Line line;
    private Label weightLabel;
    private Rectangle labelBackground;

    private UINode sourceNode;
    private UINode targetNode;
    private double weight;

    // --- ENHANCED COLOR PALETTE & STYLING CONSTANTS ---
    private static final String DEFAULT_EDGE_COLOR = "#B0BEC5";
    private static final double DEFAULT_EDGE_STROKE_WIDTH = 2.0;

    private static final String RELAXED_EDGE_COLOR = "#FFC107";
    private static final double RELAXED_EDGE_STROKE_WIDTH = 3.0;

    private static final String PATH_EDGE_COLOR = "#4CAF50";
    private static final double PATH_EDGE_STROKE_WIDTH = 4.0;

    // Colors for the label's background and text, chosen to complement the line colors
    private static final String LABEL_DEFAULT_BG_COLOR = "#FFFFFF";
    private static final String LABEL_DEFAULT_BG_STROKE_COLOR = "#BDBDBD";
    private static final String LABEL_DEFAULT_TEXT_COLOR = "#212121";

    private static final String LABEL_RELAXED_BG_COLOR = "#FFF3E0";
    private static final String LABEL_RELAXED_BG_STROKE_COLOR = "#FF9800";
    private static final String LABEL_RELAXED_TEXT_COLOR = "#E65100";

    // UPDATED: Path label background and text for better contrast
    private static final String LABEL_PATH_BG_COLOR = "#388E3C"; // Darker green for background (Material Green 700)
    private static final String LABEL_PATH_BG_STROKE_COLOR = "#2E7D32"; // Even darker green for border (Material Green 800)
    private static final String LABEL_PATH_TEXT_COLOR = "#FFFFFF"; // White text for contrast on dark green


    private static final Duration EDGE_TRANSITION_DURATION = Duration.millis(300);

    public UIEdge(UINode source, UINode target, double weight) {
        this.sourceNode = source;
        this.targetNode = target;
        this.weight = weight;

        line = new Line();
        line.setStroke(Color.web(DEFAULT_EDGE_COLOR));
        line.setStrokeWidth(DEFAULT_EDGE_STROKE_WIDTH);

        weightLabel = new Label(String.valueOf(weight));
        weightLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        weightLabel.setTextFill(Color.web(LABEL_DEFAULT_TEXT_COLOR));

        labelBackground = new Rectangle(0, 0, 30, 20);
        labelBackground.setArcWidth(5);
        labelBackground.setArcHeight(5);
        labelBackground.setFill(Color.web(LABEL_DEFAULT_BG_COLOR, 0.8));
        labelBackground.setStroke(Color.web(LABEL_DEFAULT_BG_STROKE_COLOR));
        labelBackground.setStrokeWidth(1);

        getChildren().addAll(line, labelBackground, weightLabel);

        line.startXProperty().bind(sourceNode.layoutXProperty().add(sourceNode.getCircle().radiusProperty()));
        line.startYProperty().bind(sourceNode.layoutYProperty().add(sourceNode.getCircle().radiusProperty()));
        line.endXProperty().bind(targetNode.layoutXProperty().add(targetNode.getCircle().radiusProperty()));
        line.endYProperty().bind(targetNode.layoutYProperty().add(targetNode.getCircle().radiusProperty()));

        line.startXProperty().addListener((obs, oldVal, newVal) -> updateLabelPosition());
        line.startYProperty().addListener((obs, oldVal, newVal) -> updateLabelPosition());
        line.endXProperty().addListener((obs, oldVal, newVal) -> updateLabelPosition());
        line.endYProperty().addListener((obs, oldVal, newVal) -> updateLabelPosition());

        updateLabelPosition();

        setDefaultStyle();
    }

    private void updateLineCoordinates() {
        line.setStartX(sourceNode.getLayoutX() + sourceNode.getCircle().getRadius());
        line.setStartY(sourceNode.getLayoutY() + sourceNode.getCircle().getRadius());
        line.setEndX(targetNode.getLayoutX() + targetNode.getCircle().getRadius());
        line.setEndY(targetNode.getLayoutY() + targetNode.getCircle().getRadius());
    }

    private void updateLabelPosition() {
        double startX = line.getStartX();
        double startY = line.getStartY();
        double endX = line.getEndX();
        double endY = line.getEndY();

        double midX = (startX + endX) / 2;
        double midY = (startY + endY) / 2;

        Point2D vector = new Point2D(endX - startX, endY - startY);
        Point2D perpendicular = new Point2D(-vector.getY(), vector.getX()).normalize();

        double offsetDistance = 15;

        double labelX = midX + perpendicular.getX() * offsetDistance;
        double labelY = midY + perpendicular.getY() * offsetDistance;

        double labelWidth = weightLabel.prefWidth(-1);
        double labelHeight = weightLabel.prefHeight(-1);

        weightLabel.relocate(labelX - labelWidth / 2, labelY - labelHeight / 2);

        labelBackground.setWidth(labelWidth + 10);
        labelBackground.setHeight(labelHeight + 5);
        labelBackground.relocate(labelX - (labelWidth + 10) / 2, labelY - (labelHeight + 5) / 2);
    }

    public UINode getSourceNode() {
        return sourceNode;
    }

    public UINode getTargetNode() {
        return targetNode;
    }

    public double getWeight() {
        return weight;
    }

    public Line getLine() {
        return line;
    }

    private void applyVisualStyle(String strokeColorHex, double strokeWidth,
                                  Color labelTextColor, Color labelBgFill, String labelBgStrokeColor,
                                  boolean addGlow, Double[] dashArray) {
        StrokeTransition strokeTransition = new StrokeTransition(EDGE_TRANSITION_DURATION, line, (Color) line.getStroke(), Color.web(strokeColorHex));
        strokeTransition.play();

        line.setStrokeWidth(strokeWidth);

        if (dashArray != null) {
            line.getStrokeDashArray().setAll(dashArray);
        } else {
            line.getStrokeDashArray().clear();
        }

        weightLabel.setTextFill(labelTextColor);
        labelBackground.setFill(labelBgFill);
        labelBackground.setStroke(Color.web(labelBgStrokeColor));

        if (addGlow) {
            DropShadow glow = new DropShadow();
            glow.setColor(Color.web(strokeColorHex).brighter());
            glow.setRadius(10);
            glow.setSpread(0.5);
            glow.setBlurType(BlurType.GAUSSIAN);
            line.setEffect(glow);
        } else {
            line.setEffect(null);
        }
    }

    public void highlightAsPath() {
        applyVisualStyle(PATH_EDGE_COLOR, PATH_EDGE_STROKE_WIDTH,
                Color.web(LABEL_PATH_TEXT_COLOR), Color.web(LABEL_PATH_BG_COLOR, 0.9), LABEL_PATH_BG_STROKE_COLOR, // Increased opacity slightly
                true, null);
    }

    public void resetStyle() {
        applyVisualStyle(DEFAULT_EDGE_COLOR, DEFAULT_EDGE_STROKE_WIDTH,
                Color.web(LABEL_DEFAULT_TEXT_COLOR), Color.web(LABEL_DEFAULT_BG_COLOR, 0.8), LABEL_DEFAULT_BG_STROKE_COLOR,
                false, null);
    }

    public void highlightAsRelaxed() {
        applyVisualStyle(RELAXED_EDGE_COLOR, RELAXED_EDGE_STROKE_WIDTH,
                Color.web(LABEL_RELAXED_TEXT_COLOR), Color.web(LABEL_RELAXED_BG_COLOR, 0.8), LABEL_RELAXED_BG_STROKE_COLOR,
                true, new Double[]{8.0, 5.0});
    }

    public void setDefaultStyle() {
        applyVisualStyle(DEFAULT_EDGE_COLOR, DEFAULT_EDGE_STROKE_WIDTH,
                Color.web(LABEL_DEFAULT_TEXT_COLOR), Color.web(LABEL_DEFAULT_BG_COLOR, 0.8), LABEL_DEFAULT_BG_STROKE_COLOR,
                false, null);
    }

    public void setRelaxedStyle() {
        applyVisualStyle(RELAXED_EDGE_COLOR, RELAXED_EDGE_STROKE_WIDTH,
                Color.web(LABEL_RELAXED_TEXT_COLOR), Color.web(LABEL_RELAXED_BG_COLOR, 0.8), LABEL_RELAXED_BG_STROKE_COLOR,
                true, new Double[]{8.0, 5.0});
    }

    public void setPathStyle() {
        applyVisualStyle(PATH_EDGE_COLOR, PATH_EDGE_STROKE_WIDTH,
                Color.web(LABEL_PATH_TEXT_COLOR), Color.web(LABEL_PATH_BG_COLOR, 0.9), LABEL_PATH_BG_STROKE_COLOR, // Increased opacity slightly
                true, null);
    }
}