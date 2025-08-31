# 🎵 Java Media Player  

## 📌 Overview  
This project is a **Java-based Media Player** developed step-by-step.  
It supports parsing audio file paths, extracting metadata, managing playlists, filtering/sorting, and features a **JavaFX GUI** with play/pause/stop/next controls.  

The repository contains the **final implementation**. Earlier tasks are explained below as development steps.  

---

## 🛠️ Features by Task  

### ✅ Task 1 – AudioFile Basics  
- Parse audio file paths and normalize OS separators.  
- Split filenames into **author, title, extension**.  
- Provide `toString()` for display.  

### ✅ Task 2 – Inheritance Hierarchy  
- Introduced abstract `AudioFile` and subclasses:  
  - `SampledFile` → handles playback.  
  - `TaggedFile` → reads metadata (ID3/Vorbis tags).  
  - `WavFile` → calculates duration from frame data.  
- Playback via `BasicPlayer` (play/pause/stop).  

### ✅ Task 3 – Playlist  
- Implemented `PlayList` using `LinkedList<AudioFile>`.  
- Added `add/remove`, `nextSong`, and position tracking.  
- Save/load playlists in **M3U format**.  
- Introduced **factory method** for creating the correct `AudioFile` type.  

### ✅ Task 4 – Sorting, Filtering & Iteration  
- Comparators for sorting by **author, title, album, duration**.  
- `SortCriterion` enum for user-selected order.  
- `ControllablePlayListIterator` for flexible iteration.  
- Custom `NotPlayableException` for error handling.  

### ✅ Task 5 – GUI (Final Stage)  
- Built with **JavaFX** (`studiplayer.ui`).  
- Playlist displayed in a `TableView` (`SongTable`).  
- Filter by search text and sort criterion.  
- Buttons for **Play / Pause / Stop / Next**.  
- Multi-threaded playback with a **PlayerThread** and **TimerThread** (to keep GUI responsive).  

---

## ▶️ How to Run  

### 1) Clone the Repo  
```bash
git clone https://github.com/taahaasrarahmed/java-mediaplayer.git
cd Java-MediaPlayer
