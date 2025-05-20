"use strict";

import {byId, hideElementsById, showElementById} from "./util.js";

async function getOrdersAndShow(idOrder) {
    const response = await fetch("api/order/getOrderRoute/" + idOrder);

    hideElementsById("noOrdersFound");
    hideElementsById("error");
    showElementById("tableOrders");

    const tableBody = byId("tableBody");

    tableBody.innerHTML = "";

    if (response.ok) {
        const orders = await response.json();

        orders.forEach(order => {
            const tr = tableBody.insertRow();
            tr.insertCell().innerText = order.shelf;
            tr.insertCell().innerText = order.position;
            tr.insertCell().innerText = order.name;
            tr.insertCell().innerText = order.quantityOrdered;


            const checkboxCell = tr.insertCell();
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";

            checkbox.addEventListener("change", toggleButtons);

            checkboxCell.appendChild(checkbox);
        });

        toggleButtons();

        if (orders.length === 0) {
            showElementById("noOrdersFound");
            hideElementsById("tableOrders");
        }
    } else {
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

    const finishedBtn = document.getElementById("finishedBtn");

    finishedBtn.disabled = !allChecked;
}

// Voeg een eventlistener toe aan de finishedBtn zodat bij klik getOrdersAndShow(2) wordt uitgevoerd.
document.getElementById("finishedBtn").addEventListener("click", () => {
    getOrdersAndShow(2);
});

document.getElementById("saveBackBtn").addEventListener("click", () => {
    window.location.href = "introScreen.html";
});

getOrdersAndShow(5);