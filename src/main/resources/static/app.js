const generateKeysButton =
    document.getElementById("generateKeysButton");

const signForm =
    document.getElementById("signForm");

const verifyForm =
    document.getElementById("verifyForm");

const alertBox =
    document.getElementById("alertBox");

const keysResult =
    document.getElementById("keysResult");

const signResult =
    document.getElementById("signResult");

const verifyResult =
    document.getElementById("verifyResult");


function showAlert(message, type) {
    alertBox.textContent = message;

    alertBox.className = "alert";

    if (type === "success") {
        alertBox.classList.add("alert-success");
    } else {
        alertBox.classList.add("alert-error");
    }

    alertBox.classList.remove("hidden");

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}


function downloadTextFile(filename, content) {
    const blob = new Blob(
        [content],
        { type: "text/plain;charset=utf-8" }
    );

    const url = URL.createObjectURL(blob);

    const link = document.createElement("a");

    link.href = url;
    link.download = filename;

    document.body.appendChild(link);
    link.click();
    link.remove();

    URL.revokeObjectURL(url);
}


function downloadSignatureFile(filename, base64Content) {
    downloadTextFile(filename, base64Content);
}


function showResult(element, html, type) {
    element.innerHTML = html;
    element.className = "result";

    if (type === "success") {
        element.classList.add("result-success");
    } else if (type === "warning") {
        element.classList.add("result-warning");
    } else {
        element.classList.add("result-error");
    }

    element.classList.remove("hidden");
}


generateKeysButton.addEventListener("click", async function () {
    generateKeysButton.disabled = true;
    generateKeysButton.textContent = "Generating...";

    try {
        const response = await fetch("/api/generate-keys", {
            method: "POST"
        });

        const data = await response.json();

        if (!response.ok || !data.success) {
            throw new Error(data.message);
        }

        showResult(
            keysResult,
            `
            <strong>Key pair generated successfully.</strong>
            <br><br>
            RSA Key Size: 2048 bits
            <br><br>

            <button
                id="downloadPrivateKey"
                class="download-link"
            >
                Download Private Key
            </button>

            <button
                id="downloadPublicKey"
                class="download-link"
            >
                Download Public Key
            </button>

            <p style="margin-top: 12px;">
                Keep the private key secret.
            </p>
            `,
            "success"
        );

        document
            .getElementById("downloadPrivateKey")
            .addEventListener("click", function () {
                downloadTextFile(
                    "private_key.pem",
                    data.privateKey
                );
            });

        document
            .getElementById("downloadPublicKey")
            .addEventListener("click", function () {
                downloadTextFile(
                    "public_key.pem",
                    data.publicKey
                );
            });

        showAlert(
            "RSA key pair generated successfully.",
            "success"
        );

    } catch (error) {
        showResult(
            keysResult,
            `<strong>Error:</strong> ${error.message}`,
            "error"
        );

        showAlert(error.message, "error");

    } finally {
        generateKeysButton.disabled = false;
        generateKeysButton.textContent = "Generate Key Pair";
    }
});


signForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const formData = new FormData(signForm);

    const submitButton =
        signForm.querySelector("button[type='submit']");

    submitButton.disabled = true;
    submitButton.textContent = "Signing...";

    try {
        const response = await fetch("/api/sign", {
            method: "POST",
            body: formData
        });

        const data = await response.json();

        if (!response.ok || !data.success) {
            throw new Error(data.message);
        }

        showResult(
            signResult,
            `
            <strong>Document signed successfully.</strong>
            <br><br>

            <strong>Document:</strong>
            ${escapeHtml(data.originalFilename)}
            <br>

            <strong>Algorithm:</strong>
            ${escapeHtml(data.algorithm)}
            <br>

            <strong>SHA-256 Hash:</strong>
            <div class="hash">
                ${escapeHtml(data.documentHash)}
            </div>

            <button
                id="downloadSignature"
                class="download-link"
            >
                Download Signature File
            </button>
            `,
            "success"
        );

        document
            .getElementById("downloadSignature")
            .addEventListener("click", function () {
                downloadSignatureFile(
                    data.signatureFilename,
                    data.signatureBase64
                );
            });

        showAlert(
            "Document signed successfully.",
            "success"
        );

    } catch (error) {
        showResult(
            signResult,
            `<strong>Error:</strong> ${error.message}`,
            "error"
        );

        showAlert(error.message, "error");

    } finally {
        submitButton.disabled = false;
        submitButton.textContent = "Sign Document";
    }
});


verifyForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const formData = new FormData(verifyForm);

    const submitButton =
        verifyForm.querySelector("button[type='submit']");

    submitButton.disabled = true;
    submitButton.textContent = "Verifying...";

    try {
        const response = await fetch("/api/verify", {
            method: "POST",
            body: formData
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message);
        }

        if (data.result === "VALID") {
            showResult(
                verifyResult,
                `
                <strong>Signature Verification Successful</strong>
                <br><br>

                <strong>Result:</strong> VALID
                <br>

                <strong>Document Status:</strong>
                ${escapeHtml(data.documentStatus)}
                <br>

                <strong>Signer Authentication:</strong>
                ${escapeHtml(data.signerAuthentication)}
                <br>

                <strong>Algorithm:</strong>
                ${escapeHtml(data.algorithm)}
                <br>

                <strong>SHA-256 Hash:</strong>
                <div class="hash">
                    ${escapeHtml(data.documentHash)}
                </div>
                `,
                "success"
            );

            showAlert(
                "Signature is valid. Document is authentic and unchanged.",
                "success"
            );

        } else {
            showResult(
                verifyResult,
                `
                <strong>Signature Verification Failed</strong>
                <br><br>

                <strong>Result:</strong> INVALID
                <br>

                <strong>Document Status:</strong>
                ${escapeHtml(data.documentStatus)}
                <br>

                <strong>Signer Authentication:</strong>
                ${escapeHtml(data.signerAuthentication)}
                <br>

                <br>
                Possible reasons:
                <ul>
                    <li>The document was modified.</li>
                    <li>The wrong public key was selected.</li>
                    <li>The wrong signature file was selected.</li>
                    <li>The signature file is corrupted.</li>
                </ul>

                <br>

                <strong>Current SHA-256 Hash:</strong>
                <div class="hash">
                    ${escapeHtml(data.documentHash)}
                </div>
                `,
                "error"
            );

            showAlert(
                "Signature is invalid. The document may have been modified.",
                "error"
            );
        }

    } catch (error) {
        showResult(
            verifyResult,
            `<strong>Error:</strong> ${error.message}`,
            "error"
        );

        showAlert(error.message, "error");

    } finally {
        submitButton.disabled = false;
        submitButton.textContent = "Verify Signature";
    }
});


function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}