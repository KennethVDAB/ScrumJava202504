document.querySelectorAll("a.disabled").forEach(link => {
    link.addEventListener("click", event => {
        event.preventDefault(); // Voorkomt dat de link wordt gevolgd
    });
});
