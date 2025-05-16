"use strict";

import {byId, hideElementsById, showElementById} from "./util.js";

async function getOrdersAndCountAndShow() {
    byId("amountOrders").hidden = true;

    try {
        const [ordersResponse, countResponse] = await Promise.all([
            fetch("api/order/display"),
            fetch("api/order/count")
        ]);

        hideElementsById("noOrdersFound");
        hideElementsById("error");
        showElementById("tableOrders");

        if (ordersResponse.ok && countResponse.ok) {
            byId("amountOrders").hidden = false;

            const orders = await ordersResponse.json();
            const count = await countResponse.json();

            const tableBody = byId("tableBody");
            tableBody.innerHTML = ""; // Verwijder bestaande rijen

            orders.forEach(order => {
                const tr = tableBody.insertRow();
                tr.insertCell().innerText = order.id;
                tr.insertCell().innerText = order.products;
                tr.insertCell().innerText = order.weight + " kg";
            });

            byId("amountOrders").firstElementChild.innerText = `${orders.length}/${count} Bestellingen`;

            if (orders.length === 0) {
                showElementById("noOrdersFound");
                hideElementsById("tableOrders");
            }
        } else {
            byId("amountOrders").hidden = true;
            showElementById("error");
            hideElementsById("tableOrders");
        }
    } catch (error) {
        console.error("Fout bij ophalen van orders en count:", error);
        showElementById("error");
        hideElementsById("tableOrders");
    }
}

// Execute the function immediately and repeat every 5 minutes (300000 ms)
getOrdersAndCountAndShow();
setInterval(getOrdersAndCountAndShow, 300000);
