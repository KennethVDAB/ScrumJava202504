"use strict";

import {byId, showElementById} from "./util.js";

const eanInput = byId("eanInput-1");
const quantityGoodInput = byId("aantalInput-1");
const quantityDamagedInput = byId("teruggestuurdInput-1");
const articleAddBtn = byId("artikelToevoegenKnop");
const articleContainer = byId("articleContainer");
const articleList = byId("articleList");
const supplierSubmit = byId("submitSupplier");
const supplierInput = byId("leverancier");
const deliveryTicketNumberInput = byId("leveringsbonnummer");
const deliveryTicketDateInput = byId("leveringsbondatum");
const deliveryDateInput = byId("leverdatum");

const eanWarning = byId("eanWarning");
const quantityGoodWarning = byId("quantityGoodWarning");
const quantityDamagedWarning = byId("quantityDamagedWarning");
const error = byId("error");

await getSuppliers();

async function getSuppliers() {
    const suppliersContainer = byId("leverancier");

    try {
        const response = await fetch("/api/supplier/all");
        if (!response.ok) {
            showElementById("error");
        }

        const suppliers = await response.json();
        suppliers.forEach(supplier => {
            const option = document.createElement("option");
            option.value = supplier.id || supplier.name;
            option.textContent = supplier.name;
            suppliersContainer.appendChild(option);
        });
    } catch (error) {
        showElementById("error");
    }
}

eanInput.addEventListener('blur', () => {
    const ean = eanInput.value.trim();
    if (ean && !/^\d{13}$/.test(ean)) {
        eanWarning.textContent = 'EAN moet precies 13 cijfers bevatten.';
        articleAddBtn.disabled = true;
    }
});

[
    {input: quantityGoodInput, warning: quantityGoodWarning},
    {input: quantityDamagedInput, warning: quantityDamagedWarning}
].forEach(({input, warning}) => {
    input.addEventListener('blur', (event) => {
        const value = event.target.value.trim();
        if (!value || isNaN(value) || Number(value) < 0) {
            warning.textContent = 'Aantal mag niet leeg zijn of negatief';
            articleAddBtn.disabled = true;
        }
    });
});

[eanInput, quantityGoodInput, quantityDamagedInput].forEach(input => {
    input.addEventListener('input', () => {
        eanWarning.textContent = '';
        quantityGoodWarning.textContent = '';
        quantityDamagedWarning.textContent = '';
        articleAddBtn.disabled = false;
    });
});

articleAddBtn.addEventListener("click", async () => {
    error.textContent = "";
    const ean = eanInput.value.trim();
    try {
        const response = await fetch(`/api/products/${ean}`);
        if (!response.ok) {
            if (response.status === 404) {
                error.textContent = `Artikel niet gevonden voor deze EAN: ${ean}`;
                clearAllInputs();
            } else {
                error.textContent = `Fout bij het ophalen van gegevens over artikel: ${ean}`;
            }
            return;
        }
        const product = await response.json();
        addArticle(product, quantityGoodInput.value.trim(), quantityDamagedInput.value.trim());
    } catch (error) {
        error.textContent = "Er is een probleem, probeer het later opnieuw!";
    }
});

function addArticle(product, quantityGood, quantityDamaged) {
    if (articleContainer.hidden) {
        articleContainer.hidden = false;
    }
    const tr = document.createElement("tr");
    tr.id = product.id;
    tr.innerHTML = `
    <td>${product.ean}</td><td>${product.name}</td><td>${quantityGood}</td><td>${quantityDamaged}</td>
    `
    articleList.append(tr);
}

function clearAllInputs() {
    eanInput.value = '';
    quantityDamagedInput.value = '';
    quantityGoodWarning.value = '';
}

[supplierInput, deliveryTicketNumberInput, deliveryTicketDateInput, deliveryDateInput].forEach(input => {
    input.addEventListener("focus", () => {
        [supplierInput, deliveryTicketNumberInput, deliveryTicketDateInput, deliveryDateInput].forEach(inputI => {
            inputI.style.border = "1px solid #A8A8A8";
        });
        supplierSubmit.disabled = false;
        error.textContent = '';
    })
});

supplierSubmit.addEventListener("click", async (event) => {
    event.preventDefault();
    error.textContent = "";
    let hasError = false;

    const supplierName = supplierInput.options[supplierInput.selectedIndex].text;
    const deliveryTicketNumber = deliveryTicketNumberInput.value.trim();
    const deliveryTicketDate = deliveryTicketDateInput.value;
    const deliveryDate = deliveryDateInput.value;

    if (!supplierName || supplierName === "-- Selecteer een leverancier --") {
        supplierInput.style.border = "1px solid rgb(204,121,167)";
        hasError = true;
    }
    if (!deliveryTicketNumber) {
        deliveryTicketNumberInput.style.border = "1px solid rgb(204,121,167)";
        hasError = true;
    }
    if (!deliveryTicketDate) {
        deliveryTicketDateInput.style.border = "1px solid rgb(204,121,167)";
        hasError = true;
    }

    if (!deliveryDate) {
        deliveryDateInput.style.border = "1px solid rgb(204,121,167)";
        hasError = true;
    }

    if (hasError) {
        error.textContent = "Alle velden zijn verplicht.";
        supplierSubmit.disabled = true;
        return;
    }

    const payload = {
        supplierName,
        deliveryTicketNumber,
        deliveryTicketDate,
        deliveryDate
    };

    try {
        const response = await fetch('/deliveries/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            error.textContent = "Fout bij het opslaan van de levering.";
        }
        byId("inkomendeLeveringForm").reset();
        //window.location.href
    } catch (error) {
        error.textContent = "Er is een probleem, probeer het later opnieuw!";
    }

})




