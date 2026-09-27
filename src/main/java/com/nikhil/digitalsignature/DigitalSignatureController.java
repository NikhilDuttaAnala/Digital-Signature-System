package com.nikhil.digitalsignature;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class DigitalSignatureController {

    @PostMapping("/generate-keys")
    public ResponseEntity<?> generateKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

            keyPairGenerator.initialize(2048);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            String privateKey = convertToPem(keyPair.getPrivate().getEncoded(), "PRIVATE KEY");

            String publicKey = convertToPem(keyPair.getPublic().getEncoded(), "PUBLIC KEY");

            return ResponseEntity.ok(new KeyResponse(true, "RSA key pair generated successfully.", privateKey, publicKey));

        } catch (Exception exception) {
            return errorResponse("Key generation failed: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Sign an uploaded document using a private RSA key.
     */
    @PostMapping(value = "/sign", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> signDocument(@RequestParam("document") MultipartFile document, @RequestParam("privateKey") MultipartFile privateKeyFile) {
        try {
            if (document.isEmpty()) {
                return errorResponse("Please select a document.", HttpStatus.BAD_REQUEST);
            }

            if (privateKeyFile.isEmpty()) {
                return errorResponse("Please select a private key.", HttpStatus.BAD_REQUEST);
            }

            byte[] documentBytes = document.getBytes();

            PrivateKey privateKey = loadPrivateKey(privateKeyFile.getBytes());

            Signature signature = Signature.getInstance("SHA256withRSA");

            signature.initSign(privateKey);
            signature.update(documentBytes);

            byte[] signatureBytes = signature.sign();

            String signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes);

            String documentHash = calculateSha256(documentBytes);

            String originalFilename = document.getOriginalFilename();

            if (originalFilename == null || originalFilename.isBlank()) {
                originalFilename = "document";
            }

            String signatureFilename = originalFilename + ".sig";

            return ResponseEntity.ok(new SignResponse(true, "Document signed successfully.", originalFilename, signatureFilename, signatureBase64, documentHash, "SHA256withRSA"));

        } catch (Exception exception) {
            return errorResponse("Document signing failed: " + exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * Verify a document using its signature and public key.
     */
    @PostMapping(value = "/verify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> verifySignature(@RequestParam("document") MultipartFile document, @RequestParam("signature") MultipartFile signatureFile, @RequestParam("publicKey") MultipartFile publicKeyFile) {
        try {
            if (document.isEmpty()) {
                return errorResponse("Please select a document.", HttpStatus.BAD_REQUEST);
            }

            if (signatureFile.isEmpty()) {
                return errorResponse("Please select a signature file.", HttpStatus.BAD_REQUEST);
            }

            if (publicKeyFile.isEmpty()) {
                return errorResponse("Please select a public key.", HttpStatus.BAD_REQUEST);
            }

            byte[] documentBytes = document.getBytes();

            String signatureText = new String(signatureFile.getBytes(), StandardCharsets.UTF_8).trim();

            byte[] signatureBytes = Base64.getDecoder().decode(signatureText);

            PublicKey publicKey = loadPublicKey(publicKeyFile.getBytes());

            Signature verifier = Signature.getInstance("SHA256withRSA");

            verifier.initVerify(publicKey);
            verifier.update(documentBytes);

            boolean valid = verifier.verify(signatureBytes);

            String documentHash = calculateSha256(documentBytes);

            if (valid) {
                return ResponseEntity.ok(new VerifyResponse(true, "Signature verification successful.", "VALID", "The document is authentic and unchanged.", "Successful", documentHash, "SHA256withRSA"));
            }

            return ResponseEntity.ok(new VerifyResponse(false, "Signature verification failed.", "INVALID", "The document may have been modified.", "Failed", documentHash, "SHA256withRSA"));

        } catch (Exception exception) {
            return errorResponse("Verification failed. Check the document, signature, and public key.", HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * Load a PEM encoded private RSA key.
     */
    private PrivateKey loadPrivateKey(byte[] pemBytes) throws Exception {

        String pem = new String(pemBytes, StandardCharsets.UTF_8);

        String keyContent = pem.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");

        byte[] decodedKey = Base64.getDecoder().decode(keyContent);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(keySpec);
    }

    /*
     * Load a PEM encoded public RSA key.
     */
    private PublicKey loadPublicKey(byte[] pemBytes) throws Exception {

        String pem = new String(pemBytes, StandardCharsets.UTF_8);

        String keyContent = pem.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");

        byte[] decodedKey = Base64.getDecoder().decode(keyContent);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(keySpec);
    }

    /*
     * Convert binary key data to PEM format.
     */
    private String convertToPem(byte[] keyBytes, String keyType) {
        String encodedKey = Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.UTF_8)).encodeToString(keyBytes);

        return "-----BEGIN " + keyType + "-----\n" + encodedKey + "\n-----END " + keyType + "-----\n";
    }

    /*
     * Calculate SHA-256 hash of a document.
     */
    private String calculateSha256(byte[] data) throws Exception {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] hashBytes = digest.digest(data);

        StringBuilder result = new StringBuilder();

        for (byte hashByte : hashBytes) {
            result.append(String.format("%02x", hashByte));
        }

        return result.toString();
    }

    private ResponseEntity<?> errorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(Map.of("success", false, "message", message));
    }

    public record KeyResponse(boolean success, String message, String privateKey, String publicKey) {
    }

    public record SignResponse(boolean success, String message, String originalFilename, String signatureFilename,
                               String signatureBase64, String documentHash, String algorithm) {
    }

    public record VerifyResponse(boolean success, String message, String result, String documentStatus,
                                 String signerAuthentication, String documentHash, String algorithm) {
    }

}
