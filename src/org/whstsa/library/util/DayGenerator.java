package org.whstsa.library.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.whstsa.library.Tester;
import org.whstsa.library.api.BookType;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.CannotDeregisterException;
import org.whstsa.library.api.exceptions.CheckedInException;
import org.whstsa.library.api.exceptions.NotEnoughMoneyException;
import org.whstsa.library.api.impl.Book;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;

public class DayGenerator {
	
	private static final Random RANDOM = new Random();
	
	
	/* List of things needed to randomize (brainstorm)
	 * Advance time only by user
	 * Adding Members (which means, RANDOMIZING NAMES AHHHH) 1/5 pd
	 * Removing Members (more rare-ish) 1/15 pd, MUST NOT HAVE FINE/BOOK
	 * Getting Books / Removing Books 1/5 per person, MUST HAVE NO FINE
	 * Pay Fine  1/5 per person
	 * Adding New Books (names) 1/10 pd
	 * Reserve Books (if trys to get book, instead reserve if not there)
	 **/
	
	public static void simulateDay() {
		if (chance(5)) {
			System.out.print("a");
			generateMember(randomLibrary());
		}
		if (chance(10)) {
			System.out.print("b");
			// Attempt removing a random member from a random library
			IMember member = randomMember();System.out.print("hi");
			ILibrary library = member.getLibrary();System.out.print("bi");
			try {
				library.removeMember(member);System.out.print("wtf");
				System.out.println(String.format("Deregistered %s from %s", member.getName(), library.getName()));
			} catch (CannotDeregisterException ex) {
				System.out.println(String.format("Couldn't deregister %s: %s", member.getName(), ex.getMessage()));
			} catch (NullPointerException ex) {
				System.out.println("Got em!");
			}
		}
		if (chance(5)) {
			System.out.print("c");
			generateBook();
		}
		ObjectDelegate.getAllMembers().forEach(member -> {
			if (chance(5)) {
				System.out.print("d" + member.getName());
				if (member.getBooks().size() != 0 && RANDOM.nextBoolean()) {
					ICheckout checkout = member.getCheckouts().get(0);
					try {
						member.checkIn(checkout);
						System.out.println(member.getName() + " returned " + checkout.getBook().getTitle());
					} catch (CheckedInException e) {
						System.out.println(member.getName() + " tried to return " + checkout.getBook().getTitle() + " but was already returned.");
					}
				} else if (member.getLibrary().getBooks().size() != 0) {
					List<IBook> bookDB = member.getLibrary().getBooks();
					ILibrary library = member.getLibrary();
					int randomBookIndex = RANDOM.nextInt(bookDB.size());
					if (randomBookIndex == bookDB.size() && randomBookIndex != 0) {
						randomBookIndex -= 1;
					}
					IBook book = bookDB.get(randomBookIndex);
					if (book != null) {
						library.reserveBook(member, book);
					}
				}
				else {
					System.out.print("Got em dot 2.0");
				}
			}
		});
		ObjectDelegate.getAllMembers().stream().filter(member -> chance(5) && member.getFine() != 0.0).collect(Collectors.toList()).forEach(member -> {
			System.out.print("e");
			List<ICheckout> checkoutList = member.getCheckouts();
			for (ICheckout checkout : checkoutList) {
				try {
					checkout.payFine();
				} catch (NotEnoughMoneyException ex) {
					System.out.println(String.format("Not paying off fee for %s for %s of %.2f", checkout.getBook().getTitle(), checkout.getOwner().getName(), ex.getTransaction()));
				}
			}
		});
	}

	public static void generateMember(ILibrary library) {
		// Creating a new person and adding them to a random library
		IPerson person = new Person(generateFirstName(), generateLastName(), chance(5));
		library.addMember(person);
		Loader.getLoader().loadPerson(person);
		System.out.println(String.format("Added %s %s to %s", person.getFirstName(), person.getLastName(), library.getName()));
	}

	public static void generateBook() {
		// Creating a new book and adding it to a random library
		String bookName = generateBookTitle();
		String authorName = generateName();
		BookType bookType = randomBookType();
		IBook book = new Book(bookName, authorName, bookType);
		ILibrary library = randomLibrary();
		Loader.getLoader().loadBook(book);
		library.addBook(book);
		System.out.println(String.format("Added a %s book named %s by %s to %s", bookType.name(), book.getTitle(), book.getAuthor(), library.getName()));
	}

	public static String generateFirstName() {
		String[] firstNames = new String[] {"Jacob" , "Emily" , "Michael" , "Madison" , "Joshua" , "Emma" , "Matthew" , "Olivia" , "Daniel" , "Hannah" , "Christopher" , "Abigail" , "Andrew" , "Isabella" , "Ethan" , "Samantha" , "Joseph" , "Elizabeth" , "William" , "Ashley" , "Anthony" , "Alexis" , "David" , "Sarah" , "Alexander" , "Sophia" , "Nicholas" , "Alyssa" , "Ryan" , "Grace" , "Tyler" , "Ava" , "James" , "Taylor", "John" , "Brianna" , "Johnathan" , "Lauren" , "Noah" , "Chloe" , "Brandon" , "Natalie" , "Christian" , "Kayla" , "Dylan" , "Jessica" , "Samuel" , "Anna" , "Benjamin" , "Victoria"};
		return firstNames[RANDOM.nextInt(firstNames.length - 1)];
	}
	
	public static String generateLastName() {
		String[] lastNames = new String[] {"Smith" , "Johnson" , "Williams" , "Jones" , "Brown" , "Davis" , "Miller" , "Wilson" , "Moore" , "Taylor" , "Anderson" , "Thomas" , "Jackson" , "White" , "Harris" , "Martin" , "Thompson" , "Garcia" , "Martinez" , "Robinson" , "Clark" , "Rodriguez" , "Lewis" , "Lee" , "Walker" , "Hall" , "Allen" , "Young" , "Hernandez" , "King" , "Wright" , "Lopez" , "Hill" , "Scott" , "Green" , "Adams" , "Baker" , "Gonzalez" , "Nelson" , "Carter" , "Mitchell"  , "Perez" , "Roberts" , "Turner" , "Phillips" , "Campbell" , "Parker" , "Evans" , "Edwards" , "Collins"};
		return lastNames[RANDOM.nextInt(lastNames.length - 1)];
	}

	public static String generateName() {
		return generateFirstName() + " " + generateLastName();
	}
	
	public static String generateBookTitle() {
		String[] bookName = new String[] {"Harry Potter" , "The Book Thief" , "The Chronicles of Narnia" , "Animal Farm" , "Gone with the Wind" , "The Hobbit" , "The Fault in Our Stars" , "The Hitchiker's Guide to the Galaxy" , "The Giving Tree" , "Wuthering Heights" , "The Da Vinci Code" , "Alice's Adventures in Wonderland" , "Divergent" , "Romeo and Juliet" , "The Alchemist" , "Lord of the Flies" , "Crime and Punishment" , "Ender's Game" , "City of Bones" , "Charlotte's Web" , "Of Mice and Men" , "Dracula" , "Brave New World" , "One Hundred Years of Solitude" , "A Wrinkle in Time" , "The Catcher in the Rye" , "The Adventures of Huckleberry Finn" , "Where the Wild Things Are" , "Green Eggs and Ham" , "Game of Thrones" , "The Lightning Thief" , "Life of Pi"	, "Diary of a Wimpy Kid" , "The Adventures of Sherlock Holmes" , "The Wolf Queen" , "The Doors of Sovngarde" , "Sixteen Accords of Happiness" , "The Argonian Maid" , "A Dance in Fire" , "The Infernal City" , "Tales of Tamriel" , "The Book of the Dragonborn" , "The Phantom Menace" , "The Attack of the Clones" , "The Revenge of the Sith" , "A New Hope" , "The Empire Strikes Back" , "Return of the Jedi" , "The Force Awakens" , "The Last Jedi"};
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
		return libraries.get(RANDOM.nextInt(libraries.size() - 1));
	}

	public static IMember randomMember() {
		ILibrary library = randomLibrary();
		List<IMember> members = library.getMembers();
		if (members.size() == 1) {
			return members.get(0);
		}
		if (members.size() == 0) {
			return null;
		}
		return members.get(RANDOM.nextInt(members.size() - 1));
	}

	private static boolean chance(int chance) {
		return ((int) RANDOM.nextInt(chance)) == 0;
	}
	
}
