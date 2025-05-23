"use strict";

import {byId} from "./util.js";

const bevestigBtn = byId("bevestigBtn");
const tableBody = byId("tableBody");

const deliveredProducts = JSON.parse(sessionStorage.getItem("productData") || "{}");

fetchPlacementPlan(deliveredProducts);

async function fetchPlacementPlan(products) {
    try {
        const response = await fetch("/deliveries/placement-plan", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(products)
        });

        if (!response.ok) throw new Error("Fout bij ophalen van plaatsingsplan");

        const placementItems = await response.json();
        tableBody.innerHTML = "";

        placementItems.forEach(item => {
            const tr = tableBody.insertRow();
            tr.insertCell().innerText = item.shelf;
            tr.insertCell().innerText = item.position;
            tr.insertCell().innerText = item.name;
            tr.insertCell().innerText = item.quantityToPlace;

            const checkboxCell = tr.insertCell();
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.addEventListener("change", toggleConfirmButton);
            checkboxCell.appendChild(checkbox);
        });

        toggleConfirmButton();
    } catch (error) {
        console.error("Fout:", error);
    }
}

function toggleConfirmButton() {
    const checkboxes = document.querySelectorAll("input[type='checkbox']");
    const allChecked = Array.from(checkboxes).every(cb => cb.checked);
    bevestigBtn.disabled = !allChecked;
}

bevestigBtn.addEventListener("click", async () => {
    const rows = document.querySelectorAll("#tableBody tr");
    const articles = JSON.parse(sessionStorage.getItem("articles") || "[]");
    const incomingDeliveryId = parseInt(sessionStorage.getItem("incomingDeliveryId"));

    for (const row of rows) {
        const naam = row.cells[2].innerText;
        const aantal = parseInt(row.cells[3].innerText);
        const checkbox = row.cells[4].querySelector("input[type='checkbox']");

        if (!checkbox.checked) continue;

        const article = articles.find(a => a.name === naam);

        if (!article || !incomingDeliveryId) {
            console.error("Ontbrekende gegevens voor product:", naam);
            continue;
        }

        const newDeliveryLine = {
            incomingDeliveryId: incomingDeliveryId,
            ean: article.ean,
            approvedAmount: aantal,
            returnedAmount: 0,
            warehouseLocationId: null, // Wordt in backend bepaald
            articleId: article.productId
        };

        try {
            const response = await fetch("lines", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(newDeliveryLine)
            });

            if (!response.ok) {
                console.error("Fout bij verzenden van delivery line voor:", naam);
            }
        } catch (error) {
            console.error("Netwerkfout bij verzenden van delivery line:", error);
        }
    }

    alert("Alle bevestigde producten zijn verwerkt.");
});
