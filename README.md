# ⛅ Weather App – Android MVVM

A modern **Android weather application** that provides real-time weather updates, recent city tracking, and dynamic unit conversion — all powered by a **clean, modular MVVM architecture**. Users can view current conditions, switch temperature/wind/pressure units, and use GPS-based location detection for weather retrieval.

---

## 🔧 Technologies Used

- 🤖 **Android** – Native Android app with Material Design principles  
- 🟠 **Kotlin** – Primary development language  
- 📦 **Coroutines + Flow** – For asynchronous, reactive data streams  
- 💉 **Dagger Hilt** – For dependency injection and lifecycle-scoped components  
- 🌐 **OpenWeatherMap API + Retrofit** – External weather data provider  
- 📍 **Fused Location Provider** – Device location services  
- 🗃 **DataStore** – Persistence layer for settings and preferences  
- 🧠 **MVVM** – Modular and scalable architecture  
- 🧭 **Navigation** – Type-safe, argument-driven in-app routing
- 🧱 **Jetpack Compose** – Declarative UI framework, ideal for focus-aware UIs
---

## 🧱 Architecture Overview

The app is organized into well-separated layers:

- **`app/`** – Main entry point, Hilt setup, and NavGraph  
- **`domain/`** – Business logic and core models, interfaces for use cases and repositories
- **`di/`** - Hilt modules that define how dependencies are provided and configured for injection throughout your app
- **`data/`** – Repository implementations, data sources, and mappers  
- **`presentation/`** – UI layer: ViewModels, UI models, mappers, utilities  

Each layer communicates **only with the one directly below or above** it, ensuring **clean architecture** principles are respected.

---

## 🔁 Data Flow

1. User launches app → ViewModel checks if **location** is enabled in settings.  
2. If enabled and permissions are granted, **device coordinates** are fetched.  
3. Coordinates are passed to a **CityNameResolver** to identify the city.  
4. The city name is sent to the **GetWeatherUseCase**, which queries the API.  
5. Data is **mapped to a display model** (`WeatherDisplayData`) using injected mappers.  
6. Results are emitted as **UI state** and rendered on screen.  
7. Recent cities are cached and rendered via another UI state using **GetRecentCitiesUseCase**.  
8. User can also manually search for cities or change settings (units, time format, etc.), which will **reactively update UI**.
9. Clicking on recent cities will show the weather info for that city like it was searched again

---

## 🎯 Features

- 🌍 Real-time weather for searched or current location  
- 🧠 Smart unit conversion based on user preferences (temperature, pressure, wind)  
- 🏙️ Recent cities preview with cached data  
- 📍 Location support with permission handling  
- ⚙️ Settings screen with toggleable units and formats  
- 🔁 Fully reactive ViewModel state management  
- 📐 Structured mapping between domain and UI layers  

---

## 🧠 Architectural Decisions

- **Domain layer is interface-driven**, which simplifies testing and makes code more flexible  
- **UI logic is decoupled from ViewModels** via `WeatherUiMapper` and `RecentCityUiMapper`  
- **StateFlow and sealed classes** are used to represent UI state (loading, success, error, empty)  
- **Mappers remain in presentation**, as they handle display-specific formatting  
- **Location logic** is separated into `LocationProvider` and `CityNameResolver` utilities for single responsibility  

---

## 🛠️ Planned Improvements
  
- 🔔 Notification support for severe weather alerts  
- 🔍 Advanced search with autocomplete  
- 📤 Shareable forecasts via text/social  
- 🌙 Dark/light mode support  
- 🧪 Full unit and UI test coverage  

---
