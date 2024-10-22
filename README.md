I'm choosing Gradle because

REstricted to using Just standard libraries means I have only

2 options 

File based storage

Serialization

Combining In-Memory and Persistent Storage
To achieve both fast access and persistence, you can combine an in-memory data structure with a persistent storage solution. Here’s how you can do that:

Use a HashMap for In-Memory Storage: Store your key-value pairs in a HashMap for fast access during the application runtime.

Persist Data to a File: Whenever you add or modify an entry in the HashMap, also write the changes to a file (using Properties or serialization). When your application starts, read the data from the file into the HashMap.


Benefits
Fast Access: You can quickly access key-value pairs in memory.
Persistence: Data is saved to a file, allowing it to persist across application restarts.

Yes, binary serialization can generally be faster than using a properties file for several reasons:

1. Data Format:
   Binary Serialization: Data is stored in a compact binary format, which takes less space and can be read/written faster by the I/O subsystem. This reduces the overhead associated with converting data to and from string representations.
   Properties File: Data is stored in a human-readable text format, which adds overhead for parsing strings, especially for large datasets.
2. I/O Operations:
   Binary I/O: Binary streams are often more efficient for reading and writing data compared to text streams because they do not require character encoding and decoding.
   Text I/O: Reading and writing from a properties file involves more processing due to character encoding, which can slow down performance.
3. Serialization Overhead:
   Binary Serialization: If you serialize complex objects (like a Map), the serialization process is generally faster since the object structure is written directly in binary.
   Properties File: When using properties files, you need to convert each key-value pair to a string format, which incurs additional overhead.
4. Read/Write Performance:
   Binary Serialization: Typically results in faster performance for both reading and writing, especially for large datasets, because there is less data to process and fewer conversions required.
   Properties File: Slower for large datasets, as the entire file must be read and parsed, and each entry must be converted from strings to usable data types.
   Conclusion
   If performance is a critical concern and you're dealing with a significant amount of data or complex data structures, binary serialization would likely provide better performance compared to using a properties file.
   However, it’s important to note that while binary formats can be faster, they may sacrifice human readability, which can be a downside if you need to manually inspect or edit the data.