"use strict";

import {byId, hideElementsById, showElementById} from "./util.js";

async function getOrdersAndShow() {
    const response = await fetch("api/order/display");

    hideElementsById("noOrdersFound");
    hideElementsById("error");
    showElementById("tableOrders");

    if (response.ok) {
        const orders = await response.json();

        const tableBody = byId("tableBody");

        orders.forEach(order => {
            const tr = tableBody.insertRow();
            tr.insertCell().innerText = order.id;
            tr.insertCell().innerText = order.products;
            tr.insertCell().innerText = order.weight + " kg";
        });

        byId("amountOrders").firstElementChild.innerText = orders.length + "/5 Bestellingen";

        if (orders.length === 0) {
            showElementById("noOrdersFound");
            hideElementsById("tableOrders");
        }
    } else {
        showElementById("error");
        hideElementsById("tableOrders");
    }
}

// Execute the function immediately and repeat every 5 minutes (300000 ms)
getOrdersAndShow();
setInterval(getOrdersAndShow, 300000);