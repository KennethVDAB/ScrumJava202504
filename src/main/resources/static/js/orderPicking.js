"use strict";

import {byId, hideElementsById, showElementById} from "./util.js";
const finishedBtn = document.getElementById("finishedBtn");

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

            const nameCell = tr.insertCell();
            const link = document.createElement("a");
            link.href = "productDetails.html";
            link.classList.add("no-underline");
            link.innerText = order.name;
            nameCell.appendChild(link);

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
    finishedBtn.disabled = !allChecked;
}

// Voeg een eventlistener toe aan de finishedBtn zodat bij klik getOrdersAndShow(2) wordt uitgevoerd.
finishedBtn.addEventListener("click", async () => {
    const orderIds = JSON.parse(sessionStorage.getItem("orderIds") || '[]');
    if (!orderIds.length) {
        showElementById("noOrdersFound");
        hideElementsById("tableOrders");
        finishedBtn.disabled = true;
        return;
    }

    const isOrderFinished = await finishOrder(orderIds[0]);
    if(isOrderFinished) {
        orderIds.shift();
        sessionStorage.setItem("orderIds", JSON.stringify(orderIds));

        if (orderIds.length > 0) {
            getOrdersAndShow(orderIds[0]);
        } else {
            showElementById("noOrdersFound");
            hideElementsById("tableOrders");
            finishedBtn.disabled = true;
        }
    } else {
        showElementById("error");
        hideElementsById("tableOrders");
    }

});

async function finishOrder(orderId) {
    try {
        const response = await fetch("api/order/finishOrder/" + orderId);
        return response.ok;
    } catch (e) {
        return false;
    }
}

document.getElementById("saveBackBtn").addEventListener("click", () => {
    window.location.href = "introScreen.html";
});

const storedOrderIds = JSON.parse(sessionStorage.getItem('orderIds') || '[]');
if (storedOrderIds.length > 0) {
    getOrdersAndShow(storedOrderIds[0]);
} else {
    showElementById("noOrdersFound");
}