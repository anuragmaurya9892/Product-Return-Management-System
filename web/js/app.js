/**
 * Product Return Management System (ReturnHub)
 * Client-Side JavaScript
 */

document.addEventListener("DOMContentLoaded", function () {
    // 1. Auto-dismiss alerts after 6 seconds
    const alerts = document.querySelectorAll(".alert");
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = "opacity 0.5s ease";
            alert.style.opacity = "0";
            setTimeout(function () {
                alert.remove();
            }, 500);
        }, 6000);
    });

    // 2. Image upload preview
    const imageInput = document.getElementById("evidenceImage");
    const previewContainer = document.getElementById("imagePreview");
    if (imageInput && previewContainer) {
        imageInput.addEventListener("change", function (e) {
            const file = e.target.files[0];
            previewContainer.innerHTML = "";
            if (file) {
                // Size validation
                if (file.size > 5 * 1024 * 1024) {
                    alert("Selected file is larger than 5 MB. Please select a smaller image.");
                    imageInput.value = "";
                    return;
                }
                const reader = new FileReader();
                reader.onload = function (event) {
                    const img = document.createElement("img");
                    img.src = event.target.result;
                    img.style.maxWidth = "200px";
                    img.style.maxHeight = "200px";
                    img.style.borderRadius = "8px";
                    img.style.marginTop = "10px";
                    img.style.border = "1px solid #cbd5e1";
                    previewContainer.appendChild(img);
                };
                reader.readAsDataURL(file);
            }
        });
    }

    // 3. Confirm dialog for status update / destructive actions
    const confirmButtons = document.querySelectorAll("[data-confirm]");
    confirmButtons.forEach(function (btn) {
        btn.addEventListener("click", function (e) {
            const message = btn.getAttribute("data-confirm") || "Are you sure you want to proceed?";
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });

    // 4. Character counter for description
    const descTextarea = document.getElementById("returnDescription");
    const charCountEl = document.getElementById("charCount");
    if (descTextarea && charCountEl) {
        descTextarea.addEventListener("input", function () {
            charCountEl.textContent = descTextarea.value.length;
        });
    }

    // 5. Quantity Stepper controls (+ / -)
    document.querySelectorAll(".qty-stepper").forEach(function (stepper) {
        const input = stepper.querySelector(".qty-input");
        const minusBtn = stepper.querySelector("[data-qty-minus]");
        const plusBtn = stepper.querySelector("[data-qty-plus]");
        const autoSubmit = stepper.getAttribute("data-autosubmit") === "true";

        if (!input) return;
        const min = parseInt(input.getAttribute("min") || "1", 10);
        const max = parseInt(input.getAttribute("max") || "10", 10);

        if (minusBtn) {
            minusBtn.addEventListener("click", function () {
                let val = parseInt(input.value || "1", 10);
                if (val > min) {
                    input.value = val - 1;
                    if (autoSubmit && input.form) input.form.submit();
                }
            });
        }
        if (plusBtn) {
            plusBtn.addEventListener("click", function () {
                let val = parseInt(input.value || "1", 10);
                if (val < max) {
                    input.value = val + 1;
                    if (autoSubmit && input.form) input.form.submit();
                }
            });
        }
    });

    // 6. Interactive Payment Method Radio Cards on Checkout
    const paymentCards = document.querySelectorAll(".payment-card");
    paymentCards.forEach(function (card) {
        const radio = card.querySelector('input[type="radio"]');
        card.addEventListener("click", function () {
            if (radio) {
                radio.checked = true;
                paymentCards.forEach(function (c) { c.classList.remove("selected"); });
                card.classList.add("selected");
            }
        });
    });
});
