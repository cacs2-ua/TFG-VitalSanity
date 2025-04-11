/**
 * Este JS maneja la lógica de:
 * 1) Llamar al backend para generar el PDF en Base64.
 * 2) Invocar a la firma con "sign(...)" de autoscript.js.
 * 3) Enviar el PDF firmado al backend y mostrar enlace de descarga.
 * 4) NUEVO: Realizar cofirma de un PDF previamente firmado.
 */

// Variable global para recordar el ID (uuid) del PDF firmado
let globalSignedId = null;

/**
 * FIRMAR (primera firma del documento)
 */
function onClickFirmarAutorizacion() {
    showLoading();

    // 1) Recogemos datos del formulario
    const form = document.getElementById("form-authorization-data");
    const formData = new FormData(form);

    // Llamamos por AJAX a /signer/generate-pdf para obtener un PDF base64
    fetch("/vital-sanity/api/profesional-medico/generar-pdf-autorizacion", {
        method: "POST",
        body: formData
    })
        .then(response => response.text())
        .then(pdfBase64 => {
            // 2) Invocamos la firma con AutoFirma (sign):
            AutoScript.sign(
                pdfBase64,                  // dataB64
                "SHA512withRSA",            // algorithm
                "PAdES",                    // format
                null,                       // params (simple demo)
                function (signedPdfBase64, signerCert, extraInfo) {
                    // EXITO: subimos el PDF firmado al servidor
                    subirAutorizacionFirmada(signedPdfBase64);

                    hideLoading();
                },
                function (errorType, errorMessage) {
                    alert("ERROR en firma: " + errorType + " - " + errorMessage);

                    hideLoading();
                }
            );
        })
        .catch(err => {
            alert("Error generando el PDF: " + err);
        });
}

/**
 * Subimos el PDF firmado (Base64) al servidor y mostramos enlace descarga.
 */
function subirAutorizacionFirmada(signedPdfBase64) {
    const formData = new FormData();
    formData.append("signedPdfBase64", signedPdfBase64);

    fetch("/vital-sanity/api/profesional-medico/pdf-autorizacion-firmada", {
        method: "POST",
        body: formData
    })
        .then(response => response.text())
        .then(uuid => {
            // Guardamos el ID del PDF firmado (para cofirma posterior)
            globalSignedId = uuid;

            // Mostramos el enlace de descarga
            const resultadoDiv = document.getElementById("signed-pdf-link");
            const link = document.createElement("a");
            link.href = "/vital-sanity/api/profesional-medico/pdf-autorizacion/" + uuid;
            link.target = "_blank";
            link.innerText = "Descargar PDF FIRMADO";
            resultadoDiv.innerHTML = "";
            resultadoDiv.appendChild(link);

            setTimeout(() => {
                window.location.href = "/vital-sanity/signer/exito";
            }, 1000);

        })
        .catch(err => {
            alert("Error subiendo PDF firmado: " + err);
        });
}

/**
 * NUEVO: COFIRMAR
 * 1) Descargamos el PDF firmado en Base64.
 * 2) Llamamos a cosign(...).
 * 3) Subimos resultado (cofirmado).
 */
function onClickCofirmar() {
    if (!globalSignedId) {
        alert("No se ha firmado aún ningún PDF para cofirmar.");
        return;
    }

    showLoading();

    // 1) Descargamos en Base64 el PDF previamente firmado
    fetch("/vital-sanity/signer/download-base64/" + globalSignedId)
        .then(response => response.text())
        .then(signedPdfBase64 => {
            // 2) Invocamos cofirma
            AutoScript.cosign(
                signedPdfBase64,          // firma ya existente en base64
                "SHA512withRSA",          // algoritmo
                "PAdES",                  // formato (PAdES)
                null,                     // params
                function (cosignedPdfBase64, signerCert, extraInfo) {
                    // EXITO: subimos la cofirma al servidor
                    uploadCosignedPdf(cosignedPdfBase64);

                    hideLoading();
                },
                function (errorType, errorMessage) {
                    alert("ERROR en cofirma: " + errorType + " - " + errorMessage);

                    hideLoading();
                }
            );
        })
        .catch(err => {
            alert("Error al descargar PDF firmado en base64: " + err);
        });
}

/**
 * NUEVO: Subir PDF cofirmado al servidor y mostrar enlace de descarga.
 */
function uploadCosignedPdf(cosignedPdfBase64) {
    const formData = new FormData();
    formData.append("cosignedPdfBase64", cosignedPdfBase64);

    fetch("/vital-sanity/signer/save-cosigned", {
        method: "POST",
        body: formData
    })
        .then(response => response.text())
        .then(uuid => {
            // Mostramos el enlace de descarga del PDF COFIRMADO
            const resultadoCofirmaDiv = document.getElementById("resultadoCofirma");
            const link = document.createElement("a");
            link.href = "/vital-sanity/signer/download-cosigned/" + uuid;
            link.target = "_blank";
            link.innerText = "Descargar PDF COFIRMADO";
            resultadoCofirmaDiv.innerHTML = "";
            resultadoCofirmaDiv.appendChild(link);
        })
        .catch(err => {
            alert("Error subiendo PDF cofirmado: " + err);
        });
}

/**
 * Función para mostrar la pantalla de carga.
 */
function showLoading() {
    document.getElementById("loading-overlay").style.display = "flex";
}

/**
 * Función para ocultar la pantalla de carga.
 */
function hideLoading() {
    document.getElementById("loading-overlay").style.display = "none";
}


/**
 * IMPORTANTE (recordatorio de los servicios Storage/Retriever):
 * - Por defecto, AutoScript usará Socket si el navegador lo soporta.
 * - Si deseáramos forzar el uso de servicios, haríamos:
 *      setForceWSMode(true);
 *      setServlets("/vital-sanity/storage/StorageService","/vital-sanity/retriever/RetrieveService");
 */

// Al cargar la página, inicializamos la app de @firma:
window.addEventListener("load", () => {
    // Si se detecta un dispositivo móvil (Android o iOS), forzamos el uso de servicios intermedios
    // para garantizar la compatibilidad, y configuramos las URL de los servicios Storage y Retrieve.
    if (AutoScript.isAndroid() || AutoScript.isIOS()) {
        AutoScript.setForceWSMode(true);
        AutoScript.setServlets(
            "https://192.168.147.218/vital-sanity/afirma-signature-storage/StorageService",
            "https://192.168.147.218/vital-sanity/afirma-signature-retriever/RetrieveService"
        );
    }
    // Cargamos la app de autofirma
    AutoScript.cargarAppAfirma();
});
