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

[eanInput, quantityGoodInput, quantityDamagedInput].forEach(input => {
    input.addEventListener('focus', () => {
        [eanInput, quantityGoodInput, quantityDamagedInput].forEach(inputI => {
            inputI.style.border = "1px solid #A8A8A8";
            inputI.textContent = "";
        });
        eanWarning.textContent = '';
        quantityGoodWarning.textContent = '';
        quantityDamagedWarning.textContent = '';
        error.textContent = "";
        articleAddBtn.disabled = false;
    });
});

articleAddBtn.addEventListener("click", async () => {
    error.textContent = "";
    let hasError = false;

    const ean = eanInput.value.trim();
    const quantityGood = quantityGoodInput.value.trim();
    const quantityDamaged = quantityDamagedInput.value.trim();

    if(!ean || (ean && !/^\d{13}$/.test(ean))) {
        eanInput.style.border = "1px solid rgb(204,121,167)";
        eanWarning.textContent = 'EAN moet precies 13 cijfers bevatten.';
        hasError = true;
    }

    if(!quantityGood || isNaN(quantityGood) || Number(quantityGood) < 0) {
        quantityGoodInput.style.border = "1px solid rgb(204,121,167)";
        quantityGoodWarning.textContent = 'Aantal mag niet leeg zijn of negatief';
        hasError = true;
    }

    if(!quantityDamaged || isNaN(quantityDamaged) || Number(quantityDamaged) < 0) {
        quantityDamagedInput.style.border = "1px solid rgb(204,121,167)";
        quantityDamagedWarning.textContent = 'Aantal mag niet leeg zijn of negatief';
        hasError = true;
    }

    if(hasError) {
        articleAddBtn.disabled = true;
        return;
    }

    try {
        const response = await fetch(`/api/products/${ean}`);
        if (!response.ok) {
            if (response.status === 404) {
                error.textContent = `Artikel niet gevonden voor deze EAN: ${ean}`;
                clearAllInputs();
            } else {
                error.textContent = `Fout bij het ophalen van gegevens over artikel: ${ean}`;
                clearAllInputs();
            }
            return;
        }
        const product = await response.json();
        storeArticle(product, quantityGood, quantityDamaged);
    } catch (error) {
        error.textContent = "Er is een probleem, probeer het later opnieuw!";
    }
    clearAllInputs();
});

function storeArticle (product, quantityGood, quantityDamaged) {
    let articles = JSON.parse(sessionStorage.getItem("articles")) || [];
    const exists = articles.some(article => article.productId === product.productId);
    if (exists) {
        error.textContent = `Artikel ${product.name} is al toegevoegd.`
        return;
    }
    const article = {
        productId: product.productId,
        ean: product.ean,
        name: product.name,
        quantityGood: quantityGood,
        quantityDamaged: quantityDamaged
    }
    articles.push(article);
    sessionStorage.setItem("articles", JSON.stringify(articles));
    addArticleToTable(article);
}

function addArticleToTable(article) {
    if (articleContainer.hidden) {
        articleContainer.hidden = false;
    }
    const tr = document.createElement("tr");
    tr.id = article.productId;
    tr.innerHTML = `
        <td>${article.ean}</td>
        <td>${article.name}</td>
        <td>${article.quantityGood}</td>
        <td>${article.quantityDamaged}</td>
        <td><button class="delete-btn">✖️</button></td>
    `
    articleList.append(tr);

    tr.querySelector(".delete-btn").addEventListener("click", () => {
        deleteArticle(article.productId);
        clearAllInputs();
    });
}

function deleteArticle(productId) {
    const row = document.getElementById(productId);
    if (row) {
        row.remove();
    }

    let articles = JSON.parse(sessionStorage.getItem("articles")) || [];
    articles = articles.filter(article => article.productId !== productId);
    sessionStorage.setItem("articles", JSON.stringify(articles));
    if (articles.length === 0) {
        articleContainer.hidden = true;
    }
}

function clearAllInputs() {
    eanInput.value = '';
    quantityDamagedInput.value = '';
    quantityGoodInput.value = '';
}

[supplierInput, deliveryTicketNumberInput, deliveryTicketDateInput, deliveryDateInput].forEach(input => {
    input.addEventListener("focus", () => {
        [supplierInput, deliveryTicketNumberInput, deliveryTicketDateInput, deliveryDateInput].forEach(inputI => {
            if (inputI.tagName === "INPUT") {
                inputI.style.border = "1px solid #A8A8A8";
                inputI.textContent = "";
            } else{
                inputI.style.border = "1px solid #A8A8A8";
            }
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




