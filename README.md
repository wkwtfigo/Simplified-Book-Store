# Simplified Book Store System

A simple console-based BookStore system implemented in Java, demonstrating several classic software design patterns including Singleton, Builder, Observer (Publisher-Subscriber), Strategy, and Facade.
This application allows users to register, subscribe for notifications, read books, and (for premium users) listen to audiobooks.

## Features

* 📕 Add new books to the bookstore (with title, author, and price).

* 👤 Register new users (standard or premium).

* 📬 Subscribe/unsubscribe users for price update notifications.

* 💰 Update book prices and automatically notify subscribers.

* 📖 Allow users to read books.

* 🎧 Allow premium users to listen to audiobooks.

## Design Patterns Used

| Pattern       | Purpose                                                                                |
| ------------- | -------------------------------------------------------------------------------------- |
| **Singleton** | Ensure a single instance of `BookStore` throughout the application.                    |
| **Builder**   | Simplify the creation of complex `Book` objects via a fluent interface.                |
| **Observer**  | Allow users to subscribe and receive notifications when book prices change.            |
| **Strategy**  | Define flexible behaviors for reading and listening to books for different user types. |
| **Facade**    | Provide a simple, unified interface for interacting with the bookstore system.         |

## How to Run

1. Clone the Repository

   ```bash
   git clone https://github.com/yourusername/bookstore-java.git
   cd bookstore-java
   ```

2. Compile and Run

   ```bash
   javac Main.java
   java Main
   ```

3. Available Console Commands

* `createBook <title> <author> <price>`

* `createUser <user_type> <username>` (`user_type` = `standard` or `premium`)

* `subscribe <username>`

* `unsubscribe <username>`

* `updatePrice <title> <new_price>`

* `readBook <username> <title>`

* `listenBook <username> <title>`

* `end` — to terminate the program

## Example Session

  ```text
  createBook Dune Herbert 20
  createUser premium Alice
  createUser standard Bob
  subscribe Alice
  updatePrice Dune 25
  readBook Alice Dune
  listenBook Alice Dune
  listenBook Bob Dune
  end
  ```
Output:
  ```text
  Alice notified about price update for Dune to 25
  Alice reading Dune by Herbert
  Alice listening Dune by Herbert
  No access
  ```

## Project Structure

  ```text
  .
  ├── Main.java          // Application entry point with command parser
  ├── BookStore.java     // Singleton bookstore with Observer logic
  ├── Facade.java        // Simplified interface for external interaction
  ├── Book.java          // Book class with Builder pattern
  ├── User.java          // User base class + StandardUser & PremiumUser
  ├── ReadingBehavior.java, ListenBehavior.java
  ├── CanRead.java, CanListen.java, CanNotListen.java // Strategy implementations
  └── Observable.java    // Observer interface
  ```
