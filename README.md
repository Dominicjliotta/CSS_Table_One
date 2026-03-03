# Public Transportation Safety Scoring System

## Team: TableOne

**Team Members:**  
Melissa Ryan  
David Visperas  
Kareem Fayed  
Lily Aguirre  
Dominic Liotta  

---

## Project Overview

The Public Transportation Safety Scoring System improves rider awareness by providing safety scores and incident reports for bus routes and stops.

Current transit tools focus on schedules and routing but lack real-time, rider-reported safety insights. This system adds situational awareness to help riders feel more informed and confident, especially new riders and tourists.

---

## Core Requirements

### 1. Transit Data Tracking
- Track bus routes and route numbers  
- Track stops associated with each route  
- Maintain accurate route timing  

### 2. Safety Scoring
- Calculate average safety score based on:
  - Selected route (average of stop scores)
  - Current time  
- Display safety scores for routes and stops  
- Display weekly route safety ratings  

### 3. Incident Tracking
- Track safety-related incidents from the last 6 months:
  - By route  
  - By stop  
- Provide default tags (Harassment, Theft, Aggressive Behavior, etc.)  
- Allow detailed incident descriptions  

### 4. Scope Limitation
- The system will **not** rank or recommend the “safest” routes or stops  

---

## API Overview

### Route APIs
- `getAllRoutes()`  
- `getRouteInfo(routeNumber)`  
- `getRouteStops(routeNumber)`  
- `getSortedRouteList(time, destination)` *(sorted by time/destination, not safety)*  
- `getBuses(routeNumber)`  

### Incident APIs
- `submitIncident(tag)`  
- `getIncidents(routeNumber)`  
- `getIncidentsByStop(locationName)`  
- `getRecentTags(routeNumber)`  

### Safety APIs
- `getWeeklyRating(routeNumber)`  

Additional CRUD APIs support routes, stops, incidents, and tags.

---

## Tech Stack

- **Database:** PostgreSQL  
- **UI:** Java (Console)  
- **Hosting:** Microsoft Azure  
