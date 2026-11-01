# Memory Lane

**Mobile Development Project — BSc (Hons) Computer Science, Griffith College Cork**

**Student:** Aline Andrade Costa  
**Student ID:** 3144929  
**Module:** Mobile Development  
**Project:** Developing an Android App  

## Project Overview

Memory Lane is a location-aware personal memory application for Android. It allows users to save personal memories linked to real-world places and rediscover them when they return nearby.

Instead of functioning mainly as a chronological diary, the application uses the user's current location to determine which stored memories are relevant. The core experience is built around:

- **Around You** — prioritises memories close to the user's current location.
- **You're Back Here** — highlights a saved memory when the user returns within its rediscovery radius.
- **Memory Compass** — combines location and device orientation to provide approximate directional guidance towards a selected memory.

## Core Activities

1. **Home / Around You Activity**  
   Displays nearby memories, return moments and recent memories.

2. **Add Memory Activity**  
   Allows the user to create a memory with a title, description, date, category and current location.

3. **Memory Detail Activity**  
   Displays the selected memory, current distance and actions such as Share, Open in Maps and Memory Compass.

4. **All Memories Activity**  
   Displays all saved memories and allows browsing/filtering by category and date.

5. **Memory Compass Activity**  
   Uses location and device orientation to show the approximate direction and distance to a selected memory.

## Sensors / Mobile Capabilities

### GPS / Device Location
Used to:
- capture the location associated with a memory;
- obtain the user's current location;
- calculate distance to stored memories;
- rank memories in Around You;
- trigger You're Back Here return moments;
- provide the current position used by Memory Compass.

### Device Orientation / Rotation
Used by Memory Compass to determine the direction the phone is facing and provide approximate guidance towards a selected memory.

## Storage

The application will use local persistent storage on the Android device.

Core stored information:
- title;
- description;
- date;
- category;
- latitude;
- longitude;
- rediscovery radius.

Simple local preferences may also be stored. The initial version does not require login or cloud storage.

## Android Features

- Multiple Activities with data shared between them
- Explicit intents for internal navigation
- Implicit intent to open a saved location in Maps
- Implicit Share intent
- Runtime location permission handling
- Local persistent storage
- GPS/location input
- Orientation/rotation sensor input
- Mobile-focused error, loading and empty states

## Milestone Plan

### Proposal — 20 October 2026
- Project concept
- Planned Activities
- Sensors
- Storage

### Milestone 1 — 10 November 2026
Target: application structure, navigation and basic interaction.

Planned evidence:
- at least three distinctive functional layouts;
- five planned Activities created progressively;
- explicit navigation between Activities;
- Share and Maps implicit intents;
- sample/mock memory data;
- basic UI and user interaction;
- initial design documentation;
- Git repository with visible development history.

### Milestone 2 — 24 November 2026
Target: dynamic sensor-driven behaviour and most major features.

Planned evidence:
- current device location;
- location permission handling;
- saved memory coordinates;
- distance calculation;
- Around You ranking;
- You're Back Here return moments;
- device orientation/rotation input;
- working Memory Compass;
- persistent memory data introduced before or during this milestone so location features can work with real stored memories.

### Milestone 3 — 7 December 2026
Target: complete, stable and deployment-ready application.

Planned evidence:
- final persistent local storage;
- reliable create/read memory flow;
- filtering/browsing;
- robust permission, loading, empty and error states;
- responsive mobile UI;
- testing and stability improvements;
- performance/optimisation;
- final documentation.

### Live Demonstration — 8 December 2026
The final demo should show:
- nearby memories on Home;
- creation of a new memory using the current location;
- You're Back Here / nearby behaviour;
- Memory Detail;
- Memory Compass;
- Maps implicit intent;
- Share implicit intent;
- persistence after closing and reopening the app.

## Suggested Repository Organisation

```text
MemoryLane/
├── app/
│   └── src/main/
│       ├── java/.../
│       │   ├── activities/
│       │   ├── model/
│       │   ├── data/
│       │   ├── sensors/
│       │   └── utils/
│       └── res/
├── docs/
│   ├── proposal/
│   ├── milestone-1/
│   ├── milestone-2/
│   ├── milestone-3/
│   └── screenshots/
├── README.md
└── .gitignore
```

The exact package structure may be adjusted during implementation. The repository should remain simple and easy to navigate.

## Development Priorities

**Priority 1 — Core flow**
- Activities
- navigation
- create/view memory
- sample data
- intents

**Priority 2 — Persistence and location**
- local storage
- coordinates
- current location
- distance calculation
- Around You
- You're Back Here

**Priority 3 — Second sensor**
- orientation/rotation
- Memory Compass

**Priority 4 — Stability and polish**
- permissions
- error states
- empty states
- mobile optimisation
- final UI refinement

Optional enhancements should only be added after the required core is stable.


