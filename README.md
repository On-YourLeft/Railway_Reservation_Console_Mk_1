# 🚂 Railway Reservation System (Mk. 1)

![Java](https://img.shields.io/badge/Java-17+-ED8B00.svg?logo=java)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36.svg?logo=apachemaven)
![OOP](https://img.shields.io/badge/Architecture-OOP-4CAF50.svg)

An enterprise-pattern, console-based Java application designed to simulate a real-world Railway Reservation backend. Built with strictly decoupled Object-Oriented principles, the system manages train scheduling, dynamic ticket pricing (AC vs. Sleeper), passenger PNR tracking, and secure role-based access control.

## ✨ Core Features
* **Role-Based Authentication:** Distinct operational flows for `Admin` (managing trains/fares) and `Customer` (booking/canceling tickets).
* **Robust File-Based Storage:** Utilizes a custom `FileManager` service to persist Users, Trains, Reservations, and PNR Records into CSV/Excel files safely across sessions.
* **Dynamic Fare Configuration:** Calculates ticket prices based on `ClassSeatInfo` (ACClassTicket vs. SleeperClassTicket) and base fare rules.
* **PNR Lifecycle Management:** Generates unique PNRs upon booking and allows customers to check live ticket status.
* **Data Seeding:** Includes a `DataSeeder` utility to automatically populate the system with base admins, routes, and trains on first launch.

## 🛠️ Tech Stack
* **Language:** Java
* **Dependency Management:** Apache Maven (`pom.xml`)
* **Storage:** Local File I/O (CSV / TXT / XLSX parsing logic)
* **Architecture:** MVC/Service-Repository Pattern
