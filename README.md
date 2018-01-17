# WHS Technology Student Association Coding Project (Spring 2018)
This repository hosts the code for [Wharton High School's](http://wharton.mysdhc.org) [Technology Student Association](http://www.tsaweb.org) coding project for Spring 2018. This year's project is a library management application designed to be flexible and easily scaleable.

### Overview
- This project uses JSON to store data, meaning there are no database dependencies. The drawback is that the data cannot be concurrently accessed, however we decided to implement it like this so that users can easily set up the program.
- The data format is as follows:
	- Books
	- Libraries
		- Books (A list of books that the library has in stock)
		- Members (Members refer to their parent People instances)
			- Checkouts
				- A list of checkouts for a book (Members can checkout multiple copies of the same book)
				- The checkout refers to the parent Book instance for its metadata.
	- People (People can be registered in multiple libraries)

### License
This project is distributed under the [MIT license](https://raw.githubusercontent.com/WHSTSALibrary2017/Library/master/LICENSE)

### Credits
- Andre Roberts **(Backend)**
- Cameron Newborn **(User Interface)**
- Eric Rabil **(Backend)**
