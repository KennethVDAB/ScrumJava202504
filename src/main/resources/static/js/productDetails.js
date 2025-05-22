import {byId, capitalizeFirstWord} from "./util.js";

const orderData = JSON.parse(sessionStorage.getItem("orderData"));

if (!orderData) {
    byId("no-product-found").hidden = false;
}

try {
    const response = await fetch(`api/products/${orderData.id}/${orderData.shelf}/${orderData.position}`);

    if (response.ok) {
        const productDetails = await response.json();

        populateProductDetails(productDetails[0]); // Neem de eerste (en enige) productdetails
    } else {
        byId("error").hidden = false;
    }
} catch (error) {
    byId("error").hidden = false;
}

function populateProductDetails(product) {
    byId("name").innerText = capitalizeFirstWord(product.productName);
    byId("ean").innerText = product.ean;
    byId("rack").innerText = product.shelf;
    byId("place").innerText = product.position;
    byId("number-to-pick").innerText = product.quantityOrdered;
    byId("price").innerText = product.price.toFixed(2) + " €";
    byId("stock-available").innerText = product.quantityStock;
    byId("supplier").innerText = product.supplier;
    byId("weight").innerText = product.weight + " kg";
}

