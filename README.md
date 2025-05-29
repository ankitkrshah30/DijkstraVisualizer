# Dijkstra's Algorithm Visualizer

An interactive educational tool designed to help users understand and visualize Dijkstra's shortest path algorithm through a dynamic graphical interface.

## 🚀 Features

### Interactive Graph Building
- **Add Nodes**: Create custom nodes on the canvas with unique IDs
- **Add Edges**: Connect nodes with weighted edges to define connection costs
- **Graph Management**: Save, load, and reset graph configurations

### Algorithm Visualization
- **Step-by-Step Execution**: Watch the algorithm unfold in real-time
- **Animation Controls**: Play, pause, step forward/backward through the visualization
- **Adjustable Speed**: Control the animation pace to match your learning speed
- **State Display**: Monitor priority queue and node distances in real-time

### User Interface
- **Intuitive Design**: Clean JavaFX GUI with responsive layout
- **Dynamic Resizing**: Adjustable split-pane for optimal viewing experience
- **Visual Feedback**: Clear indication of current algorithm state and progress

## 📋 Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven
- JavaFX SDK 21 or higher
- JGraphT Library

## 🛠️ Installation

1. Clone the repository: https://github.com/ankitkrshah30/DijkstraVisualizer.git
2. Ensure your JAVA_HOME environment variable is properly configured.
3. Build the project: mvn clean install
3. Build the project: mvn clean install
4. Run the application: mvn javafx:run
## 🎮 Usage Guide

### Basic Operations

1. **Creating Nodes**
   - Click "Add Node" button
   - Click anywhere on the canvas
   - Enter a unique ID when prompted

2. **Creating Edges**
   - Click "Add Edge" button
   - Select first node
   - Select second node
   - Enter edge weight when prompted

3. **Running the Algorithm**
   - Select start and end nodes from dropdowns
   - Click "Run Dijkstra"
   - Use animation controls to navigate through the visualization

### Animation Controls

- ▶️ Play: Start automatic visualization
- ⏸️ Pause: Freeze the current state
- ⏭️ Step Forward: Move one step ahead
- ⏮️ Step Backward: Return to previous step
- 🎚️ Speed Slider: Adjust animation speed

## 📁 Project Structure
dijkstra-visualizer/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── dmsproject/
        │           └── dijkstravisualizer/
        │               ├── Main.java
        │               ├── DijkstraController.java
        │               ├── UINode.java
        │               ├── UIEdge.java
        │               ├── AlgorithmStep.java
        │               └── NodeDistance.java  (Helper class for PQ/ListView)
        └── resources/
            └── com/
                └── dmsproject/
                    └── dijkstravisualizer/
                        └── DijkstraVisualizer.fxml
                └── icons/                   <-- Place your .png icons here
                    ├── new.png
                    ├── load.png
                    ├── save.png
                    ├── exit.png
                    ├── play.png
                    ├── pause.png
                    ├── reset.png
                    ├── info.png
                    ├── node.png
                    ├── edge.png
                    └── path.png

## ⚙️ Configuration

### Maven Dependencies

Ensure your `pom.xml` includes:
- JavaFX dependencies
- JGraphT library
- Appropriate compiler source/target versions

### Resource Requirements

- Icon files must be placed in `src/main/resources/icons/`
- FXML file must be properly configured with correct icon paths
- Default window size: 1200x1000 pixels (adjustable in FXML)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.
