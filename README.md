# Digital Signature Verification System

A web-based Digital Signature Verification System developed as a Cryptography PBL project.

The application uses RSA 2048-bit cryptography and SHA-256 hashing to sign and verify digital documents. It checks document authenticity, confirms signer authentication, and detects unauthorized modifications.

## Live Demo

[Open the deployed application](https://digital-signature-system-o92u.onrender.com/)

## Features

- Generate RSA 2048-bit public and private keys
- Sign digital documents using a private key
- Verify digital signatures using a public key
- Detect modified or tampered documents
- Calculate SHA-256 document hashes
- Download generated keys and signature files
- Web interface using HTML, CSS, and JavaScript
- Spring Boot REST backend
- Supports TXT, PDF, DOCX, images, and other file types

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
- Docker
- Render

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
3. A SHA-256 hash is generated internally by the signature algorithm.
4. The document is signed using the sender's private key.
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

Install the following:

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

On Windows:

```bash
mvnw.cmd spring-boot:run
```

If Maven is installed globally:

```bash
mvn spring-boot:run
```

Open the application:

```text
http://localhost:8080
```

## Build the Application

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

### Generate Keys

1. Open the application.
2. Click **Generate Key Pair**.
3. Download the private key.
4. Download the public key.

Keep the private key secret.

### Sign a Document

1. Select a document.
2. Select the private key.
3. Click **Sign Document**.
4. Download the generated `.sig` signature file.

### Verify a Document

1. Select the original document.
2. Select the `.sig` file.
3. Select the public key.
4. Click **Verify Signature**.

A valid document should display:

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

The modified document should display:

```text
Result: INVALID
The document may have been modified.
```

## Deployment

This project can be deployed using Render or another cloud platform that supports Docker.

Deployment configuration:

- Runtime: Docker
- Branch: `main`
- Root Directory: Leave blank
- Dockerfile Path: `./Dockerfile`

## Security Note

This project is developed for educational and academic purposes as part of a Cryptography PBL project.

The application is not intended for production use without additional security improvements, secure private-key storage, user authentication, certificate management, and proper access control.

Do not upload or commit private keys to GitHub.

Never share the private key publicly.

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
- Private-key management is simplified.
- It does not provide complete identity verification.
- The free deployment service may sleep when inactive.

## Future Enhancements

- User registration and login
- Database integration
- Signature verification history
- Certificate Authority support
- Digital certificates
- Password-protected private keys
- Cloud storage
- Email-based document sharing
- Blockchain-based verification records

## Author

**Nikhil Dutta Anala**

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.