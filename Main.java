import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Facade bookstore = new Facade();

        String command = scanner.next();

        while (!command.equals("end")) {
            switch (command) {
                case "createBook" -> {
                    String title = scanner.next();
                    String author = scanner.next();
                    String price = scanner.next();
                    bookstore.createBook(title, author, price);
                }
                case "createUser" -> {
                    String user_type = scanner.next();
                    String username = scanner.next();
                    bookstore.createUser(username, user_type);
                }
                case "subscribe" -> {
                    String username = scanner.next();
                    bookstore.subscribe(username);
                }
                case "unsubscribe" -> {
                    String username = scanner.next();
                    bookstore.unsubscribe(username);
                }
                case "updatePrice" -> {
                    String title = scanner.next();
                    String price = scanner.next();
                    bookstore.updatePrice(title, price);
                }
                case "readBook" -> {
                    String username = scanner.next();
                    String title = scanner.next();
                    bookstore.readBook(username, title);
                }
                case "listenBook" -> {
                    String username = scanner.next();
                    String title = scanner.next();
                    bookstore.listenBook(username, title);
                }
            }
            command = scanner.next();
        }
    }
}

/**
 * The Facade class provides a simplified interface for interacting with the BookStore system.
 * It encapsulates the complexity of the system and exposes high-level methods for common operations.
 */

class Facade {
    private BookStore bookstore;

    public Facade() {
        bookstore = BookStore.getInstance();
    }

    /**
     * Creates a new book with the use of Builder design pattern and adds it to the bookstore.
     *
     * @param title  the title of the book
     * @param author the author of the book
     * @param price  the price of the book
     */
    public void createBook(String title, String author, String price) {
        Book.BookBuilder bookBuilder = new Book.BookBuilder();
        Book book = bookBuilder.nameBook(title)
                .setAuthor(author)
                .setPrice(price)
                .forSell();
        bookstore.addBook(book);
    }

    /**
     * Creates a new user and adds them to the bookstore.
     *
     * @param username the username of the user
     * @param userType the type of the user (standard or premium)
     */
    public void createUser(String username, String userType) {
        if (userType.equals("standard")) {
            bookstore.addUser(new StandardUser(username));
        } else {
            bookstore.addUser(new PremiumUser(username));
        }
    }

    /**
     * Subscribes a user to the bookstore's notifications.
     *
     * @param username the username of the user to subscribe
     */
    public void subscribe(String username) {
        bookstore.registerSubscriber(bookstore.getUser(username));
    }

    /**
     * Unsubscribes a user from the bookstore's notifications.
     *
     * @param username the username of the user to unsubscribe
     */
    public void unsubscribe(String username) {
        bookstore.removeSubscriber(bookstore.getUser(username));
    }

    /**
     * Updates the price of a book and notifies subscribers.
     *
     * @param title the title of the book to update
     * @param price the new price of the book
     */
    public void updatePrice(String title, String price) {
        bookstore.notifySubscribers(bookstore.getBook(title), price);
    }

    /**
     * Allows a user to read a book from the bookstore.
     *
     * @param username the username of the user
     * @param title    the title of the book to read
     */
    public void readBook(String username, String title) {
        bookstore.readBook(bookstore.getBook(title), bookstore.getUser(username));
    }

    /**
     * Allows a user to listen to an audiobook from the bookstore.
     *
     * @param username the username of the user
     * @param title    the title of the audiobook to listen to
     */
    public void listenBook(String username, String title) {
        bookstore.listenBook(bookstore.getBook(title), bookstore.getUser(username));
    }
}

/**
 * The Observable interface represents an object that can be observed by subscribers.
 * It defines methods for managing subscribers and notifying them of changes.
 */

interface Observable {
    void registerSubscriber(User user);
    void removeSubscriber(User user);
    void notifySubscribers(Book book, String price);
}

/**
 * The BookStore class represents a store that manages books and users.
 * It implements the Observable interface to allow users to subscribe and receive notifications about book prices updates.
 */

class BookStore implements Observable {
    /** The list of books available in the bookstore. */
    public List<Book> books;
    /** The list of users registered in the bookstore. */
    public List<User> users;
    /** The list of subscribers who receive notifications about book updates. */
    public List<User> subscribers;

    /** The singleton instance of the BookStore class. */
    private static BookStore instance;
    private BookStore() {
        books = new ArrayList<>();
        users = new ArrayList<>();
        subscribers = new ArrayList<>();
    }
    public static BookStore getInstance() {
        if (instance == null) {
            instance = new BookStore();
        }
        return instance;
    }

    public void addBook(Book book) {
        for (Book item : books) {
            if (item.title.equals(book.title)) {
                System.out.println("Book already exists");
                return;
            }
        }
        books.add(book);
    }

    public void addUser(User user) {
        for (User item : users) {
            if (user.userName.equals(item.userName)) {
                System.out.println("User already exists");
                return;
            }
        }
        users.add(user);
    }

    @Override
    public void registerSubscriber(User user) {
        if (subscribers.contains(user)) {
            System.out.println("User already subscribed");
        } else {
            subscribers.add(user);
        }
    }

    @Override
    public void removeSubscriber(User user) {
        if (!subscribers.contains(user)) {
            System.out.println("User is not subscribed");
        } else {
            subscribers.remove(user);
        }
    }

    @Override
    public void notifySubscribers(Book book, String price) {
        book.price = price;
        for (User item : subscribers) {
            item.update(book);
        }
    }

    public void readBook(Book book, User user) {
        user.doRead(book);
    }

    public void listenBook(Book book, User user) {
        user.doListen(book);
    }

    public User getUser(String username) {
        for (User user : users) {
            if (username.equals(user.userName)) {
                return user;
            }
        }
        return null;
    }

    public Book getBook(String title) {
        for (Book book : books) {
            if (title.equals(book.title)) {
                return book;
            }
        }
        return null;
    }
}

class Book {
    String title;
    String author;
    String price;

    public Book(String title, String author, String price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    /**
     * The BookBuilder class provides a fluent interface for building Book objects.
     */
    public static class BookBuilder {
        private String title;
        private String author;
        private String price;

        /**
         * Sets the title of the book being built.
         *
         * @param title the title of the book
         * @return the BookBuilder instance for method chaining
         */
        public BookBuilder nameBook(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the author of the book being built.
         *
         * @param author the author of the book
         * @return the BookBuilder instance for method chaining
         */
        public BookBuilder setAuthor(String author) {
            this.author = author;
            return this;
        }

        /**
         * Sets the price of the book being built.
         *
         * @param price the price of the book
         * @return the BookBuilder instance for method chaining
         */
        public BookBuilder setPrice(String price) {
            this.price = price;
            return this;
        }

        /**
         * Constructs and returns a new Book object based on the provided parameters.
         *
         * @return the constructed Book object
         */
        public Book forSell() {
            return new Book(title, author, price);
        }
    }
}

/**
 * The ReadingBehavior interface represents the behavior of user reading a book.
 */
interface ReadingBehavior {
    void read(Book book, String userName);
}

/**
 * The ListenBehavior interface represents the behavior of user listening to a book.
 */
interface ListenBehavior {
    void listen(Book book, String userName);
}

/**
 * The CanRead class implements the ReadingBehavior interface for users who can read books.
 */
class CanRead implements ReadingBehavior {
    public void read(Book book, String userName) {
        System.out.println(userName + " reading " + book.title + " by " + book.author);
    }
}

/**
 * The CanListen class implements the ListenBehavior interface for users who can listen to books.
 */
class CanListen implements ListenBehavior {
    public void listen(Book book, String userName) {
        System.out.println(userName + " listening " + book.title + " by " + book.author);
    }
}

/**
 * The CanNotListen class implements the ListenBehavior interface for users who cannot listen to books.
 */
class CanNotListen implements ListenBehavior {
    public void listen(Book book, String userName) {
        System.out.println("No access");
    }
}

/**
 * The User class represents a user of the bookstore.
 */
class User {
    /** The behavior for reading books. */
    public ReadingBehavior readingBehavior;
    /** The behavior for listening to books. */
    public ListenBehavior listenBehavior;

    String userName;

    public User(String userName) {
        this.userName = userName;
    }

    /**
     * Reads the specified book using the assigned reading behavior.
     *
     * @param book the book to be read
     */
    void doRead(Book book) {
        readingBehavior.read(book, this.userName);
    }

    /**
     * Listens to the specified book using the assigned listening behavior.
     *
     * @param book the book to be listened to
     */
    void doListen(Book book) {
        listenBehavior.listen(book, this.userName);
    }

    /**
     * Updates the user with the price update for the specified book.
     *
     * @param book the book with the updated price
     */
    void update(Book book) {
        System.out.println(userName + " notified about price update for " + book.title + " to " + book.price);
    }
}

/**
 * The StandardUser class represents a standard user of the bookstore.
 */
class StandardUser extends User {
    /**
     * Constructs a new StandardUser object with the specified username and behavior.
     *
     * @param userName the username of the standard user
     */
    public StandardUser(String userName) {
        super(userName);
        readingBehavior = new CanRead();
        listenBehavior = new CanNotListen();
    }
}

/**
 * The PremiumUser class represents a premium user of the bookstore.
 */
class PremiumUser extends User {
    /**
     * Constructs a new PremiumUser object with the specified username and behavior.
     *
     * @param userName the username of the premium user
     */
    public PremiumUser(String userName) {
        super(userName);
        readingBehavior = new CanRead();
        listenBehavior = new CanListen();
    }
}
