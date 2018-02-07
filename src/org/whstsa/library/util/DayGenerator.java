package org.whstsa.library.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.whstsa.library.api.BookType;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.CannotDeregisterException;
import org.whstsa.library.api.exceptions.CheckedInException;
import org.whstsa.library.api.exceptions.OutstandingFinesException;
import org.whstsa.library.api.exceptions.OutOfStockException;
import org.whstsa.library.api.impl.Book;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;

public class DayGenerator {

	private static final Random RANDOM = new Random();


	/* List of things needed to randomize (brainstorm)
	 * Adding Members 1/4 pd
	 * Removing Members (more rare-ish) 1/50 per person, MUST NOT HAVE FINE/BOOK, if cannot remove, will remove as soon as possible
	 * Adding New Books (names) 1/4 pd
	 * Getting Books / Removing Books 1/5 per person, MUST HAVE NO FINE AND NOT ON DEREGISTERED LIST
	 * Pay Fine  1/5 per person with fine, IF THEY HAVE ENOUGH
	 **/

	private static Logger LOGGER = new Logger("DayGenerator");
	private static List<UUID> deregisterPendingPeople = new ArrayList<>();
	private static List<String> actions = new ArrayList<>();

	public static List<String> simulateDay() {
		actions = new ArrayList<>();
		ObjectDelegate.getAllMembers().stream().filter(member -> deregisterPendingPeople.contains(member.getID())).collect(Collectors.toList()).forEach(member -> {
			try {
				member.getLibrary().removeMember(member);
				actions.add(String.format("%s has finally been deregistered ", member.getName()));
				deregisterPendingPeople.remove(member.getID());
			} catch (CannotDeregisterException ex) {
				//actions.add(String.format("Couldn't deregister %s yet: %s ", member.getName(), ex.getMessage())); (Commented out because don't want debug screen to be bombarded with couldn't deregister yet)
			}
		});
		while (chance(4)) {
			actions.add(generateMember(randomLibrary()));
		}
		ObjectDelegate.getAllMembers().forEach(member -> {
			if (chance(50) && !deregisterPendingPeople.contains(member)) {
				try {
					member.getLibrary().removeMember(member);
					actions.add(String.format("%s has been deregistered ", member.getName()));
				} catch (CannotDeregisterException ex) {
					actions.add(String.format("Couldn't deregister %s: %s Deregistering as soon as possible", member.getName(), ex.getMessage()));
					deregisterPendingPeople.add(member.getID());
				}
			}
		});
		while (chance(4)) {
			actions.add(generateBook());
		}
		ObjectDelegate.getAllMembers().forEach(member -> {
			if (chance(5)) {
				if (member.getBooks().size() != 0 && (RANDOM.nextBoolean() || deregisterPendingPeople.contains(member.getID()))) {
					ICheckout checkout = member.getCheckouts().get(0);
					try {
						member.checkIn(checkout);
						actions.add(member.getName() + " returned " + checkout.getBook().getName() + " (" + member.getLibrary().getQuantity(checkout.getBook().getID()) + ")");
						member.removeBook(checkout.getBook());
					} catch (CheckedInException e) {
						actions.add(member.getName() + " tried to return " + checkout.getBook().getName() + " but was already returned.");
					} catch (OutstandingFinesException e) {
						actions.add(e.getMessage());
					}
				}
				else if (member.getLibrary().getBooks().size() != 0 && member.getFine() == 0 && !deregisterPendingPeople.contains(member.getID())) {
					List<IBook> bookDB = member.getLibrary().getBooks();
					ILibrary library = member.getLibrary();
					int randomBookIndex = RANDOM.nextInt(bookDB.size());
					if (randomBookIndex == bookDB.size() && randomBookIndex != 0) {
						randomBookIndex -= 1;
					}
					IBook book = bookDB.get(randomBookIndex);
					if (book != null) {
						try {
							library.reserveBook(member, book, RANDOM.nextInt(10));
							actions.add(member.getName() + " took " + book.getName() + " (" + library.getQuantity(book.getID()) + " books remaining )");
						}
						catch (OutOfStockException e) {
							actions.add(e.getMessage());
						}
					}
				}
				else {
					actions.add(member.getName() + " still has fines to pay before they can grab another book ($" + member.getFine()  + ") or they are on the deregister list");
				}
			}
		});
		ObjectDelegate.getAllMembers().stream().filter(member -> chance(5) && member.getFine() != 0.0).collect(Collectors.toList()).forEach(member -> {
			List<ICheckout> checkoutList = member.getCheckouts();
			for (ICheckout checkout : checkoutList) {
				double fine = checkout.getFine();
				if (fine != 0.0) {
					checkout.payFine();
					actions.add(String.format("Payed off fee for %s for %s of $%s", checkout.getBook().getName(), checkout.getOwner().getName(), fine));
				}
			}
		});
		return actions;
	}

	public static String generateMember(ILibrary library) {
		// Creating a new person and adding them to a random library
		IPerson person = new Person(generateFirstName(), generateLastName(), chance(5));
		library.addMember(person);
		Loader.getLoader().loadPerson(person);
		return (String.format("Added %s %s to %s (%s)", person.getFirstName(), person.getLastName(), library.getName(), person.isTeacher()));
	}

	public static String generateBook() {
		// Creating a new book and adding it to a random library
		String bookName = generateBookTitle();
		String authorName = generateName();
		BookType bookType = randomBookType();
		IBook book = new Book(bookName, authorName, bookType);
		ILibrary library = randomLibrary();
		Loader.getLoader().loadBook(book);
		library.addBook(book, RANDOM.nextInt(9) + 1);
		return (String.format("Added a %s book named %s by %s to %s", bookType.name(), book.getName(), book.getAuthorName(), library.getName()));
	}

	public static String generateFirstName() {
		String[] firstNames = new String[] {"Jacob", "Emily", "Michael", "Madison", "Joshua", "Emma", "Matthew", "Olivia", "Daniel", "Hannah", "Christopher", "Abigail", "Andrew", "Isabella", "Ethan", "Samantha", "Joseph", "Elizabeth", "William", "Ashley", "Anthony", "Alexis", "David", "Sarah", "Alexander", "Sophia", "Nicholas", "Alyssa", "Ryan", "Grace", "Tyler", "Ava", "James", "Taylor", "John", "Brianna", "Johnathan", "Lauren", "Noah", "Chloe", "Brandon", "Natalie", "Christian", "Kayla", "Dylan", "Jessica", "Samuel", "Anna", "Benjamin", "Victoria"};
		return firstNames[RANDOM.nextInt(firstNames.length - 1)];
	}

	public static String generateLastName() {
		String[] lastNames = new String[] {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall", "Allen", "Young", "Hernandez", "King", "Wright", "Lopez", "Hill", "Scott", "Green", "Adams", "Baker", "Gonzalez", "Nelson", "Carter", "Mitchell" , "Perez", "Roberts", "Turner", "Phillips", "Campbell", "Parker", "Evans", "Edwards", "Collins"};
		return lastNames[RANDOM.nextInt(lastNames.length - 1)];
	}

	public static String generateName() {
		return generateFirstName() + " " + generateLastName();
	}

	public static String generateBookTitle() {
		String[] bookName = new String[] {"Harry Potter", "The Book Thief", "The Chronicles of Narnia", "Animal Farm", "Gone with the Wind", "The Hobbit", "The Fault in Our Stars", "The Hitchiker's Guide to the Galaxy", "The Giving Tree", "Wuthering Heights", "The Da Vinci Code", "Alice's Adventures in Wonderland", "Divergent", "Romeo and Juliet", "The Alchemist", "Lord of the Flies", "Crime and Punishment", "Ender's Game", "City of Bones", "Charlotte's Web", "Of Mice and Men", "Dracula", "Brave New World", "One Hundred Years of Solitude", "A Wrinkle in Time", "The Catcher in the Rye", "The Adventures of Huckleberry Finn", "Where the Wild Things Are", "Green Eggs and Ham", "Game of Thrones", "The Lightning Thief", "Life of Pi"	, "Diary of a Wimpy Kid", "The Adventures of Sherlock Holmes", "The Wolf Queen", "The Doors of Sovngarde", "Sixteen Accords of Happiness", "The Argonian Maid", "A Dance in Fire", "The Infernal City", "Tales of Tamriel", "The Book of the Dragonborn", "The Phantom Menace", "The Attack of the Clones", "The Revenge of the Sith", "A New Hope", "The Empire Strikes Back", "Return of the Jedi", "The Force Awakens", "The Last Jedi"};
		return bookName[RANDOM.nextInt(bookName.length - 1)];
	}

	public static BookType randomBookType() {
		BookType[] bookTypes = BookType.values();
		return bookTypes[RANDOM.nextInt(bookTypes.length - 1)];
	}

	public static ILibrary randomLibrary() {
		List<ILibrary> libraries = ObjectDelegate.getLibraries();
		if (libraries.size() == 1) {
			return libraries.get(0);
		}
		return libraries.get(RANDOM.nextInt(libraries.size()));
	}

	private static boolean chance(int chance) {
		return ((int) RANDOM.nextInt(chance)) == 0;
	}

}