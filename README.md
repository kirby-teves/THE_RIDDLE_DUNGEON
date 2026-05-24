🎲 The Riddle Dungeon 🏰

A Java-based escape room game featuring procedural riddles and progressive difficulty for our OOP Capstone Project.

Members:
Deanver C. Gudin
Jojan Kaizer Jian Ramirez
John Patrick H. Escaran
Hayes Huxley Belangel
Justine Kirby T. Lepon
Awit Sandoy

🌟 Story Overview

The Riddle Dungeon plunges you into a mysterious underground labyrinth guarded by six unique Game Masters. Trapped by ancient magic, your only way out is to solve a series of increasingly difficult riddles. Each room presents a new challenge, a new guardian, and a new test of your wit. With limited hearts and no second chances, can you outsmart the dungeon masters and claim your freedom?

🎯 Game Objective:

Navigate through 6 themed rooms, defeat Game Masters by solving their riddles, manage your health (hearts) wisely, and escape the dungeon before running out of lives.

🏗️ System Architecture

Core Game Systems:
Room Progression: Linear progression through 6 distinct difficulty tiers (Easy → Medium → Hard)
Game Master AI: Unique personalities and greeting styles for each guardian (Kirby, Deanver, Jojan, Hayes, Awit, Patrick)
Dynamic Riddle Engine: Loads randomized riddles from external configuration (.env) for high replayability
Health & Penalty System: Heart-based life system with warnings and permanent death on zero hearts
Map Navigation: Visual dungeon map tracking completed and current rooms
Technical Features:
External Configuration: Riddles, answers, and hints loaded from .env file for easy editing without recompilation
Robust Save/Load System: Serialize game state to disk with corruption protection and auto-delete on death
Exception Handling: Graceful handling of missing files or corrupted saves with user-friendly dialogs
OOP Principles: Strict adherence to Encapsulation, Abstraction, Inheritance, and Polymorphism
GUI Integration: Seamless binding between IntelliJ GUI Designer forms and Java logic using CardLayout

🎮 Gameplay Features

Room Progression:
Rooms 1-2 (Easy): Warm-up riddles to learn the mechanics
Rooms 3-4 (Medium): Increased complexity and heart rewards
Rooms 5-6 (Hard): Challenging puzzles requiring careful thought
Game Masters:
Unique Guardians: Each room is guarded by a specific character with unique dialogue
Randomized Challenges: 10+ riddles per Game Master, randomly selected each playthrough
Interactive Dialogue: Immersive introductions before each puzzle begins
Mechanics:
Heart System: Start with 3 hearts, gain extra hearts at Rooms 3 and 5
Instant Feedback: Immediate visual cues for correct/incorrect answers
Low Health Warning: Pop-up alert when down to the last heart
Save & Resume: Manual save button to preserve progress across sessions
Map System:
Visual Tracker: See which rooms are completed, current location, and locked doors
Progress Overview: Quick glance at how far you’ve escaped

🚀 Quick Start

Prerequisites
Java JDK 17 or higher
IntelliJ IDEA (recommended for GUI Form binding)
Installation
Clone the repository.
Ensure the .env file is in the project root directory (contains all riddle data).
Open the project in IntelliJ IDEA.
Build and Run Main.java.
Controls
Type your answer in the text field.
Press "SOLVE" or Enter to submit.
Use "MAP" to view your progress.
Use "SAVE" to backup your current state.
Project Structure
src/
├── main/ # Entry point and Main Application controller
├── panels/ # UI Panels (Game, Menu, Map)
├── gamemasters/ # Abstract GameMaster and concrete implementations
├── model/ # Core Logic (Player, Room, GameManager)
├── util/ # Utilities (FileManager for Save/Load)
└── .env # External Riddle Configuration

⚙️ Configuration

The game uses a .env file to store riddles. Format:
KEY=VALUE
Example:
KIRBY_RIDDLE_1=What has keys but no locks?
KIRBY_ANSWER_1=piano
KIRBY_HINT_1=It makes music.
Ensure all keys follow the pattern: [MASTER]_[TYPE]_[NUMBER] where TYPE is RIDDLE, ANSWER, or HINT.
