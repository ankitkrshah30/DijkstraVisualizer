// src/main/java/com/dmsproject/dijkstravisualizer/UINode.java
package com.dmsproject.dijkstravisualizer;

import javafx.animation.FillTransition;
import javafx.animation.StrokeTransition;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents a visual node in the graph, extending StackPane to allow
 * layering of a Circle (the node's body) and Labels (ID and distance).
 * It also manages its own styling based on algorithm state and handles clicks.
 * Enhanced with visual effects and smoother transitions.
 */
public class UINode extends StackPane {

    private Circle circle;
    private Label idLabel;
    private Label distanceLabel;

    private String nodeId;
    private double currentDistance = Double.POSITIVE_INFINITY;
    private UINode previousNodeInPath;

    // --- NEW COLOR PALETTE & STYLES ---
    // Using a more cohesive and visually pleasing palette.
    private static final String DEFAULT_NODE_FILL = "#BBDEFB"; // Light Blue (Material Design Blue 100)
    private static final String DEFAULT_NODE_STROKE = "#2196F3"; // Deeper Blue (Material Design Blue 500)

    private static final String VISITED_NODE_FILL = "#E0E0E0"; // Light Grey (Material Design Grey 300)
    private static final String VISITED_NODE_STROKE = "#9E9E9E"; // Medium Grey (Material Design Grey 500)

    private static final String CURRENT_NODE_FILL = "#FFCC80"; // Light Orange (Material Design Orange 200)
    private static final String CURRENT_NODE_STROKE = "#FF9800"; // Vibrant Orange (Material Design Orange 500)

    private static final String PATH_NODE_FILL = "#A5D6A7"; // Light Green (Material Design Green 200)
    private static final String PATH_NODE_STROKE = "#4CAF50"; // Vibrant Green (Material Design Green 500)

    private static final String UNREACHABLE_NODE_FILL = "#BDBDBD"; // Medium Grey (Material Design Grey 400)
    private static final String UNREACHABLE_NODE_STROKE = "#757575"; // Dark Grey (Material Design Grey 600)

    private static final String FINALIZED_NODE_FILL = "#C8E6C9"; // Even Lighter Green (for finalized path nodes if distinct)

    // --- Highlighting Colors ---
    // Using a distinct, attention-grabbing color for selection
    private static final String SELECTION_HIGHLIGHT_COLOR = "#FFD54F"; // Amber (Material Design Amber 300)
    private static final String SELECTION_HIGHLIGHT_STROKE_COLOR = "#FFC107"; // Darker Amber (Material Design Amber 500)

    // --- NEW START/END NODE COLORS ---
    // Visually distinct colors for start and end nodes.
    private static final String START_NODE_FILL = "#9FA8DA"; // Indigo 200 (Soft blue-purple)
    private static final String START_NODE_STROKE = "#3F51B5"; // Indigo 500 (Deeper blue-purple)

    private static final String END_NODE_FILL = "#FFAB91"; // Deep Orange 200 (Warm, distinct orange)
    private static final String END_NODE_STROKE = "#FF5722"; // Deep Orange 500 (Vibrant orange)


    // Animation durations
    private static final Duration STYLE_TRANSITION_DURATION = Duration.millis(300);

    private Consumer<UINode> onClickHandler;

    public UINode(String nodeId, double x, double y) {
        this.nodeId = nodeId;

        circle = new Circle(25); // Slightly larger node for better visibility
        // Subtle shadow (30% opaque black)
        circle.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.web("#4D000000"), 5, 0.5, 0, 0));

        idLabel = new Label(nodeId);
        idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Clearer font
        idLabel.setTextFill(Color.BLACK); // Changed to black for better contrast on lighter default node

        distanceLabel = new Label("");
        distanceLabel.setFont(Font.font("Arial", 10)); // Smaller font for distance
        distanceLabel.setTextFill(Color.web("#616161")); // Darker grey for distance, contrasts with light node
        distanceLabel.setTranslateY(25); // Position below the node, might need adjustment for larger circle

        getChildren().addAll(circle, idLabel, distanceLabel);
        setDefaultStyle(); // Apply initial styling

        relocate(x - circle.getRadius(), y - circle.getRadius());

        this.setOnMouseClicked(this::handleNodeClicked);

        // Add hover effect for better interactivity
        this.setOnMouseEntered(e -> circle.setStrokeWidth(4)); // Thicker stroke on hover
        this.setOnMouseExited(e -> {
            String currentFill = ((Color) circle.getFill()).toString();
            // Revert stroke width based on the current fill color
            // Ensure the hover-out state accurately reflects the current node style's default stroke width
            if (currentFill.equalsIgnoreCase(Color.web(DEFAULT_NODE_FILL).toString()) ||
                    currentFill.equalsIgnoreCase(Color.web(UNREACHABLE_NODE_FILL).toString()) ||
                    // Also check for start/end node fills to revert to their intended stroke width
                    currentFill.equalsIgnoreCase(Color.web(START_NODE_FILL).toString()) ||
                    currentFill.equalsIgnoreCase(Color.web(END_NODE_FILL).toString())) {
                circle.setStrokeWidth(2); // Default, Unreachable, Start, End nodes can have 2px or adjusted base stroke
            } else if (currentFill.equalsIgnoreCase(Color.web(VISITED_NODE_FILL).toString()) ||
                    currentFill.equalsIgnoreCase(Color.web(FINALIZED_NODE_FILL).toString())) {
                circle.setStrokeWidth(3); // Visited and Finalized nodes have 3px stroke
            } else if (currentFill.equalsIgnoreCase(Color.web(CURRENT_NODE_FILL).toString()) ||
                    currentFill.equalsIgnoreCase(Color.web(PATH_NODE_FILL).toString())) {
                circle.setStrokeWidth(3.5); // Current and Path nodes have 3.5px stroke
            } else {
                circle.setStrokeWidth(2); // Fallback to default stroke width
            }
        });
    }

    public void setOnClickHandler(Consumer<UINode> handler) {
        this.onClickHandler = handler;
    }

    private void handleNodeClicked(MouseEvent event) {
        event.consume();
        if (onClickHandler != null) {
            onClickHandler.accept(this);
        }
    }

    // --- Getters ---
    public String getNodeId() {
        return nodeId;
    }

    public Circle getCircle() {
        return circle;
    }

    public double getCurrentDistance() {
        return currentDistance;
    }

    public UINode getPreviousNodeInPath() {
        return previousNodeInPath;
    }

    // --- Setters for Dijkstra-related data ---

    public void setCurrentDistance(double currentDistance) {
        this.currentDistance = currentDistance;
        updateDistanceLabel(currentDistance);
    }

    public void setPreviousNodeInPath(UINode previousNodeInPath) {
        this.previousNodeInPath = previousNodeInPath;
    }

    public void updateDistanceLabel(double distance) {
        if (distance == Double.POSITIVE_INFINITY) {
            distanceLabel.setText("∞");
        } else {
            distanceLabel.setText(String.format("%.1f", distance));
        }
    }

    // --- Node Styling Methods (using FillTransition for smooth changes) ---

    private void applyNodeStyle(String fillColorHex, String strokeColorHex, double strokeWidth, Color idLabelColor) {
        FillTransition fillTransition = new FillTransition(STYLE_TRANSITION_DURATION, circle, (Color) circle.getFill(), Color.web(fillColorHex));
        fillTransition.play();

        StrokeTransition strokeTransition = new StrokeTransition(STYLE_TRANSITION_DURATION, circle, (Color) circle.getStroke(), Color.web(strokeColorHex));
        strokeTransition.play();

        circle.setStrokeWidth(strokeWidth); // Stroke width changes instantly or can be animated too

        idLabel.setTextFill(idLabelColor); // ID label color change is instant for readability
        distanceLabel.setTextFill(idLabelColor.darker().darker()); // Distance label slightly darker for contrast
    }

    public void setDefaultStyle() {
        applyNodeStyle(DEFAULT_NODE_FILL, DEFAULT_NODE_STROKE, 2, Color.BLACK); // Black text on light blue
        circle.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.web("#4D000000"), 5, 0.5, 0, 0)); // Reset subtle shadow
    }

    public void setVisitedStyle() {
        applyNodeStyle(VISITED_NODE_FILL, VISITED_NODE_STROKE, 3, Color.BLACK); // Black text on grey
        circle.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.web("#4D000000"), 5, 0.5, 0, 0)); // Subtle shadow
    }

    public void setCurrentStyle() {
        applyNodeStyle(CURRENT_NODE_FILL, CURRENT_NODE_STROKE, 3.5, Color.BLACK); // Black text on light orange
        // More prominent shadow, using the current node's stroke color for coherence
        circle.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.web(CURRENT_NODE_STROKE), 10, 0.7, 0, 0));
    }

    // This style is for the nodes that are part of the final shortest path
    public void setPathStyle() {
        applyNodeStyle(PATH_NODE_FILL, PATH_NODE_STROKE, 3.5, Color.WHITE); // White text on green
        // More prominent shadow, using the path node's stroke color for coherence
        circle.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.web(PATH_NODE_STROKE), 10, 0.7, 0, 0));
    }

    // This style is for the specific END node if a path was found to it
    public void setPathEndStyle() {
        applyNodeStyle(PATH_NODE_FILL, PATH_NODE_STROKE, 3.5, Color.WHITE); // White text on green
        // Even more prominent shadow for the end node, slightly brighter glow
        circle.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.web(PATH_NODE_STROKE).brighter(), 15, 0.8, 0, 0));
    }

    public void setUnreachableStyle() {
        applyNodeStyle(UNREACHABLE_NODE_FILL, UNREACHABLE_NODE_STROKE, 2, Color.BLACK); // Black text on grey
        circle.setEffect(null); // Remove shadow if unreachable, makes it look "flat" or inactive
    }

    public void setFinalizedStyle() {
        // This is for nodes that have their final distance set in "all paths" mode
        applyNodeStyle(FINALIZED_NODE_FILL, VISITED_NODE_STROKE, 3, Color.BLACK); // Black text on light green
        circle.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.web("#4D000000"), 5, 0.5, 0, 0)); // Reset to subtle shadow
    }

    // --- NEW: Styles for Start and End Nodes (Aesthetic additions) ---
    /**
     * Applies the style for the designated start node.
     */
    public void setStartNodeStyle() {
        applyNodeStyle(START_NODE_FILL, START_NODE_STROKE, 4, Color.WHITE); // Thicker stroke for prominence
        // Add a slightly stronger shadow to make it pop
        circle.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.web(START_NODE_STROKE), 12, 0.7, 0, 0));
    }

    /**
     * Applies the style for the designated end node.
     */
    public void setEndNodeStyle() {
        applyNodeStyle(END_NODE_FILL, END_NODE_STROKE, 4, Color.WHITE); // Thicker stroke for prominence
        // Add a slightly stronger shadow to make it pop
        circle.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.web(END_NODE_STROKE), 12, 0.7, 0, 0));
    }

    /**
     * Applies a temporary highlight style when the node is selected for edge creation.
     * This makes the node visually distinct and clearly indicates its selection.
     */
    public void highlightAsSelectedForEdge() {
        // Apply the highlight color to the stroke
        circle.setStroke(Color.web(SELECTION_HIGHLIGHT_STROKE_COLOR));
        // Make the stroke significantly thicker to stand out
        circle.setStrokeWidth(5); // Increased stroke width for clear highlight

        // Add a strong glow effect specifically for selection
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web(SELECTION_HIGHLIGHT_COLOR).brighter()); // Brighter version for the glow
        glow.setRadius(15); // Large radius for a noticeable glow
        glow.setSpread(0.6); // How far the glow spreads
        glow.setBlurType(BlurType.GAUSSIAN);
        circle.setEffect(glow); // Apply the glow effect
    }

    // --- Overrides for proper collection usage ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UINode uiNode = (UINode) o;
        return Objects.equals(nodeId, uiNode.nodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId);
    }
}