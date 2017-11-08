# Index format

The index for an author is stored as two objects

 1. Token count map
 2. References map

## Token count map

Format: `HashMap<String, Integer>`

**Key:** A token eg: "JEDUTHUN"  
**Value:** The number of references to that token in the author.

## References map

Format: `HashMap<String, short[]> `

**Key:** A token eg: "JEDUTHUN"  
**Value:** A list of references, format explained below

The reference list is an array containing volume and page numbers for each
occurrence of a token. If a token appears more than once on each page, it will
only have the reference recorded once for that page.

A volume number is stored as a negative number and a page number is stored as
a positive number. The example array below has references in volume 5 pages 1
and 4, volume 6 page 7, and volume 9 pages 3, 7 and 10.

`[ -5, 1, 4, -6, 7, -9, 3, 7, 10]`

The volumes and pages are in ascending order (first by volume, then by page).
The sign of the volume number is ignored.
