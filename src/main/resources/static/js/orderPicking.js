"use strict";

import {byId, hideElementsById, showElementById} from "./util.js";

async function getOrdersAndShow() {
    const response = await fetch("api/order/getOrderRoute/1");

    hideElementsById("noOrdersFound");
    hideElementsById("error");
    showElementById("tableOrders");

    if (response.ok) {
        const orders = await response.json();

        const tableBody = byId("tableBody");

        orders.forEach(order => {
            const tr = tableBody.insertRow();
            tr.insertCell().innerText = order.shelf;
            tr.insertCell().innerText = order.position;
            tr.insertCell().innerText = order.name;
            tr.insertCell().innerText = order.quantityOrdered;


            const checkboxCell = tr.insertCell();
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkboxCell.appendChild(checkbox);

        });

        if (orders.length === 0) {
            showElementById("noOrdersFound");
            hideElementsById("tableOrders");
        }
    } else {
        showElementById("error");
        hideElementsById("tableOrders");
    }
}

getOrdersAndShow();