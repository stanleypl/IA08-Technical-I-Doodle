 IA08: DoodleApp

This project is a simple doodle drawing application built for my IA08: Technical I, Doodle assignment. The goal of the app is to practice modern Android development using Kotlin and Jetpack Compose, including state management, Canvas drawing, and gesture handling.

The app lets the user draw freely on a canvas, change brush settings, and clear or undo strokes. A bonus feature (Undo) is also included.

Features: 
Drawing Canvas
- Draw freely using touch or mouse
- Real time rendering 
- Supports lines, curves, circles, and any freehand shape

 Brush Controls
- Change brush size with a slider 
- Pick from 20+ colors
- Current brush color is highlighted
- Colors update instantly for new strokes

Canvas Controls
- Clear button removes all strokes
- Undo button removes the last stroke 

Technologies Used
- Kotlin
- Jetpack Compose
- Canvas API
- PointerInput + detectDragGestures()
- State management (mutableStateListOf)
