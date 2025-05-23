"use strict";

import { byId, hideElementsById, showElementById } from "./util.js";

const finishedBtn = byId("finishedBtn");
let orderId = 99999;

async function getOrdersAndShow() {
    try {
        const response = await fetch("api/order/getOrderRoute/");

        hideElementsById("noOrdersFound");
        hideElementsById("error");
        showElementById("tableOrders");

        const tableBody = byId("tableBody");
        tableBody.innerHTML = "";

        if (response.ok) {
            const orders = await response.json();

            console.info(orders);

            if (orders.length > 0) {
                orderId = orders[0].orderId;
                console.log("OrderId set op:", orderId);
            } else {
                console.error("Geen bestellingen gevonden, orderId blijft ongewijzigd");
                showElementById("noOrdersFound");
                hideElementsById("tableOrders");
                return;
            }

            orders.forEach(order => {
                const tr = tableBody.insertRow();
                tr.insertCell().innerText = order.shelf;
                tr.insertCell().innerText = order.position;

                const nameCell = tr.insertCell();
                const link = document.createElement("a");
                link.href = "productDetails.html";
                link.classList.add("no-underline");
                link.innerText = order.name;

                // Voeg een eventlistener toe om sessionStorage te vullen bij klikken
                link.addEventListener("click", () => {
                    const orderData = {
                        id: order.productId,
                        position: order.position,
                        shelf: order.shelf
                    };
                    sessionStorage.setItem("orderData", JSON.stringify(orderData));
                });

                nameCell.appendChild(link);

                tr.insertCell().innerText = order.pickedQuantity;

                const checkboxCell = tr.insertCell();
                const checkbox = document.createElement("input");
                checkbox.type = "checkbox";

                checkbox.addEventListener("change", toggleButtons);
                checkboxCell.appendChild(checkbox);
            });

            toggleButtons();
        } else {
            showElementById("error");
            hideElementsById("tableOrders");
        }
    } catch (error) {
        console.error("Fout bij ophalen van bestellingen:", error);
        showElementById("error");
        hideElementsById("tableOrders");
    }
}

function areAllCheckboxesChecked() {
    const checkboxes = document.querySelectorAll("input[type='checkbox']");
    return Array.from(checkboxes).every(checkbox => checkbox.checked);
}

function toggleButtons() {
    const allChecked = areAllCheckboxesChecked();
    finishedBtn.disabled = !allChecked;
}

// Voeg een eventlistener toe aan finishedBtn zodat bij klik getOrdersAndShow() wordt uitgevoerd.
finishedBtn.addEventListener("click", async () => {
    const isOrderFinished = await finishOrder(orderId);
    if (isOrderFinished) {
        console.log("Bestelling succesvol afgerond.");
        getOrdersAndShow();
    } else {
        console.error("Fout bij afronden van bestelling.");
    }
});

async function finishOrder(orderId) {
    try {
        const response = await fetch("api/order/finishOrder/" + orderId, {
            method: "POST"
        });
        return response.ok;
    } catch (error) {
        console.error("Fout bij afronden van bestelling:", error);
        return false;
    }
}

byId("saveBackBtn").addEventListener("click", () => {
    window.location.href = "introScreen.html";
});

getOrdersAndShow();
