# Digital Signature Verification System

A web-based Digital Signature Verification System developed as a Cryptography PBL project.

The application uses **RSA 2048-bit cryptography** and **SHA-256 hashing** to sign and verify digital documents. It checks document authenticity, confirms signer authentication, and detects unauthorized modifications.

## Features

- Generate RSA 2048-bit public and private keys
- Sign digital documents using a private key
- Verify digital signatures using a public key
- Detect modified or tampered documents
- Calculate SHA-256 document hashes
- Download generated keys and signature files
- Web interface using HTML, CSS, and JavaScript
- Spring Boot REST backend
- Supports documents such as TXT, PDF, DOCX, images, and other file types

## Technologies Used

- Java 21
- Spring Boot
- Maven
- HTML5
- CSS3
- JavaScript
- RSA 2048-bit
- SHA-256
- SHA256withRSA

## Project Objective

The objective of this project is to verify the authenticity and integrity of digital documents using digital signatures.

The system helps to:

- Authenticate the signer
- Detect unauthorized document modifications
- Verify document integrity
- Improve trust in electronic document exchange

## How Digital Signatures Work

### Signing Process

1. The user selects a document.
2. The system reads the document data.
3. A SHA-256 hash is generated.
4. The hash is signed using the sender's private key.
5. A digital signature file is generated.

### Verification Process

1. The receiver selects the document.
2. The receiver uploads the signature file.
3. The receiver uploads the sender's public key.
4. The system verifies the signature.
5. The system displays whether the document is valid or modified.

## Project Structure

```text
Digital-Signature-System/
├── Dockerfile
├── pom.xml
├── mvnw
├── mvnw.cmd
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── nikhil/
        │           └── digitalsignature/
        │               ├── DigitalsignatureApplication.java
        │               └── DigitalSignatureController.java
        └── resources/
            └── static/
                ├── index.html
                ├── style.css
                └── app.js
```

## Requirements

Install the following software:

- Java 21 or later
- Maven 3.9 or later
- Git

Check Java:

```bash
java -version
```

Check Maven:

```bash
mvn -version
```

## Run Locally

Clone the repository:

```bash
git clone https://github.com/NikhilDuttaAnala/Digital-Signature-System.git
```

Open the project directory:

```bash
cd Digital-Signature-System
```

Run the application using Maven:

```bash
./mvnw spring-boot:run
```

On Windows, use:

```bash
mvnw.cmd spring-boot:run
```

Alternatively, if Maven is installed globally:

```bash
mvn spring-boot:run
```

Open the application in your browser:

```text
http://localhost:8080
```

## Build the Application

To create an executable JAR file:

```bash
./mvnw clean package -DskipTests
```

On Windows:

```bash
mvnw.cmd clean package -DskipTests
```

Run the generated JAR file:

```bash
java -jar target/digitalsignature-0.0.1-SNAPSHOT.jar
```

## How to Use

### 1. Generate Keys

1. Open the application.
2. Click **Generate Key Pair**.
3. Download the private key.
4. Download the public key.

Keep the private key secret.

### 2. Sign a Document

1. Select a document.
2. Select the private key.
3. Click **Sign Document**.
4. Download the generated `.sig` signature file.

### 3. Verify a Document

1. Select the original document.
2. Select the `.sig` file.
3. Select the public key.
4. Click **Verify Signature**.

A valid document will display:

```text
Result: VALID
Document Status: The document is authentic and unchanged.
Signer Authentication: Successful
```

## Tamper Detection Test

1. Generate a key pair.
2. Sign a document using the private key.
3. Verify the original document.
4. Modify one character in the document.
5. Verify the modified document using the old signature.

The modified document should produce:

```text
Result: INVALID
The document may have been modified.
```

## Deployment

This application can be deployed using Render or another cloud platform that supports Docker.

The project includes a Dockerfile for deployment.

For Render:

1. Create a new Web Service.
2. Connect this GitHub repository.
3. Select the `main` branch.
4. Choose `Docker` as the environment.
5. Set the Dockerfile path to:

```text
./Dockerfile
```

6. Leave the root directory blank.
7. Select the Free plan for testing.
8. Click **Deploy web service**.

## Security Note

This project is intended for educational purposes.

Do not upload or commit private keys to GitHub. Never share the private key publicly.

Private key files should be added to `.gitignore`:

```gitignore
*.pem
*.sig
```

For production systems, private keys should be encrypted and securely stored.

## Expected Outcomes

The system is expected to:

- Verify the authenticity of digital documents
- Confirm signer authentication
- Detect unauthorized document changes
- Demonstrate RSA digital signatures
- Demonstrate SHA-256 hashing
- Improve trust in electronic document exchange

## Limitations

- This is an educational project.
- It does not use a Certificate Authority.
- Private key management is simplified.
- It does not provide complete identity verification.
- The deployed free service may sleep when inactive.

## Future Enhancements

- User registration and login
- Database integration
- Signature verification history
- Certificate Authority support
- Email-based document sharing
- Digital certificates
- Cloud storage
- Password-protected private keys
- Blockchain-based verification records

## Author

**Nikhil Dutta Anala**

## License

This project is intended for educational and academic use.