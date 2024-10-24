# KyPoint - Key/Value Store

KyPoint is a simple key-value store implemented in Java. It combines fast in-memory access using a `HashMap` and persistent storage via binary serialization to ensure data is preserved across application restarts.


## Features
- In-memory storage for fast access using a `HashMap`
- Persistent storage using binary serialization to a `.bin` file
- Automatic data replication across multiple storage files for fault tolerance
- Batch put operations for handling multiple key-value pairs
- RESTful API for interacting with the key-value store
- Failover mechanism for replicated files in case of read errors

## Prerequisites
To run this project, you will need:
- **Java 17** or later installed
- **Gradle** installed (if not, the project includes a Gradle wrapper)
- **Docker** for containerization (Optional, if you wish to run the project in Docker)
   - [Install Docker](https://docs.docker.com/get-docker/) if you don't have it already.

## Running the Application

You have two options to run the project:

1. **Using Gradle**:
   - Run the following command to start the application:
     ```bash
     ./gradlew run
     ```

2. **Using the JAR file**:
   - After building the project, you can run the application using the JAR file in the project root:
     ```bash
     java -jar kypoint.jar
     ```

## Binary Serialization vs Properties File
Binary serialization is used in KyPoint for efficient data storage and retrieval. Here's why:
- **Binary Format**: More compact and faster to read/write compared to text-based formats like properties files.
- **I/O Efficiency**: Binary I/O is more efficient as it avoids the overhead of encoding/decoding text data.
- **Performance**: For large datasets, binary serialization offers better performance in both read and write operations.

## API Endpoints
The key-value store exposes a RESTful API. Here's a summary of available routes:

- `GET /key-value/read?key=yourKey`: Read a value by key.
- `GET /key-value/readkeyrange?startkey=start&endkey=end`: Read values in a key range.
- `PUT /key-value/put`: Insert a key-value pair.
- `PUT /key-value/batchput`: Insert multiple key-value pairs in a single batch.
- `DELETE /key-value/delete?key=yourkey`: Delete Key value pair



